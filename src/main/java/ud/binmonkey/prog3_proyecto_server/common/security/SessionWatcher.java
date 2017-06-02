package ud.binmonkey.prog3_proyecto_server.common.security;

/**
 * Daemon that monitors and manages session timeouts
 */
public class SessionWatcher {

    /* SessionHandler to watch */
    private SessionHandler sessionHandler;
    /* Time to wait before each revision */
    private long PERIOD;

    public SessionWatcher(SessionHandler sessionHandler, long period) {
        this.sessionHandler = sessionHandler;
        this.PERIOD = period;
    }

    /**
     * Default period
     * @param sessionHandler Session sessionHandler to watch
     */
    public SessionWatcher(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
        this.PERIOD = 5000; /* 5 seconds */
    }

    /**
     * SHOULD BE RUN IN BACKGROUND
     * Periodically checks SessionHandler sessions and removes the ones
     * that timed out.
     */
    public void watch() {
        while (true) {
            for (String session: sessionHandler.getSessions().keySet()) {
                long currentTime = System.currentTimeMillis();
                long keepAlive = sessionHandler.getSessions().get(session).getKeepAlive();
                long lastUpdate = sessionHandler.getSessions().get(session).getLastUpdate();

                /* if session timed out */
                if (currentTime > lastUpdate + keepAlive) {
                    /* remove session */
                    sessionHandler.getSessions().remove(session);
                }
            }
            try {
                Thread.sleep(this.PERIOD);
            } catch (InterruptedException e) {}
        }
    }
}
