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


    }
}
