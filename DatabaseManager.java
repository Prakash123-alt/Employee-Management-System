// src/main/java/com/ems/util/DatabaseManager.java
package com.ems.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:employee_management.db"; // SQLite DB file name

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "first_name TEXT NOT NULL," +
                         "last_name TEXT NOT NULL," +
                         "email TEXT UNIQUE NOT NULL," +
                         "phone_number TEXT," +
                         "hire_date TEXT NOT NULL," + // Stored as YYYY-MM-DD
                         "job_title TEXT NOT NULL," +
                         "salary REAL NOT NULL" +
                         ");";
            stmt.execute(sql);
            System.out.println("Database 'employee_management.db' initialized and 'employees' table ensured.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}