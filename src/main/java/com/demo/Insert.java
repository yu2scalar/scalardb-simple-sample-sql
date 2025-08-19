package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;
import com.scalar.db.sql.exception.SqlException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Insert {
    public static void main(String[] args) {
        System.out.println("Inserting record using SQL INSERT...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();

            try (SqlSession session = sessionFactory.createSqlSession()) {

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

            } catch (SqlException e) {
                System.err.println("Error inserting record: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}