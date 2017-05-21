package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import org.bson.Document;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UserNotFoundException;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.http.URI;
import ud.binmonkey.prog3_proyecto_server.mongodb.MongoDB;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

public class UserInfoHandler implements HttpHandler {
    @SuppressWarnings("Duplicates")
    @Override
    public void handle(HttpExchange he) throws IOException {
        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;
        try {

            HashMap<String, String> args = URI.getArgs(hes.getRequestURI());
            if (args == null || args.get("username") == null || args.get("token") == null) {
                byte[] response = "Missing arguments.".getBytes();

                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(403, 0);
                os = hes.getResponseBody();
                os.write(response);
                os.close();
                return;
            }

            String userName = args.get("username");
            String token = args.get("token");

            boolean validToken = SessionHandler.INSTANCE.validToken(userName, token);

            if (validToken) {

                SessionHandler.INSTANCE.userActivity(userName);
                Document user;
                try {
                    user = MongoDB.getUser(userName);
                } catch (UserNotFoundException e) {

                    hes.getResponseHeaders().add("content-type", "text/plain");
                    hes.sendResponseHeaders(401, 0);
                    os = hes.getResponseBody();
                    os.write(("User '" + userName + "' not found").getBytes());
                    os.close();
                    return;
                }

                String response = "{\n" +
                        "\t\"username\": \"" + userName + "\",\n" +
                        "\t\"display_name\": \"" + user.getString("display_name") + "\",\n" +
                        "\t\"email\": \"" + user.getString("email") + "\",\n" +
                        "\t\"role\": \"" + user.getString("role") + "\",\n" +
                        "\t\"preferred_language\": \"" + user.getString("preferred_language") + "\",\n" +
                        "\t\"birth_date\": \"" + user.getString("birth_date") + "\",\n" +
                        "\t\"gender\": \"" + user.getString("gender") + "\"\n" +
                        "}";
                hes.getResponseHeaders().add("content-type", "application/json");
                hes.sendResponseHeaders(200, 0);
                os = hes.getResponseBody();
                os.write(response.getBytes());

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
