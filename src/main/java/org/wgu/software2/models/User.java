package org.wgu.software2.models;

/** This class defines the static object of the user logged in.
 * @author Nabeel Aref.
 */
public class User {
    static String userName;
    static int id;



    /**
     * @return gets the user name
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * @param newUserName sets the username if logged in
     */
    public static void setUserName(String newUserName) {
        userName = newUserName;
    }

    /**
     * @param newId gets the userId
     */
    public static void setId(int newId) {id = newId;}

    /**
     * @return setsTheUserId
     */
    public int getId() {
        return id;
    }
}
