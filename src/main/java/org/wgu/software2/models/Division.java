package org.wgu.software2.models;

/** This is the division object class
 * @author Nabeel Aref
 */
public class Division {
    private String DivisionName;
    private int id;
    private int countryId;

    /** creates new object
     *
     * @param DivisionName the DivisionName(name of the division)
     * @param id the id
     * @param countryId the id of the country of the division
     */
    public Division(String DivisionName, int id, int countryId) {
        this.id = id;
        this.DivisionName = DivisionName;
        this.countryId = countryId;
    }

    /**
     * @return returns the division name
     */
    public String getDivisionName() {
        return DivisionName;
    }

    /**
     * @param DivisionName sets new division name
     */
    public void setDivisionName(String DivisionName) {
        this.DivisionName = DivisionName;
    }

    /**
     * @return returns the division id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id sets new division id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return returns the division country
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId sets new country for the division
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
