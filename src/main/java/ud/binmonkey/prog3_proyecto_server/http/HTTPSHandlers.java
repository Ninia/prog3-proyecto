package ud.binmonkey.prog3_proyecto_server.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;

import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("WeakerAccess")
public class HTTPSHandlers {

    static class ServerHandlers {

        static class TestHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String response = "Hi there";
                HttpsExchange httpsExchange = (HttpsExchange) httpExchange;
                httpsExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpsExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        static class AntigravityHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String html = "    <html>\n" +
                        "    <body><script>\n" +
                        "    window.location = \"http://xkcd.com/353/\"\n" +
                        "    </script>\n" +
                        "    </noscript>\n" +
                        "    <h3> <a href=\"http://www.xkcd.com/353/\">http://www.xkcd.com/353/</a> </h3>\n" +
                        "    </noscript></body>\n" +
                        "    </html>";
                HttpsExchange httpsExchange = (HttpsExchange) httpExchange;
                httpsExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpsExchange.sendResponseHeaders(200, html.length());
                OutputStream os = httpsExchange.getResponseBody();
                os.write(html.getBytes());
                os.close();
            }
        }
    }
}
