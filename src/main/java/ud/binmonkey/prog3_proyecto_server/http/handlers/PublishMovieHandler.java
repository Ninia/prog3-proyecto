package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import org.json.JSONObject;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.http.URI;
import ud.binmonkey.prog3_proyecto_server.neo4j.Neo4jUtils;
import ud.binmonkey.prog3_proyecto_server.omdb.OmdbMovie;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;
import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.validateArgs;

public class PublishMovieHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        System.out.println("0");

        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;

        HashMap<String, String> args = null;

        try {
            try {
                args = URI.getArgs(hes.getRequestURI());
                boolean err = validateArgs(hes, args
//                    ,"username", "token",
                );
                if (err) {
                    return;
                }
            } catch (NullPointerException e) {}


//            String userName = args.get("username");
//            String token = args.get("token");



//            boolean validToken = SessionHandler.INSTANCE.validToken(userName, token);

//            if (validToken) {

                String content = convertStreamToString(hes.getRequestBody());
                System.out.println(content);
                OmdbMovie movie = new OmdbMovie(new JSONObject(content));
                Neo4jUtils utils = new Neo4jUtils();
                utils.addMovie(movie);
                hes.getResponseHeaders().add("content-type", "application/json");
                hes.sendResponseHeaders(200, 0);
                os = hes.getResponseBody();
                os.write("OK".toString().getBytes());
//
//            } else {
//                hes.getResponseHeaders().add("content-type", "text/plain");
//                hes.sendResponseHeaders(401, 0);
//                os = hes.getResponseBody();
//                os.write("Unauthorized.".getBytes());
//            }

        } catch (EmptyArgException | UriUnescapedArgsException e) {
            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(e.getMessage().getBytes());
        }
        os.close();
    }
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
