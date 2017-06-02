package ud.binmonkey.prog3_proyecto_server.users.attributes;

public enum Language {
    EN("en"), ES("es"), EU("eu");

    private final String lang;

    Language(String lang){
        this.lang = lang;
    }

    @Override
    public String toString() {
        return lang;
    }
}
