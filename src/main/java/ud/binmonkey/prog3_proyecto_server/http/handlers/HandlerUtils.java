package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.time.DateUtils;
import ud.binmonkey.prog3_proyecto_server.http.HTTPSServer;
import ud.binmonkey.prog3_proyecto_server.http.URI;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandlerUtils {

    private static final Logger LOG = Logger.getLogger(HTTPSServer.class.getName() + ".Requests");
    static {
        try {
            LOG.addHandler(new FileHandler(
                    "logs/" + HTTPSServer.class.getName() + ".Requests" + "." +
                            DateUtils.currentFormattedDate() + ".log.xml", true));
        } catch (SecurityException | IOException e) {
            LOG.log(Level.SEVERE, "Unable to create log file.");
        }
    }

    /**
     * Log and show request in terminal
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
            LOG.log(Level.INFO, request);
            System.out.println(request);
        }).run();
    }

    /**
     * Checks if HashMap contains the args passed as parameter (String[]) and writes HTTPS response if any are null
     * @param hes HttpsExchange of handle
     * @param args complete HashMap of args
     * @param vArgs args to check
     * @return false if none where null, true if any was null
     * @throws IOException HTTPS error
     */
    public static boolean validateArgs(HttpsExchange hes, HashMap<String, String> args, String... vArgs)
            throws IOException {

        boolean nullArgs = false;

        for (String arg: vArgs) {
            if (args.get(arg) == null) {
                nullArgs = true;
            }
        }

        if (args == null || nullArgs) {

            byte[] response = "Missing arguments.".getBytes();

            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(403, 0);

            OutputStream os = hes.getResponseBody();
            os.write(response);
            os.close();

            return true;
        }
        return false;
    }
}
