package ud.binmonkey.prog3_proyecto_server.influxdb;

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

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
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

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        String out = "";
        out += "measurement: \"" + measurement + "\",\n";
        out += "time: " + time + " ms,\n";
        out += "fields: {\n";
        short len = (short) fields.size(); /* correct field size shouldn't be too big for short. */
        short count = 1;
        for (String field : fields.keySet()) {
            out += "\t" + field + ": " + fields.get(field);
            if (len > count) {
                out += ",";
                count += 1; /* primitives. are. immutable. */
            }
            out += "\n";
        }
        out += "},\n";
        out += "tags: {\n";
        len = (short) tags.size(); /* correct field size shouldn't be too big for short. */
        count = 1;
        for (String tag : tags.keySet()) {
            out += "\t" + tag + ": " + tags.get(tag);
            if (len > count) {
                out += ",";
                count += 1; /* primitives. are. immutable. */
            }
            out += "\n";
        }
        out += "}\n";
        return out;
    }
}
