package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String userName) {
        super("Incorrect password for user `" + userName + "`");
    }
}
