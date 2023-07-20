package org.wgu.software2.dbModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
  User Database
  @author Nabeel Aref
 */
public class UserDB {

    /**
      gets the username based on the id given
      @param id the wanted id of the user
      @return the username of the user
     @throws SQLException in case of a sql database error
     */
    public static String getUsername(int id) throws SQLException {
        String username = "";
        ResultSet results = ConnectDB.SqlQuery("SELECT User_Name FROM users WHERE User_Id = " + id);
        while (results.next()) username = results.getString("User_Name");

        return username;
    }

    /**
      gets the userid if the name supplied is found
      @param username the name of the user wanted
      @return returns the user id if found else -1 is returned
     @throws SQLException in case of a sql database error
     */
    public static int getUserId(String username) throws SQLException {
        int id = -1;
        ResultSet results = ConnectDB.SqlQuery("SELECT User_Id FROM users WHERE User_Name = '" + username + "'");
        while (results.next()) id = results.getInt("User_Id");

        return id;
    }

    /**
      gets all the users id of all the users in the database
      @return Ids returns the users id of all user in the database as a list of integers
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static ObservableList<Integer> getAllUserIds() throws SQLException {
        ObservableList<Integer> Ids = FXCollections.observableArrayList();
        ResultSet results = ConnectDB.SqlQuery("SELECT User_Id FROM users");
        while (results.next()) Ids.add( results.getInt("User_Id"));

        return Ids;
    }

    /**
      returns the user id if the username and password are given correctly
      @param username the username given in the login controller
      @param password the password the username given in the login controller
      @return  id of the user, if its invaild it will be negative one
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static int verifyCredentials(String username, String password) throws SQLException {
        ResultSet results = ConnectDB.SqlQuery("SELECT User_Id FROM users WHERE User_Name = '" + username + "'AND Password = '" + password + "'");
        int id = -1;
        while (results.next())  id = results.getInt("User_Id");
        return Math.max(id, -1);
    }

    /**
      Logs user in if the username and password is correct
      @param username the username given in the login controller
      @param passwod the password the username given in the login controller
      @return returns if the user logged in successfully
      @throws SQLException this exception happens in case of a sql error
     */
    public static boolean loginUser(String username,String passwod) throws SQLException {
        int id=UserDB.verifyCredentials(username,passwod);
        if(id !=-1)
        {
            User.setUserName(username);
            User.setId(id);
            return true;
        }
        return false;
    }
    /**
      returns the appointments that has that specific userId
      @param id user Id for the wanted appointments
      @return Returns integer id of all the appointments
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static List<Integer> getUserAppointments(int id) throws SQLException {
        List<Integer> userAppointment = new ArrayList<>();
        ResultSet results = ConnectDB.SqlQuery("SELECT Appointment_Id FROM appointments WHERE User_Id = " + id);
        while (results.next()) userAppointment.add(results.getInt("Appointment_Id"));

        return userAppointment;
    }
}
