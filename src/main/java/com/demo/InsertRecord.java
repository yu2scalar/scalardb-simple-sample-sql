package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InsertRecord {
    public static void main(String[] args) {
        System.out.println("Inserting record using SQL INSERT...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb_sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                boolean transactionStarted = false;
                
                try {
                    // Begin transaction
                    session.begin();
                    transactionStarted = true;
                    System.out.println("Transaction started");
                    
                    // Insert a record
                    String insertSql = """
                        INSERT INTO sample_ns.sample_table (id, name, age, email) 
                        VALUES (1, 'John Doe', 30, 'john.doe@example.com')
                        """;
                    
                    session.execute(insertSql);
                    System.out.println("Record inserted: ID=1, Name=John Doe, Age=30, Email=john.doe@example.com");
                    
                    // Insert another record
                    insertSql = """
                        INSERT INTO sample_ns.sample_table (id, name, age, email) 
                        VALUES (2, 'Jane Smith', 25, 'jane.smith@example.com')
                        """;
                    
                    session.execute(insertSql);
                    System.out.println("Record inserted: ID=2, Name=Jane Smith, Age=25, Email=jane.smith@example.com");
                    
                    // Commit transaction
                    session.commit();
                    System.out.println("Transaction committed successfully");
                    transactionStarted = false;
                    
                } catch (Exception e) {
                    if (transactionStarted) {
                        try {
                            System.out.println("Attempting to rollback transaction...");
                            session.rollback();
                            System.out.println("Transaction rolled back");
                        } catch (Exception rollbackEx) {
                            System.err.println("Rollback failed: " + rollbackEx.getMessage());
                        }
                    }
                    throw e;
                }
                
            } catch (Exception e) {
                System.err.println("Error inserting record: " + e.getMessage());
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