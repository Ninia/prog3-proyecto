package ud.binmonkey.prog3_proyecto_server.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.TextFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("WeakerAccess")
public class HTTPSHandlers {



    /**
     * Show request in terminal
     */
    static void printRequest(HttpExchange he) {

        HttpsExchange hes = (HttpsExchange) he;
        System.out.println(
                "(" + hes.getProtocol() + ") " + hes.getRequestMethod() + "  -  " + hes.getRequestURI().getPath())
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

            HttpsExchange hes = (HttpsExchange) he;
            hes.sendResponseHeaders(200, html.length());
            hes.getResponseHeaders().add("Content-type", "application/html");

            OutputStream os = hes.getResponseBody();
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

            HttpsExchange hes = (HttpsExchange) he;
            hes.sendResponseHeaders(200, response.length());
            OutputStream os = hes.getResponseBody();
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
    static class IndexHandler implements HttpHandler {
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
     * Returns specified file
     */
    static class WebHandler implements HttpHandler {
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


//    static class ServerHandlers {
//
//        static class TestHandler implements HttpHandler {
//            @Override
//            public void handle(HttpExchange httpExchange) throws IOException {
//                System.out.println("Hi");
//                String response = "Hi there";
//                HttpsExchange httpsExchange = (HttpsExchange) httpExchange;
//                httpsExchange.sendResponseHeaders(200, response.length());
//                OutputStream os = httpsExchange.getResponseBody();
//                os.write(response.getBytes());
//                os.close();
//            }
//        }
//
//        static class AntigravityHandler implements HttpHandler {
//            @Override
//            public void handle(HttpExchange httpExchange) throws IOException {
//                String html = "    <html>\n" +
//                        "    <body><script>\n" +
//                        "    window.location = \"http://xkcd.com/353/\"\n" +
//                        "    </script>\n" +
//                        "    </noscript>\n" +
//                        "    <h3> <a href=\"http://www.xkcd.com/353/\">http://www.xkcd.com/353/</a> </h3>\n" +
//                        "    </noscript></body>\n" +
//                        "    </html>";
//                HttpsExchange httpsExchange = (HttpsExchange) httpExchange;
//                httpsExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//                httpsExchange.sendResponseHeaders(200, html.length());
//                OutputStream os = httpsExchange.getResponseBody();
//                os.write(html.getBytes());
//                os.close();
//            }
//        }
//    }
}
