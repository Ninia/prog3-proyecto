package ud.binmonkey.prog3_proyecto_server.http;

import ud.binmonkey.prog3_proyecto_server.common.exceptions.EmptyArgException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.UriUnescapedArgsException;

import java.util.HashMap;

public class URI {
    public static HashMap<String, String> getArgs(java.net.URI uri) throws EmptyArgException, UriUnescapedArgsException {
        HashMap<String, String> args = new HashMap<>();

        String query = uri.getQuery();
        String[] mixedArgs = query.split("&");

        int kvlength;
        String[] kv;

        for (String arg: mixedArgs) {

            kvlength = arg.split("=").length;

            if (kvlength < 2) {
                throw new EmptyArgException(uri.toString());
            } else if (kvlength > 2) {
                throw new UriUnescapedArgsException(uri.toString());
            }

            kv = arg.split("=");
            args.put(kv[0], kv[1]);
        }
        return args;
    }
}
