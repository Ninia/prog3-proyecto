package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.*;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;
import ud.binmonkey.prog3_proyecto_server.http.HTTPSServer;
import ud.binmonkey.prog3_proyecto_server.http.URI;
import ud.binmonkey.prog3_proyecto_server.users.UserManager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

public class PasswordChangeHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger(HTTPSServer.class.getName() + ".Password");
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + HTTPSServer.class.getName() + ".Password." +
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
            if (args == null || args.get("username") == null ||
                    args.get("password") == null || args.get("token") == null ) {
                byte[] response = "Missing arguments.".getBytes();
                hes.sendResponseHeaders(403, 0);
                os = hes.getResponseBody();
                os.write(response);
                os.close();
                return;
            }

            String userName = args.get("username");
            String oldPassword = args.get("oldPassword");
            String newPassword = args.get("newPassword");
            String token = args.get("token");

            boolean validToken = SessionHandler.INSTANCE.validToken(userName, token);
            if (validToken) {

                SessionHandler.INSTANCE.userActivity(userName);
                try {

                    UserManager.changePassword(userName, oldPassword, newPassword);
                    hes.getResponseHeaders().add("content-type", "text/plain");
                    hes.sendResponseHeaders(200, 0);
                    os = hes.getResponseBody();
                    os.write("OK".getBytes());

                } catch (UserNotFoundException | IncorrectPasswordException | AdminEditException e) {

                    hes.getResponseHeaders().add("content-type", "text/plain");
                    hes.sendResponseHeaders(401, 0);
                    os = hes.getResponseBody();
                    os.write(e.getMessage().getBytes());
                }
            } else {
                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(401, 0);
                os = hes.getResponseBody();
                os.write("Invalid token".getBytes());
            }

        } catch (UriUnescapedArgsException | EmptyArgException e) {
            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(e.getMessage().getBytes());
        }
        os.close();
    }
}
