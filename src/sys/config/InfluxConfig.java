package sys.config;

public class InfluxConfig {
    protected static int PORT = 56786;
    protected static String URL = "http://localhost";

    public static int getPORT() {
        return PORT;
    }

    public static String getURL() {
        return URL;
    }
}
