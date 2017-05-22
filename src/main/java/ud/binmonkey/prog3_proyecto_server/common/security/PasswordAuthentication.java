package ud.binmonkey.prog3_proyecto_server.common.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordAuthentication {

    private static final String ID = "$42$";
    private static final int DEFAULT_COST = 16;
    private static final String ALGORITH = "PBKDF2WithHmacSHA1";
    private static final int SIZE = 128;
    private static final Pattern pattern = Pattern.compile("\\$42\\$(\\d\\d?)\\$(.{43})");
    private final SecureRandom random;
    private final int cost;

    public PasswordAuthentication() {
        this(DEFAULT_COST);
    }

    public PasswordAuthentication(int cost){
        iterations(cost);
        this.cost = cost;
        this.random = new SecureRandom();
    }

    /**
     * from http://stackoverflow.com/a/2861125/7557549
     * calculates number of iterations for specified cost
     */
    private static int iterations(int cost) {
        if ((cost & ~0x1E) != 0) {
            throw new IllegalArgumentException("cost" + cost);
        }
        return 1 << cost;
    }

    public String hash(char[] password) {
        byte[] salt = new byte[SIZE / 8];
        random.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt, 1 << cost);
        byte[] hash = new byte[salt.length + dk.length];
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);

        return ID + cost + '$' + (Base64.getUrlEncoder().withoutPadding()).encodeToString(hash);
    }

    @Deprecated
    public String hash(String password) {
        return hash(password.toCharArray());
    }

    /** Authenticate user
     *
     * @param password password introduced by user
     * @param token stored hash
     * @return true if authenticated, false if not
     */
    public boolean authenticate(char[] password, String token) {

        /* check token validity */
        Matcher m = pattern.matcher(token);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid token format");
        }
        int iterations = iterations(Integer.parseInt(m.group(1 /* cost */)));
        byte[] hash = Base64.getUrlDecoder().decode(m.group(2 /* hash */));
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
        byte[] check = pbkdf2(password, salt, iterations);

        int zero = 0;
        for (int idx = 0; idx < check.length; ++idx) {
             zero |= hash[salt.length + idx] ^ check[idx];
        }
        return zero == 0;
    }

    /** Authenticate user
     *
     * @param password password introduced by user
     * @param token stored hash
     * @return true if authenticated, false if not
     */
    public boolean authenticate(char[] password, char[] token) {
        return authenticate(password, new String(token));
    }

    /** Authenticate user
     *
     * @param password password introduced by user
     * @param token stored hash
     * @return true if authenticated, false if not
     */
    @Deprecated
    public boolean authenticate(String password, char[] token) {
        return authenticate(password.toCharArray(), new String(token));
    }

    /** Authenticate user
     *
     * @param password password introduced by user
     * @param token stored hash
     * @return true if authenticated, false if not
     */
    @Deprecated
    public boolean authenticate(String password, String token) {
        return authenticate(password.toCharArray(), token.toCharArray());
    }


    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITH);
            return f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITH, e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Invalid SecretKeyFactory", e);
        }
    }

    protected static Pattern testPattern() {
        return pattern;
    }


}
