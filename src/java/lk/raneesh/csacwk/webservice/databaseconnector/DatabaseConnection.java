/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.raneesh.csacwk.webservice.databaseconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Raneesh Gomez
 */
public class DatabaseConnection {
    
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String HOST = "localhost";
    private static final String URI = "jdbc:mysql://localhost:3306/dragx_chat";
    
    public static Connection dbConnection() {
        Connection connection = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URI, USERNAME, PASSWORD);
            System.out.println("Database Connection Successful!");
        }
        catch (SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception" + e);
        }
        return connection;
    }
    
    
}
