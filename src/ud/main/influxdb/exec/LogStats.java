package ud.main.influxdb.exec;

import ud.main.influxdb.monitor.InfluxDB;
import ud.main.influxdb.monitor.Log;
import ud.main.influxdb.monitor.Point;

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
