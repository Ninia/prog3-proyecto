package ud.binmonkey.prog3_proyecto_server.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ud.binmonkey.prog3_proyecto_server.common.network.URI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class HTTPServer {

    private HttpServer httpServer;
    private HashMap<String, HttpHandler> contexts = new HashMap<String, HttpHandler>() {{
       put("/antigravity", new HTTPHandlers.AntigravityHandler());
       put("/js/", new HTTPHandlers.JsHandler());
       put("/", new HTTPHandlers.DefaultHandler());
    }};

    public HTTPServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(URI.getPort("http-server")), 0);
        for (String context: contexts.keySet()) {
            httpServer.createContext(context, contexts.get(context));
        }
        this.httpServer.setExecutor(null);
    }

    public static void main(String[] args) {
        try {
            (new HTTPServer()).httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
