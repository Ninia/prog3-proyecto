package main;

import sys.monitoring.influxdb.InfluxDB;
import sys.monitoring.influxdb.Point;
import sys.monitoring.influxdb.Log;

import java.util.ArrayList;

/** Automatically logs sys stats to InfluxDB
 *
 */
public class LogStats {
    public static void main(String[] args) {
        String db = "server_stats";
        InfluxDB.createDataBase(db);
        while (true) {
            ArrayList<Point> points = Log.generatePoints(10, 5);
            InfluxDB.writePoints(db, points);
        }
    }
}
