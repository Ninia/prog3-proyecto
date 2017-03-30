package ud.binmonkey.prog3_proyecto_server.common;

/**
 *  Create or compare indexed pairs of strings
 */
public class Pair<String> {

    private String key;
    private String value;

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }

    @Override
    public int hashCode() { return key.hashCode() ^ value.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return this.key.equals(pair.getKey()) &&
                this.value.equals(pair.getValue());
    }

}