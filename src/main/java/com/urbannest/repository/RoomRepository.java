package com.urbannest.repository;

import com.urbannest.models.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms(room_number, type, price, status) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setString(4, room.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
        }
    }

    public List<Room> getAllRooms() {
        String sql = "SELECT * FROM rooms";
        List<Room> rooms = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("status")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        return rooms;
    }

    public void updateRoomStatus(String roomNumber, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE room_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, roomNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating room status: " + e.getMessage());
        }
    }

    public int getTotalRooms() {
        String sql = "SELECT COUNT(*) FROM rooms";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
             System.err.println(e.getMessage());
        }
        return 0;
    }

    public int getAvailableRooms() {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status = 'Available'";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public void deleteRoom(String roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
        }
    }
}
