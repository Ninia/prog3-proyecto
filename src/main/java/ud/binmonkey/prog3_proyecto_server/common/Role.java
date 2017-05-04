package ud.binmonkey.prog3_proyecto_server.common;

public enum Role {
    ADMIN("admin"), MOD("mod"), USER("user");

    private final String role;
    Role(String role) {
        this.role = role;
    }
}
