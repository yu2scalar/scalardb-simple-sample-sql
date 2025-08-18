package com.demo;

import com.scalar.db.sql.SqlSession;
import com.scalar.db.sql.SqlSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DropTable {
    public static void main(String[] args) {
        System.out.println("Dropping table using SQL DROP TABLE...");
        
        try {
            // Load ScalarDB configuration
            Properties properties = new Properties();
            properties.load(new FileInputStream("scalardb_sql.properties"));
            
            // Create SqlSession
            SqlSessionFactory sessionFactory = SqlSessionFactory.builder()
                    .withProperties(properties)
                    .build();
            
            try (SqlSession session = sessionFactory.createSqlSession()) {
                // Drop the table
                String dropTableSql = "DROP TABLE IF EXISTS sample_ns.sample_table";
                session.execute(dropTableSql);
                System.out.println("Table 'sample_ns.sample_table' has been dropped successfully");
                
            } catch (Exception e) {
                System.err.println("Error dropping table: " + e.getMessage());
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