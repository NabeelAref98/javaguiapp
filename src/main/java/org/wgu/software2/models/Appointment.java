package org.wgu.software2.models;

/** This class holds the appointment data model used by the application which has the datastructure of the appointment table in the database
 * @author Nabeel Aref.
 */
public class Appointment {
    private String description;
    private int contactId;
    private String location;
    private String appointmentType;
    private String endDateTime;
    private int customerId;
    private int userId;
    private String createdBy;
    private String createDate;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int id;
    private String appointmentTitle;
    private String startDateTime;

    /** Creates a new Object
     *
     * @param id the new id of the appointment.
     * @param appointmentTitle appointment new name.
     * @param startDateTime Time the appointment starts
     * @param description the description
     * @param contactId the id of the contact
     * @param location where the appointment is
     * @param appointmentType the appointmentType
     * @param endDateTime Time the appointment ends
     * @param customerId the customer id
     * @param userId the user id
     * @param createdBy who created it
     * @param createDate when its created
     * @param lastUpdate when its last updated
     * @param lastUpdatedBy who updated it
     */
    public Appointment(int id, String appointmentTitle,  String description, String location, String appointmentType, String startDateTime, String endDateTime,
                        int customerId, int userId,int contactId, String createdBy, String createDate, String lastUpdate, String lastUpdatedBy) {
        this.appointmentTitle = appointmentTitle;
        this.id = id;
        this.startDateTime = startDateTime;
        this.description = description;
        this.contactId = contactId;
        this.location = location;
        this.appointmentType = appointmentType;
        this.endDateTime = endDateTime;
        this.customerId = customerId;
        this.userId = userId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }



    /**
     * @return returns the appointment description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description sets a new description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return returns the appointment contact id
     */
    public int getContactId() {
        return contactId;
    }
    /**
     * @param contactId sets a new contact id
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    /**
     * @return returns the appointment location
     */
    public String getLocation() {
        return location;
    }
    /**
     * @param location sets a new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return returns the appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * @param appointmentType sets a new appointment type
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
    /**
     * @return returns the appointment end date time
     */
    public String getEndDateTime() {
        return endDateTime;
    }
    /**
     * @param endDateTime sets a new endDateTime
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
    /**
     * @return returns the appointment customer id
     */
    public int getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId sets a new customer id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**
     * @return returns the appointment creator
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    /**
     * @param lastUpdatedBy sets a new updater for the appointment
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /**
     * @return the title
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }
    /**
     * @param appointmentTitle sets a new title
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }
    /**
     * @return returns the appointment start time
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime sets a new start time
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }


    /**
     * @return returns the id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id sets a new id
     */
    public void setId(int id) {
        this.id = id;
    }



    /**
     * @return returns the appointment userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId sets a new user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return returns the appointment creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy sets a new appointment creator
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return returns the appointment created date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate sets a new create date of the appointment
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return returns the appointment last update
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate sets a new last update for the appoointment
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


}
