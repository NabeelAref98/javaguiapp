package org.wgu.software2.dbModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.models.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
  Country database
  @author Nabeel Aref
 */
public class CountriesDB {

    /**
      gets the list of countires in the database.
      @return allCountries returns all countries
     @throws SQLException this exception happens in case of a sql error
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        ResultSet results = ConnectDB.SqlQuery("SELECT * FROM countries");
        while (results.next()) {
            int id = results.getInt("Country_Id");
            String name = results.getString("Country");
            Country country = new Country(name,id);
            allCountries.add(country);
        }
        return allCountries;
    }

    /**
      returns the country id if we know t he name
      @param name the name needed to find the id
     @return returns the id of the country wanted
      @throws SQLException this exception happens in case of a sql error
     */
    public static int getCountryId(String name) throws SQLException {
        int id = 0;
        ResultSet results = ConnectDB.SqlQuery("SELECT Country_Id FROM countries WHERE Country = " + "'" + name + "'");
        while (results.next()) id = results.getInt("Country_Id");

        return id;
    }

}
