package server;

/**
 * TestProtocol based on KnockKnockProtocol from
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockProtocol.java
 */
public class TestProtocol {
    private static final int WAITING = 0;
    private static final int SENTHANDSHAKE = 1;

    private int state = WAITING;

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            theOutput = "SYN";
            state = SENTHANDSHAKE;
        } else if (state == SENTHANDSHAKE) {
            if (theInput.equalsIgnoreCase("SYN-ACK")) {
                theOutput = "ACK";
            } else if (theInput.equalsIgnoreCase("CLOSE")) {
                theOutput = "CLOSE";
                state = WAITING;
            } else {
                theOutput = "Error: Unexpected response";
            }
        }
        return theOutput;
    }
}
