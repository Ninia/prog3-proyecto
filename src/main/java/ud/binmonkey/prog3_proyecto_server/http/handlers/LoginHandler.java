package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.AdminEditException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UserNotFoundException;
import ud.binmonkey.prog3_proyecto_server.common.security.Session;
import ud.binmonkey.prog3_proyecto_server.http.URI;
import ud.binmonkey.prog3_proyecto_server.users.UserManager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

/**
 * GET method is deprecated, call with POST instead
 * Handles log ins, returns token if succeeded
 */
public class LoginHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange he) throws IOException {

        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;
        try {
            HashMap<String, String> args = URI.getArgs(hes.getRequestURI());

            if (args == null || args.get("username") == null || args.get("password") == null) {
                byte[] response = "Missing arguments.".getBytes();
                hes.sendResponseHeaders(403, 0);
                os = hes.getResponseBody();
                os.write(response);
            }

            String username = args.get("username");
            String password = args.get("password");

            byte[] response;

            if (UserManager.userExists(username) && UserManager.authUser(username, password.toCharArray())) {
                String responseToken = Session.generateSessionToken(32);
                /**
                 * Save token and use it to handle requests
                 */
                response = responseToken.getBytes();
                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(200, 0);
            } else {
                hes.sendResponseHeaders(401, 0);
                response = ("Username " + username + "not found.").getBytes();
            }

            os = hes.getResponseBody();
            os.write(response);

        } catch (UriUnescapedArgsException | EmptyArgException e) {

            byte[] response = e.getMessage().getBytes();

            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(response);

        }  catch (UserNotFoundException | AdminEditException e) {

            byte[] response = e.getMessage().getBytes();
            hes.sendResponseHeaders(401, 0);
            os = hes.getResponseBody();
            os.write(response);
        }
        os.close();
    }
}
