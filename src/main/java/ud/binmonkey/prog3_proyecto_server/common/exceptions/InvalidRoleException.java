package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class InvalidRoleException extends Exception {
    public InvalidRoleException(String role) {
        super("Invalid role: `" + role + "`");
    }
}
