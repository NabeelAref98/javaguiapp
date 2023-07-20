package org.wgu.software2.dbModels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.software2.Database.ConnectDB;
import org.wgu.software2.models.Division;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
  Divisions Database
  @author Nabeel Aref
 */
public class DivisionsDB {

    /**
      returns all the dvisions in alist from the database
      @return allDivisions the list of the divsions
     @throws SQLException in case of a sql database error
     */
    public static ObservableList<Division> getAllDivisions() throws SQLException {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        ResultSet results = ConnectDB.SqlQuery("SELECT * FROM first_level_divisions");
        while (results.next()) {
            int id = results.getInt("Division_Id");
            String division = results.getString("Division");
            int countryId = results.getInt("Country_Id");
            Division newDivision = new Division( division,id, countryId);
            allDivisions.add(newDivision);
        }
        return allDivisions;
    }





    /**
      Get divisions Id by its name
      @param name Country Id to grab division Id from
      @return name of the division of the wanted id
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static int getDivisionIdbyName(String name) throws SQLException {
        int id = 0;
        ResultSet results = ConnectDB.SqlQuery("SELECT Division_Id FROM first_level_divisions WHERE Division = " + "'" + name + "'");
        while (results.next()) id = results.getInt("Division_Id");

        return id;
    }

    /**
      get the division name from the id given of the division
      @param id the division id of the wanted division name
      @return name of the division
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static String getDivisionName(int id) throws SQLException {
        String name = "";
        ResultSet results = ConnectDB.SqlQuery("SELECT Division FROM first_level_divisions WHERE Division_Id = " + id);
        while (results.next()) {
            name = results.getString("Division");
        }
        return name;
    }

    /**
      get the division country from the division id
      @param id the id of the divison
      @return country name of that division of the given id
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static String getCountryName(int id) throws SQLException {
        String name = "";
        ResultSet results = ConnectDB.SqlQuery("SELECT Country FROM countries AS c, first_level_divisions AS f WHERE f.Country_Id = c.Country_Id AND Division_Id = " + id);
        while (results.next()) name = results.getString("Country");
        return name;
    }

    /**
        gets all the divisions stored in the database that has the watned country id
      @param id Country Id to get division names
      @return allDivisions returns an array of the divisions that the country id refers to
      @throws SQLException this exception happens in case of a sql error SQL exception handler
     */
    public static ArrayList<String> getDivisionsByCountry(int id) throws SQLException {
        ArrayList<String> allDivisions = new ArrayList<>();
        ResultSet results = ConnectDB.SqlQuery("SELECT Division FROM first_level_divisions WHERE Country_Id = " + id);
        while (results.next()) allDivisions.add(results.getString("Division"));
        return allDivisions;
    }
}
