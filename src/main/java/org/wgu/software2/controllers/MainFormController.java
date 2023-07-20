package org.wgu.software2.controllers;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import org.wgu.software2.dbModels.AppointmentsDB;
import org.wgu.software2.dbModels.Customersdb;
import org.wgu.software2.misc.Helpers;
import org.wgu.software2.models.Appointment;
import org.wgu.software2.models.Customer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 Controls how the mainForm.fxml scene behaves. The customer Table and appointment_ are shown.
 Buttons for add, update, delete, reports, and quit are included on the tabbed table.
 BasicStage variable is defined here for usage in subsequent methods to change scenes,
 and lambda expression is used for abstraction and clearer code in SetStageInterface.
 @author Nabeel Aref
 */
public class MainFormController implements Initializable {

    public MainFormController() throws SQLException {
    }



    public TableView<Appointment> appointment_Table;
    public TableColumn<Appointment, Integer> appointment_Id;
    public TableColumn<Appointment, String> appointment_Title;
    public TableColumn<Appointment, String> appointment_Description;
    public TableColumn<Appointment, Integer> appointment_Contact;
    public TableColumn<Appointment, String> appointment_Location;
    public TableColumn<Appointment, String> appointment_Type;
    public TableColumn<Appointment, String > appointment_Start;
    public TableColumn<Appointment, String > appointment_End;
    public TableColumn<Appointment, Integer> appointment_CustomerId;
    public TableColumn<Appointment, Integer> appointment_UserId;
    public Label appointment_ErrorSelect;
    public TableView<Customer> customer_Table;
    public TableColumn<Customer, Integer> customer_Id;
    public TableColumn<Customer, String> customer_Name;
    public TableColumn<Customer, String > customer_Address;
    public TableColumn<Customer, String> customer_Zip;
    public TableColumn<Customer, String> customer_Country;
    public TableColumn<Customer, String> customer_State;
    public TableColumn<Customer, String> customer_Phone;
    public TableColumn<Customer, String > customer_CreateDate;
    public TableColumn<Customer, String> customer_Createdby;
    public TableColumn<Customer, String > customer_LastUpdated;
    public TableColumn<Customer, String> customer_UpdatedBy;
    public TableColumn<Customer, Integer> customer_DivisionId;
    public Label customer_ErrorSelect;
    public RadioButton appointment_ViewAll;
    public RadioButton appointment_ViewWeek;
    public RadioButton appointment_ViewMonth;
    private ObservableList<Customer> customers = Customersdb.getAllCustomers();
    private ObservableList<Appointment> Appointments = AppointmentsDB.getAllAppointments();

    /**
     The addUpdateCustomer form is modified to enable the introduction of a new Customer.
     Helper function is used to switch between screens
     @param actionEvent the action event
     @throws IOException Catches any exceptions that are raised when data is being entered or output.
     */
    public void onAddCust(ActionEvent actionEvent) throws IOException {
        Helpers.openMenu(actionEvent, "/wgu/software2/AddUpdateCustomer.fxml");
    }

    /**
     Checks to see whether a Customer is chosen and, if it is, sets the scene to addUpdateCustomer.fxml. A notification
     urging the user to choose a customer appears if there is no customer chosen.
     Helper function is used to switch between screens
     @param actionEvent the action event
     @throws IOException Catches any exceptions that are raised when data is being entered or output.
     */
    public void onUpdateCust(ActionEvent actionEvent) throws IOException {
        boolean isNull = isCustSelected();

        if (!isNull) {
            Customer customer = customer_Table.getSelectionModel().getSelectedItem();
            AddUpdateCustomerController.setModifiedCust(customer);
            Helpers.openMenu(actionEvent, "/wgu/software2/AddUpdateCustomer.fxml");
        }

    }

    /**
     Removes the chosen client from the database. displays a notice so the user may confirm their deletion intention.
     If no customer is chosen, a notice instructing the user to choose a customer shows.
     @throws SQLException this exception happens in case of a sql error
     */
    public void onDeleteCust() throws SQLException {
        boolean failure = false;
        boolean isNull = isCustSelected();
        if (!isNull) {
            Customer choosenCustomer = customer_Table.getSelectionModel().getSelectedItem();
            ObservableList<Appointment> allAppointments = AppointmentsDB.getAllAppointments();
            for (Appointment appointment : allAppointments) {
                if (choosenCustomer.getId().equals(appointment.getCustomerId())) {
                    customer_ErrorSelect.setVisible(true);
                    customer_ErrorSelect.setText(String.format("Delete all %s\'s Appointments before deleting", choosenCustomer.getCustomerName()));
                    failure = true;
                    break;
                }
            }

            if (!failure) {
                boolean results = Helpers.ConfirmationBox("Delete " + choosenCustomer.getCustomerName());

                if (results) {
                    Customersdb.deleteCustomerById(choosenCustomer.getId());
                    customer_Table.setItems(Customersdb.getAllCustomers());
                }
            }
        }
    }

    /**
     Creates a new Appointment by altering the scene of the addUpdateAppointment form.
     To switch between scenes, use basicStage.setStage() as specified in the preceding lambda expression.
     @param actionEvent the action event
     @throws IOException Catches any exceptions that are raised when data is being entered or output.
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Helpers.openMenu(actionEvent, "/wgu/software2/AddUpdateAppts.fxml");
    }

    /**
     Updates to existing appointments are now possible thanks to a modification to the addUpdateAppointment form.
     If no appointment is selected, a message encouraging the user to pick an appointment's basicStage appears.
     SetStage(), which is declared in the lambda expression above, is used in this case to modify the sceneries.
     @param actionEvent the action event
     @throws IOException Catches any exceptions that are raised when data is being entered or output.
     */
    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException {
        boolean isNull = isAppointmentSelected();

        if (!isNull) {
            Appointment appointment = appointment_Table.getSelectionModel().getSelectedItem();
            AddUpdateAppointmentsController.setModifiedAppointment(appointment);
            Helpers.openMenu(actionEvent, "/wgu/software2/AddUpdateAppts.fxml");
        }
    }

    /**
     The specified Appointment is removed from the database. displays a notice so the user may confirm their deletion intention.
     If no appointment is chosen, a notice instructing the user to choose one shows.
     @throws SQLException this exception happens in case of a sql error
     */
    public void onDeleteAppointment() throws SQLException {
        boolean isNull = isAppointmentSelected();
        if (isNull) return;
        Appointment deletedAppointment = appointment_Table.getSelectionModel().getSelectedItem();
        boolean results = Helpers.ConfirmationBox("Delete " + deletedAppointment.getAppointmentType() + " at " + deletedAppointment.getStartDateTime()
        +" of type "+deletedAppointment.getAppointmentType());

        if (results)
        {
            AppointmentsDB.deleteAppointment(deletedAppointment.getId());
            Appointments = AppointmentsDB.getAllAppointments();
            if (appointment_ViewWeek.isSelected()) {
                this.onWeekAppointmentView();
            } else if (appointment_ViewMonth.isSelected()) {
                this.onMonthAppointmentView();
            }
            else onAllAppointmentView();
        }
    }


    /**
     Filters the appointment_Table to display all appointments.
     */
    public void onAllAppointmentView() {

        appointment_Table.setItems(Appointments);
        appointment_Table.refresh();

    }

    /**
     Filters the appointments in the appointment Table so that only ones from the current week are displayed.
     @throws SQLException this exception happens in case of a sql error
     */
    public void onWeekAppointmentView() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentsDB.getAllAppointments();
        ObservableList<Appointment>weeklyWanted = FXCollections.observableArrayList();
        appointments.forEach(thisAppointment->{
            if(LocalDateTime.parse(  thisAppointment.getStartDateTime()).isBefore(LocalDateTime.now().plusDays(3))
            && LocalDateTime.parse( thisAppointment.getStartDateTime()).isAfter (LocalDateTime.now().minusDays(3)))
                weeklyWanted.add(thisAppointment);

        });
        
        appointment_Table.setItems(weeklyWanted);
        appointment_Table.refresh();


    }

    /**
     Filters the appointments in the appointment Table so that only ones from the current month are displayed.
     @throws SQLException this exception happens in case of a sql error
     */
    public void onMonthAppointmentView() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentsDB.getAllAppointments();
        ObservableList<Appointment>wantedMonthly = FXCollections.observableArrayList();

        for (Appointment thisAppointment:appointments) {
            if(LocalDateTime.parse(  thisAppointment.getStartDateTime()).getMonth() == LocalDateTime.now().getMonth())
                wantedMonthly.add(thisAppointment);

        }
        


        appointment_Table.setItems(wantedMonthly);
        appointment_Table.refresh();
    }

    /**
     Identifies if an appointment is chosen when the update or delete buttons are used.
     @return true if an Appointment is selected, otherwise return false.
     */
    public boolean isAppointmentSelected() {
        appointment_ErrorSelect.setVisible(false);
        boolean isNull = true;
        Appointment Appointment = appointment_Table.getSelectionModel().getSelectedItem();
        try {
            if (Appointment.getAppointmentType() != null) {
                isNull = false;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            appointment_ErrorSelect.setVisible(true);
        }
        return isNull;
    }

    /**
     Identifies if a Customer is chosen when the update or delete buttons are pressed.
     @return true if a Customer is selected, otherwise return false.
     */
    public boolean isCustSelected() {
        customer_ErrorSelect.setVisible(false);
        boolean isNull = true;
        Customer customer = customer_Table.getSelectionModel().getSelectedItem();
        try {
            if (customer.getCustomerName() != null) {
                isNull = false;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            customer_ErrorSelect.setVisible(true);
        }
        return isNull;
    }

    /**
     Helper function is used to switch screen.W
     @param actionEvent the action event
     @throws IOException throws exceptions as the helper function is called
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Helpers.openMenu(actionEvent, "/wgu/software2/Reports.fxml");
    }

    /**
     Exits the program.
     */
    public void onExit() {
        Platform.exit();
    }
    /**
     prepares the appointment Table and customer Table for data acceptance and fills them with database information.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //     lambda expressions were used to load the customer properties in the table.
        customer_Id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        customer_Name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        customer_Address.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerAddress()));
        customer_Zip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerZipCode()));
        customer_Country.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountry()));
        customer_State.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDivisionName()));
        customer_Phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerPhone()));
        customer_CreateDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedDate()));
        customer_Createdby.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedBy()));
        customer_LastUpdated.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastUpdate()));
        customer_UpdatedBy.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastUpdatedBy()));
        customer_DivisionId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDivisionId()).asObject());

        appointment_Id.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("id"));
        appointment_Title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointmentTitle"));
        //     lambda expression was also used here to clear the letter T in the application, which improves and eases up the modification rather than doing a for loop
        appointment_Start.setCellValueFactory(cellData -> new SimpleStringProperty( Helpers.getLocalTime( LocalDateTime.parse( cellData.getValue(). getStartDateTime())).toString().replace("T"," ")));
        appointment_Description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
        appointment_Contact.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("contactId"));
        appointment_Location.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
        appointment_Type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointmentType"));
        appointment_End.setCellValueFactory(cellData -> new SimpleStringProperty( Helpers.getLocalTime(LocalDateTime.parse( cellData.getValue(). getEndDateTime())).toString().replace("T"," ")));
        appointment_CustomerId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
        appointment_UserId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userId"));

        appointment_Table.setItems(Appointments);
        customer_Table.setItems(customers);
        onAllAppointmentView();

    }
}