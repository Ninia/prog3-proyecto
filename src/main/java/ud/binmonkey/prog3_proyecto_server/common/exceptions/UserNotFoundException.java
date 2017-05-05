package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String userName) {
        super("User `" + userName + "` not found.");
    }
}
