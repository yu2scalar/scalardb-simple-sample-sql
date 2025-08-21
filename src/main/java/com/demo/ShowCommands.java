package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
                GenericSqlUtil sqlUtil = new GenericSqlUtil(session);
                
                // Execute SHOW NAMESPACES
                System.out.println("\n=== SHOW NAMESPACES ===");
                try {
                    List<Map<String, Object>> namespaces = sqlUtil.executeQuery("SHOW NAMESPACES");
                    if (namespaces.isEmpty()) {
                        System.out.println("No namespaces found");
                    } else {
                        for (Map<String, Object> row : namespaces) {
                            System.out.println("Namespace: " + row);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error executing SHOW NAMESPACES: " + e.getMessage());
                }
                
                System.out.println("\n=== SHOW TABLES FROM sample_ns ===");
                try {
                    List<Map<String, Object>> tables = sqlUtil.executeQuery("SHOW TABLES FROM sample_ns");
                    if (tables.isEmpty()) {
                        System.out.println("No tables found in sample_ns namespace");
                    } else {
                        for (Map<String, Object> row : tables) {
                            System.out.println("Table: " + row);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error executing SHOW TABLES FROM sample_ns: " + e.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("Error with SQL session: " + e.getMessage());
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