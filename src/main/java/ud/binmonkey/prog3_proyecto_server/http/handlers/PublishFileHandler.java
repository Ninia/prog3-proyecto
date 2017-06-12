package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.filesystem.FileUtils;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.http.URI;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;
import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.validateArgs;

public class PublishFileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;
        try {
            HashMap<String, String> args = URI.getArgs(hes.getRequestURI());

            boolean err = validateArgs(hes, args, "username", "token", "filePath");
            if (err) {
                return;
            }

            String filePath = args.get("filePath");
            String userName = args.get("username");
            String token    = args.get("token");

            boolean validToken = SessionHandler.INSTANCE.validToken(userName, token);

            if (validToken) {
                FileUtils.publishFile(filePath, userName);
                hes.sendResponseHeaders(200, 0);
                hes.getResponseHeaders().add("content-type", "text/plain");
                os = hes.getResponseBody();
                os.write("OK".getBytes());
            }

        } catch (UriUnescapedArgsException e) {
            e.printStackTrace();
        } catch (EmptyArgException e) {
            e.printStackTrace();
        }
    }
}
