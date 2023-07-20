package org.wgu.software2.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.dbModels.AppointmentsDB;
import org.wgu.software2.dbModels.UserDB;
import org.wgu.software2.misc.Helpers;
import org.wgu.software2.misc.Logger;
import org.wgu.software2.models.Appointment;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.wgu.software2.models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 This property governs the behavior of the LoginScreen.fxml scene.
 Allows access to the application by comparing user input of username and password with existing users in the database.
 A successful login returns the scene to the main Form.fxml. Incorrect credentials cause an error message to be displayed.
 All attempts are recorded in the file login activity.txt.
 @author Nabeel Aref
 */
public class LoginScreenController implements Initializable {
    public Button login_Submit;
    public Label login_Error;
    public TextField password_Field;
    public TextField user_Fieldname;
    public Label login_Label;
    public Label user_LabelLocation;
    public Label label_Location;
    @FXML
    public Label label_region;


    /**
     Translates the loginForm to the French language.
     */
    private void translateApp() {
        login_Submit.setText("SOUMETTRE");
        login_Error.setText("Nom d'utilisateur ou mot de passe non reconnu. \nVeuillez réessayer");
        user_LabelLocation.setText("Emplacement de l'utilisateur:");
        password_Field.setPromptText("LE MOT DE PASSE");
        user_Fieldname.setPromptText("NOM D'UTILISATEUR");
        login_Label.setText("CONNEXION");
    }

    /**
     @param actionEvent the action event throws an IOException if the user input of username and password does not match the existing user information in the database.
     Exceptions thrown during data input / output are logged to the login activity.txt file.
     @throws IOException,SQLException this exception happens in case of a sql error
     */
    public void onLoginSubmit(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        if(!ConnectDB.isStillConnected())
        {
            ConnectDB.openConnection();
            if(!ConnectDB.isStillConnected());
            {
                Helpers.WarningBox("You are not connected to the database, please make sure the information in src/main/java/org/wgu/software2/Database/ConnectDB.java is correct.\n Was not able to connect the database, server name, database username and password are correct. please make sure the connection information to the database is correct.");
                ConnectDB.show_database_information();

                return;
            }
        }
        String userName = user_Fieldname.getText();
        String password = password_Field.getText();
        boolean validLogin = UserDB.loginUser(userName, password);
        String currentDate = Helpers.getUTCTime(LocalDateTime.now()).toString();
        currentDate = Helpers.getLocalTime(LocalDateTime.parse(currentDate)).toString();

        if (validLogin) {

            URL url = new File("src/main/resources/org/wgu/software2/MainForm.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 600);
            stage.setScene(scene);
            stage.show();
            ObservableList<Appointment> allAppointments = AppointmentsDB.getAllAppointments();
            AppointmentAlert(allAppointments);
            try {
                Logger.log(true);
            } catch (IOException e) {
                System.out.println("error occured writing to file: ");
                e.printStackTrace();
            }
        } else {
            try {
                Logger.log(false);

            }
            catch (IOException e) {
                System.out.println("error occured writing to file: ");
                e.printStackTrace();
            }
            login_Error.setVisible(true);
            password_Field.setText("");
        }
    }

    /**
     * checks if the appointment is in 15 minutes
     * @param appointment the appointment to see its in 15 minutes
     * @return returns true if its in 15 minutes, else returns false
     */
    private boolean checkAppointmentTime(Appointment appointment) {
        ObservableList<Appointment> appointmentsIn15Minutes;
        try {
            appointmentsIn15Minutes = AppointmentsDB.getAllAppointmentsIn15Minutes();
        } catch (Exception e) {
            Helpers.WarningBox(e.toString());
            return false;
        }

        for (Appointment validAppointment : appointmentsIn15Minutes) {
            if(validAppointment.getId() .equals( appointment.getId()))
                return true;
        }
        return false;
    }
    /**
     When a user successfully logs in, an alert is generated that displays
     any existing Appointments that are within 15 minutes of the user's login.
     @param alerts, the possible alerts that are upcomming
     */
    private void AppointmentAlert(ObservableList<Appointment> alerts) {
        Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
        alertBox.setTitle("Upcoming Appointments");
        alertBox.setHeaderText("No Appointment in 15 minutes");
        String appointmentsMessage = "";

        int amountAppointments=0;
        for (Appointment a : alerts) {

            if(checkAppointmentTime(a)) {
                String addedStr = "AppointmentId: " + a.getId() + "  Date/Time: " + a.getStartDateTime();
                appointmentsMessage = appointmentsMessage + "\n" + addedStr;
                amountAppointments++;

            }
            if(amountAppointments>0)
                alertBox.setHeaderText(String.valueOf(amountAppointments)+" Appointments in 15 minutes:");
        }
        alertBox.setContentText(appointmentsMessage);
        alertBox.showAndWait();
    }

    /**
     Exits the program
     */
    public void onCancel()  {
        Platform.exit();
    }

    /**
     Starts the form with the user's location data and, if the user's computer
     is set to French, translates it to French by calling translateApp ().
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String displayLanguage = TimeZone.getTimeZone(ZoneId.systemDefault()).getDisplayName();
        String language = Locale.getDefault().getLanguage();
        label_Location.setText(displayLanguage);
        login_Submit.setText("submit");
        label_region.setText(Locale.getDefault().getDisplayCountry());
        login_Error.setText("error: the username and password is incorrect\n try again please.");
        user_LabelLocation.setText("current location : ");
        password_Field.setPromptText("password");
        user_Fieldname.setPromptText("username");
        login_Label.setText("LOGIN");
        if (language.equals( "fr")) translateApp();
    }
}
