package com.demo;

import com.demo.GenericSqlUtil;
import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("ScalarDB Simple Sample SQL");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb_sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession();
                 Scanner scanner = new Scanner(System.in)) {
                
                // Create GenericSqlUtil instance
                GenericSqlUtil sqlUtil = new GenericSqlUtil(session);
                
                System.out.println("Successfully connected to ScalarDB cluster");
                System.out.println("Interactive SQL Command Prompt");
                System.out.println("Type 'exit' to quit the application");
                System.out.println("----------------------------------------");
                
                while (true) {
                    System.out.print("SQL> ");
                    String input = scanner.nextLine().trim();
                    
                    // Check for exit command
                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Goodbye!");
                        break;
                    }
                    
                    // Skip empty input
                    if (input.isEmpty()) {
                        continue;
                    }
                    
                    try {
                        // Execute SQL query
                        List<Map<String, Object>> results = sqlUtil.executeQuery(input);
                        
                        // Display results
                        if (results.isEmpty()) {
                            System.out.println("No results returned.");
                        } else {
                            displayResults(results);
                        }
                        
                    } catch (Exception e) {
                        System.err.println("Error executing query: " + e.getMessage());
                    }
                    
                    System.out.println();
                }
                
            } catch (Exception e) {
                System.err.println("Error executing SQL operations: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error initializing ScalarDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void displayResults(List<Map<String, Object>> results) {
        if (results.isEmpty()) {
            return;
        }
        
        // Get column names from first row
        Map<String, Object> firstRow = results.get(0);
        String[] columns = firstRow.keySet().toArray(new String[0]);
        
        // Calculate column widths
        int[] widths = new int[columns.length];
        for (int i = 0; i < columns.length; i++) {
            widths[i] = columns[i].length();
            for (Map<String, Object> row : results) {
                Object value = row.get(columns[i]);
                String valueStr = value != null ? value.toString() : "NULL";
                widths[i] = Math.max(widths[i], valueStr.length());
            }
        }
        
        // Print header
        printSeparator(widths);
        printRow(columns, widths);
        printSeparator(widths);
        
        // Print data rows
        for (Map<String, Object> row : results) {
            String[] values = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                Object value = row.get(columns[i]);
                values[i] = value != null ? value.toString() : "NULL";
            }
            printRow(values, widths);
        }
        
        printSeparator(widths);
        System.out.println("(" + results.size() + " row(s) returned)");
    }
    
    private static void printSeparator(int[] widths) {
        System.out.print("+");
        for (int width : widths) {
            for (int i = 0; i < width + 2; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
    }
    
    private static void printRow(String[] values, int[] widths) {
        System.out.print("|");
        for (int i = 0; i < values.length; i++) {
            System.out.printf(" %-" + widths[i] + "s |", values[i]);
        }
        System.out.println();
    }
}