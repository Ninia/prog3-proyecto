package ud.main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadConnection extends Thread {

    private int connectionPort;
    private Socket clientSocket;

    public ThreadConnection(int port) {

        connectionPort = port;
        try {
            clientSocket = new Socket("127.0.0.1", connectionPort);
            System.out.println("Thread: established connection: " + connectionPort);
        } catch (IOException e) {
            System.err.println("Thread: Could not establish connection " + connectionPort);
            System.exit(1);
        }
    }

    public void start() {

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
        clientSocket.close();
    }
}

