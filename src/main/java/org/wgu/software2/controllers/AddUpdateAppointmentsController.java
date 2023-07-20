package org.wgu.software2.controllers;

import javafx.fxml.Initializable;
import org.wgu.software2.dbModels.AppointmentsDB;
import org.wgu.software2.dbModels.ContactsDB;
import org.wgu.software2.dbModels.Customersdb;
import org.wgu.software2.dbModels.UserDB;
import org.wgu.software2.misc.Helpers;
import org.wgu.software2.models.Appointment;
import org.wgu.software2.models.Contact;
import org.wgu.software2.models.Customer;
import org.wgu.software2.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

/**
 Moderates the behavior of the AddUpdateAppointments.fxml scene.
 User input in displayed form will make offers functionality to add or modify new appointments.
 Lambda defined the setStage method of the functional interface using a lambda expression.
 Use SetStageInterface in many ways.
 @author Nabeel Aref
 */
public class AddUpdateAppointmentsController  implements Initializable {

    ObservableList<Contact> contactAll = ContactsDB.getAllContacts();


    public TextField idAppointmentField;
    public TextField typeAppointmentField;
    public TextField locAppointmentField;
    public TextArea descAppointmentField;
    public TextField titleAppointmentField;
    public ComboBox<String> contactAppointmentField;
    public ComboBox<Integer> custAppointmentField;
    public ComboBox<Integer> userIdAppointmentField;
    public TextField startDateAppointmentField;
    public TextField startTimeAppointmentField;
    public TextField endDateAppointmentField;
    public TextField endTimeAppointmentField;
    public Label titleAppointmentForm;
    public Label labelErrorMessage;
    private static Appointment appointmentModified = null;


    public AddUpdateAppointmentsController() throws SQLException {
    }



    /**
     Sets modifiedAppointment variable to the currently selected Appointment to be changed from the mainForm controller.
     @param Appointment the Appointment to set
     */
    public static void setModifiedAppointment(Appointment Appointment) {
        appointmentModified = Appointment;
    }

    /**
     Changes the current scene back to the MainForm.fxml.
     lambda expression here is used tp change current scene to MainForm.fxml
     @param actionEvent is the action event
     @throws IOException catches any failed that may have occurred during data entry or output.
     */
    public void onAppointmentCancel(ActionEvent actionEvent) throws IOException {
        appointmentModified = null;
        Helpers.openMenu(actionEvent, "/wgu/software2/mainForm.fxml");

    }

    /**
     Change or new appointment is saved to the database.
     Verifies that the user-provided date and time are in a proper format and that all combobox choices have been made.
     Either a new appointment is stored to the database or a user-correctable error notice is presented.
     Using a lambda expression, the current scene is changed to MainForm.fxml after a successful save.
     @param actionEvent the action event
     @throws IOException,SQLException catches any failed that may have occurred during data entry or output.
     */
    public void onSaveAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        String appointmentTitle = titleAppointmentField.getText();
        String appointmentDescription = descAppointmentField.getText();
        String appointmentLocation = locAppointmentField.getText();
        String appointmentType = typeAppointmentField.getText();
        
        String startTime = null;
        String endTime = null;
        String end = null;
        String start = null;

        String username = User.getUserName();
        int userId = 0;
        String contact;
        int customer_Id = 0;
        int contactId = 0;
        int appointmentId;

        boolean failed = false;


        if (Helpers.verifyTimeisValid(startTimeAppointmentField.getText()) && Helpers.verifyTimeisValid(endTimeAppointmentField.getText())
                && Helpers.verifyDateisValid(endDateAppointmentField.getText()) && Helpers.verifyDateisValid(startDateAppointmentField.getText())) {
            startTime = startTimeAppointmentField.getText();
            endTime = endTimeAppointmentField.getText();
            end = LocalDateTime.parse( endDateAppointmentField.getText() + "T" + endTime).toString();
            start = LocalDateTime.parse(startDateAppointmentField.getText() + "T" + startTime).toString();

            if ( !(custAppointmentField ==null) && !(userIdAppointmentField.getValue() ==null) && !(contactAppointmentField.getValue() ==null) &&!contactAppointmentField.getValue().isBlank() ) {
                contact = contactAppointmentField.getValue();
                customer_Id = custAppointmentField.getValue();
                userId = userIdAppointmentField.getValue();
                for (Contact item : contactAll)
                    if (item.getContactName().equals( contact)) contactId = item.getId();
                int checkId = (appointmentModified == null) ? 0 : appointmentModified.getId();

                if (!Helpers.verifyTimeAvailable(start, end, customer_Id,checkId)) {
                    Helpers.WarningBox("Time Overlaps with customer or is in a sunday or is in a saturday or is not in bussiness hours.");
                    failed = true;

                }
            } else {
                labelErrorMessage.setText("Error: Please select an option in each of the drop-down combo boxes");
                failed = true;
            }
        } else {
            failed = true;
            labelErrorMessage.setText("Error: Date must be entered in yyyy-MM-dd format. \n       Time must be entered in 24 hour HH:mm:ss format");
        }


        if(!failed) {
            try {
                Appointment newAppointment = new Appointment(Helpers.generateRandomId(),appointmentTitle,appointmentDescription,appointmentLocation,appointmentType,start,end,customer_Id,userId,contactId,username,
                        Helpers.getUTCTime(LocalDateTime.now()).toString(),Helpers.getUTCTime(LocalDateTime.now()).toString(),username);
                if (appointmentModified == null) {
//                    AppointmentsDB.addAppointment(newAppointment);
                        AppointmentsDB.insert(newAppointment);
                } else {
                    appointmentId = parseInt(idAppointmentField.getText());
                    newAppointment.setId(appointmentId);
//                    AppointmentsDB.modifyAppointment(newAppointment,appointmentId);
                    AppointmentsDB.update(newAppointment,appointmentId);
                }
                appointmentModified = null;
                labelErrorMessage.setText("");
                Helpers.openMenu(actionEvent,"/wgu/software2/mainForm.fxml");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     Starts the form with the data already present if Appointment has Due to just having one parameter and one statement in
     the body to run when the modify button on mainForm is hit,
     two lambda expressions (forEach) are utilized to reduce the code.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        labelErrorMessage.setText("");
        ObservableList<Customer> customersInDatabase = Customersdb.getAllCustomers();
        ObservableList<Integer> customers = FXCollections.observableArrayList();
        ObservableList<Integer> allUserIds = null;
        try {
            allUserIds = UserDB.getAllUserIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> contacts = FXCollections.observableArrayList();
        contactAll.forEach( (item) -> {contacts.add(item.getContactName());});
            customersInDatabase.forEach( (item) -> {customers.add(item.getId());});

            contactAppointmentField.setItems(contacts);
            custAppointmentField.setItems(customers);
            userIdAppointmentField.setItems(allUserIds);
            if(appointmentModified != null)
            {
                for(Contact item : contactAll) {
                    if(item.getId() == appointmentModified.getContactId()) {
                        contactAppointmentField.setValue(item.getContactName());
                    }
                }
                for(Customer item : customersInDatabase) {
                    if(item.getId() == appointmentModified.getCustomerId()) {
                        custAppointmentField.setValue(item.getId());
                    }
                }
                for(Integer i : allUserIds) {
                    if(Objects.equals(i, appointmentModified.getUserId())) {
                        userIdAppointmentField.setValue(i);
                    }
                }
                titleAppointmentForm.setText("update appointment");
                descAppointmentField.setText(appointmentModified.getDescription());
                titleAppointmentField.setText(appointmentModified.getAppointmentTitle());
                typeAppointmentField.setText(appointmentModified.getAppointmentType());
                idAppointmentField.setText(appointmentModified.getId().toString());
                locAppointmentField.setText(appointmentModified.getLocation());
                LocalDateTime startDate =  LocalDateTime.parse( appointmentModified.getStartDateTime());
                LocalDateTime endDate = LocalDateTime.parse( appointmentModified.getEndDateTime());

                startDateAppointmentField.setText( Helpers.SeperateDateTimeGetDate(( startDate.toString()  )));
                endDateAppointmentField.setText(Helpers.SeperateDateTimeGetDate((endDate.toString())));

                startTimeAppointmentField.setText(Helpers.SeperateDateTimeGetTime((startDate.toString())));
                endTimeAppointmentField.setText(Helpers.SeperateDateTimeGetTime((endDate.toString())));

            }
        else
            {
                idAppointmentField.setText("Randomly generated ID");
            }

    }
}
