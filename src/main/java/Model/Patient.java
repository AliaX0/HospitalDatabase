package Model;

import Logic.Error;
import Logic.Hashing;
import Database.DatabaseContext;
import Database.IPatientAccessEntity;

import java.sql.ResultSet;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <h1>Patient Model</h1>
 * <p>
 * Model class representing a patient entity that could exist in the database and the operations that can be performed upon it.
 * <p>
 * Implements the IPatientAccessEntity interface
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.2
 * @since 23/03/2021
 */
public class Patient implements IPatientAccessEntity {

    private int patientID; // Placeholder, not sure what format key will be
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;
    private String passwordHash; // Placeholder - we don't need to know this when user logged in
    private String passwordSalt; // Placeholder - we don't need this when the user is logged in
    private Character sex; // Could be an enum
    private Integer nhsNumber;
    private Character preferredDoctorSex; // Could share sex enum
    private String preferredContactMethod; // Should be an enum (text, email .etc)
    private String emergencyContact; // Pointer to foreign key - emergency Contact table

    /**
     * Class Constructor
     * @param firstName <code>String</code> representing Patients Forename
     * @param lastName <code>String</code> representing Patients Surname
     * @param dateOfBirth <code>Date</code> representing the Patients date of birth
     * @param email <code>String</code> representing Patients Email Address
     * @param phoneNumber <code>int</code> representing the Patients phone number
     * @param sex <code>Char</code> representing the Patients sex
     * @param nhsNumber <code>Int</code> representing the patients NHS number
     * @param preferredDoctorSex <code>Char</code> representing the patients preferred doctor sex
     * @param preferredContactMethod <code>String</code> representing patients phone number
     * @param emergencyContact <code>int</code> representing the patients Emergency Contact Number
     */
    public Patient(String firstName, String lastName, Date dateOfBirth, String email, String phoneNumber,
            Character sex, int nhsNumber, Character preferredDoctorSex, String preferredContactMethod,
            String emergencyContact) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.nhsNumber = nhsNumber;
        this.preferredDoctorSex = preferredDoctorSex;
        this.preferredContactMethod = preferredContactMethod;
        this.emergencyContact = emergencyContact;
    }

    /**
     * Getter for the passwordHash field
     * @return String representation of the password field, null if not set
     */
    public String getPasswordHash() {
        return this.passwordHash;
    }

    /**
     * Getter for the passwordSalt field
     * @return the salt used as part of the hash generation process
     */
    public String getPasswordSalt() {
        return this.passwordSalt;
    }

    /**
     * Given a plaintext password, create the hash of the password
     * Handles cases where a salt has not been set
     * @param password <code>String</code> representing plaintext password
     */
    public void setPasswordHash(String password) {
        //TODO: Probably should move this to the Registration logic instead of operating this in the model!
        setPasswordSalt();
        this.passwordHash = Hashing.sha256SumString(password.concat(this.passwordSalt));
    }

    private void setPatientID(int id){
        this.patientID = id;
    }

    /**
     * Private helper set method that sets the password salt to a randomnly generated 8 character string
     * Not Cryptographically secure
     */
    private void setPasswordSalt(){
        //randomly generate
        if(null == this.passwordSalt){
            this.passwordSalt = Hashing.generatePseudoRandomString(8);
        }

    }

    /**
     * Attempt to create a patient row in the datbase using the fields set in this patient instance
     * @param context The <code>DatabaseContext</code> upon which this operation should be performed
     */
    public void createInstance(DatabaseContext context) throws SQLException
    {
        //TODO: Cleanly handle instances where a unique value is already in the datbase
        //TODO: Cleanly handle instances where the patient is already in the datbase (do nothing!)
        //Check to see if entity already exists check two alternate keys (fName, lName, DOB) and (nhsNumber)
        //If does not exist, insert into Patient Table
        //set dbKey

        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO patient (firstName, lastName, dob, email, phoneNumber, passwordHash, passwordSalt, ");
        sb.append("sex, nhsNumber, preferredDoctorSex, preferredContactMethod, emergencyNumber) ");
        sb.append("VALUES ");
        sb.append("('".concat(firstName).concat("', "));
        sb.append("'".concat(lastName).concat("', "));
        sb.append("'".concat(dateOfBirth.toString()).concat("', "));
        sb.append("'".concat(email).concat("', "));
        sb.append("'".concat(phoneNumber.toString()).concat("', "));
        sb.append("'".concat(passwordHash).concat("', "));
        sb.append("'".concat(passwordSalt).concat("', "));
        sb.append("'".concat(sex.toString()).concat("', "));
        sb.append("'".concat(nhsNumber.toString()).concat("', "));
        sb.append("'".concat(preferredDoctorSex.toString()).concat("', "));
        sb.append("'".concat(preferredContactMethod).concat("', "));
        sb.append("'".concat(emergencyContact.toString()).concat("');"));

        try {
            context.commitStatement(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("This patient cannot be created");
        }

        String statement = "SELECT patientID FROM patient WHERE nhsNumber = ?;";

        PreparedStatement ps = context.createPrepStatement(statement);
        ps.setInt(1,nhsNumber);
        ResultSet newPatient = context.executePreparedQuery(ps);
        while (newPatient.next()){
            setPatientID(newPatient.getInt(1));
        }


        //TODO: Figure out why this PS does not want to work
        /**
        sb.append("INSERT INTO patient (firstName, lastName, dob, email, phoneNumber, passwordHash, passwordSalt, ");
        sb.append("sex, nhsNumber, preferredDoctorSex, preferredContactMethod, emergencyNumber) ");
        sb.append("VALUES (? ? ? ? ? ? ? ? ? ? ? ?);");

        String statement = sb.toString();
        try{
            PreparedStatement ps = context.createPrepStatement(statement);

            ps.setString(1, this.firstName);
            ps.setString(2, this.lastName);
            ps.setString(3, this.dateOfBirth.toString()); //TODO: Possible to change this to a timestamp?
            ps.setString(4, this.email);
            ps.setInt(5, this.phoneNumber);
            ps.setString(6, this.passwordHash);
            ps.setString(7, this.passwordHash);
            ps.setString(8, this.sex.toString());
            ps.setInt(9, nhsNumber);
            ps.setString(10, preferredDoctorSex.toString());
            ps.setString(11, this.preferredContactMethod);
            ps.setInt(12, emergencyContact);

            context.executePreparedUpdate(ps);
        } catch (SQLException e){
            e.printStackTrace();
        }
        */
    }

    /**
     * Compute the difference between an in memory version of the the database row and the row as it currently
     * exists in the database and create an update query containing only those changes.
     * @param context The <code>DatabaseContext</code> upon which this operation should be performed
     */
    public void updateInstance(DatabaseContext context)
    {
        throw new UnsupportedOperationException("Not Implemented");

        //Implies that the entity already exists in the table.
        //Throw error if dbKey is not set
        //Get database copy of this object from dbKey (resultSet)
        //compare in memory copy of this object
        //If database field not equal to in memory filed, append this field to be updated to correct value on query
        //Commit Update (limit with where bound by dbID)
    }

    /**
     * Removes the patient by taking a SQL statement, adding their
     * patientID then executing the statement to remove the row from the table
     * @param context The <code>DatabaseContext</code> upon which this operation should be performed
     */
    public void deleteInstance(DatabaseContext context)
    {
        //TODO: Cleanly handle instances where this row does not exist in the table
        //TODO: use an alternate key in instances where the Model is unaware of the current patientID
        String statement = "DELETE FROM patient WHERE patientID = ?;";

        try {
            PreparedStatement ps = context.createPrepStatement(statement);
            ps.setInt(1,patientID);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //statement
        //create preparedstatement
        //give params
        //execute statement

    }
}
