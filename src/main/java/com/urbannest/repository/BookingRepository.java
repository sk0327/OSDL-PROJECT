package com.urbannest.repository;

import com.urbannest.models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public synchronized void addBooking(Booking booking) {
        String sql = "INSERT INTO bookings(customer_name, customer_contact, room_number, booking_date) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, booking.getCustomerName());
            pstmt.setString(2, booking.getCustomerContact());
            pstmt.setString(3, booking.getRoomNumber());
            pstmt.setString(4, booking.getBookingDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }

    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM bookings";
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_contact"),
                        rs.getString("room_number"),
                        rs.getString("booking_date")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }

    public void removeBookingByRoom(String roomNumber) {
        String sql = "DELETE FROM bookings WHERE room_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing booking: " + e.getMessage());
        }
    }
}
