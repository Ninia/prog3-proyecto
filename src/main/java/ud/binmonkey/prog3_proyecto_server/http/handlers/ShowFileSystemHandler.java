package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.DirIsFileException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.filesystem.Scanner;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.http.URI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

@SuppressWarnings("Duplicates")
/**
 * Returns http response with the following as content:
 *
 * A json file representing the directory requested by the user
 * TODO: documentation
 */
public class ShowFileSystemHandler implements HttpHandler {

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
                String directory = "";

                if (args.get("directory") != null) {
                    String dir = args.get("directory");
                    if (dir.contains("/")) {
                        String[] components = dir.split("/");
                        if (components.length != 0) {
                            for (String component: components) {
                                directory += "/" + component;
                            }
                        }
                    }
                }
                try {
                    System.out.println(Scanner.getFtpd() + userName + directory);
                    String response = Scanner.scanDir(Scanner.getFtpd() + userName + directory).toString();
                    hes.getResponseHeaders().add("content-type", "application/json");
                    hes.sendResponseHeaders(200, 0);
                    os = hes.getResponseBody();
                    os.write(response.getBytes());

                } catch (DirIsFileException e) {
                    hes.sendResponseHeaders(400, 0);
                    os = hes.getResponseBody();
                    os.write(("Requested directory " + directory + " is a file").getBytes());

                } catch (FileNotFoundException e) {
                    hes.sendResponseHeaders(404, 0);
                    os = hes.getResponseBody();
                    os.write(("File " + directory + " not found").getBytes());
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
}
