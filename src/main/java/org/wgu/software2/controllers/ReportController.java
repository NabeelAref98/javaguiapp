package org.wgu.software2.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.dbModels.AppointmentsDB;
import org.wgu.software2.dbModels.ContactsDB;
import org.wgu.software2.dbModels.CountriesDB;
import org.wgu.software2.misc.Helpers;
import org.wgu.software2.models.Appointment;
import org.wgu.software2.models.Contact;
import org.wgu.software2.models.Country;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 @author Nabeel Aref
 Report menu controller. Generates report data and displays in text view.
 */
public class ReportController implements Initializable {
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    private ObservableList<Appointment> allAppointments = AppointmentsDB.getAllAppointments();
    private ObservableList<Appointment> typeMonthAppointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> contacthAppointments = FXCollections.observableArrayList();
    private ObservableList<Contact> allContacts = ContactsDB.getAllContacts();
    private ObservableList<Country> countries = FXCollections.observableArrayList();
    private ArrayList<Integer> country_ids = new ArrayList<>();

    @FXML
    TableView appointment_Table_type;
    @FXML
    TableView appointment_Table_contact;
    @FXML
    public TableColumn<Appointment, Integer> appointment_Id_contacts;
    @FXML

    public TableColumn<Appointment, String> appointment_Title_contacts;
    @FXML

    public TableColumn<Appointment, String> appointment_Description_contacts;
    @FXML

    public TableColumn<Appointment, Integer> appointment_Contact_contacts;
    @FXML


    public TableColumn<Appointment, String> appointment_Type_contacts;
    @FXML

    public TableColumn<Appointment, String > appointment_Start_contacts;
    @FXML

    public TableColumn<Appointment, String > appointment_End_contacts;
    @FXML

    public TableColumn<Appointment, Integer> appointment_CustomerId_contacts;
    @FXML
    public TableColumn<Appointment, Integer> appointment_Id;
    @FXML

    public TableColumn<Appointment, String> appointment_Title;
    @FXML

    public TableColumn<Appointment, String> appointment_Description;
    @FXML

    public TableColumn<Appointment, Integer> appointment_Contact;
    @FXML

    public TableColumn<Appointment, String> appointment_Location;
    @FXML

    public TableColumn<Appointment, String> appointment_Type;
    @FXML

    public TableColumn<Appointment, String > appointment_Start;
    @FXML

    public TableColumn<Appointment, String > appointment_End;
    @FXML

    public TableColumn<Appointment, Integer> appointment_CustomerId;
    @FXML

    public TableColumn<Appointment, Integer> appointment_UserId;

    @FXML
    public TextArea countries_Text;
    @FXML
    public Label total_label;
    @FXML
    public Label total_contacts;
    @FXML
    public ComboBox<String> appointment_type_combo;
    @FXML
    public ComboBox<String> appointment_month_combo;
    @FXML
    public ComboBox<String> contact_combo;

    public ReportController() throws SQLException {
    }
    /**
     * gathers all the types available from appointments and returns them in a list
     * @return the list of all the types
     */
    public ObservableList<String> gatherTypes()
    {
        ObservableList<String> allTypes = FXCollections.observableArrayList();
        allTypes.add("all");
        //lambda with multiple lines used in this function
        allAppointments.forEach(appointment -> {
            if(!allTypes.contains(appointment.getAppointmentType().toLowerCase()))
                allTypes.add(appointment.getAppointmentType().toLowerCase());
        });
        return allTypes;
    }
    /**
     * gathers all the contacts available from appointments and returns them in a list
     * @return the list of all the contacts
     */
    public ObservableList<String> gatherContacts()
    {
        ObservableList<String> allContacts = FXCollections.observableArrayList();
        allContacts.add("all");
        //lambda with multiple lines used in this function
        allAppointments.forEach(appointment -> {
            try {
                String contactName = ContactsDB.getContactName(appointment.getContactId()).toLowerCase();
                if(!allContacts.contains(contactName) )allContacts.add(contactName);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
        return allContacts;
    }

    /**
     * gathers all the months available from appointments and returns them in a list
     * @return the list of all the months
     */
    public ObservableList<String> gatherMonths()
    {
        ObservableList<String> allMonths = FXCollections.observableArrayList();
        allMonths.add("all");
        //lambda with multiple lines used in this function
        allAppointments.forEach(appointment -> {
            String month = String.valueOf( LocalDateTime.parse(appointment.getStartDateTime()).getMonth().getValue()) +"/"+
                    String.valueOf(LocalDateTime.parse(appointment.getStartDateTime()).getYear());
            if(!allMonths.contains(month))
                allMonths.add(month);
        });
        return allMonths;
    }




    /**
     Queries and formats report for displaying all country information
     @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public void getCountryData() throws SQLException {
        countries = CountriesDB.getAllCountries();
        String data ="";
        countries.forEach(country -> country_ids.add(country.getId()));
        for (int countryId : country_ids) {
            ResultSet results = ConnectDB.SqlQuery("SELECT Country_Id,Country FROM countries WHERE Country_Id = " + countryId);
            while (results.next()) {
                String countryIdData = "Country_Id: " + results.getString("Country_Id");
                String countryData = "Country: " + results.getString("Country");
                data+=String.format("%-20s%-20s", countryIdData, countryData)+"\n";
            }
        }
        countries_Text.setText(data);
    }

    /**
     * returns the month-year format of an appointment
     * @param appointment needed appointment to get the string format of month-year
     * @return returns the month-year
     */
    public String getMonth(Appointment appointment)
    {
        return  String.valueOf( LocalDateTime.parse(appointment.getStartDateTime()).getMonth().getValue()) +"/"+
                String.valueOf(LocalDateTime.parse(appointment.getStartDateTime()).getYear());
    }

    /**
     * this sorts the month given and leaves everything out
     * @param month only this month will stay
     */
    public void sortMonth(String month)
    {
        if(month.equals("all"))return;
        for (int i=0;i<typeMonthAppointments.size();)
        {

            Appointment appointment = typeMonthAppointments.get(i);
            if(!getMonth(appointment).equals(month))
                typeMonthAppointments.remove(i);
            else i++;

        }
    }

    /**
     * this functions sorts the types from the type given
     * @param type only this type will stay
     */
    public void sortType(String type)
    {
        if(type.equals("all"))return;
        for (int i=0;i<typeMonthAppointments.size();)
        {
            Appointment appointment = typeMonthAppointments.get(i);
            if(!appointment.getAppointmentType().toLowerCase().equals(type))
                typeMonthAppointments.remove(i);
            else i++;
        }
    }

    /**
     * sorts only the contacts alone
     * @throws SQLException this exception happens in case of a sql error may throw exception
     */
    public void sortContact() throws SQLException {
        contacthAppointments = FXCollections.observableArrayList();
        allAppointments.forEach(appointment -> contacthAppointments.add(appointment));
        total_contacts.setText("Total: "+String.valueOf( contacthAppointments.size()));
        appointment_Table_contact.setItems(contacthAppointments);
        if(contact_combo.getValue()==null) return;
        String contact = contact_combo.getValue();

        if(contact.equals("all"))return;
        for (int i=0;i<contacthAppointments.size();)
        {
            Appointment appointment = contacthAppointments.get(i);

            String contactName = ContactsDB.getContactName(appointment.getContactId()).toLowerCase();
            if(!contactName.equals(contact))
                contacthAppointments.remove(i);
            else i++;

        }
        total_contacts.setText("Total: "+String.valueOf( contacthAppointments.size()));
        appointment_Table_contact.setItems(contacthAppointments);
    }

    /**
     * sorts the types and months together combing both sort functions
      */
    public void SortTypesMonth()
    {
        typeMonthAppointments = FXCollections.observableArrayList();
        allAppointments.forEach(appointment -> typeMonthAppointments.add(appointment));
        if(appointment_month_combo.getValue()!=null)
            sortMonth(appointment_month_combo.getValue());
        if(appointment_type_combo.getValue()!=null)
            sortType(appointment_type_combo.getValue());
        appointment_Table_type.setItems(typeMonthAppointments);

    }
    /**
     Return user to main menu
     @param click execute on button click
     @throws SQLException happens in case of a sql database error
     */
    public void onContactsChange(ActionEvent click) throws SQLException {
        sortContact();
        total_label.setText("Total: "+String.valueOf( typeMonthAppointments.size()));

    }
    /**
     calls the sort function
     @param click execute on button click
     @throws IOException File open exception handler
     */
    public void onTypesChange(ActionEvent click) throws IOException {
        SortTypesMonth();
        total_label.setText("Total: "+String.valueOf( typeMonthAppointments.size()));

    }
    /**
     calls the sort function
     @param click execute on button click
     @throws IOException File open exception handler
     */
    public void onMonthChange(ActionEvent click) throws IOException {
        SortTypesMonth();
        total_label.setText("Total: "+String.valueOf( typeMonthAppointments.size()));
    }



    /**
      Return user to main menu
      @param click execute on button click
      @throws IOException File open exception handler
     */
    public void onHome(ActionEvent click) throws IOException {
        Helpers.openMenu(click, "wgu/software2/MainForm.fxml");
    }
    /**
     Fills out text sections with relevant report data
     @param url unused
     @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allAppointments.forEach(appointment ->
            {
                contacthAppointments.add(appointment);
                typeMonthAppointments.add(appointment);
            });



            getCountryData();
            appointment_Id.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("id"));
            appointment_Title.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointmentTitle"));
            //     lambda expression was also used here to clear the letter T in the application, which improves and eases up the modification rather than doing a for loop
            appointment_Start.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime().replace("T"," ")));
            appointment_Description.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
            appointment_Contact.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("contactId"));
            appointment_Location.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
            appointment_Type.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointmentType"));
            //     lambda expression was also used here to clear the letter T in the application, which improves and eases up the modification rather than doing a for loop

            appointment_End.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDateTime().replace("T"," ")));
            appointment_CustomerId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
            appointment_UserId.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userId"));


            appointment_Id_contacts.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("id"));
            appointment_Title_contacts.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointmentTitle"));
            //     lambda expression was also used here to clear the letter T in the application, which improves and eases up the modification rather than doing a for loop
            appointment_Start_contacts.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDateTime().replace("T"," ")));
            appointment_Description_contacts.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
            appointment_Contact_contacts.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("contactId"));
            appointment_CustomerId_contacts.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("customerId"));
            appointment_Type_contacts.setCellValueFactory(new PropertyValueFactory<Appointment,String>("appointmentType"));

            appointment_Table_contact.setItems(contacthAppointments);
            appointment_Table_type.setItems(typeMonthAppointments);

            contact_combo.setItems(gatherContacts());
            appointment_type_combo.setItems(gatherTypes());
            appointment_month_combo.setItems(gatherMonths());
            total_contacts.setText("Total: "+String.valueOf( contacthAppointments.size()));
            total_label.setText("Total: "+String.valueOf( typeMonthAppointments.size()));



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
