package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.http.URI;
import ud.binmonkey.prog3_proyecto_server.omdb.Omdb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;
import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.validateArgs;

public class GetMovieJSONHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;

        HashMap<String, String> args = null;

        try {
            args = URI.getArgs(hes.getRequestURI());
            boolean err = validateArgs(hes, args, "username", "token", "id");
            if (err) {
                return;
            }
            String userName = args.get("username");
            String token = args.get("token");

            /* Get id */
            String id = args.get("id");

            boolean validToken = SessionHandler.INSTANCE.validToken(userName, token);

            if (validToken) {

                JSONObject response = Omdb.search(id, "Movie");
                if (response != null) {
                    hes.getResponseHeaders().add("content-type", "application/json");
                    hes.sendResponseHeaders(200, 0);
                    os = hes.getResponseBody();
                    os.write(response.toString().getBytes());
                } else {
                    hes.getResponseHeaders().add("content-type", "text/plain");
                    hes.sendResponseHeaders(404, 0);
                    os = hes.getResponseBody();
                    os.write("Not found.".getBytes());
                }

            } else {
                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(401, 0);
                os = hes.getResponseBody();
                os.write("Unauthorized.".getBytes());
            }
        } catch (UriUnescapedArgsException | EmptyArgException e) {
            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(e.getMessage().getBytes());
        }
        os.close();
    }

    public static void main(String[] args) {
    }
}
