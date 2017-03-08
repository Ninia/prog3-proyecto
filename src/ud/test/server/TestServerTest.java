package ud.test.server;

import ud.main.client.TestClient;
import ud.main.server.TestServer;

public class TestServerTest {

    TestServer tServer;
    TestClient tClient1;

    @org.junit.Before
    public void setUp() throws Exception {
        tServer = new TestServer();
        tClient1 = new TestClient();

    }

    @org.junit.Test
    public void basicInteraction() throws Exception {
        tServer.start();
        tClient1.start();
    }
}