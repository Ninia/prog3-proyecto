package sys.monitoring.influxdb;

/* TODO: create http requests
 * TODO: Generate points measuring system usages
 */

import sys.config.InfluxConfig;
import sys.config.SystemInfo;
import sys.stats.usage.CPU;
import sys.stats.usage.Memory;
import utils.HttpUrlConnection;

import java.util.ArrayList;

public class Log {

    public static Point getCPUPoint(){
        Point point = new Point();
        point.setMeasurement("cpu_usage");
        point.getTags().put("host", "\"" + SystemInfo.getSYSNAME() + "\"");
        point.getFields().put("value", CPU.getUsage());
        point.setTime(System.currentTimeMillis());

        return point;
    }

    public static Point getMemPoint(){
        Point point = new Point();
        point.setMeasurement("mem_usage");
        point.getTags().put("host", "\"" + SystemInfo.getSYSNAME() + "\"");
        point.getFields().put("value", Memory.getUsage());
        point.setTime(System.currentTimeMillis());

        return point;
    }

    public static ArrayList<Point> generatePoints(int n,int s) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i=0; i<n; i++){
            points.add(getCPUPoint());
            points.add(getMemPoint());
            try {
                Thread.sleep(s * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return points;
    }


    public static void main(String[] args) {
        String db = "server_stats";
        InfluxDB.createDataBase(db);
        ArrayList<Point> points = generatePoints(2, 0);
        InfluxDB.writePoints(db, points);
    }
}
