package ud.main.server;


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

    private int serverPort = 3379;
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
        } catch (IOException e) {
            System.err.println("Connection Established.");
            System.exit(1);
        }
    }

    public void handshake() throws IOException {

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;

        TestProtocol tProtocol = new TestProtocol();

        outputLine = tProtocol.processInput(null);
        out.println(outputLine);

        while ((inputLine = in.readLine()) != null) {
            outputLine = tProtocol.processInput(inputLine);
            out.println(outputLine);
            if (outputLine.equals("CLOSE"))
                break;
        }

        out.close();
        in.close();

    }

    public void close() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {

        TestServer tServer = new TestServer();

        tServer.listen();
        tServer.handshake();

        tServer.close();
    }
}