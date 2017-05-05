package ud.binmonkey.prog3_proyecto_server.common.exceptions;

public class UnsupportedLanguageException extends Exception {
    public UnsupportedLanguageException(String lang) {
        super("Unsupported language: `" + lang + "`");
    }
}
