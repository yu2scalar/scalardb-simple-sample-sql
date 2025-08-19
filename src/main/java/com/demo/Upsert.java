package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;
import com.scalar.db.sql.exception.SqlException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Upsert {
    public static void main(String[] args) {
        System.out.println("Upserting record using SQL UPSERT...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Upsert a record (will insert if not exists, update if exists)
                String upsertSql = """
                    UPSERT INTO sample_ns.sample_table (id, name, age, email) 
                    VALUES (3, 'Charlie Upserted', 35, 'charlie.upserted@example.com')
                    """;
                
                session.execute(upsertSql);
                System.out.println("Upsert completed: ID=3, Name=Charlie Upserted, Age=35, Email=charlie.upserted@example.com");
                
            } catch (SqlException e) {
                System.err.println("Error upserting record: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}