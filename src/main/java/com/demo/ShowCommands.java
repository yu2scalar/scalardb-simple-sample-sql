package com.demo;

import com.scalar.db.sql.Record;
import com.scalar.db.sql.ResultSet;
import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;
import com.scalar.db.sql.ColumnDefinitions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ShowCommands {
    public static void main(String[] args) {
        System.out.println("Executing SHOW commands...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Execute SHOW NAMESPACES
                System.out.println("\n=== SHOW NAMESPACES ===");
                String showNamespacesSql = "SHOW NAMESPACES";
                ResultSet namespacesResultSet = session.execute(showNamespacesSql);
                List<Record> namespaceRecords = namespacesResultSet.all();
                
                if (namespaceRecords.isEmpty()) {
                    System.out.println("No namespaces found");
                } else {
                    System.out.println("Namespaces found:");
                    ColumnDefinitions namespaceCols = namespacesResultSet.getColumnDefinitions();
                    for (Record record : namespaceRecords) {
                        for (int i = 0; i < namespaceCols.size(); i++) {
                            String columnName = namespaceCols.getColumnDefinition(i).getColumnName();
                            System.out.println("  " + columnName + ": " + record.getText(columnName));
                        }
                        System.out.println();
                    }
                }
                
                // Execute SHOW TABLES FROM sample_ns
                System.out.println("\n=== SHOW TABLES FROM sample_ns ===");
                String showTablesSql = "SHOW TABLES FROM sample_ns";
                ResultSet tablesResultSet = session.execute(showTablesSql);
                List<Record> tableRecords = tablesResultSet.all();
                
                if (tableRecords.isEmpty()) {
                    System.out.println("No tables found in sample_ns namespace");
                } else {
                    System.out.println("Tables found:");
                    ColumnDefinitions tableCols = tablesResultSet.getColumnDefinitions();
                    for (Record record : tableRecords) {
                        for (int i = 0; i < tableCols.size(); i++) {
                            String columnName = tableCols.getColumnDefinition(i).getColumnName();
                            System.out.println("  " + columnName + ": " + record.getText(columnName));
                        }
                        System.out.println();
                    }
                }
                
            } catch (Exception e) {
                System.err.println("Error executing SQL commands: " + e.getMessage());
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
}