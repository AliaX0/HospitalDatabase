package Logic;

import Database.DatabaseContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h1>Login - Logic Class</h1>
 * Contains all of the logic used to handle the login process
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.3
 * @since 23/03/2021
 */
public class Login {
    private static String currentLoggedIn;
    private static int currentLoggedInID;

    /**
     * Checks to see if a a given password matches the hash in the database after concatenating with salt
     * and applying hashing algorithm
     * @param email <code>String</code> email to check for in DB
     * @param password <code>String</code> representing password provided
     * @param context - <code>DatabseContext</code> to run SQL queries against
     * @return True if there was a match, False otherwise
     */
    public static boolean checkLoginDetails(String email, String password, DatabaseContext context)
    {
        boolean authenticated = false;
        String statement = "SELECT passwordHash, passwordSalt, patientID\n"
                + "FROM patient \n"
                + "WHERE email = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(statement);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            String hash = rs.getString("passwordHash");
            String salt = rs.getString("passwordSalt");

            if(Hashing.sha256SumString(password.concat(salt)).equals(hash)){
                authenticated = true;
                setLoggedInID(rs.getInt("patientID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authenticated;
    }

    public static void setLoggedIn(String loggedIn){
        Login.currentLoggedIn = loggedIn;
    }
    private static void setLoggedInID(int loggedInID) {
        Login.currentLoggedInID = loggedInID;
    }
    public static String getLogin() {
        return currentLoggedIn;
    }
    public static int getLoginID(){
        return Login.currentLoggedInID;
    }
}
