package ud.binmonkey.prog3_proyecto_server.common.security;

import java.util.HashMap;
import java.util.Random;

public enum SessionHandler {
    INSTANCE;

    private final HashMap<String, Session> sessions = new HashMap<>();
    private static final long KEEPALIVE = 5 * 60 * 1000;  /* min * sec * millis */

    public class Session {
        private String userName;
        private String token;
        private long keepAlive;
        private long lastUpdate;

        /**
         * User session. If same user starts new session previous one is deleted.
         * @param userName username
         * @param token token assigned to username
         * @param keepAlive in millis: keepAlive time for session if time is not updated
         * @param lastUpdate in millis: last time session was updated
         */
        public Session(String userName, String token, long keepAlive, long lastUpdate) {
            this.userName = userName;
            this.token = token;
            this.keepAlive = keepAlive;
            this.lastUpdate = lastUpdate;
        }

        public String getUserName() {
            return userName;
        }

        public String getToken() {
            return token;
        }

        public long getKeepAlive() {
            return keepAlive;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setKeepAlive(long keepAlive) {
            this.keepAlive = keepAlive;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }

    /**
     * Update lastUpdate of user with current time
     * @param userName username of user to be updated
     */
    public void userActivity(String userName) {
        sessions.get(userName).setLastUpdate(System.currentTimeMillis());
    }


    /**
     * Assigns one token of length @size to a user session and returns that token
     * @param size size of token
     * @param userName username starting session
     * @return token
     */
    public String generateSessionToken(int size, String userName) {

        /* Generate random password of length $size */
        char[] token = new char[size];
        for (int i = 0; i < size; i++) {
            token[i] = (char) ((new Random()).nextInt(26) + 'a');
        }

        try {
            this.sessions.remove(userName);
        } catch (Exception e) {} /* TODO: specify exception to item not found */

        this.sessions.put(userName, new Session(userName, new String(token), KEEPALIVE, System.currentTimeMillis()));
        return new String(token);
    }

    /**
     * Assigns one token to a user session and returns that token (default token size)
     * @param userName userName starting session
     * @return token
     */
    public String generateSessionToken(String userName) {
        return this.generateSessionToken(32, userName);
    }

    public boolean validToken(String userName, String token) {
        Session s = sessions.get(userName);
        if (s != null) {
            return s.getToken().equals(token);
        }
        return false;
    }

    public HashMap<String, Session> getSessions() {
        return sessions;
    }

    public static long getKEEPALIVE() {
        return KEEPALIVE;
    }
}
