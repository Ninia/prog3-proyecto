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

public class PropertyChangeHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger(HTTPSServer.class.getName() + ".Properties");
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + HTTPSServer.class.getName() + ".Properties." +
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
            if (args == null || args.get("username") == null || args.get("property") == null
                    || args.get("value") == null || args.get("token") == null) {
                byte[] response = "Missing arguments.".getBytes();

                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(403, 0);
                os = hes.getResponseBody();
                os.write(response);
                os.close();
                return;
            }

            String userName = args.get("username");
            String property = args.get("property");
            String value = args.get("value");

            boolean validToken = SessionHandler.INSTANCE.validToken(userName, args.get("token"));

            if (validToken) {
                try {
                    switch (property) {

                        case "birth_date":
                            UserManager.changeBirthDate(userName, value);
                            break;

                        case "display_name":
                            UserManager.changeDisplayName(userName, value);
                            break;

                        case "email":
                            UserManager.changeEmail(userName, value);
                            break;

                        case "gender":
                            UserManager.changeGender(userName, value);
                            break;

                        case "preferred_language":
                            UserManager.changePreferredLanguage(userName, value);
                            break;

                        /* Role can't be changed by user */

                        default:
                            hes.getResponseHeaders().add("content-type", "text/plain");
                            hes.sendResponseHeaders(400, 0);
                            os = hes.getResponseBody();
                            os.write(("Unknown property: " + property).getBytes());
                            os.close();
                            return;
                    }

                } catch (AdminEditException e) {
                    hes.getResponseHeaders().add("content-type", "text/plain");
                    hes.sendResponseHeaders(401, 0);
                    os = hes.getResponseBody();
                    os.write("Permission denied".getBytes());
                    os.close();
                    return;

                } catch (IncorrectFormatException | UnsupportedLanguageException e) {
                    hes.getResponseHeaders().add("content-type", "text/plain");
                    hes.sendResponseHeaders(400, 0);
                    os = hes.getResponseBody();
                    os.write(e.getMessage().getBytes());
                    os.close();
                    return;
                }

                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(200, 0);
                os = hes.getResponseBody();
                LOG.log(Level.INFO, "Property: '" + property + "' changed to '" + value + "'");
                os.write(("Property: '" + property + "' changed to '" + value + "'").getBytes());
                os.close();
            }

        }catch (UriUnescapedArgsException | EmptyArgException e){

            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(e.getMessage().getBytes());
            os.close();
        }
    }
}
