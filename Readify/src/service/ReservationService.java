package service;

import database.DBConnection;
import models.Book;
import models.Reservation;
import models.Rental;
import models.User;

import java.sql.*;
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

    public void rentBook(User user, Book book, int days) {
        if (book.getStock() <= 0) throw new RuntimeException("Not enough stock");
        String sqlUpdate = "UPDATE books SET stock = stock - 1 WHERE id = ?";
        String sqlInsert = "INSERT INTO rentals (user_id, book_id, days) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {

            stmtUpdate.setLong(1, book.getId());
            stmtUpdate.executeUpdate();

            stmtInsert.setLong(1, user.getId());
            stmtInsert.setLong(2, book.getId());
            stmtInsert.setInt(3, days);
            stmtInsert.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Reservation> getReservations() {
        List<Reservation> list = new ArrayList<>();
        return list;
    }

    public List<Rental> getRentals() { return new ArrayList<>(); }
}
