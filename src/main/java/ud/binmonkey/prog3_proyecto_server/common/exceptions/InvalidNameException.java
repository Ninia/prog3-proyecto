package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class InvalidNameException extends Exception {
    public InvalidNameException(String userName) {
        super("Invalid username: `" + userName + "`");
    }
}
