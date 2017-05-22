package ud.binmonkey.prog3_proyecto_server.users.attributes;

public enum Role {
    ADMIN("admin"), MOD("mod"), USER("user");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }

}
