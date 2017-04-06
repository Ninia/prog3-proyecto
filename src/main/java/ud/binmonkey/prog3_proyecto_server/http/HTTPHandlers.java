package ud.binmonkey.prog3_proyecto_server.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPHandlers {

    /**
     * Show request in terminal
     */
    static void printRequest(HttpExchange he) {
        System.out.println(
                "(" + he.getProtocol() + ") " + he.getRequestMethod() + "  -  " + he.getRequestURI().getPath())
        ;
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

            he.sendResponseHeaders(200, html.length());
            he.getResponseHeaders().add("Content-type", "application/html");

            OutputStream os = he.getResponseBody();
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
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
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
            he.sendResponseHeaders(200, 0);

            OutputStream os = he.getResponseBody();
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

            he.sendResponseHeaders(200, 0);
            he.getResponseHeaders().add("Content-type", "application/html");

            OutputStream os = he.getResponseBody();
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

            String filePath = he.getRequestURI().getPath();
            he.sendResponseHeaders(200, 0);

            OutputStream os = he.getResponseBody();
            os.write(Files.readAllBytes(Paths.get("src/main/web/" + filePath)));
            os.close();
        }
    }
}
