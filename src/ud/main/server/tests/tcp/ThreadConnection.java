package ud.main.server.tests.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ThreadConnection extends Thread {

    private InetAddress clientAddress;
    private int clientPort;

    private Socket clientSocket;

    public ThreadConnection(InetAddress address, int port) {

        clientAddress = address;
        clientPort = port;
        try {
            clientSocket = new Socket(address, clientPort);
            System.out.println("Server: Thread - established connection with " + address);
        } catch (IOException e) {
            System.err.println("Server: Thread - Could not establish connection to " + address);
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

    private void handshake() throws IOException {

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

