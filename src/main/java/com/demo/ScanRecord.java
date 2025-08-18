package com.demo;

import com.scalar.db.sql.Record;
import com.scalar.db.sql.ResultSet;
import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ScanRecord {
    public static void main(String[] args) {
        System.out.println("Scanning records using SQL SELECT...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Scan all records
                System.out.println("Scanning all records:");
                String scanSql = "SELECT * FROM sample_ns.sample_table";
                ResultSet resultSet = session.execute(scanSql);
                List<Record> records = resultSet.all();
                
                if (records.isEmpty()) {
                    System.out.println("  No records found");
                } else {
                    for (Record record : records) {
                        System.out.println("  ID: " + record.getInt("id") + 
                                         ", Name: " + record.getText("name") + 
                                         ", Age: " + record.getInt("age") + 
                                         ", Email: " + record.getText("email"));
                    }
                    System.out.println("  Total: " + records.size() + " record(s)");
                }
                
            } catch (Exception e) {
                System.err.println("Error scanning records: " + e.getMessage());
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