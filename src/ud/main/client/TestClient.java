package ud.main.client;


import ud.main.utils.network.*;
import ud.main.utils.network.URI;

import java.io.*;
import java.net.*;

/**
 * TestClient based on KnockKnockClient from
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockClient.java
 */
public class TestClient {

    private String hostName = URI.getHost("test-client");
    private int portNumber = URI.getPort("test-client");

    public TestClient() {

    }

    public void handshake() {
        try {
            Socket serverSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
            String fromServer;
            String fromUser = "SYN-ACK";

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("CLOSE")) {
                    break;

                } else if (fromServer.equals("ACK")) {
                    fromUser = "CLOSE";
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);

                } else {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Invalid host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {

        TestClient tClient = new TestClient();

        tClient.handshake();

    }
}
