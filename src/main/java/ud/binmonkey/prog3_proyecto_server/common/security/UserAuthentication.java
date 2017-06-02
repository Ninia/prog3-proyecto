package ud.binmonkey.prog3_proyecto_server.common.security;

import ud.binmonkey.prog3_proyecto_server.common.exceptions.AdminEditException;
import ud.binmonkey.prog3_proyecto_server.common.exceptions.InvalidNameException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAuthentication {

    private static String ADMIN = "admin";

    /**
     * Checks if string equals admin and throws exception if it does
     * @param userName username to check
     * @throws AdminEditException username equals admin
     */
    public static void checkAdmin(String userName) throws AdminEditException {
        if (userName.equals(ADMIN)) {
            throw new AdminEditException();
        }
    }

    /**
     * Checks if "admin" is present in array of Strings and throws exception if it does
     * @param userNames usernames to check
     * @throws AdminEditException username equals admin
     */
    public static void checkAdmin(String... userNames) throws AdminEditException {
        for (String userName: userNames){
            if (userName.equals(ADMIN)) {
                throw new AdminEditException();
            }
        }
    }

    /**
     * Check if string contains something that is not a lowercase letter or a digit
     * @param userName username to check
     * @return true if username contains only lowercase letters and digit, false if not
     */
    private static boolean secureName(String userName) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(userName);
        return !m.find();
    }

    /**
     * Runs @secureName() and throws @InvalidNameException if false is returned
     * @param userName username to check
     * @throws InvalidNameException username contained forbidden characters
     */
    public static void isValidName(String userName) throws InvalidNameException {
        if (!secureName(userName)) {
            throw new InvalidNameException(userName);
        }
    }
}
