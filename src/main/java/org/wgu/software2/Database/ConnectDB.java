package org.wgu.software2.Database;

import org.wgu.software2.misc.Helpers;
import org.wgu.software2.models.User;

import java.sql.*;
import java.util.Locale;

public class ConnectDB {
    private static User currentUser;
    private static final String sqlProtocol = "jdbc";
    private static final String splVend = ":mysql:";
    private static final String sqlDbLoc = "//localhost/";
    private static final String sqlDbName = "client_schedule";
    private static final String sqlIp = sqlProtocol + splVend + sqlDbLoc + sqlDbName + "?connectionTimeZone = SERVER";
    private static final String sqlDriver = "com.mysql.cj.jdbc.Driver";
    private static final String appUser = "sqlUser";
    public static Connection appConn;

    /**
     Establishes the database connection.
     */
    public static void openConnection() {
        String password = "Passw0rd!";

        try {
            Class.forName(sqlDriver);
            appConn = DriverManager.getConnection(sqlIp, appUser, password);
            System.out.println("You are connected");
        } catch (Exception e) {
            System.out.println("Was not able to connect the database "+ sqlDbName +" on "+ sqlDbLoc+" with username "+appUser +
                    "\n and password " +password+" please make sure the connection information to the database is correct. Thank you");
        }
    }

    /**
     * shows the information that the application uses to connect to database
     */
    public static void show_database_information()
    {
        Helpers.WarningBox("Database Info:\n Database server : "+sqlDbLoc+"\n"+
                "Database Name : "+sqlDbName+"\n"+
                "Database Username : "+appUser+"\n"+
                "Database Passowrd : <Redacted check file>\n");
    }
    /**
     Establishes the connection to the database
     @return the connection
     @param query, the string to query the database for
     @throws SQLException this exception happens in case of a sql error
     */
    public static ResultSet SqlQuery(String query) throws SQLException {

        ResultSet myResult = null;
        if(query.toLowerCase().contains("select"))
            myResult = appConn.prepareStatement(query).executeQuery();
        else
            appConn.prepareStatement(query).executeUpdate();
        return myResult;
    }


    /**
     checks to see if the database connection is still active and returns true if it is.
     @throws SQLException this exception happens in case of a sql error
     @return returns the bool that provides the info if the application is connects
     */
    public static Boolean isStillConnected() throws SQLException {
        if(appConn==null) return false;
        return !appConn.isClosed();
    }

    /**
     Closes the established connection to database.
     */
    public static void closeConnection() {
        try {
            appConn.close();
            System.out.println("No longer connected");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

}
