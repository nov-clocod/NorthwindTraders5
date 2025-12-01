package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error on required application details to run");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try (Scanner myScanner = new Scanner(System.in)) {
            boolean isDone = false;

            while (!isDone) {
                System.out.println("\nWhat do you want to do?");
                System.out.println("  1) Display all products");
                System.out.println("  2) Display all customers");
                System.out.println("  3) Display all categories");
                System.out.println("  0) Exit");
                System.out.println("Select an option:");
                String userInput = myScanner.nextLine().trim();

                int userChoice = Integer.parseInt(userInput);

                switch (userChoice) {
                    case 1:
                        displayAllProducts(dataSource);
                        break;
                    case 2:
                        displayAllCustomers(dataSource);
                        break;
                    case 3:
                        displayAllCategories(dataSource);
                        System.out.println("\nChoose a categoryID to display products in that category");
                        String userInputCategoryID = myScanner.nextLine().trim();

                        displayCategoryProducts(dataSource, userInputCategoryID);
                        break;
                    case 0:
                        isDone = true;
                        break;
                    default:
                        System.out.println("Invalid Choice!");
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Please check your inputs!");
            System.out.println(ex.getMessage());
        }
    }

    public static void displayCategoryProducts(BasicDataSource dataSource, String categoryID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = """
                    SELECT ProductID, ProductName, UnitPrice, UnitsInStock
                    FROM products
                    WHERE CategoryID = ?
                    """;

            try (Connection connection = dataSource.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(query)
            ) {
                preparedStatement.setString(1, categoryID);

                try (ResultSet results = preparedStatement.executeQuery())
                {
                    System.out.println("\nID  " + " Name" + " ".repeat(37) + "Price   " + "Stock");
                    System.out.println("---- " + "-".repeat(40) + " -------" + " -----");

                    while (results.next()) {
                        int productID = results.getInt(1);
                        String productName = results.getString(2);
                        double productPrice = results.getDouble(3);
                        int productStock = results.getInt(4);

                        System.out.printf("%-4d %-40s %-7.2f %-5d\n",
                                productID, productName, productPrice, productStock);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error occurred!");
            System.out.println(ex.getMessage());
        }
    }

    public static void displayAllCategories(BasicDataSource dataSource) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = """
                    SELECT CategoryID, CategoryName
                    FROM categories
                    """;

            try (Connection connection = dataSource.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(query);

                 ResultSet results = preparedStatement.executeQuery()
            ) {
                System.out.println("\nCategoryID " + "Category" + " ".repeat(8));
                System.out.println("---------- " + "-".repeat(15));

                while (results.next()) {
                    int categoryID = results.getInt(1);
                    String categoryName = results.getString(2);

                    System.out.printf("%-10d %-15s\n",
                            categoryID, categoryName);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error occurred!");
            System.out.println(ex.getMessage());
        }
    }

    public static void displayAllProducts(BasicDataSource dataSource) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = """
                    SELECT ProductID, ProductName, UnitPrice, UnitsInStock
                    FROM products
                    """;

            try (Connection connection = dataSource.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(query);

                 ResultSet results = preparedStatement.executeQuery()
            ) {
                System.out.println("\nID  " + " Name" + " ".repeat(37) + "Price   " + "Stock");
                System.out.println("---- " + "-".repeat(40) + " -------" + " -----");

                while (results.next()) {
                    int productID = results.getInt(1);
                    String productName = results.getString(2);
                    double productPrice = results.getDouble(3);
                    int productStock = results.getInt(4);

                    System.out.printf("%-4d %-40s %-7.2f %-5d\n",
                            productID, productName, productPrice, productStock);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error occurred!");
            System.out.println(ex.getMessage());
        }
    }

    public static void displayAllCustomers(BasicDataSource dataSource) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = """
                    SELECT ContactName, CompanyName, City, Country, Phone
                    FROM customers
                    ORDER BY Country
                    """;

            try (Connection connection = dataSource.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(query);

                 ResultSet results = preparedStatement.executeQuery()
            ) {
                System.out.println("\nContact Name" + " ".repeat(19)
                        + "Company" + " ".repeat(34)
                        + "City" + " ".repeat(12)
                        + "Country" + " ".repeat(9)
                        + "Phone" + " ".repeat(7));
                System.out.println("-".repeat(30) + " "
                        + "-".repeat(40) + " "
                        + "-".repeat(15) + " "
                        + "-".repeat(15) + " "
                        + "-".repeat(12));

                while (results.next()) {
                    String contactName = results.getString(1);
                    String companyName = results.getString(2);
                    String city = results.getString(3);
                    String country = results.getString(4);
                    String phone = results.getString(5);

                    System.out.printf("%-30s %-40s %-15s %-15s %-12s\n",
                            contactName, companyName, city, country, phone);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error occur!");
            System.out.println(ex.getMessage());
        }
    }
}
