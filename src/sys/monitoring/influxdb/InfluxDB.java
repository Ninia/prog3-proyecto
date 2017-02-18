package sys.monitoring.influxdb;

import sys.config.InfluxConfig;
import utils.HttpUrlConnection;

import java.util.ArrayList;

public class InfluxDB {

    public static void createDataBase(String db) {
        try {
            HttpUrlConnection.sendPost(
                    InfluxConfig.getURL() + ":" + InfluxConfig.getPORT() + "/query",
                    "q=CREATE%20DATABASE%20"+ db) ;
        } catch (Exception e) {}
    }

    public static void writePoints(String db, ArrayList<Point> points) {
        String url = InfluxConfig.getURL() + ":" + InfluxConfig.getPORT() + "/write?db=" + db + "&precision=millis";
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
            HttpUrlConnection.sendPost(url, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
