package ud.binmonkey.prog3_proyecto_server.common.security;

public class SessionWatcher {
    private SessionHandler sessionHandler;
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
        this.PERIOD = 5000; /* 10 seconds */
    }

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
