package com.demo;

import com.scalar.db.sql.Record;
import com.scalar.db.sql.ResultSet;
import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class UpdateRecord {
    public static void main(String[] args) {
        System.out.println("Updating record using SQL UPDATE...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb_sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Show record before update
                System.out.println("Record before update:");
                String selectSql = "SELECT * FROM sample_ns.sample_table WHERE id = 1";
                ResultSet resultSet = session.execute(selectSql);
                List<Record> records = resultSet.all();
                
                if (!records.isEmpty()) {
                    Record record = records.get(0);
                    System.out.println("  ID: " + record.getInt("id") + 
                                     ", Name: " + record.getText("name") + 
                                     ", Age: " + record.getInt("age") + 
                                     ", Email: " + record.getText("email"));
                }
                
                // Begin transaction
                session.begin();
                
                // Update the record
                String updateSql = """
                    UPDATE sample_ns.sample_table 
                    SET name = 'John Updated', age = 31, email = 'john.updated@example.com'
                    WHERE id = 1
                    """;
                
                session.execute(updateSql);
                System.out.println("\nRecord updated successfully");
                
                // Commit transaction
                session.commit();
                System.out.println("Transaction committed");
                
                // Show record after update
                System.out.println("\nRecord after update:");
                resultSet = session.execute(selectSql);
                records = resultSet.all();
                
                if (!records.isEmpty()) {
                    Record record = records.get(0);
                    System.out.println("  ID: " + record.getInt("id") + 
                                     ", Name: " + record.getText("name") + 
                                     ", Age: " + record.getInt("age") + 
                                     ", Email: " + record.getText("email"));
                }
                
            } catch (Exception e) {
                System.err.println("Error updating record: " + e.getMessage());
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