package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;
import ud.binmonkey.prog3_proyecto_server.http.HtmlParser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

/**
 * Handlers for web services
 */
public class WebHandlers {

    /**
     * Little easter egg
     */
    public static class AntigravityHandler implements HttpHandler {

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
     * Returns favicon
     */
    public static class FavIcoHandler implements HttpHandler {
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
    public static class IndexHandler implements HttpHandler {
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
     * Returns specified web file
     */
    public static class WebFileHandler implements HttpHandler {
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
