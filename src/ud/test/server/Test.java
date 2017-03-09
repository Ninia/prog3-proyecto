package ud.test.server;

import ud.main.client.TestClient;
import ud.main.server.tests.tcp.TestServer;

public class Test {

    public static void main(String[] args) {
        TestServer tServer = new TestServer();
        TestClient tClient1 = new TestClient();

        tServer.start();
        tClient1.start();
    }
}
