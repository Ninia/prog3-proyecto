package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.http.URI;

import java.util.HashMap;

public class HandlerUtils {

    /**
     * Show request in terminal
     * TODO: log requests
     */
    public static void printRequest(HttpExchange he) {
        new Thread(() -> {
            HttpsExchange hes = (HttpsExchange) he;
            String request = "(" + hes.getProtocol() + ") " + hes.getRequestMethod() + "  -  " + hes.getRequestURI().getPath();

            if (!hes.getRequestURI().toString().endsWith(hes.getRequestURI().getPath())){
                try {
                    HashMap<String, String> args = URI.getArgs(he.getRequestURI());
                    request += "\t{";
                    for (String key: args.keySet()) {
                        request += " " + key + ": " + args.get(key) + ",";
                    }
                    request = request.substring(0, request.length() - 1) + " }";
                } catch (UriUnescapedArgsException | EmptyArgException e) {}
            }
            System.out.println(request);
        }).run();
    }
}
