package service;

import database.DBConnection;
import models.User;

import java.sql.*;

public class UserService {

    public User register(String username, String email, String password) {
        // Check if email already exists
        if (getUserByEmail(email) != null) {
            throw new RuntimeException("Email already exists");
        }

        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, 'USER') RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong(1), username, email, password, "USER");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User login(String email, String password) {
        String sql = "SELECT id, username, email, password, role FROM users WHERE email=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User addOrGetUser(String username, String role) {
        User user = getUserByUsername(username);
        if (user != null) return user;

        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username + "@readify.com");
            stmt.setString(3, "default123");
            stmt.setString(4, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong(1), username, username + "@readify.com", "default123", role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, email, password, role FROM users WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT id, username, email, password, role FROM users WHERE email=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

