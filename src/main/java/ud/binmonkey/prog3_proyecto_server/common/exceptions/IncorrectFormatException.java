package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class IncorrectFormatException extends Exception {
    public IncorrectFormatException(String format, String expectedFormat) {
        super("Incorrect format in '" + format + "', expected format was: " + expectedFormat);
    }
}
