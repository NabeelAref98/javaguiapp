package org.wgu.software2.dbModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.models.Customer;
import org.wgu.software2.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
  Customer Database
  @author Nabeel Aref
 */
public class Customersdb {
    /**
      adds a new customer to the database
      @param newCustomer the customer that is going to be added to the database
     @throws SQLException this exception happens in case of a sql error
     */
    public static void addCustomer(Customer newCustomer) throws SQLException {
        String query = String.format("INSERT INTO customers VALUES(%d, '%s', '%s', '%s', '%s', NOW(), '%s'," +
                "NOW(), '%s', %d)", newCustomer.getId(), newCustomer.getCustomerName(), newCustomer.getCustomerAddress(),
                newCustomer.getCustomerZipCode(), newCustomer.getCustomerPhone(), newCustomer.getCreatedBy(), newCustomer.getLastUpdatedBy(), newCustomer.getDivisionId());
                ConnectDB.SqlQuery(query);
    }

    /**
      returns all the customers available in the database
      @return allCustomers which is the variable list containing all the database
     */
    public static ObservableList<Customer> getAllCustomers()
    {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        try {
            String query = "SELECT * from customers";
            ResultSet myResult = ConnectDB.SqlQuery(query);

            while(myResult.next()) {
                int id = myResult.getInt("Customer_Id");
                String name = myResult.getString("Customer_Name");
                String address = myResult.getString("Address");
                String zip = myResult.getString("Postal_Code");
                String phone = myResult.getString("Phone");
                String createDate = myResult.getString("Create_Date");
                String createdBy = myResult.getString("Created_By");
                String update = myResult.getString("Last_Update");
                String updatedBy = myResult.getString("Last_Updated_By");
                int divId = myResult.getInt("Division_Id");

                ResultSet divResult = ConnectDB.SqlQuery(String.format("SELECT * FROM first_level_divisions WHERE Division_Id = %d", divId));

                while(divResult.next()) {
                    String stateDivision = divResult.getString("Division");
                    int countryId = divResult.getInt("Country_Id");

                    ResultSet countryResult = ConnectDB.SqlQuery( String.format("SELECT Country FROM countries WHERE Country_Id = %d", countryId));

                    while(countryResult.next()) {
                        String country = countryResult.getString("Country");
                        Customer customer = new Customer(id, name, address, zip, phone, createDate, createdBy, update, updatedBy, divId, stateDivision, country);
                        allCustomers.add(customer);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return allCustomers;
    }

    /**
      returns the customer that has that given id in the database
      @param id the id of the customer wanted
      @return Customer object of the id/ returns null if not found
     */
    public static Customer getCustomerById(int id)

    {
        try {
            String query = String.format("DELETE FROM customers WHERE Customer_Id = %d", id);;
            ResultSet myResult = ConnectDB.SqlQuery(query);

            while(myResult.next()) {
                String name = myResult.getString("Customer_Name");
                String address = myResult.getString("Address");
                String zip = myResult.getString("Postal_Code");
                String phone = myResult.getString("Phone");
                String createDate = myResult.getString("Create_Date");
                String createdBy = myResult.getString("Created_By");
                String update = myResult.getString("Last_Update");
                String updatedBy = myResult.getString("Last_Updated_By");
                int divId = myResult.getInt("Division_Id");

                ResultSet divResult = ConnectDB.SqlQuery(String.format("SELECT * FROM first_level_divisions WHERE Division_Id = %d", divId));

                while(divResult.next()) {
                    String stateDivision = divResult.getString("Division");
                    int countryId = divResult.getInt("Country_Id");

                    ResultSet countryResult = ConnectDB.SqlQuery( String.format("SELECT Country FROM countries WHERE Country_Id = %d", countryId));

                    while(countryResult.next()) {
                        String country = countryResult.getString("Country");
                        return new Customer(id, name, address, zip, phone, createDate, createdBy, update, updatedBy, divId, stateDivision, country);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     returns the id of the customer if the name is given
     @param name the id of the wanted customer
     @return id the customer id in Integer/ null is returned if not found
     */
    public static Integer getCustomerIdByName(String name)

    {
        try {
            String query = String.format("DELETE FROM customers WHERE Customer_Name = %s", name);
            ;
            ResultSet myResult = ConnectDB.SqlQuery(query);

            Integer id = null;
            while (myResult.next()) id = myResult.getInt("Customer_Id");
            return id;
        }
        catch (Exception exp)
        {

        }
        return null;
    }

    /**
     changes the customer in the database with the given id to that given customer object
      @param id the id of the customer to be changed
      @param newCustomer the customer object that will replace the old old
     * @throws SQLException this exception happens in case of a sql error
     */
    public static void updateCustomerById(int id,Customer newCustomer) throws SQLException {
        String query = String.format("UPDATE customers SET Customer_Name = '%s', Address = '%s', Postal_Code = '%s', Phone = '%s'," +
                        "Last_Update = NOW(), Last_Updated_By = '%s', Division_Id = %d WHERE Customer_Id = %d",
                newCustomer.getCustomerName(), newCustomer.getCustomerAddress(), newCustomer.getCustomerZipCode(), newCustomer.getCustomerPhone(), newCustomer.getLastUpdatedBy(), newCustomer.getDivisionId(), id);
        ConnectDB.SqlQuery(query);
    }

    /**
     removes a customer record from the database that has that id
     param id the id of the customer to be removed
     @param id requires the id to delete the customer
     */
    public static void deleteCustomerById(int id)
    {
        try {

            String custQuery = String.format("DELETE FROM customers WHERE Customer_Id = %d", id);
            ConnectDB.SqlQuery(custQuery);
        } catch (SQLException e) {
        }

    }

    /**
      returns the appointments id of the customer that has that specific customerId
      @param customerId the id of the wanted customer
      @return customerAppointment Returns Integer list of appointment Ids
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static List<Integer> getCustomersAppointments(int customerId) throws SQLException {
        List<Integer> customerAppointment = new ArrayList<>();
        ResultSet results = ConnectDB.SqlQuery("SELECT Appointment_Id FROM appointments WHERE Customer_Id = " + customerId);
        while (results.next()) customerAppointment.add(results.getInt("Appointment_Id"));

        return customerAppointment;
    }
}
