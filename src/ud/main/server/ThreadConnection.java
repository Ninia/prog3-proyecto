package ud.main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadConnection extends Thread {

    private int connectionPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public ThreadConnection(int port) {

        connectionPort = port;

        try {
            serverSocket = new ServerSocket(connectionPort);
            System.out.println("Thread: Listening at port: " + connectionPort);
        } catch (IOException e) {
            System.err.println("Thread: Could not listen on port: " + connectionPort);
            System.exit(1);
        }
    }

    public void run() {

        try {
            handshake();
            close();
        } catch (IOException e) {
            e.printStackTrace();
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
}

