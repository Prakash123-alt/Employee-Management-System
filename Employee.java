// src/main/java/com/ems/model/Employee.java
package com.ems.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private String jobTitle;
    private double salary;

    // Constructor for creating new employees (ID is auto-generated by DB)
    public Employee(String firstName, String lastName, String email, String phoneNumber,
                    LocalDate hireDate, String jobTitle, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.jobTitle = jobTitle;
        this.salary = salary;
    }

    // Constructor for retrieving employees from DB (includes ID)
    public Employee(int id, String firstName, String lastName, String email, String phoneNumber,
                    LocalDate hireDate, String jobTitle, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName = lastName; // Typo fix
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.jobTitle = jobTitle;
        this.salary = salary;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getSalary() {
        return salary;
    }

    // Setters (if you need to modify Employee objects in memory before updating DB)
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "ID: " + id +
               ", Name: " + firstName + " " + lastName +
               ", Email: " + email +
               ", Phone: " + (phoneNumber != null ? phoneNumber : "N/A") +
               ", Hire Date: " + hireDate.format(formatter) +
               ", Job Title: " + jobTitle +
               ", Salary: $" + String.format("%.2f", salary);
    }
}