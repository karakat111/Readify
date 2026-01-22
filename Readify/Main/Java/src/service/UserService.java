package service;

import database.DBConnection;
import models.User;

import java.sql.*;

public class UserService {

    public User addOrGetUser(String username, String role) {
        User user = getUserByUsername(username);
        if (user != null) return user;
        String sql = "INSERT INTO users (username, role) VALUES (?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new User(rs.getLong(1), username, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, role FROM users WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new User(rs.getLong("id"), rs.getString("username"), rs.getString("role"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
