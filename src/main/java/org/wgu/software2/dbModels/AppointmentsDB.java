package org.wgu.software2.dbModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.misc.Helpers;
import org.wgu.software2.models.Appointment;
import org.wgu.software2.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;

/**
 Appointment Database
 @author Nabeel Aref
 */
public class AppointmentsDB {

    /**
     Creates list of all appointments from database.
     @return allAppointmentsList
     @throws SQLException this exception happens in case of a sql error
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet results = ConnectDB.SqlQuery( "SELECT * FROM appointments");
        while (results.next()) {
            int appointmentId = results.getInt("Appointment_Id");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String updatedBy = results.getString("Last_Updated_By");
            String createdBy = results.getString("Created_By");
            String type = results.getString("Type");
            LocalDateTime appointmentStart = results.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = results.getTimestamp("End").toLocalDateTime();
            LocalDateTime lastUpdate = results.getTimestamp("Last_Update").toLocalDateTime();
            LocalDateTime createdDate = results.getTimestamp("Create_Date").toLocalDateTime();
            int customerId = results.getInt("Customer_Id");
            int userId = results.getInt("User_Id");
            int contactId = results.getInt("Contact_Id");
            Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                    appointmentStart.toString(), appointmentEnd.toString(), customerId, userId, contactId,createdBy,createdDate.toString(),updatedBy,lastUpdate.toString());
            allAppointments.add(appointment);
        }
        return allAppointments;
    }

    public static ObservableList<Appointment> getAllAppointmentsIn15Minutes() throws SQLException {

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        ResultSet results = ConnectDB.SqlQuery( "SELECT * FROM appointments WHERE Start BETWEEN ('"+Helpers.getUTCTime( LocalDateTime.now()).toString().replace("T"," ").substring(0,19)+"' - INTERVAL 15 MINUTE) AND ('"+Helpers.getUTCTime( LocalDateTime.now()) .toString().replace("T"," ").substring(0,19)+ "' + INTERVAL 15 MINUTE)");
        System.out.println("SELECT * FROM appointments WHERE Start BETWEEN ('"+Helpers.getUTCTime( LocalDateTime.now()).toString().replace("T"," ").substring(0,19)+"' - INTERVAL 15 MINUTE) AND ('"+Helpers.getUTCTime( LocalDateTime.now()) .toString().replace("T"," ").substring(0,19)+ "' + INTERVAL 15 MINUTE)");
        while (results.next()) {
            int appointmentId = results.getInt("Appointment_Id");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String updatedBy = results.getString("Last_Updated_By");
            String createdBy = results.getString("Created_By");
            String type = results.getString("Type");
            LocalDateTime appointmentStart = results.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = results.getTimestamp("End").toLocalDateTime();
            LocalDateTime lastUpdate = results.getTimestamp("Last_Update").toLocalDateTime();
            LocalDateTime createdDate = results.getTimestamp("Create_Date").toLocalDateTime();
            int customerId = results.getInt("Customer_Id");
            int userId = results.getInt("User_Id");
            int contactId = results.getInt("Contact_Id");
            Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                    appointmentStart.toString(), appointmentEnd.toString(), customerId, userId, contactId,createdBy,createdDate.toString(),updatedBy,lastUpdate.toString());
            allAppointments.add(appointment);
        }
        return allAppointments;
    }

    /**
     A new appointment will be added to the database
     @param apt Pushing an appointment object into the database.
     @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static void addAppointment(Appointment apt) throws SQLException {
        ConnectDB.SqlQuery( "INSERT INTO appointments VALUES (" + generateId() + ",  '" + apt.getAppointmentTitle() + "', '" +
                apt.getDescription() + "', '" + apt.getLocation() + "', '" + apt.getAppointmentType() + "', '" +
                Timestamp.valueOf(  LocalDateTime.parse(apt.getStartDateTime())).toString() + "', '" +
                Timestamp.valueOf(LocalDateTime.parse(apt.getEndDateTime())).toString() + "', NOW(),'" +
                User.getUserName() + "', NOW(),'" + User.getUserName() + "', " + apt.getCustomerId() + ", " + apt.getUserId() + ", " + apt.getContactId() + ");");
    }
    /**
     A new appointment will be added to the database
     @param apt Pushing an appointment object into the database.
     @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
 public static void insert(Appointment apt) throws SQLException {
     String sql = "INSERT INTO appointments (start,end,customer_id,user_id,contact_id,appointment_id,description,location,type,title,created_by,last_updated_by,create_date,last_update) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())";
     PreparedStatement ps = ConnectDB.appConn.prepareStatement(sql);
     ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(apt.getStartDateTime()) ));
     ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.parse( apt.getEndDateTime())));
     ps.setInt(3,apt.getCustomerId());
     ps.setInt(4,apt.getUserId());
     ps.setInt(5,apt.getContactId());
     ps.setInt(6,apt.getId());
     ps.setString(7,apt.getDescription());
     ps.setString(8,apt.getLocation());
     ps.setString(9,apt.getAppointmentType());
     ps.setString(10,apt.getAppointmentTitle());
     ps.setString(11,User.getUserName());
     ps.setString(12,User.getUserName());

     ps.execute();
 }
    /**
      generates a new random number as an id
      @return returns a new number that is used as an id
     */

    private static String generateId() {
        Integer newId =(new Random().nextInt(9999999));
        return newId.toString();
    }

    /**changes the appointment in the database to the given appointment.
     @param apt the appointment new details
     @param id the id of the replaced appointment
     @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static void modifyAppointment(Appointment apt,int id) throws SQLException {
        ConnectDB.SqlQuery(
                "UPDATE appointments SET " +
                        "Title = '" + apt.getAppointmentTitle() +
                        "', Description = '" + apt.getDescription() +
                        "', Location = '" + apt.getLocation() +
                        "', Type = '" + apt.getAppointmentType() +
                        "', Start = '" + Timestamp.valueOf( LocalDateTime.parse(apt.getStartDateTime())).toString() +
                        "', End = '" + Timestamp.valueOf( LocalDateTime.parse(apt.getEndDateTime())).toString() +
                        "', Last_Update = NOW(), Last_Updated_By = '" + User.getUserName() +
                        "', Customer_Id = " + apt.getCustomerId() +
                        ", User_Id = " + apt.getUserId() +
                        ", Contact_Id = " + apt.getContactId() +
                        " WHERE Appointment_Id = " + id
        );
    }
    /**changes the appointment in the database to the given appointment.
     @param apt the appointment new details
     @param id the id of the replaced appointment
     @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static void update(Appointment apt,int id) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_Id = ?, User_Id = ?, Contact_Id = ? WHERE Appointment_Id = ?";
        PreparedStatement ps = ConnectDB.appConn.prepareStatement(sql);
        ps.setString(1,apt.getAppointmentTitle());
        ps.setString(2,apt.getDescription());
        ps.setString(3,apt.getLocation());
        ps.setString(4,apt.getAppointmentType());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.parse(apt.getStartDateTime()) ));
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.parse( apt.getEndDateTime())));
        ps.setString(7,User.getUserName());
        ps.setInt(8,apt.getCustomerId());
        ps.setInt(9,apt.getUserId());
        ps.setInt(10,apt.getContactId());
        ps.setInt(11,id);
        ps.execute();
    }

    /**
     remove the appointment that has this id.
     @param id the to be deleted appointment.
     @throws SQLException this exception happens in case of a sql error
     */
    public static void deleteAppointment(int id) throws SQLException {
        ConnectDB.SqlQuery("DELETE FROM appointments WHERE Appointment_Id =" + id);
    }

    /**
     Gets Appointment from Appointment ID.
     @param id of selected Appointment.
     @return Appointment selected from the ID.
     @throws SQLException this exception happens in case of a sql error
     */
    public static Appointment getAppointmentsById(Integer id) throws SQLException {
        Appointment appointment = null;
        ResultSet results = ConnectDB.SqlQuery( "SELECT * FROM appointments WHERE Appointment_Id = " + id);
        while (results.next()) {
            int appointmentId = results.getInt("Appointment_Id");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String updatedBy = results.getString("Last_Updated_By");
            String createdBy = results.getString("Created_By");
            String type = results.getString("Type");
            LocalDateTime appointmentStart = results.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = results.getTimestamp("End").toLocalDateTime();
            LocalDateTime lastUpdate = results.getTimestamp("Last_Update").toLocalDateTime();
            LocalDateTime createdDate = results.getTimestamp("Create_Date").toLocalDateTime();
            int customerId = results.getInt("Customer_Id");
            int userId = results.getInt("User_Id");
            int contactId = results.getInt("Contact_Id");
            appointment = new Appointment(appointmentId, title, description, location, type,
                    appointmentStart.toString(), appointmentEnd.toString(), customerId, userId, contactId,createdBy,createdDate.toString(),updatedBy,lastUpdate.toString());

        }
        return appointment;
    }

    /**
      this function returns all the appointments that have the customer from them
      @param id the id of the customer
      @return returns those specific appointments
     * @throws SQLException this exception happens in case of a sql error
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerId(Integer id) throws SQLException {
        Appointment appointment = null;
        ObservableList<Appointment> appointments =  FXCollections.observableArrayList();
        ResultSet results = ConnectDB.SqlQuery( "SELECT * FROM appointments WHERE Customer_Id = " + id);
        while (results.next()) {
            int appointmentId = results.getInt("Appointment_Id");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String updatedBy = results.getString("Last_Updated_By");
            String createdBy = results.getString("Created_By");
            String type = results.getString("Type");
            LocalDateTime appointmentStart = results.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = results.getTimestamp("End").toLocalDateTime();
            LocalDateTime lastUpdate = results.getTimestamp("Last_Update").toLocalDateTime();
            LocalDateTime createdDate = results.getTimestamp("Create_Date").toLocalDateTime();
            int customerId = results.getInt("Customer_Id");
            int userId = results.getInt("User_Id");
            int contactId = results.getInt("Contact_Id");
            appointment = new Appointment(appointmentId, title, description, location, type,
                    appointmentStart.toString(), appointmentEnd.toString(), customerId, userId, contactId,createdBy,createdDate.toString(),updatedBy,lastUpdate.toString());
            appointments.add(appointment);
        }
        return appointments;
    }

}