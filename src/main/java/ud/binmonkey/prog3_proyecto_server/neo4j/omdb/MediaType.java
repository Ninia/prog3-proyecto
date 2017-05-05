package ud.binmonkey.prog3_proyecto_server.neo4j.omdb;

public enum MediaType {
    MOVIE("movie"),
    SERIES("series"),
    EPISODE("episode"),
    ALL("all");

    private final String name;

    MediaType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
