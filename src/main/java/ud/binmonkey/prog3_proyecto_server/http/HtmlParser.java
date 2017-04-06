package ud.binmonkey.prog3_proyecto_server.http;

import ud.binmonkey.prog3_proyecto_server.common.network.URI;

import java.util.HashMap;

public class HtmlParser {

    static HashMap<String, String> parseRules = new HashMap<String, String>() {{
       put("%{url}", "http://" + URI.getHost("http-server") + ":" + URI.getPort("http-server"));
    }};

    public static String parse(String html) {
        System.out.println("Hi");
        for (String rule : parseRules.keySet()) {
            System.out.println(rule);
            System.out.println(parseRules.get(rule));
            html = html.replace(rule, parseRules.get(rule));
        }
        System.out.println("Bye");
        return html;
    }
}
