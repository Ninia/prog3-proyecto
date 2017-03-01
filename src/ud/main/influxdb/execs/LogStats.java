package ud.main.influxdb.execs;

import ud.main.influxdb.monitoring.InfluxDB;
import ud.main.influxdb.monitoring.Point;
import ud.main.influxdb.monitoring.Log;

import java.util.ArrayList;

/** Automatically logs sys stats to InfluxDB
 *
 */
public class LogStats {
    public static void main(String[] args) {
        System.out.println("");
        String db = "server_stats";
        InfluxDB.createDataBase(db);
        while (true) {
            System.out.print("\rWriting points.    ");
            ArrayList<Point> points = Log.generatePoints(10, 5);
            InfluxDB.writePoints(db, points);
            System.out.print("\rWriting points .");
        }
    }
}
