package ud.binmonkey.prog3_proyecto_server.influxdb.exec;

import org.w3c.dom.Element;
import ud.binmonkey.prog3_proyecto_server.influxdb.InfluxDB;
import ud.binmonkey.prog3_proyecto_server.influxdb.Log;
import ud.binmonkey.prog3_proyecto_server.influxdb.Point;
import ud.binmonkey.prog3_proyecto_server.common.DocumentReader;


import java.util.ArrayList;

/** Continuously logs server stats to InfluxDB
 *
 */
public class LogStats {

    public static int size;
    public static int time;

    /**
     *  Create specified database and continuously write points to it
     */
    public static void main(String[] args) {

        Element settings = (Element) DocumentReader.getDoc(
                "conf/InfluxDBLog.xml").getElementsByTagName("log").item(0);
        size = Integer.parseInt(settings.getElementsByTagName("size").item(0).getTextContent());
        time = Integer.parseInt(settings.getElementsByTagName("time").item(0).getTextContent());


        System.out.println("");
        String db = "server_stats";
        InfluxDB.createDataBase(db);
        while (true) {
            System.out.print("\rWriting points.    ");
            ArrayList<Point> points = Log.generatePoints(size);
//            InfluxDB.writePoints(db, points);
//            for (Point point: points){
//                System.out.println(point);
//            }
            System.out.print("\rWriting points .");
            try {
                Thread.sleep(time * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
