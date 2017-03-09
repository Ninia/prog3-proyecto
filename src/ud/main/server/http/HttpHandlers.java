package ud.main.server.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HttpHandlers {

    static class ServerHandlers {

        static class TestHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String response = "Hi there";
                he.sendResponseHeaders(200, response.length());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        static class AntigravityHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String html = "    <html>\n" +
                        "    <body><script>\n" +
                        "    window.location = \"https://xkcd.com/353/\"\n" +
                        "    </script>\n" +
                        "    </noscript>\n" +
                        "    <h3> <a href=\"https://www.xkcd.com/353/\">https://www.xkcd.com/353/</a> </h3>\n" +
                        "    </noscript></body>\n" +
                        "    </html>";
                he.sendResponseHeaders(200, html.length());
                OutputStream os = he.getResponseBody();
                os.write(html.getBytes());
                os.close();
            }
        }


    }
}
