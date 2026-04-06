package com.urbannest.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:urbannest.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_number TEXT UNIQUE NOT NULL,"
                + "type TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "status TEXT NOT NULL"
                + ");";

        String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "customer_name TEXT NOT NULL,"
                + "customer_contact TEXT NOT NULL,"
                + "room_number TEXT NOT NULL,"
                + "booking_date TEXT NOT NULL,"
                + "FOREIGN KEY(room_number) REFERENCES rooms(room_number)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createRoomsTable);
            stmt.execute(createBookingsTable);
            
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
