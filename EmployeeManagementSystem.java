// src/main/java/com/ems/main/EmployeeManagementSystem.java
package com.ems.main;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;
import com.ems.util.DatabaseManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementSystem {

    private static EmployeeDAO employeeDAO = new EmployeeDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase(); // Ensure database and table exist

        int choice;
        do {
            displayMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    viewEmployeeById();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
                    showEmployeeSummary();
                    break;
                case 0:
                    System.out.println("Exiting Employee Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Consume the leftover newline
            scanner.nextLine(); // Wait for user to press Enter
        } while (choice != 0);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- Employee Management System ---");
        System.out.println("1. Add New Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. View Employee by ID");
        System.out.println("4. Update Employee Details");
        System.out.println("5. Delete Employee");
        System.out.println("6. Employee Summary");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
            return -1; // Return an invalid choice
        }
    }

    private static void addEmployee() {
        scanner.nextLine(); // Consume newline left-over
        System.out.println("\n--- Add New Employee ---");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email (must be unique): ");
        String email = scanner.nextLine();
        System.out.print("Phone Number (optional): ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Hire Date (YYYY-MM-DD): ");
        LocalDate hireDate = null;
        try {
            hireDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Using current date.");
            hireDate = LocalDate.now();
        }
        System.out.print("Job Title: ");
        String jobTitle = scanner.nextLine();
        System.out.print("Salary: ");
        double salary = 0;
        try {
            salary = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.err.println("Invalid salary. Using 0.0.");
            scanner.nextLine(); // Consume invalid input
        }


        Employee newEmployee = new Employee(firstName, lastName, email, phoneNumber, hireDate, jobTitle, salary);

        if (employeeDAO.addEmployee(newEmployee)) {
            System.out.println("Employee added successfully! ID: " + newEmployee.getId());
        } else {
            System.out.println("Failed to add employee.");
        }
    }

    private static void viewAllEmployees() {
        System.out.println("\n--- All Employees ---");
        List<Employee> employees = employeeDAO.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }

    private static void viewEmployeeById() {
        System.out.println("\n--- View Employee by ID ---");
        System.out.print("Enter Employee ID: ");
        int id = getUserChoice(); // Already handles invalid int input
        if (id == -1) return;

        Employee employee = employeeDAO.getEmployeeById(id);
        if (employee != null) {
            System.out.println(employee);
        } else {
            System.out.println("Employee with ID " + id + " not found.");
        }
    }

    private static void updateEmployee() {
        scanner.nextLine(); // Consume newline
        System.out.println("\n--- Update Employee Details ---");
        System.out.print("Enter Employee ID to update: ");
        int id = getUserChoice();
        if (id == -1) return;
        scanner.nextLine(); // Consume newline after nextInt()

        Employee employeeToUpdate = employeeDAO.getEmployeeById(id);
        if (employeeToUpdate == null) {
            System.out.println("Employee with ID " + id + " not found.");
            return;
        }

        System.out.println("Current Details: " + employeeToUpdate);

        System.out.println("\nEnter new details (leave blank to keep current value):");
        System.out.print("First Name (" + employeeToUpdate.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) employeeToUpdate.setFirstName(firstName);

        System.out.print("Last Name (" + employeeToUpdate.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) employeeToUpdate.setLastName(lastName);

        System.out.print("Email (" + employeeToUpdate.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) employeeToUpdate.setEmail(email);

        System.out.print("Phone Number (" + (employeeToUpdate.getPhoneNumber() != null ? employeeToUpdate.getPhoneNumber() : "N/A") + "): ");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.isEmpty()) employeeToUpdate.setPhoneNumber(phoneNumber);

        System.out.print("Hire Date (YYYY-MM-DD) (" + employeeToUpdate.getHireDate().toString() + "): ");
        String hireDateStr = scanner.nextLine();
        if (!hireDateStr.isEmpty()) {
            try {
                employeeToUpdate.setHireDate(LocalDate.parse(hireDateStr));
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format. Keeping current hire date.");
            }
        }

        System.out.print("Job Title (" + employeeToUpdate.getJobTitle() + "): ");
        String jobTitle = scanner.nextLine();
        if (!jobTitle.isEmpty()) employeeToUpdate.setJobTitle(jobTitle);

        System.out.print("Salary (" + String.format("%.2f", employeeToUpdate.getSalary()) + "): ");
        String salaryStr = scanner.nextLine();
        if (!salaryStr.isEmpty()) {
            try {
                employeeToUpdate.setSalary(Double.parseDouble(salaryStr));
            } catch (NumberFormatException e) {
                System.err.println("Invalid salary format. Keeping current salary.");
            }
        }

        if (employeeDAO.updateEmployee(employeeToUpdate)) {
            System.out.println("Employee updated successfully!");
        } else {
            System.out.println("Failed to update employee.");
        }
    }

    private static void deleteEmployee() {
        System.out.println("\n--- Delete Employee ---");
        System.out.print("Enter Employee ID to delete: ");
        int id = getUserChoice();
        if (id == -1) return;

        if (employeeDAO.deleteEmployee(id)) {
            System.out.println("Employee with ID " + id + " deleted successfully!");
        } else {
            System.out.println("Failed to delete employee with ID " + id + ". It might not exist.");
        }
    }

    private static void showEmployeeSummary() {
        System.out.println("\n--- Employee Summary ---");
        int count = employeeDAO.getEmployeeCount();
        System.out.println("Total number of employees: " + count);
    }
}