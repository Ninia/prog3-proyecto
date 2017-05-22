package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class UriUnescapedArgsException extends Exception {
    public UriUnescapedArgsException(String uri) {
        super("Unescaped characters found in URI: `" + uri + "`");
    }
}
