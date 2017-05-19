package ud.binmonkey.prog3_proyecto_server.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.AdminEditException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UserNotFoundException;
import ud.binmonkey.prog3_proyecto_server.common.security.Session;
import ud.binmonkey.prog3_proyecto_server.users.UserManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class HTTPSHandlers {



    /**
     * Show request in terminal
     */
    static void printRequest(HttpExchange he) {

        HttpsExchange hes = (HttpsExchange) he;

        String request = "(" + hes.getProtocol() + ") " + hes.getRequestMethod() + "  -  " + hes.getRequestURI().getPath();

        if (!hes.getRequestURI().toString().endsWith(hes.getRequestURI().getPath())){
            try {
                HashMap<String, String> args = URI.getArgs(he.getRequestURI());
                request += "\t{";
                for (String key: args.keySet()) {
                    request += " " + key + ": " + args.get(key) + ",";
                }
                request = request.substring(0, request.length() - 1) + " }";
            } catch (UriUnescapedArgsException | EmptyArgException e) {}
        }
        System.out.println(request);
    }

    /**
     * Little easter egg
     */
    static class AntigravityHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String html = TextFile.read("src/main/web/antigravity.html");
            html = HtmlParser.parse(html);

            HttpsExchange hes = (HttpsExchange) he;
            hes.sendResponseHeaders(200, html.length());
            hes.getResponseHeaders().add("Content-type", "application/html");

            OutputStream os = hes.getResponseBody();
            os.write(html.getBytes());
            os.close();

        }
    }

    /**
     * Handler for root
     */
    static class DefaultHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String response = HtmlParser.parse(TextFile.read("src/main/web/default.html"));

            HttpsExchange hes = (HttpsExchange) he;
            hes.sendResponseHeaders(200, 0);
            OutputStream os = hes.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    /**
     * GET method is deprecated, call with POST instead
     * Handles log ins, returns token if succeeded
     */
    static class LoginHandler implements HttpHandler {

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

    /**
     * Returns specified file
     */
    static class FavIcoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String filePath = "src/main/web/images/favicon.ico";

            HttpsExchange hes = (HttpsExchange) he;
            hes.sendResponseHeaders(200, 0);

            OutputStream os = hes.getResponseBody();
            os.write(Files.readAllBytes(Paths.get(filePath)));
            os.close();
        }
    }

    /**
     * Returns index
     */
    static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String fileContent = HtmlParser.parse(TextFile.read("src/main/web/vendor/worthy/index.html"));

            HttpsExchange hes = (HttpsExchange) he;
            hes.sendResponseHeaders(200, 0);
            hes.getResponseHeaders().add("Content-type", "application/html");

            OutputStream os = hes.getResponseBody();
            os.write(fileContent.getBytes());
            os.close();
        }
    }

    /**
     * Returns specified file
     */
    static class WebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            HttpsExchange hes = (HttpsExchange) he;
            String filePath = hes.getRequestURI().getPath();
            hes.sendResponseHeaders(200, 0);

            OutputStream os = hes.getResponseBody();
            os.write(Files.readAllBytes(Paths.get("src/main/web/" + filePath)));
            os.close();
        }
    }
}
