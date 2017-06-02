package ud.binmonkey.prog3_proyecto_server.common.security;

import org.junit.Before;
import org.junit.Test;


import java.util.Random;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class PasswordAuthenticationTest {

    private int SIZE = 16;
    private char[] password = new char[SIZE];
    private PasswordAuthentication passAuth;

    @Before
    public void setUp() throws Exception {

        passAuth = new PasswordAuthentication();

        /* Generate random password of length $SIZE */
        for (int i = 0; i < SIZE; i++) {
            password[i] = (char) ((new Random()).nextInt(26) + 'a');
        }
    }

    @Test
    public void hash() throws Exception {
        Matcher m = PasswordAuthentication.testPattern().matcher(
                passAuth.hash(password)
        );
        assertTrue(m.matches());
    }

    @Test
    public void authenticate() throws Exception {
        assertTrue(passAuth.authenticate(password, passAuth.hash(password)));
    }

}