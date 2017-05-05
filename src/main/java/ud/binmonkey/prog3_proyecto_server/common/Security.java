package ud.binmonkey.prog3_proyecto_server.common;

import ud.binmonkey.prog3_proyecto_server.common.exceptions.AdminEditException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.InvalidNameException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Security {

    private static String ADMIN = "admin";

    public static void checkAdmin(String userName) throws AdminEditException {
        if (userName.equals(ADMIN)) {
            throw new AdminEditException();
        }
    }

    public static void checkAdmin(String... userNames) throws AdminEditException {
        for (String userName: userNames){
            if (userName.equals(ADMIN)) {
                throw new AdminEditException();
            }
        }
    }

    private static boolean secureName(String userName) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(userName);
        return !m.find();
    }

    public static void isValidName(String userName) throws InvalidNameException {
        if (!secureName(userName)) {
            throw new InvalidNameException(userName);
        }
    }
}
