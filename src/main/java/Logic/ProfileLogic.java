package Logic;

import Database.DatabaseContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * <h1>Profile Logic</h1>
 * <p>Contains all of the logic for viewing the user profile</p>
 * @author Rahul Mistry : rm721
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @version 0.1
 * @since 16/03/2021
 */
public class ProfileLogic {

    /**
     * Method to get the patientID for the current user
     * @param email - the email of the currently logged in user
     * @param context - gives access to the database
     * @return patID - the patient ID of the current user
     */
    public int getPatientID (String email, DatabaseContext context) {
        int patID = 0;

        String s = "SELECT patientID FROM patient WHERE email = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                patID = rs.getInt("patientID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return patID;
    }

    /**
     * Method to return a list with all the details of the current user
     * @param email - the email of the currently logged in user
     * @param context - gives access to the database
     * @return details - a list of info on the user
     */
    public ArrayList<String> getPatientInfo (String email, DatabaseContext context) {
        ArrayList<String> details = new ArrayList<>();

        int patID = getPatientID(email, context);

        String s = "SELECT * FROM patient WHERE patientID = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setInt(1,patID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String dob = rs.getObject("dob").toString();
                String patientEmail = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String sex = rs.getString("sex");
                Integer nhsNumber = rs.getInt("nhsNumber");
                String prefDocSex = rs.getString("preferredDoctorSex");

                details.add(firstName);
                details.add(lastName);
                details.add(dob);
                details.add(patientEmail);
                details.add(phoneNumber);
                details.add(sex);
                details.add(nhsNumber.toString());
                details.add(prefDocSex);

            } 
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
        
        return details;
    }

    /**
     * Method to update the preferred sex of the doctor
     * @param email - the email of the currently logged in user
     * @param c - the new sex of the doctor
     * @param context - gives access to the database
     */
    public void updateSex(String email, String c, DatabaseContext context) {
        Character prefDocSex = c.toCharArray()[0];
        int patID = getPatientID(email, context);
        String s = "UPDATE patient SET preferredDoctorSex = ? WHERE patientID = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1, String.valueOf(prefDocSex));
            ps.setInt(2,patID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
    }
}
