package com.demo;

import com.scalar.db.sql.Record;
import com.scalar.db.sql.ResultSet;
import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Select {
    public static void main(String[] args) {
        System.out.println("Selecting record using SQL SELECT...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Select all records
                String selectSql = "SELECT * FROM sample_ns.sample_table";
                ResultSet resultSet = session.execute(selectSql);
                List<Record> records = resultSet.all();
                
                if (records.isEmpty()) {
                    System.out.println("No records found");
                } else {
                    System.out.println("Records found:");
                    for (Record record : records) {
                        System.out.println("  ID: " + record.getInt("id") + 
                                         ", Name: " + record.getText("name") + 
                                         ", Age: " + record.getInt("age") + 
                                         ", Email: " + record.getText("email"));
                    }
                }
                
            } catch (Exception e) {
                System.err.println("Error getting record: " + e.getMessage());
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