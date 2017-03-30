package ud.binmonkey.prog3_proyecto_server.influxdb;

import static org.junit.Assert.*;

public class LogTest {
    @org.junit.Test
    public void getCPUUsagePoint() throws Exception {
        if (System.getProperty("os.name").equals("Linux")) {
            assertNotNull(Log.getCPUUsagePoint());
        }
    }

    @org.junit.Test
    public void getCPUTemperaturePoint() throws Exception {
        if (System.getProperty("os.name").equals("Linux")) {
            assertNotNull(Log.getCPUUsagePoint());
        }
    }
}