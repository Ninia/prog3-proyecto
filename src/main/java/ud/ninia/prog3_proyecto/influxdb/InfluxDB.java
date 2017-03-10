package ud.ninia.prog3_proyecto.influxdb;

import ud.ninia.prog3_proyecto.common.Pair;
import ud.ninia.prog3_proyecto.utils.network.HttpMethods;
import ud.ninia.prog3_proyecto.utils.network.Request;
import ud.ninia.prog3_proyecto.utils.network.URI;

import java.util.ArrayList;


public class InfluxDB {


    public static void createDataBase(String db) {

        try {
            Request.REST.sendRequest(HttpMethods.GET, URI.getHost("influxdb"), URI.getPort("influxdb"),
                    "/query", new Pair<>("q", "CREATE%20DATABASE%20"+ db));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writePoints(String db, ArrayList<Point> points) {

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
            Request.REST.sendRequest(HttpMethods.POST, URI.getHost("influxdb"), URI.getPort("influxdb"),
                    "/write", payload, new Pair<>("db", db), new Pair<>("precision", "ms"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        InfluxDB.createDataBase("test");
    }
}
