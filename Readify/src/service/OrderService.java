package service;

import database.DBConnection;
import models.Book;
import models.Order;
import models.OrderItem;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public Order createOrder(User user, List<OrderItem> items) {
        String sqlOrder = "INSERT INTO orders (user_id) VALUES (?) RETURNING id";
        String sqlItem = "INSERT INTO order_items (order_id, book_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder);
             PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {

            stmtOrder.setLong(1, user.getId());
            ResultSet rs = stmtOrder.executeQuery();
            if (rs.next()) {
                long orderId = rs.getLong(1);
                for (OrderItem item : items) {
                    stmtItem.setLong(1, orderId);
                    stmtItem.setLong(2, item.getBook().getId());
                    stmtItem.setInt(3, item.getQuantity());
                    stmtItem.executeUpdate();
                }
                return new Order(orderId, user, items);
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
