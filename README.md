# ScalarDB Cluster - Simple Sample

This is a set of simple examples to demonstrate how to use ScalarDB Cluster SQL API.

## Prerequisites

ScalarDB Cluster environment version 3.15.3 or later
JDK 17
Gradle 8.8 or later

## Directory Structure

```bash
│  README.md
│  scalardb_sql.properties  # Please configure this file for your environment.
└─src
    └──main
        └──java
            └─com
                └─demo
                    ├─CreateTable.java
                    ├─DropTable.java
                    ├─InsertRecord.java
                    ├─GetRecord.java
                    ├─UpdateRecord.java
                    ├─UpsertRecord.java
                    ├─ScanRecord.java
                    └─DeleteRecord.java

