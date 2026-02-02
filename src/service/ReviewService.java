package service;

import database.DBConnection;
import models.Book;
import models.Review;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {

    public Review addReview(User user, Book book, int rating, String comment) {
        String sql = "INSERT INTO reviews (user_id, book_id, rating, comment) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, user.getId());
            stmt.setLong(2, book.getId());
            stmt.setInt(3, rating);
            stmt.setString(4, comment);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Review(rs.getLong(1), user, book, rating, comment);
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Review> getBookReviews(Long bookId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.id, r.rating, r.comment, " +
                "u.id as uid, u.username, u.email, u.password, u.role, " +
                "b.id as bid, b.title, b.author, b.price, b.stock " +
                "FROM reviews r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN books b ON r.book_id = b.id " +
                "WHERE b.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getLong("uid"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                Book book = new Book(
                        rs.getLong("bid"),
                        rs.getString("title"),
                        rs.getString("author"),
                        null,
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                reviews.add(new Review(rs.getLong("id"), user, book, rs.getInt("rating"), rs.getString("comment")));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return reviews;
    }

    public double getAverageRating(Long bookId) {
        String sql = "SELECT AVG(rating) as avg FROM reviews WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("avg");

        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }
}
