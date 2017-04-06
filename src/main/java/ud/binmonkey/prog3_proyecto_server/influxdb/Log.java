package ud.binmonkey.prog3_proyecto_server.influxdb;

import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;
import ud.binmonkey.prog3_proyecto_server.influxdb.monitor.CPU;
import ud.binmonkey.prog3_proyecto_server.influxdb.monitor.Memory;

import java.util.ArrayList;

/**
 * Obtain data about the server contained in Points so it can be logged to InfluxDB
 */
public class Log {

    private static String SYSNAME = DocumentReader.getDoc(
            "conf/System.xml").getElementsByTagName("hostname").item(0).getTextContent();


    /**
     * Static method used for obtaining current CPU usage
     *
     * @return string containing current CPU usage percentage
     */
    public static Point getCPUUsagePoint() {

        Point point = new Point();
        point.setMeasurement("cpu_usage");
        point.getTags().put("host", "\"" + SYSNAME + "\"");
        point.getFields().put("value", CPU.getUsage());
        point.setTime(System.currentTimeMillis());

        return point;
    }


    /**
     * Static method used for obtaining current CPU temperature
     *
     * @return string containing current CPU temperature percentage
     */
    public static Point getCPUTemperaturePoint() {

        Point point = new Point();
        point.setMeasurement("cpu_temperature");
        point.getTags().put("host", "\"" + SYSNAME + "\"");
        point.getFields().put("value", CPU.getTemperature());
        point.setTime(System.currentTimeMillis());

        return point;
    }


    /**
     * Static method used for obtaining current memory usage
     *
     * @return string containing current memory usage percentage
     */
    public static Point getMemUsagePoint() {
        Point point = new Point();
        point.setMeasurement("mem_usage");
        point.getTags().put("host", "\"" + SYSNAME + "\"");
        point.getFields().put("value", Memory.getUsage());
        point.setTime(System.currentTimeMillis());

        return point;
    }


    /**
     * Generates ArrayLists
     *
     * @param n of batch to be sent to InfluxDB
     * @return ArrayList containing the generated points
     */
    public static ArrayList<Point> generatePoints(int n) {

        boolean sensors = false;

        try {
            if (getCPUTemperaturePoint().getFields().get("value") != null) {
                sensors = true;
            }
        } catch (Exception e) {
        }

        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            points.add(getCPUUsagePoint());
            points.add(getCPUTemperaturePoint());
            if (sensors) {
                points.add(getMemUsagePoint());
            }
        }
        return points;
    }


    public static void main(String[] args) {
        String db = "server_stats";
        InfluxDB.createDataBase(db);
        ArrayList<Point> points = generatePoints(10);
        for (Point point : points) {
            System.out.println(point);
        }
        InfluxDB.writePoints(db, points);
    }
}
