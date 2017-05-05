package ud.binmonkey.prog3_proyecto_server.influxdb;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class Point {
    protected long time;
    private String measurement;
    private HashMap<String, ?> tags;
    private HashMap<String, ?> fields;

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
        StringBuilder out = new StringBuilder();
        out.append("measurement: \"").append(measurement).append("\",\n");
        out.append("time: ").append(time).append(" ms,\n");
        out.append("fields: {\n");
        short len = (short) fields.size(); /* correct field size shouldn't be too big for short. */
        short count = 1;
        for (String field : fields.keySet()) {
            out.append("\t").append(field).append(": ").append(fields.get(field));
            if (len > count) {
                out.append(",");
                count += 1; /* primitives. are. immutable. */
            }
            out.append("\n");
        }
        out.append("},\n");
        out.append("tags: {\n");
        len = (short) tags.size(); /* correct field size shouldn't be too big for short. */
        count = 1;
        for (String tag : tags.keySet()) {
            out.append("\t").append(tag).append(": ").append(tags.get(tag));
            if (len > count) {
                out.append(",");
                count += 1; /* primitives. are. immutable. */
            }
            out.append("\n");
        }
        out.append("}\n");
        return out.toString();
    }
}
