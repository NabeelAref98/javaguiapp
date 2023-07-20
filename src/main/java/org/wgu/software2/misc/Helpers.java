package org.wgu.software2.misc;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.dbModels.AppointmentsDB;
import org.wgu.software2.models.Appointment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 @author Nabeel Aref
 Most frequently utilized features of the application.
 */
public class Helpers {


    /**
     Most frequently used components of the program.
     */
    public static void setLocale() {
        Locale.setDefault(Locale.getDefault());
    }

    /**
      verify if the time string given is parsable
      @param time the time string to try and parse
      @return returns true if its parsable else returns false
     */
    public static boolean verifyDateTimeisValid(String time)
    {
        try{
            LocalDateTime.parse(time);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
      verify if the time string given is parsable
      @param time the time string to try and parse
      @return returns true if its parsable else returns false
     */
    public static boolean verifyTimeisValid(String time)
    {
        try{
            LocalTime.parse(time);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
      verify if the date string given is parsable
      @param date the date string to try and parse
      @return returns true if its parsable else returns false
     */
    public static boolean verifyDateisValid(String date)
    {
        try{
            LocalDate.parse(date);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    /**
     Helper menu switcher
     @param event Event to engage menu switch.
     @param newMenu string representing the new menu upon FXML file opening.
     @throws IOException Handle in case of file opening error occurs
     */
    public static void openMenu(ActionEvent event, String newMenu) throws IOException {
        URL url = new File("src/main/resources/org/"+newMenu).toURI().toURL();
        Parent parent = FXMLLoader.load(url);
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    /**
     Get Zone ID.
     @return Current Zone ID
     */
    public static ZoneId getZoneId()
    {
        return ZoneId.systemDefault();
    }

    /**
     Accepts a String as input and generates an alert with that string.
     @param info output of error screen
     */
    public static void WarningBox(String info) {
        Alert alert = new Alert(Alert.AlertType.WARNING,info);
        alert.setTitle("Warning");
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setContentText(info);
        alert.showAndWait();
    }

    /**
     Accepts a String as input and outputs it as a confirmation message.
     @param info a confirmation screen's output
     @return returns true if the user chooses ok
     */
    public static boolean ConfirmationBox(String info) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(info);
        alert.setTitle("are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
      Returns a new id for the database for any object, the ID is random.
      @return the randomly generated ID.
     */
    public static int generateRandomId()
    {
        return new Random().nextInt(999999);
    }

    /**
     @param ldt conversion of the local timestamp to UTC.
     @return UTC timestamp.
     */
    public static LocalDateTime getUTCTime(LocalDateTime ldt) {
        return ldt;// ldt.atZone(getZoneId()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    /**
     @param utc converts a UTC timestamp to the user's local time zone.
     @return converted local timestamp.
     */
    public static LocalDateTime getLocalTime(LocalDateTime utc) {
        return utc;//utc.atZone(getZoneId()).toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(getZoneId()).toLocalDateTime();
    }

    /**
     @param utc To convert UTC time to EST for commercial needs.
     @return converted EST timestamp
     */
    public static LocalDateTime getBusinessTime(LocalDateTime utc) {
        return utc.atZone(getZoneId()).toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("US/Eastern")).toLocalDateTime();
    }

    /**
      check hour is in business time
      @param currentTime the time to check if the is in the business hours
      @return returns true if time is in business hours else it returns false
     */
    public static boolean isHourInBusinessHours(LocalDateTime currentTime) {
        if(currentTime.getDayOfWeek() == DayOfWeek.SATURDAY || currentTime.getDayOfWeek() == DayOfWeek.SUNDAY) return false;
        if(currentTime.getHour() <7 || currentTime.getHour() > 21) return false;
        return true;
    }

    /**
     Returns if the  appointment time is in business hours
     @param startTime the time that the appointment starts
     @param endTime the time that the appointment ends
     @return returns true if it is in business hours
     */
    public static boolean isAppointmentTimeInBusinessHours(LocalDateTime startTime,LocalDateTime endTime)
    {
        if(startTime.getDayOfMonth()!=endTime.getDayOfMonth())
            return false;
        if(isHourInBusinessHours(startTime) && isHourInBusinessHours(endTime))
            return true;
        return false;
    }

    /**
     Make that the new or amended appointment is being scheduled within business hours
     and that it won't conflict with any of the customer's already scheduled appointments.
     It is decided whether to return true or false by calling verifyOverlap() and verifyBusHours().
     @param startTime set the appointment's starting dateTime (yyyy-MM-dd HH:mm:ss).
     @param endTime set the appointment's ending dateTime string (yyyy-MM-dd HH:mm:ss).
     @param customer_Id set the customer id linked to the newly created or updated appointment.
     @return true if there is no overlap, and it occurs within business hours; otherwise, it returns false.
     @throws SQLException in case of a sql database error
     */
    public static boolean verifyTimeAvailable(String startTime, String endTime, int customer_Id,int appointment_Id) throws SQLException {

        if (verifyIsntAppointmentPossible(startTime, endTime, customer_Id,appointment_Id)) {
            Helpers.WarningBox("Error: Appointment time overlaps another appointment \n       with the same customer");
            return false;
        }
        if (!Helpers.isAppointmentTimeInBusinessHours( getBusinessTime(LocalDateTime.parse( startTime)),getBusinessTime(LocalDateTime.parse( endTime)))) {
            Helpers.WarningBox("Error: Appointment time is outside of business hours" +
                    " \n       Business hours: (08:00:00 - 22:00:00 EST)");

            return false;
        }
        return true;
    }

    /**
      returns the date from the dataTime String
      @param dateString the date and time needed.
      @return returns the date of the string
     */
    public static String SeperateDateTimeGetDate(String dateString) {
        String[] split = dateString.split("T");
        return split[0];

    }

    /**
      returns the time from the dataTime String
      @param dateString the date and time needed.
      @return returns the time of the string
     */
    public static String SeperateDateTimeGetTime(String dateString) {
        String[] split = dateString.split("T");
        return split[1];
    }

    /**
      Verify if appointment does not overlap with the other appointments this customer has
      @param startTime start time of the appointment
      @param endTime end time of the appointment
     @param customer_Id the id of the customer that has the appointments
     @param appointment_Id the id of the appointment that is being checked
      @return returns true if its possible to have the appointment or false if not
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static boolean verifyIsntAppointmentPossible(String startTime,String endTime,int customer_Id, int appointment_Id) throws SQLException {
        int amountCut = (startTime.length()>17) ?19:16;
        ResultSet results= ConnectDB.SqlQuery("SELECT * FROM appointments "
                + "WHERE ('"+startTime.toString().replace("T"," ").substring(0,amountCut) +"' BETWEEN start AND end OR '"+
                endTime.toString().replace("T"," ").substring(0,amountCut) +"' BETWEEN start AND end OR '"+
                startTime.toString().replace("T"," ").substring(0,amountCut) +"' < start AND '"+
                endTime.toString().replace("T"," ").substring(0,amountCut) +"' > end) AND (Customer_ID = "+String.valueOf(customer_Id)
                +" AND appointment_ID != "+ String.valueOf(appointment_Id) +")");


        if (results.next()) {
            return true;
        }

        return false;
    }

}
