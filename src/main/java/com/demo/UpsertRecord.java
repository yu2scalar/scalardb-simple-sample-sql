package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UpsertRecord {
    public static void main(String[] args) {
        System.out.println("Upserting record using SQL UPSERT...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb_sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Begin transaction
                session.begin();
                
                // Upsert a record (will insert if not exists, update if exists)
                String upsertSql = """
                    UPSERT INTO sample_ns.sample_table (id, name, age, email) 
                    VALUES (3, 'Charlie Upserted', 35, 'charlie.upserted@example.com')
                    """;
                
                session.execute(upsertSql);
                System.out.println("Upsert completed: ID=3, Name=Charlie Upserted, Age=35, Email=charlie.upserted@example.com");
                
                // Commit transaction
                session.commit();
                System.out.println("Transaction committed successfully");
                
            } catch (Exception e) {
                System.err.println("Error upserting record: " + e.getMessage());
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