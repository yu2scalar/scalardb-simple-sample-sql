package com.demo;

import com.scalar.db.sql.Record;
import com.scalar.db.sql.ResultSet;
import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;
import com.scalar.db.sql.exception.SqlException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Delete {
    public static void main(String[] args) {
        System.out.println("Deleting record using SQL DELETE...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb-sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Show records before delete
                System.out.println("Records before delete:");
                String selectSql = "SELECT * FROM sample_ns.sample_table";
                ResultSet resultSet = session.execute(selectSql);
                List<Record> records = resultSet.all();
                
                for (Record record : records) {
                    System.out.println("  ID: " + record.getInt("id") + 
                                     ", Name: " + record.getText("name") + 
                                     ", Age: " + record.getInt("age") + 
                                     ", Email: " + record.getText("email"));
                }
                
                // Delete a specific record
                String deleteSql = "DELETE FROM sample_ns.sample_table WHERE id = 1";
                session.execute(deleteSql);
                System.out.println("\nRecord with ID = 1 deleted successfully");
                
                // Show records after delete
                System.out.println("\nRecords after delete:");
                resultSet = session.execute(selectSql);
                records = resultSet.all();
                
                if (records.isEmpty()) {
                    System.out.println("No records found");
                } else {
                    for (Record record : records) {
                        System.out.println("  ID: " + record.getInt("id") + 
                                         ", Name: " + record.getText("name") + 
                                         ", Age: " + record.getInt("age") + 
                                         ", Email: " + record.getText("email"));
                    }
                }
                
            } catch (SqlException e) {
                System.err.println("Error deleting record: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}