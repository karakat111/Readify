package service;

import database.DBConnection;
import models.Category;

import java.sql.*;

public class CategoryService {
    public Category addCategory(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(rs.getLong(1), name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Category getCategoryByName(String name) {
        String sql = "SELECT id, name FROM categories WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(rs.getLong("id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
