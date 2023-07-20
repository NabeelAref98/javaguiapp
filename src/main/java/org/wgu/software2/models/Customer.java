package org.wgu.software2.models;

/**
 * This class creates the Customer class
 * @author Nabeel Aref
 */
public class Customer {
    private Integer id;
    private String customerAddress;
    private String customerZipCode;
    private String customerPhone;
    private String createdDate;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private Integer divisionId;
    private String customerName;
    private String divisionName;
    private String country;

    /** Initializes new Customer object
     *
     * @param id id of this customer.
     * @param customerName customerName of this customer.
     * @param customerAddress customerAddress of this customer
     * @param customerZipCode the postal code of this customer
     * @param customerPhone the minimum allowed number of this product in stock
     * @param createdDate date and time this customer was created
     * @param createdBy the user that created this customer record
     * @param lastUpdated date and time this customer record was last updated
     * @param lastUpdatedBy user that updated this customer record last
     * @param divisionId id of the first-level-division where this customer is located
     * @param divisionName customerName of the first-level-division where this customer is located
     * @param country customerName of the country where this customer is located
     */
    public Customer(int id, String customerName, String customerAddress, String customerZipCode, String customerPhone,
                    String createdDate, String createdBy, String lastUpdated, String lastUpdatedBy,
                    int divisionId, String divisionName, String country) {
        this.id = id;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerZipCode = customerZipCode;
        this.customerPhone = customerPhone;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.country = country;

    }

    /**
     * @return returns the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName sets a new customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return returns the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id replaces the old id with the given one
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return returns the customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }
    /**
     * set the customerAddress
     * @param customerAddress  sets the customer address for the object
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
    /**
     *  set the customerZipCode
     * @param customerZipCode  sets the zipcode for the object
     */
    public void setCustomerZipCode(String customerZipCode) {
        this.customerZipCode = customerZipCode;
    }
    /**
     *  set the customerPhone
     * @param customerPhone sets the phone for the object
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    /**
     *  set the createdDate
     * @param createdDate sets the created date for the object
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    /**
     *  set the LastUpdated
     * @param lastUpdated sets the last updated by for the object
     */
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    /**
     *  set the sets the updater for the last update
     * @param lastUpdatedBy sets the last updated by for the object
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /**
     *  set the division id
     * @param divisionId the new id to be set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
    /**
     *  set the division name
     * @param divisionName the new division name
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /**
     *  set the country
     * @param country  the country to be set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return returns customerZipCode
     */
    public String getCustomerZipCode() {
        return customerZipCode;
    }

    /**
     * @return returns the  customerPhone number
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone the customerPhone to set for the Customer
     */
    public void setPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * @return the createdDate of the Customer
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @return returns the creator name
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy sets the new creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return get the last time the record got updated
     */
    public String getLastUpdate() {
        return lastUpdated;
    }


    /**
     * @return returns the last updated of this record
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @return the divisionId of the Customer
     */
    public Integer getDivisionId() {
        return divisionId;
    }


    /**
     * @return returns the divsion name
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @return returns the country
     */
    public String getCountry() {
        return country;
    }
}
