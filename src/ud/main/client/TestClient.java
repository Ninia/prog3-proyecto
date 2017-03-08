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
public class TestClient {

    private String hostName = URI.getHost("test-server");

    private int serverPort = URI.getPort("test-server");
    private int clientPort = URI.getPort("test-client");

    private ServerSocket clientSocket;
    private Socket serverSocket = null;

    public TestClient() {
        try {
            clientSocket = new ServerSocket(clientPort);
            System.out.println("Listening at port: " + clientPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverPort);
            System.exit(1);
        }
    }

    public void givePort() {
        try {
            Socket serverSocket = new Socket(hostName, serverPort);
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            serverSocket.getInputStream()));
            String inputLine;

            out.println(clientPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            serverSocket = clientSocket.accept();
            System.err.println("Connection Established.");
        } catch (IOException e) {
            System.err.println("Couldn't establish connection");
            System.exit(1);
        }
    }

    public void handshake() {
        try {
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

        tClient.givePort();
        tClient.listen();
        tClient.handshake();

    }
}