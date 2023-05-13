package Model;

import Database.DatabaseContext;
import Database.IPatientAccessEntity;
import Logic.Error;

import java.sql.ResultSet;
import java.util.Date;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <h1>Doctor Model</h1>
 * <p>
 * Model class representing a Doctor entity that could exist in the database and the operations that can be performed upon it.
 * <p>
 * Implements the IPatientAccessEntity interface
 *
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 23/03/2021
 */
public class Doctor implements IPatientAccessEntity {
    private Integer doctorID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private Character sex;
    private Integer nhsNumber; 

    public Doctor(String firstName, String lastName, Date dateOfBirth,
                 String phoneNumber, Character sex, Integer nhsNumber) 
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.nhsNumber = nhsNumber;
    }

    private void setDoctorID(int id){
        this.doctorID = id;
    }

    /**
     * takes the values of the current Doctor object, creates a string to insert
     * the data into the table. This statement is then executed
     * @throws Exception
     */
    public void createInstance(DatabaseContext context) throws SQLException {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO doctor (firstName, lastName, dob, phoneNumber,sex, nhsNumber) ");
        sb.append("VALUES ");
        sb.append("('".concat(firstName).concat("', "));
        sb.append("'".concat(lastName).concat("', "));
        sb.append("'".concat(dateOfBirth.toString()).concat("', "));
        sb.append("'".concat(phoneNumber.toString()).concat("', "));
        sb.append("'".concat(sex.toString()).concat("', "));
        sb.append("'".concat(nhsNumber.toString()).concat("');"));

        String statement = sb.toString();

        try {
            context.commitStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("The doctor cannot be created");
        }

        statement = "SELECT doctorID FROM doctor WHERE nhsNumber = ?;";
        try {
            PreparedStatement ps = context.createPrepStatement(statement);
            ps.setInt(1,nhsNumber);
            ResultSet newDoctor = context.executePreparedQuery(ps);
            while (newDoctor.next()){
                setDoctorID(newDoctor.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Error.displayErrorInGUI("Uh Oh - Something went wrong :(", Error.getStackTraceStringFromException(e));
        }

    }

    public void updateInstance(DatabaseContext context) {
        throw new UnsupportedOperationException("Not Implemented");
        //this is a method that would be needed but with the time 
        //we have, this is not something that needs to be implemented
        //at this time.
    }

    public void deleteInstance(DatabaseContext context) {

        String statement = "DELETE FROM doctor WHERE doctorID = ?;";

        try {
            PreparedStatement ps = context.createPrepStatement(statement);
            ps.setInt(1,doctorID);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

    }
    
}
