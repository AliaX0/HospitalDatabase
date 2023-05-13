package Logic;

import Model.Prescription;
import Database.DatabaseContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * <h1>Prescription Logic</h1>
 * <p>
 * Logic class contianig operations that can be performed using Prescription objects.
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 23/03/2021
 */
public class PrescriptionLogic {

    /**
     * Given a patientID and a Context retrieve the prescriptions for that patient as an <code>ArrayList</code>
     * @param patientID patient who's prescriptions to return should be
     * @param context <code>DatabaseContext</code> to check prescriptions in
     * @return <code>ArrayList</code> of <code>Prescription</code> model objects
     */
    public ArrayList<Prescription> getUserPrescriptions(int patientID, DatabaseContext context){
        return getUserPrescriptions(
                getUserPrescriptionsAsResultSet(patientID, context)
        );
    }

    /**
     * Given a ResultSet of prescriptions, create an ArrayList of Hydrated Prescriptions contained within the ResultSet
     * @param rs A <code>ResultSet</code> from a query operated on the prescription table.
     * @return <code>ArrayList</code> of prescriptions re-hydrated from the database.
     */
    private static ArrayList<Prescription> getUserPrescriptions(ResultSet rs){
        ArrayList<Prescription> prescriptionsList = new ArrayList<>();

        try {
            while(rs.next()){
                prescriptionsList.add(new Prescription(
                        rs.getInt("prescriptionID"),   //Primary Key
                        Util.dateFromSQLiteString(rs.getString("pDate")),  //Date of Prescription
                        rs.getString("pDescription"),//Description
                        rs.getString("pPeriod"),//Period
                        rs.getString("pInstructions"),//Instructions
                        rs.getInt("doctorID"),   //DoctorID
                        rs.getInt("patientID"))); //PatientID
            }
        } catch (SQLException | NullPointerException e) {
            //This should catch RS being set to null and throw a null pointer exception in which case we will return an empty ArrayList
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        return  prescriptionsList;
    }

    /**
     * Get the currently logged in users prescriptions
     * @return A <code>ResultSet</code> of all the prescriptions that the logged in user has on record.
     */
    private static ResultSet getUserPrescriptionsAsResultSet(int pID, DatabaseContext context) {
        //Get all the prescriptions that match this patientID
        //String s = "SELECT * FROM prescription WHERE patientID = ?;";
        String s = "SELECT * FROM prescription WHERE patientID = " + pID + " ;";
        ResultSet rs = null;
        try {

            rs = context.runQuery(s);
            System.out.println(rs.getInt("prescriptionID"));

        } catch (SQLException e){
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        return rs;
    }

}
