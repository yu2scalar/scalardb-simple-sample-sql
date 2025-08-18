package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CreateTable {
    public static void main(String[] args) {
        System.out.println("Creating sample table using SQL DDL...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Create namespace if not exists
                String createNamespace = "CREATE NAMESPACE IF NOT EXISTS sample_ns";
                session.execute(createNamespace);
                System.out.println("Namespace 'sample_ns' created or already exists");
                
                // Create table
                String createTable = """
                    CREATE TABLE IF NOT EXISTS sample_ns.sample_table (
                        id INT PRIMARY KEY,
                        name TEXT,
                        age INT,
                        email TEXT
                    )
                    """;
                
                session.execute(createTable);
                System.out.println("Table 'sample_ns.sample_table' created successfully");
                
            } catch (Exception e) {
                System.err.println("Error creating table: " + e.getMessage());
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