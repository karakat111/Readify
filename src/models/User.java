package models;

public class User {
    private Long id;
    private String username;
    private String role; // ADMIN, MANAGER, USER

    public User() {}

    public User(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role.toUpperCase();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role.toUpperCase(); }


    public boolean isAdminOrManager() { return role.equals("ADMIN") || role.equals("MANAGER"); }
    public boolean canRent() { return role.equals("USER") || isAdminOrManager(); }
    public boolean canReserve() { return role.equals("USER") || isAdminOrManager(); }

    @Override
    public String toString() {
        return String.format("User ID:%d | %s | Role:%s", id, username, role);
    }
}
