package org.wgu.software2.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.wgu.software2.dbModels.CountriesDB;
import org.wgu.software2.dbModels.Customersdb;
import org.wgu.software2.dbModels.DivisionsDB;
import org.wgu.software2.misc.Helpers;
import org.wgu.software2.models.Country;
import org.wgu.software2.models.Customer;
import org.wgu.software2.models.Division;
import org.wgu.software2.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

/**
 Controls the AddUpdateCustomer.fxml scene's functionality.
 Based on user input to the displayed form, provides capabilities to either add a new Customer or alter an existing Customer.
 @author Nabeel Aref
 */
public class AddUpdateCustomerController implements Initializable {
    private static Customer customer_Modified = null;
    public Label customer_Formtitle;
    public TextField customer_NameField;
    public TextField customer_Phonefield;
    public TextField customer_Addressfield;
    public ComboBox<String> customer_Countrybox;
    public ComboBox<String> customer_Statebox;
    public Label customer_errorMessageLabel;
    public TextField customer_FieldID;
    public TextField customer_FieldZip;
    boolean failed = false;




    /**
     Sets the customer_Modified variable to the Customer to be changed from the mainForm controller.
     @param customer the Customer to set
     */
    public static void setModifiedCust(Customer customer) {
        customer_Modified = customer;
    }

    /**
     Returns the current scene to MainForm.fxml.
     The lambda expression is used to update the current scene to MainForm.fxml.
     @param actionEvent is the action event
     @throws IOException Exceptions thrown during data input / output are caught.
     */
    public void onCustCancel(ActionEvent actionEvent) throws IOException {
        customer_Modified = null;
        Helpers.openMenu(actionEvent, "/wgu/software2/MainForm.fxml");
    }


    /**
     When a new or updated customer is added to the database, an error notice is provided for the user to correct.
     Verifies that all combobox selections have been made.
     On successful save, lambda expression is used to convert the current scene to MainForm.fxml.
     @param actionEvent is the action event
     @throws IOException Exceptions thrown during data input / output are caught by IOExceptions.
     */
    public void onCustSave(ActionEvent actionEvent) throws IOException {
        failed = false;
        customer_errorMessageLabel.setVisible(false);
        int customerId = Helpers.generateRandomId();
        String customerName = customer_NameField.getText();
        String customerAddress = customer_Addressfield.getText();
        String customerZip = customer_FieldZip.getText();
        String customerPhone = customer_Phonefield.getText();
        String username = User.getUserName();
        int division_ID;
        try {
            if (customer_Modified == null) {
                if (  !(customer_Countrybox.getValue() ==null) &&!(customer_Statebox.getValue() ==null) && !customer_Countrybox.getValue().isBlank() &&
                        !customer_Statebox.getValue().isBlank() ) {
                    division_ID = DivisionsDB.getDivisionIdbyName(customer_Statebox.getValue());
                    Customer newCustomer = new Customer(customerId,customerName,customerAddress,customerZip,customerPhone,Helpers.getUTCTime(LocalDateTime.now()).toString(),
                            username,Helpers.getUTCTime(LocalDateTime.now()).toString(),username,division_ID, DivisionsDB.getDivisionName(division_ID),customer_Countrybox.getValue());

                    Customersdb.addCustomer(newCustomer);
                } else {
                    customer_errorMessageLabel.setVisible(true);
                    failed = true;
                }
            } else {
                division_ID = DivisionsDB.getDivisionIdbyName(customer_Statebox.getValue());
                customerId = parseInt(customer_FieldID.getText());
                Customer newCustomer = new Customer(customerId,customerName,customerAddress,customerZip,customerPhone,Helpers.getUTCTime(LocalDateTime.now()).toString(),
                        customer_Modified.getCreatedBy(),Helpers.getUTCTime(LocalDateTime.now()).toString(),username,division_ID, DivisionsDB.getDivisionName(division_ID),customer_Countrybox.getValue());
                Customersdb.updateCustomerById(newCustomer.getId(),newCustomer);
            }
            if (!failed) {
                customer_Modified = null;
                Helpers.openMenu(actionEvent, "/wgu/software2/mainForm.fxml");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     The customer Statebox combobox selections are determined by the user's selection of the Country combobox.
     @throws SQLException this exception happens in case of a sql error
     */
    public void setDivisions() throws SQLException {
        int country = CountriesDB.getCountryId(customer_Countrybox.getValue());
        ObservableList<Division> allDivisions = DivisionsDB.getAllDivisions();
        ObservableList<String> divisions = FXCollections.observableArrayList();
        customer_Statebox.setDisable(false);
        //lambda statement used here to iterate divisions

        allDivisions.forEach(division -> {
            if (division.getCountryId() == country) {
                divisions.add(division.getDivisionName());
            }
        });
        customer_Statebox.setItems(divisions);
    }
    /**
     If Customer has been chosen and the modify button on mainForm has been clicked, the form is initiated with the current data.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Country> countries = null;
        try {
            countries = CountriesDB.getAllCountries();
        } catch (SQLException e) {
            Helpers.WarningBox("Was not able to fetch the countries from the database.");
        }
        ObservableList<String> countryFormData = FXCollections.observableArrayList();
        if(customer_Modified != null) {
            customer_FieldID.setText(String.valueOf(customer_Modified.getId()));
            customer_Formtitle.setText("UPDATE CUSTOMER");
            customer_Addressfield.setText(customer_Modified.getCustomerAddress());
            customer_FieldZip.setText(customer_Modified.getCustomerZipCode());
            customer_Phonefield.setText(customer_Modified.getCustomerPhone());
            customer_NameField.setText(customer_Modified.getCustomerName());
            customer_Countrybox.setValue(customer_Modified.getCountry());
            customer_Statebox.setValue(customer_Modified.getDivisionName());
            try {
                setDivisions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            customer_FieldID.setText("Randomly Generated Number");
        }
        //lambda statement used here to iterate countries
        countries.forEach( (country) -> {countryFormData.add(country.getCountryName());});
            customer_Countrybox.setItems(countryFormData);
    }
}
