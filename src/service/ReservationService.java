package service;

import database.DBConnection;
import models.Book;
import models.Reservation;
import models.Rental;
import models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    public void reserveBook(User user, Book book) {
        if (book.getStock() <= 0) throw new RuntimeException("No stock available");
        String sql = "INSERT INTO reservations (user_id, book_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            stmt.setLong(2, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Rental rentBook(User user, Book book, int days) {
        if (book.getStock() <= 0) throw new RuntimeException("Not enough stock");

        String sqlUpdate = "UPDATE books SET stock = stock - 1 WHERE id = ?";
        String sqlInsert = "INSERT INTO rentals (user_id, book_id, days, rented_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP) RETURNING id, rented_at";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {

            stmtUpdate.setLong(1, book.getId());
            stmtUpdate.executeUpdate();

            stmtInsert.setLong(1, user.getId());
            stmtInsert.setLong(2, book.getId());
            stmtInsert.setInt(3, days);

            ResultSet rs = stmtInsert.executeQuery();
            if (rs.next()) {
                LocalDateTime rentedAt = rs.getTimestamp("rented_at").toLocalDateTime();
                return new Rental(rs.getLong("id"), user, book, days, rentedAt);
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Reservation> getReservations() {
        List<Reservation> list = new ArrayList<>();
        return list;
    }

    public List<Rental> getRentals() {
        return new ArrayList<>();
    }


    public List<Rental> getUserRentals(Long userId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.id, r.days, r.rented_at, b.id as bid, b.title, b.author, b.price, b.stock, " +
                "u.id as uid, u.username, u.email, u.password, u.role " +
                "FROM rentals r " +
                "JOIN books b ON r.book_id = b.id " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
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
                LocalDateTime rentedAt = rs.getTimestamp("rented_at").toLocalDateTime();
                rentals.add(new Rental(rs.getLong("id"), user, book, rs.getInt("days"), rentedAt));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return rentals;
    }
}
