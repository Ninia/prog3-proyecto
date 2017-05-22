package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class EmptyArgException extends Exception {
    public EmptyArgException(String uri) {
        super("Empty arg found in URI: `" + uri + "`");
    }
}
