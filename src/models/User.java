package models;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(Long id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role.toUpperCase();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role.toUpperCase(); }


    public boolean isAdminOrManager() { return role.equals("ADMIN") || role.equals("MANAGER"); }
    public boolean canRent() { return role.equals("USER") || isAdminOrManager(); }
    public boolean canReserve() { return role.equals("USER") || isAdminOrManager(); }

    @Override
    public String toString() {
        return String.format("User ID:%d | %s | %s | Role:%s", id, username, email, role);
    }
}
