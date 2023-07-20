package org.wgu.software2.dbModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.models.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contact Database
 * @author Nabeel Aref
 */
public class ContactsDB {

    /**
      returns all the contacts in the database in a list
      @return allContactsList
     @throws SQLException this exception happens in case of a sql error
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        ResultSet results = ConnectDB.SqlQuery("SELECT * FROM contacts");
        while (results.next()) {
            String name = results.getString("Contact_Name");
            int id = results.getInt("Contact_Id");

            String email = results.getString("Email");
            Contact contact = new Contact(name,id,email);
            allContacts.add(contact);
        }
        return allContacts;
    }

    /**
      gets  the contact name from the database of that specific id
      @param id the id of the wanted contact
      @return the contact name in string
     @throws SQLException this exception happens in case of a sql error
     */
    public static String getContactName(int id) throws SQLException {
        String name = "";
        ResultSet results = ConnectDB.SqlQuery("SELECT Contact_Name FROM contacts WHERE Contact_Id = " + id);
        while (results.next()) name = results.getString("Contact_Name");
        return name;
    }

    /**
     returns the id of the contact based on the name
      @param name the needed name to find the id
      @return associated contact Id
     @throws SQLException this exception happens in case of a sql error
     */
    public static int getContactId(String name) throws SQLException  {
        int id = 0;
        ResultSet results = ConnectDB.SqlQuery("SELECT Contact_Id FROM contacts WHERE Contact_Name = '" + name + "'");
        while (results.next())id = results.getInt("Contact_Id");

        return id;
    }
}
