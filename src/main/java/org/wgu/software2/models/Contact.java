package org.wgu.software2.models;

/** This class creates Contact object data model
 * @author Nabeel Aref.
 */
public class Contact {
    private String contactName;


    private String contactEmail;
    private int id;

    /** creates new object
     *
     * @param id id of the contact.
     * @param contactName name of the contact.
     */
    public Contact(String contactName, int id) {
        this.contactName = contactName;
        this.id = id;
        this.contactEmail=contactName+"@company.com";
    }
    public Contact(String contactName, int id,String contactEmail) {
        this.contactName = contactName;
        this.id = id;
        this.contactEmail=contactEmail;
    }

    /**
     * @return returns the name of the contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName sets the name of the contact
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return returns the id of the contact
     */
    public int getId() {
        return id;
    }

    /**
     * @param id sets the id of the contact
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
