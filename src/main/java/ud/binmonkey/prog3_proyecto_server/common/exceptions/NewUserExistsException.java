package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class NewUserExistsException extends Exception {
    public NewUserExistsException(String userName) {
        super("User `" + userName + "` already exists.");
    }
}
