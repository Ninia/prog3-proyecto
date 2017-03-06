package ud.main.influxdb.monitor;

import ud.main.utils.network.Request;
import ud.main.utils.network.URI;

import java.util.ArrayList;


public class InfluxDB {


    public static void createDataBase(String db) {

        try {
            Request.httpPOST(
                    URI.getURI("influxdb") + "/query",
                    "q=CREATE%20DATABASE%20"+ db) ;
        } catch (Exception e) {}

    }

    public static void writePoints(String db, ArrayList<Point> points) {
        String url = URI.getURI("influxdb") + "/write?db=" + db + "&precision=ms";
        String payload = "";
        for (Point point: points) {
            payload += point.getMeasurement() + " ";
            for (Object key: point.getTags().keySet()) {
                payload += "" + key.toString() + "=" + point.getTags().get(key.toString());
            }
            for (Object key: point.getFields().keySet()) {
                payload += "," + key.toString() + "=" + point.getFields().get(key.toString());
            }
            payload += " " + point.getTime() + "\n";
        }
        try {
            Request.httpPOST(url, payload);
        } catch (Exception e) {
            System.out.println("Error writing points.");
        }
    }


    public static void main(String[] args) {
        System.out.println(URI.getURI("influxdb"));
    }
}
