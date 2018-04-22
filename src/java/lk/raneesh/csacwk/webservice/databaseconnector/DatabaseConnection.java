/**
 * File Name: DatabaseConnection.java
 */

package lk.raneesh.csacwk.webservice.databaseconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
        
    private static final String USERNAME = "root"; // Database username    
    private static final String PASSWORD = ""; // Database password    
    private static final String HOST = "localhost"; // Database hostname
    private static final String URI = "jdbc:mysql://localhost:3306/dragx_chat"; // Database Connection URI
    
    public static Connection dbConnection() {
        Connection connection = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Uses JDBC Driver to connect to MySQL Database through java
            connection = DriverManager.getConnection(URI, USERNAME, PASSWORD); // Create connection
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
