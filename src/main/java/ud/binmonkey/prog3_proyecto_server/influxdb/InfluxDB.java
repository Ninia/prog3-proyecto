package ud.binmonkey.prog3_proyecto_server.influxdb;

import ud.binmonkey.prog3_proyecto_server.common.Pair;
import ud.binmonkey.prog3_proyecto_server.common.network.HttpMethods;
import ud.binmonkey.prog3_proyecto_server.common.network.Request;
import ud.binmonkey.prog3_proyecto_server.common.network.URI;

import java.util.ArrayList;

public class InfluxDB {


    public static void createDataBase(String db) {

        try {
            Request.REST.sendRequest(HttpMethods.GET, URI.getHost("influxdb"), URI.getPort("influxdb"),
                    "/query", new Pair<>("q", "CREATE%20DATABASE%20" + db));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void writePoints(String db, ArrayList<Point> points) {

        StringBuilder payload = new StringBuilder();
        boolean notNull = false;
        for (Point point : points) {
            if (point != null) {
                notNull = true;
                payload.append(point.getMeasurement()).append(" ");
                for (Object key : point.getTags().keySet()) {
                    payload.append("").append(key.toString()).append("=").append(point.getTags().get(key.toString()));
                }
                for (Object key : point.getFields().keySet()) {
                    payload.append(",").append(key.toString()).append("=").append(point.getFields().get(key.toString()));
                }
                payload.append(" ").append(point.getTime()).append("\n");
            }
        }
        if (notNull) {
            try {
                Request.REST.sendRequest(HttpMethods.POST, URI.getHost("influxdb"), URI.getPort("influxdb"),
                        "/write", payload.toString(), new Pair<>("db", db), new Pair<>("precision", "ms"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        InfluxDB.createDataBase("test");
    }
}
