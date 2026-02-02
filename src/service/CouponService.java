package service;

import database.DBConnection;
import models.Coupon;

import java.sql.*;

public class CouponService {

    public Coupon createCoupon(String code, double discount) {
        String sql = "INSERT INTO coupons (code, discount, active) VALUES (?, ?, true) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            stmt.setDouble(2, discount);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Coupon(rs.getLong(1), code, discount, true);
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Coupon getCouponByCode(String code) {
        String sql = "SELECT id, code, discount, active FROM coupons WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Coupon(rs.getLong("id"), rs.getString("code"),
                        rs.getDouble("discount"), rs.getBoolean("active"));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public double applyDiscount(double price, Coupon coupon) {
        if (coupon == null || !coupon.isActive()) return price;
        return price * (1 - coupon.getDiscount() / 100);
    }
}
