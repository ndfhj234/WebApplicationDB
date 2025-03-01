/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapplicationdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnection {
    
    private static DBConnection instance;
    private String jdbcUrl;
    private String userName;
    private String password;
    private String databaseName;
    private String port;
    
    private DBConnection() {
        try {
            Properties props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
            
            if (inputStream != null) {
                props.load(inputStream);
                
                this.port = props.getProperty("db.port");
                this.databaseName = props.getProperty("db.name");
                this.userName = props.getProperty("db.username");
                this.password = props.getProperty("db.password");
                
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Construct JDBC URL
                this.jdbcUrl = "jdbc:mysql://localhost:" + port + "/" + databaseName + 
                               "?user=" + userName + "&password=" + password +
                               "&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            } else {
                throw new IOException("Unable to load database properties file.");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Database connection initialization failed: " + e.getMessage(), e);
        }
    }
    
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}
