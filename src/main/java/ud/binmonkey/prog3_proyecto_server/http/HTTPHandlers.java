package ud.binmonkey.prog3_proyecto_server.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;
import ud.binmonkey.prog3_proyecto_server.common.network.URI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPHandlers {

    static void printRequest(HttpExchange he) {
        System.out.println(
                "(" + he.getProtocol() + ") " + he.getRequestMethod() + "  -  " + he.getRequestURI().getPath())
        ;
    }

    static class DefaultHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String response = HtmlParser.parse(TextFile.read("src/main/web/default.html"));
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class AntigravityHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String html = TextFile.read("src/main/web/antigravity.html");
            html = HtmlParser.parse(html);

            he.sendResponseHeaders(200, html.length());
            he.getResponseHeaders().add("Content-type", "application/html");

            OutputStream os = he.getResponseBody();
            os.write(html.getBytes());
            os.close();

        }
    }

    /**
     * Returns specified .js file
     */
    static class JsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {

            printRequest(he);

            String filePath = he.getRequestURI().getPath().replaceFirst("/js", "");
            String fileContent = TextFile.read("src/main/web/js" + filePath);

            he.sendResponseHeaders(200, fileContent.length());
            he.getResponseHeaders().add("Content-type", "text/javascript");

            OutputStream os = he.getResponseBody();
            os.write(fileContent.getBytes());
            os.close();
        }
    }
}
