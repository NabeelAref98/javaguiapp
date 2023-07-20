package org.wgu.software2.models;

/** This class creates the Country data model object.
 * @author Nabeel Aref.
 */
public class Country {
    private String countryName;
    private int id;

    /** creates new object
     *
     * @param id the id of the that specific country.
     * @param countryName name of that country.
     */
    public Country(String countryName, int id) {
        this.id = id;
        this.countryName = countryName;
    }

    /**
     * @return returns the country name
     */
    public String getCountryName() {
        return countryName;
    }
    /**
     * @param countryName sets new country name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return returns the country id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id sets new country id
     */
    public void setId(int id) {
        this.id = id;
    }
}
