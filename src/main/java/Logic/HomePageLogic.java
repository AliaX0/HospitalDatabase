package Logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DatabaseContext;

/**
 * <h1>Home Page Logic</h1>
 * <p>This class is responsible for the handling the home page logic.</p>
 *
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 12/03/2021
 */
public class HomePageLogic {

    /**
     * Checks if the user has any bookings
     * @param email - currently logged in user
     * @param context - - this is so we can access all of database features
     * @return check - true if there are bookings and false if there aren't any
     */
    public boolean checkBookings(String email, DatabaseContext context) {
        String s = "SELECT COUNT() as total FROM booking b LEFT JOIN patient p ON b.patientID = p.patientID WHERE email = ?";
        boolean check = false;

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int t = rs.getInt("total");
                if (t != 0 ) {
                    check = true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        return check;
    }

    public boolean checkPrescriptions(String email, DatabaseContext context) {
        String s = "SELECT COUNT() as total FROM prescription pr LEFT JOIN patient p ON pr.patientID = p.patientID WHERE email = ?";
        boolean check = false;

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int t = rs.getInt("total");
                if (t != 0 ) {
                    check = true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        return check;
    }
    
}
