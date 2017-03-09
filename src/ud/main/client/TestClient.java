package ud.main.client;


import ud.main.utils.network.URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TestClient based on KnockKnockClient from
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockClient.java
 */
public class TestClient extends Thread {

    private String hostName = URI.getHost("test-server");

    private int serverPort = URI.getPort("test-server");
    private int clientPort = URI.getPort("test-client");

    private ServerSocket clientSocket;
    private Socket serverSocket = null;


    public TestClient() {
        try {
            clientSocket = new ServerSocket(clientPort);
            System.out.println("Client - Listening at port: " + clientPort);
        } catch (IOException e) {
            System.err.println("Client - Could not listen on port: " + serverPort);
            System.exit(1);
        }
    }

    /**
     * Constructor for TestClient with custom port
     *
     * @param port - Custom port
     */
    public TestClient(int port) {
        try {
            clientSocket = new ServerSocket(port);
            System.out.println("Client - Listening at port: " + port);
        } catch (IOException e) {
            System.err.println("Client - Could not listen on port: " + port);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {

        TestClient tClient = new TestClient();

        tClient.start();

    }

    /**
     * Gives port to Server
     */
    private void givePort() {
        try {
            Socket serverSocket = new Socket(hostName, serverPort);
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);

            out.println(clientPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listens for new connection with Server
     */
    private void listen() {
        try {
            serverSocket = clientSocket.accept();
            System.out.println("Client - Connection Established.");
        } catch (IOException e) {
            System.err.println("Client - Couldn't establish connection");
            System.exit(1);
        }
    }

    /**
     * Handshake method
     */
    private void handshake() {
        try {
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));
            String fromServer;
            String fromUser = "SYN-ACK";

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Client - Server: " + fromServer);
                if (fromServer.equals("CLOSE")) {
                    break;

                } else if (fromServer.equals("ACK")) {
                    fromUser = "CLOSE";
                    System.out.println("Client - Client: " + fromUser);
                    out.println(fromUser);

                } else {
                    System.out.println("Client - Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Client - Invalid host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Client - Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public void run() {
        givePort();
        listen();
        handshake();
    }
}