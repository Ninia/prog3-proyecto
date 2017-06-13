package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.http.URI;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;
import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.validateArgs;

@SuppressWarnings("Duplicates")
public class SearchMovieHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;

        HashMap<String, String> args = null;

        try {
            args = URI.getArgs(hes.getRequestURI());
            boolean err = validateArgs(hes, args, "username", "token", "title", "type");
            if (err) {
                return;
            }

            String userName = args.get("username");
            String token = args.get("token");

            /* Get title and type */
            String title = args.get("title");
            String type = args.get("type");

            boolean validToken = SessionHandler.INSTANCE.validToken(userName, token);

            if (validToken) {

                JSONObject response;
                /* TODO: modify JSON here*/


                hes.getResponseHeaders().add("content-type", "application/json");
                hes.sendResponseHeaders(200, 0);
                os = hes.getResponseBody();
                os.write(response.toString().getBytes());


            } else {
                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(401, 0);
                os = hes.getResponseBody();
                os.write("Unauthorized.".getBytes());
            }

        } catch (EmptyArgException | UriUnescapedArgsException e) {
            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(e.getMessage().getBytes());
        }
        os.close();
    }
}
