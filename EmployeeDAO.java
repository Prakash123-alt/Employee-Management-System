// src/main/java/com/ems/dao/EmployeeDAO.java
package com.ems.dao;

import com.ems.model.Employee;
import com.ems.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // --- CREATE ---
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees(first_name, last_name, email, phone_number, hire_date, job_title, salary) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPhoneNumber());
            pstmt.setString(5, employee.getHireDate().toString()); // Convert LocalDate to String for DB
            pstmt.setString(6, employee.getJobTitle());
            pstmt.setDouble(7, employee.getSalary());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setId(generatedKeys.getInt(1)); // Set the auto-generated ID back to the object
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            // Specific error for unique constraint violation (email)
            if (e.getMessage().contains("UNIQUE constraint failed: employees.email")) {
                System.err.println("Error: An employee with this email already exists.");
            }
        }
        return false;
    }

    // --- READ ALL ---
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, email, phone_number, hire_date, job_title, salary FROM employees";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(new Employee(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    LocalDate.parse(rs.getString("hire_date")), // Convert String from DB to LocalDate
                    rs.getString("job_title"),
                    rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all employees: " + e.getMessage());
        }
        return employees;
    }

    // --- READ BY ID ---
    public Employee getEmployeeById(int id) {
        String sql = "SELECT id, first_name, last_name, email, phone_number, hire_date, job_title, salary FROM employees WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        LocalDate.parse(rs.getString("hire_date")),
                        rs.getString("job_title"),
                        rs.getDouble("salary")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee by ID: " + e.getMessage());
        }
        return null; // Employee not found
    }

    // --- UPDATE ---
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
                     "hire_date = ?, job_title = ?, salary = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPhoneNumber());
            pstmt.setString(5, employee.getHireDate().toString());
            pstmt.setString(6, employee.getJobTitle());
            pstmt.setDouble(7, employee.getSalary());
            pstmt.setInt(8, employee.getId()); // WHERE clause

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            if (e.getMessage().contains("UNIQUE constraint failed: employees.email")) {
                System.err.println("Error: An employee with this email already exists.");
            }
        }
        return false;
    }

    // --- DELETE ---
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
        return false;
    }

    // --- Reporting ---
    public int getEmployeeCount() {
        String sql = "SELECT COUNT(*) FROM employees";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee count: " + e.getMessage());
        }
        return 0;
    }
}