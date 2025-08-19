package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;
import com.scalar.db.sql.exception.SqlException;
import com.scalar.db.sql.exception.TransactionRetryableException;
import com.scalar.db.sql.exception.UnknownTransactionStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InsertTran {
    public static void main(String[] args) {
        System.out.println("Inserting record using SQL INSERT with transaction...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();

            try (SqlSession session = sessionFactory.createSqlSession()) {

                try {
                    // Begin transaction
                    session.begin();

                    // Insert a record
                    String insertSql = """
                    INSERT INTO sample_ns.sample_table (id, name, age, email) 
                    VALUES (1, 'John Doe', 30, 'john.doe@example.com')
                    """;

                    session.execute(insertSql);

                    // Insert another record
                    insertSql = """
                    INSERT INTO sample_ns.sample_table (id, name, age, email) 
                    VALUES (2, 'Jane Smith', 25, 'jane.smith@example.com')
                    """;
                    session.execute(insertSql);

                    // Commit transaction
                    session.commit();
                    System.out.println("Transaction committed successfully");
                    
                } catch (TransactionRetryableException e) {
                    System.err.println("Transaction failed (retryable): " + e.getMessage());
                    session.rollback();
                } catch (UnknownTransactionStatusException e) {
                    System.err.println("Transaction status unknown: " + e.getMessage());
                    session.rollback();
                } catch (SqlException e) {
                    System.err.println("Error inserting record: " + e.getMessage());
                    session.rollback();
                    e.printStackTrace();
                }
                
            } catch (SqlException e) {
                System.err.println("Error with session: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}