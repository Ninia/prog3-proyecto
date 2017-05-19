package ud.binmonkey.prog3_proyecto_server.common.security;

import java.util.Random;

public class Session {

    public static String generateSessionToken(int size) {

        /* Generate random password of length $size */
        char[] token = new char[size];
        for (int i = 0; i < size; i++) {
            token[i] = (char) ((new Random()).nextInt(26) + 'a');
        }
        return new String(token);
    }

    /**
     * Default size
     * @return token
     */
    public static String generateSessionToken() {
        return generateSessionToken(16);
    }
}
