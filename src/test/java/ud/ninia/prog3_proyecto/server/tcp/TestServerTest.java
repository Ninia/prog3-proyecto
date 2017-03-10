package ud.ninia.prog3_proyecto.server.tcp;

public class TestServerTest {

    private TestServer tServer;
//    private TestClient tClient1;

    @org.junit.Before
    public void setUp() throws Exception {
        tServer = new TestServer();
//        tClient1 = new TestClient();

    }

    @org.junit.Test
    public void basicInteraction() throws Exception {
        tServer.start();
//        tClient1.start();
        Thread.sleep(5000);
    }
}