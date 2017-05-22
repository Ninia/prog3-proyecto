package ud.binmonkey.prog3_proyecto_server.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;
import ud.binmonkey.prog3_proyecto_server.common.security.SessionHandler;
import ud.binmonkey.prog3_proyecto_server.http.URI;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import static ud.binmonkey.prog3_proyecto_server.http.handlers.HandlerUtils.printRequest;

/**
 * Returns info about current session
 */
public class SessionInfoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        HttpsExchange hes = (HttpsExchange) he;
        printRequest(hes);

        OutputStream os;
        try {
            HashMap<String, String> args = URI.getArgs(hes.getRequestURI());
            String username = args.get("username");
            String token = args.get("token");
            SessionHandler.Session session = SessionHandler.INSTANCE.getSessions().get(username);

            /* if session did not exists or token wasn't user's token */
            if (session == null || !session.getToken().equals(token)) {

                hes.getResponseHeaders().add("content-type", "text/plain");
                hes.sendResponseHeaders(403,0);
                os = hes.getResponseBody();
                os.write("Unauthorized".getBytes());
                os.close();
                return;
            }
            /* update user activity */
            SessionHandler.INSTANCE.userActivity(username);

            /* format dates */
            Date updateDate = new Date(session.getLastUpdate());
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendDays()
                    .appendSuffix("d")
                    .appendHours()
                    .appendSuffix("h")
                    .appendMinutes()
                    .appendSuffix("m")
                    .appendSeconds()
                    .appendSuffix("s")
                    .toFormatter();

            String response = "{\n" +
                    "\t\"Username\": \"" + session.getUserName() + "\",\n" +
                    "\t\"Last Update\": \"" + updateDate.toString() + "\",\n" +
                    "\t\"Expires In\": \"" + formatter.print((new Duration(session.getKeepAlive()).toPeriod())) +
                    "\"\n}";

            hes.getResponseHeaders().add("content-type", "application/json");
            hes.sendResponseHeaders(200, 0);
            os = hes.getResponseBody();
            os.write(response.getBytes());

        } catch (EmptyArgException | UriUnescapedArgsException e) {

            hes.getResponseHeaders().add("content-type", "text/plain");
            hes.sendResponseHeaders(400, 0);
            os = hes.getResponseBody();
            os.write(e.getMessage().getBytes());
        }
        os.close();
    }
}
