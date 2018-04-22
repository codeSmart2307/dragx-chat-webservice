/**
 * Author: Raneesh Gomez
 * IIT Student ID: 2016087
 * UoW ID: 16266986
 * Client Server Architecture
 * Coursework: SOAP Web Services based Java Chat Application
 * File Name: UserService.java
 */

package lk.raneesh.csacwk.webservice.userservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.soap.Addressing;
import lk.raneesh.csacwk.webservice.databaseconnector.DatabaseConnection;
import lk.raneesh.csacwk.webservice.utility.RegistrationValidation;

/**
 *
 * @author Raneesh Gomez
 */
@Addressing(enabled = true, required = false)
@WebService(serviceName = "UserService")
public class UserService {

    private static Connection dbConn = DatabaseConnection.dbConnection();

    /**
     * Web service operation to log a user into the application
     */
    @WebMethod(operationName = "login")
    public String login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        String loginStatus = "&";

        String dbLoginId = "null";
        String dbLoginPassword = "null";
        String dbNickname = "null";

        try {
            // Declare a MySQL query statement
            Statement selectStatement = (Statement) dbConn.createStatement();
            String select_sql = "SELECT username, password, nickname FROM users WHERE username = '" + username + "'";
            ResultSet rs = selectStatement.executeQuery(select_sql); // Contains the result of the retrieved data from the select query

            while (rs.next()) {
                // Assign retrieved field values to variables
                dbLoginId = rs.getString("username");
                dbLoginPassword = rs.getString("password");
                dbNickname = rs.getString("nickname");
                System.out.println("Username exists! w/ password: " + dbLoginPassword);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Validation for user name and password
        if (dbLoginId.equals(username)) {
            if (dbLoginPassword.equals(password)) {
                loginStatus = "success&" + dbNickname;
            } else {
                loginStatus = "Invalid Password! Please try again&";
            }
        }
        else {
            loginStatus = "Invalid username! Please try again&";
        }

        return loginStatus;
    }

    /**
     * Web service operation to register a new user to the application
     */
    @WebMethod(operationName = "register")
    public String register(@WebParam(name = "nickname") String nickname, @WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "confirmPassword") String confirmPassword) {
        String registrationStatus = "failure";        

        boolean isPasswordValid = RegistrationValidation.validatePassword(password, confirmPassword);
        boolean isNicknameValid = RegistrationValidation.validateNickname(nickname);        

        boolean isLoginIdValid = false;
        String dbLoginId = "undefined";

        try {
            // Declare a MySQL query statement
            Statement selectStatement = (Statement) dbConn.createStatement();
            String select_sql = "SELECT username FROM users WHERE username = '" + username + "'";
            ResultSet rs = selectStatement.executeQuery(select_sql); // Contains the result of the retrieved data from the select query
            while (rs.next()) {
                // Assign retrieved field values to variables
                dbLoginId = rs.getString("username");
                System.out.println(dbLoginId);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Validation for registration credentials
        if (dbLoginId.equals("undefined")) {
            if (username.length() < 30) {
                isLoginIdValid = true;
            } else {
                return "Login ID is too long! Please keep it below 30 characters.";
            }
        } else {
            return "Username already exists! Please choose another.";
        }

        if (isLoginIdValid && isPasswordValid && isNicknameValid) {            
            try {
                // If credentials are valid inputs the credentials are stored in the MySQL Database
                Statement insertStatement = (Statement) dbConn.createStatement();
                String insert_sql = "INSERT INTO users(nickname, username, password) VALUES('" + nickname + "', '" + username + "', '" + password + "')";
                insertStatement.executeUpdate(insert_sql);
                System.out.println("Inserted New User Successfully!");
                registrationStatus = "success";
            } catch (SQLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (!isPasswordValid) {
            return "Password Mismatch! Please try again.";
        }
        else if (!isNicknameValid) {
            return "Nickname is too long! Please keep it below 20 characters.";
        }

        return registrationStatus;
    }
}
