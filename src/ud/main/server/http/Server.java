package ud.main.server.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ud.main.utils.network.URI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Server {

    private HttpServer httpServer;
    private HashMap<String, HttpHandler> contexts = new HashMap<String, HttpHandler>() {{
        put("/test", new HttpHandlers.ServerHandlers.TestHandler());
        put("/antigravity", new HttpHandlers.ServerHandlers.AntigravityHandler());
    }};


    public Server() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(URI.getPort("http-server")), 0);
        for (String context: contexts.keySet()) {
            httpServer.createContext(context, contexts.get(context));
        }
        this.httpServer.setExecutor(null);
    }

    public static void main(String[] args) {
        try {
            (new Server()).httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
