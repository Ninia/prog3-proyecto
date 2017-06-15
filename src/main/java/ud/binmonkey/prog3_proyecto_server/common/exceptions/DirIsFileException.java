package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class DirIsFileException extends Exception {
    public DirIsFileException(String dir) {
        super("Requested directory: " + dir + " is a file.");
    }
}
