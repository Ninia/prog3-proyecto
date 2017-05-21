package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;
import ud.binmonkey.prog3_proyecto_server.http.HtmlParser;

import java.io.IOException;
import java.io.OutputStream;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

/**
 * Handler for root
 */
public class DefaultHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        printRequest(he);

        String response = HtmlParser.parse(TextFile.read("src/main/web/default.html"));

        HttpsExchange hes = (HttpsExchange) he;
        hes.sendResponseHeaders(200, 0);
        hes.getResponseHeaders().add("content-type", "text/plain");
        OutputStream os = hes.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
