package ud.binmonkey.prog3_proyecto_server.common.time;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Clock {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String getFullTime() {
        Date date = Date.from(Instant.now());

        return df.format(date);
    }
}
