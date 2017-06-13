package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import org.apache.ftpserver.ftplet.FtpException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.*;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;
import ud.binmonkey.prog3_proyecto_server.http.HTTPSServer;
import ud.binmonkey.prog3_proyecto_server.http.URI;
import ud.binmonkey.prog3_proyecto_server.users.User;
import ud.binmonkey.prog3_proyecto_server.users.UserManager;
import ud.binmonkey.prog3_proyecto_server.users.attributes.Language;
import ud.binmonkey.prog3_proyecto_server.users.attributes.Role;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;
import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.validateArgs;

/**
 * Creates user from HTTP request
 * TODO: documentation
 */
public class SignUpHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger(HTTPSServer.class.getName() + ".SignUp");
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + HTTPSServer.class.getName() + ".SignUp." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handle(HttpExchange he) throws IOException {

        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;
        try {
            HashMap<String, String> args = URI.getArgs(hes.getRequestURI());

            boolean err = validateArgs(hes, args, "username", "password");
            if (err) {
                return;
            }

            String[] attrs = new String[7];
            attrs[0] = args.get("username");
            attrs[1] = args.get("password");
            attrs[2] = args.get("display_name");
            attrs[3] = args.get("email");
            attrs[4] = args.get("gender");
            attrs[5] = args.get("birth_date");
            attrs[6] = args.get("preferred_language");

            String username = attrs[0];
            byte[] response;

            for (int i = 0; i < attrs.length; i++) {
                if (attrs[i] == null) {
                    if (i == 0 || i == 1 /* username / password */) {
                        hes.getResponseHeaders().add("content-type", "text/plain");
                        response = "Password and Username can't be empty".getBytes();
                        hes.sendResponseHeaders(401, 0);
                        os = hes.getResponseBody();
                        os.write(response);
                        os.close();
                        return;
                    } else if (i == 2 /*Display name*/) {
                        attrs[2] = attrs[0]; /*Default display name is username */
                    } else {
                        attrs[i] = "";
                    }
                }
            }


            if (!UserManager.userExists(username)) {

                Language lang;
                try {
                    lang = Language.valueOf(attrs[6]);
                } catch (Exception e) {
                    lang = Language.EN;
                }

                /*
                TODO: Map instead of array for clarity
                 */
                try {
                    UserManager.createUser(new User(attrs[5], attrs[2], attrs[3], attrs[4], attrs[1].toCharArray(),
                            lang, Role.USER, username));
                } catch (FtpException | InvalidNameException e) {
                    response = e.getMessage().getBytes();
                    hes.sendResponseHeaders(400, 0);
                    hes.getResponseHeaders().add("content-type", "text/plain");
                    os = hes.getResponseBody();
                    os.write(response);
                    os.close();
                    return;
                }
                response = ("User: `" + username + "` created.").getBytes();
                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(200, 0);
                LOG.log(Level.INFO, "User: `" + username + "` created.");

            } else {
                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(400, 0);
                response = ("Username " + username + " already found.").getBytes();
            }

            os = hes.getResponseBody();
            os.write(response);

        } catch (AdminEditException | UriUnescapedArgsException | EmptyArgException e) {

            byte[] response = e.getMessage().getBytes();
            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(response);
        }
        os.close();
    }
}
