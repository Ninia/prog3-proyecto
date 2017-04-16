package ud.binmonkey.prog3_proyecto_server.tcp;

import ud.binmonkey.prog3_proyecto_server.common.network.URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TestServer based on KnockKnockServer from
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockServer.java
 */
public class TestServer extends Thread {

    private InetAddress clientAddress;

    private int serverPort = URI.getPort("test-server");
    private int clientPort;

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    public TestServer() {

        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server - Listening at port: " + serverPort);
        } catch (IOException e) {
            System.err.println("Server - Could not listen on port: " + serverPort);
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {

        TestServer tServer = new TestServer();
        tServer.start();

    }

    /**
     * Listen for incoming Client connections
     */
    private void listen() {
        try {
            clientSocket = serverSocket.accept();
            System.err.println("Server - Connection established with: " + clientSocket.getInetAddress());
            startThread(); /* Calls startThread method */

        } catch (IOException e) {
            System.err.println("Server - Couldn't establish connection with client");
            System.exit(1);
        }
    }

    /**
     * Creates and starts a new ThreadConnection to free the server port
     *
     * @throws IOException
     */
    private void startThread() throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));

        clientPort = Integer.parseInt(in.readLine());
        clientAddress = clientSocket.getInetAddress();
        ThreadConnection threadConnection = new ThreadConnection(clientAddress, clientPort);
        threadConnection.start();
    }

    public void close() throws IOException {
        serverSocket.close();
        this.interrupt();

    }

    public void run() {
        listen();
    }
}