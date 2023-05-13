package Model;

import Database.DatabaseContext;
import Database.IPatientAccessEntity;
import Logic.Error;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h1>Prescription Model</h1>
 * <p>
 * Model class representing a prescription entity that could exist in the database and the operations that can be performed upon it.
 * <p>
 * Implements the IPatientAccessEntity interface
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 18/03/2021
 */
public class Prescription implements IPatientAccessEntity {
    // Primary Key
    private int prescriptionID;
    //Date prescription set
    private Date prescriptionDate;
    //What the is being prescribed
    private String prescriptionDescription;
    //How long the prescription is valid for
    private String prescriptionPeriod;
    //Instructions provided by the doctor for use
    private String prescriptionInstructions;
    //FKey for prescribing doctor
    private Integer doctorID;
    //FKey for the patient prescription prescribed to
    private Integer patientID;

    /**
     * Constructor for a brand new prescription - only exists so that prescriptions can be entered into the database
     * @param pDate Date the prescriprion starts
     * @param pDescription The medication being prescribed
     * @param pPeriod The duration of the prescription
     * @param pInstructions The usage instructions for the medication
     * @param doctorID The PKey of the prescribing doctor
     * @param patientID The PKey of the patient being prescribed to
     */
    public Prescription(Date pDate, String pDescription, String pPeriod, String pInstructions, int doctorID, int patientID) {
        this.prescriptionDate = pDate;
        this.prescriptionDescription = pDescription;
        this.prescriptionPeriod = pPeriod;
        this.prescriptionInstructions = pInstructions;
        this.doctorID = doctorID;
        this.patientID = patientID;
    }

    /**
     * Constructor for a prescription already in the database - should not be called unless mapping prescriptions from result sets
     * @param prescID The PKey of this prescription in the database
     * @param pDate Date the prescriprion starts
     * @param pDescription The medication being prescribed
     * @param pPeriod The duration of the prescription
     * @param pInstructions The usage instructions for the medication
     * @param doctorID The PKey of the prescribing doctor
     * @param patientID The PKey of the patient being prescribed to
     */
    public Prescription(int prescID, Date pDate, String pDescription, String pPeriod, String pInstructions, int doctorID, int patientID) {
        this.prescriptionID = prescID;
        this.prescriptionDate = pDate;
        this.prescriptionDescription = pDescription;
        this.prescriptionPeriod = pPeriod;
        this.prescriptionInstructions = pInstructions;
        this.doctorID = doctorID;
        this.patientID = patientID;
    }

    // Get the prescriptionID for the current prescription.
    public int getPrescriptionID(){
        return prescriptionID;
    }

    // Get the prescriptionDate for the current prescription.
    public Date getPrescriptionDate(){
        return prescriptionDate;
    }

    // Get the prescriptionDescription for the current prescription.
    public String getPrescriptionDescription(){
        return prescriptionDescription;
    }

    // Get the prescriptionPeriod for the current prescription.
    public String getPrescriptionPeriod(){
        return prescriptionPeriod;
    }

    // Get the prescriptionInstructions for the current prescription.
    public String getPrescriptionInstructions(){
        return prescriptionInstructions;
    }
    

    /**
     * Creates a prescription from a result set)
     * @param rs
     */
    private Prescription (ResultSet rs){

    }

    /**
     * Creates a prescription in the database using the values set in the current instance of the object
     * @param context The database context upon which the new table row will be created in.
     * @throws SQLException
     */
    @Override
    public void createInstance(DatabaseContext context) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO prescription (pDate, pDescription, pPeriod, pInstructions, patientID, doctorID) ");
        sb.append("VALUES ");
        sb.append("('".concat(prescriptionDate.toString()).concat("', "));
        sb.append("'".concat(prescriptionDescription).concat("', "));
        sb.append("'".concat(prescriptionPeriod).concat("', "));
        sb.append("'".concat(prescriptionInstructions).concat("', "));
        sb.append("'".concat(patientID.toString()).concat("', "));
        sb.append("'".concat(doctorID.toString()).concat("');"));

        String statement = sb.toString();

        try {
            context.commitStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("This prescription cannot be created");
        }
    }

    @Override
    public void updateInstance(DatabaseContext context) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public void deleteInstance(DatabaseContext context) {
        String statement = "DELETE FROM prescription WHERE prescriptionID = ?;";

        try {
            PreparedStatement ps = context.createPrepStatement(statement);
            ps.setInt(1,patientID);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
    }

   
}
