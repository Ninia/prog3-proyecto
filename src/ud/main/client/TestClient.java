package ud.main.client;


import ud.main.utils.network.URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TestClient based on KnockKnockClient from
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockClient.java
 */
public class TestClient {

    private String hostName = URI.getHost("test-main.ud.client");
    private int portNumber = URI.getPort("test-main.ud.client");

    public TestClient() {

    }

    public void createConnection() {
        try {
            Socket serverSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
            String fromServer;
            String fromUser = "SYN-ACK";

            while ((fromServer = in.readLine()).equals("SYN")) {
                try {
                    int port = Integer.parseInt(fromServer);
                    Socket connectionSocket = new Socket(hostName, port);
                } catch (NumberFormatException e) {

                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handshake() {
        try {
            Socket serverSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
            String fromServer;
            String fromUser = "SYN-ACK";

            while ((fromServer = in.readLine()).equals("SYN")) {
                try {
                    int port = Integer.parseInt(fromServer);
                    Socket connectionSocket = new Socket(hostName, port);
                } catch (NumberFormatException e) {

                }
            }

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