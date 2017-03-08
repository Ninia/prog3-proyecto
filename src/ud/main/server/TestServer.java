package ud.main.server;


import ud.main.utils.network.URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TestServer based on KnockKnockServer from
 * https://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockServer.java
 */
public class TestServer {

    private int serverPort = URI.getPort("test-server");
    private int clientPort;

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    public TestServer() {

        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Listening at port: " + serverPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverPort);
            System.exit(1);
        }
    }

    public void listen() {
        try {
            clientSocket = serverSocket.accept();
            System.err.println("Connection established at port: " + serverPort);

        } catch (IOException e) {
            System.err.println("Couldn't establish connection at port: " + serverPort);
            System.exit(1);
        }
    }

    public void startThread() throws IOException {

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine;


        clientPort = Integer.parseInt(in.readLine());
        System.out.println(clientPort);
        ThreadConnection threadConnection = new ThreadConnection(clientPort);
        threadConnection.start();
    }

    public void close() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {

        TestServer tServer = new TestServer();

        tServer.listen();
        tServer.startThread();
        tServer.close();

    }
}