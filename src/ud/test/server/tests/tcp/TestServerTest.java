package ud.test.server.tests.tcp;

import ud.main.client.TestClient;
import ud.main.server.tests.tcp.TestServer;

public class TestServerTest {

    private TestServer tServer;
    private TestClient tClient1;

    @org.junit.Before
    public void setUp() throws Exception {
        tServer = new TestServer();
        tClient1 = new TestClient();

    }

    @org.junit.Test
    public void basicInteraction() throws Exception {
        tServer.start();
        tClient1.start();
        Thread.sleep(5000);
    }
}