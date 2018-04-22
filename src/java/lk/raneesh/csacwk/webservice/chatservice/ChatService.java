/**
 * Author: Raneesh Gomez
 * IIT Student ID: 2016087
 * UoW ID: 16266986
 * Client Server Architecture
 * Coursework: SOAP Web Services based Java Chat Application
 * File Name: ChatService.java
 */

package lk.raneesh.csacwk.webservice.chatservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.soap.Addressing;
import lk.raneesh.csacwk.webservice.databaseconnector.DatabaseConnection;
import lk.raneesh.csacwk.webservice.models.ChatMessage;
import lk.raneesh.csacwk.webservice.models.ChatThread;
import lk.raneesh.csacwk.webservice.userservice.UserService;

@Addressing(enabled = true, required = false)
@WebService(serviceName = "ChatService")
public class ChatService {

    // Provides access to the MySQL database
    private static Connection dbConn = DatabaseConnection.dbConnection();

    /**
     * Web service operation to add a new thread to database and retrieve the same for the client
     */
    @WebMethod(operationName = "addThread")
    public ChatThread addThread(@WebParam(name = "threadTitle") String threadTitle, @WebParam(name = "threadCreator") String threadCreator) {
        int queryExecute = 0;
        ChatThread newThread = null;
        // Get the current date and time to be saved
        LocalDateTime newThreadDate = LocalDateTime.now();
        DateTimeFormatter testFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String formattedString = newThreadDate.format(testFormatter);

        try {
            // Declare a MySQL query statement
            Statement insertStatement = (Statement) dbConn.createStatement();
            String insert_sql = "INSERT INTO threads(threadTitle, threadCreator, threadDate) VALUES('" + threadTitle + "', '" + threadCreator + "', '" + formattedString + "')";
            queryExecute = insertStatement.executeUpdate(insert_sql); // Execute query
            System.out.println("New Thread Stored Successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        //If executeUpdate() successfully added the record to the database the queryExecute variable would be 1
        if (queryExecute > 0) {
            try {
                Statement selectStatement = (Statement) dbConn.createStatement();
                String select_sql = "SELECT threadId, threadTitle, threadCreator, threadDate FROM threads ORDER BY threadId DESC LIMIT 1";
                ResultSet rs = selectStatement.executeQuery(select_sql); // Contains the result of the retrieved data from the select query

                int threadId = 0;
                String threadName = "";
                String threadAuthor = "";
                String threadDate = "";

                while (rs.next()) {
                    // Assign retrieved field values to variables
                    threadId = rs.getInt("threadId");
                    threadName = rs.getString("threadTitle");
                    threadAuthor = rs.getString("threadCreator");
                    threadDate = rs.getString("threadDate");
                }

                // Declare a new object to send the thread back to the client
                newThread = new ChatThread(threadId, threadName, threadAuthor, threadDate);

            } catch (SQLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            newThread = null;
        }

        return newThread;
    }

    /**
     * Web service operation to retrieve all the chat threads in the application
     */
    @WebMethod(operationName = "retrieveAllThreads")
    public List<ChatThread> retrieveAllThreads() {

        List<ChatThread> currentThreadsList = new ArrayList<>();

        try {
            Statement selectStatement = (Statement) dbConn.createStatement();
            String select_sql = "SELECT threadId, threadTitle, threadCreator, threadDate FROM threads ORDER BY threadId DESC";            
            ResultSet rs = selectStatement.executeQuery(select_sql); // Executes and stores the data retrieved from the select query

            while (rs.next()) {
                // Assign retrieved field values to variables
                int threadId = rs.getInt("threadId");
                String threadTitle = rs.getString("threadTitle");
                String threadCreator = rs.getString("threadCreator");
                String threadDate = rs.getString("threadDate");
                
                // Each threads data is initialized into a new thread object and returned to the client
                ChatThread currThread = new ChatThread(threadId, threadTitle, threadCreator, threadDate);
                
                // Declare a MySQL query statement
                Statement selectDateStatement = (Statement) dbConn.createStatement();
                // Querying for the last edited user and date for each thread
                String select_sql_last_edited_date = "SELECT messageAuthor, messageDate FROM messages WHERE threadId = " + threadId + " ORDER BY messageId DESC LIMIT 1";
                ResultSet rsDate = selectDateStatement.executeQuery(select_sql_last_edited_date); // Executes and stores the data retrieved from the select query
                String threadLastEditedDate = null;
                String threadLastEditedAuthor = null;
                while (rsDate.next()) {
                    // Assign retrieved field values to variables
                    threadLastEditedAuthor = rsDate.getString("messageAuthor");
                    threadLastEditedDate = rsDate.getString("messageDate");
                }                
                
                // Creating a thread object with last edited user and date if thread contains messages
                if (threadLastEditedDate != null && threadLastEditedAuthor != null) {
                    currThread = new ChatThread(threadId, threadTitle, threadLastEditedAuthor, threadLastEditedDate);
                }    
                // Creating a thread object with threadCreator and thread creation date if the thread contains no messages
                else {
                    currThread = new ChatThread(threadId, threadTitle, threadCreator, threadDate);
                }
                
                currentThreadsList.add(currThread);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return currentThreadsList;
    }

    /**
     * Web service operation to retrieve all the messages of a particular thread
     */
    @WebMethod(operationName = "retrieveAllMessages")
    public List<ChatMessage> retrieveAllMessages(@WebParam(name = "threadId") int threadId) {
        List<ChatMessage> currentMessagesList = new ArrayList<>();

        try {
            // Declare a MySQL query statement
            Statement selectStatement = (Statement) dbConn.createStatement();
            String select_sql_messages = "SELECT * FROM messages WHERE threadId = " + threadId;
            ResultSet rs = selectStatement.executeQuery(select_sql_messages); // Executes and stores the data retrieved from the select query

            while (rs.next()) {
                // Assign retrieved field values to variables
                int messageThreadId = rs.getInt("threadId");
                int messageId = rs.getInt("messageId");
                String messageBody = rs.getString("messageBody");
                String messageAuthor = rs.getString("messageAuthor");
                String messageDate = rs.getString("messageDate");
                ChatMessage currMessage = new ChatMessage(messageThreadId, messageId, messageBody, messageAuthor, messageDate);
                currentMessagesList.add(currMessage);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return currentMessagesList;
    }

    /**
     * Web service operation to add a new message to a thread
     */
    @WebMethod(operationName = "addMessage")
    public ChatMessage addMessage(@WebParam(name = "threadId") int threadId, @WebParam(name = "messageBody") String messageBody, @WebParam(name = "messageAuthor") String messageAuthor) {
        int queryExecute = 0;
        ChatMessage newMessage = null;
        LocalDateTime newMessageDate = LocalDateTime.now();
        DateTimeFormatter testFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String formattedString = newMessageDate.format(testFormatter);

        try {
            // Declare a MySQL query statement
            Statement insertStatement = (Statement) dbConn.createStatement();
            String insert_sql = "INSERT INTO messages(threadId, messageBody, messageAuthor, messageDate) VALUES('" + threadId + "', '" + messageBody + "', '" + messageAuthor + "', '" + formattedString + "')";
            queryExecute = insertStatement.executeUpdate(insert_sql); // Execute query
            System.out.println("New Message Stored Successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in storing message to database!");
        }

        //If executeUpdate() successfully added the record to the database the queryExecute variable would be 1
        if (queryExecute > 0) {
            try {
                // Declare a MySQL query statement
                Statement selectStatement = (Statement) dbConn.createStatement();
                String select_sql = "SELECT * FROM messages ORDER BY messageId DESC LIMIT 1";
                ResultSet rs = selectStatement.executeQuery(select_sql); // Executes and stores the data retrieved from the select query

                int messageThreadId = 0;
                int messageId = 0;
                String messageContent = "";
                String messageOwner = "";
                String messageDateTime = "";

                while (rs.next()) {
                    // Assign retrieved field values to variables
                    messageThreadId = rs.getInt("threadId");
                    messageId = rs.getInt("messageId");
                    messageContent = rs.getString("messageBody");
                    messageOwner = rs.getString("messageAuthor");
                    messageDateTime = rs.getString("messageDate");
                }

                // Declare a new message object and initialize with the retrieved data from the select query
                newMessage = new ChatMessage(messageThreadId, messageId, messageContent, messageOwner, messageDateTime);

            } catch (SQLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            newMessage = null;
        }

        return newMessage;

    }

    /**
     * Web service operation to retrieve the thread title for the message lists of a certain thread in the client
     */
    @WebMethod(operationName = "retrieveThreadTitle")
    public String retrieveThreadTitle(@WebParam(name = "threadId") int threadId) {
        String threadTitle = null;
        try {
            // Declare a MySQL query statement
            Statement selectStatement = (Statement) dbConn.createStatement();
            String select_sql = "SELECT threadTitle FROM threads WHERE threadId = " + threadId;
            ResultSet rs = selectStatement.executeQuery(select_sql); // Executes and stores the data retrieved from the select query

            while (rs.next()) {    
                // Assign retrieved field values to variables
                threadTitle = rs.getString("threadTitle");                
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return threadTitle;
    }
}
