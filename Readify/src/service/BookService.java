package service;

import database.DBConnection;
import models.Book;
import models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    public Book addBook(String title, String author, Category category, double price, int stock) {
        String sql = "INSERT INTO books (title, author, category_id, price, stock) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, category.getId().intValue());
            stmt.setDouble(4, price);
            stmt.setInt(5, stock);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new Book(rs.getLong(1), title, author, category, price, stock);

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.author, b.price, b.stock, c.id as cid, c.name as cname " +
                "FROM books b LEFT JOIN categories c ON b.category_id = c.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(rs.getLong("cid"), rs.getString("cname"));
                books.add(new Book(rs.getLong("id"), rs.getString("title"),
                        rs.getString("author"), category, rs.getDouble("price"), rs.getInt("stock")));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    public Book getBookById(Long id) {
        String sql = "SELECT b.id, b.title, b.author, b.price, b.stock, c.id as cid, c.name as cname " +
                "FROM books b LEFT JOIN categories c ON b.category_id = c.id WHERE b.id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category(rs.getLong("cid"), rs.getString("cname"));
                return new Book(rs.getLong("id"), rs.getString("title"),
                        rs.getString("author"), category, rs.getDouble("price"), rs.getInt("stock"));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
