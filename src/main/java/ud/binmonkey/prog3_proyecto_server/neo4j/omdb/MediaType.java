package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

public enum MediaType {
    movie ("movie"),
    series ("series"),
    episode ("episode"),
    all ("all");

    private final String name;

    private MediaType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
