package ud.main.influxdb;

import java.util.HashMap;

public class Point {
    protected long time;
    protected String measurement;
    protected HashMap<String, ?> tags;
    protected HashMap<String, ?> fields;

    public Point() {
        this.tags = new HashMap<>();
        this.fields = new HashMap<>();
    }

    public String getMeasurement() {
        return measurement;
    }

    public HashMap getTags() {
        return tags;
    }

    public HashMap getFields() {
        return fields;
    }

    public long getTime() {
        return time;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
