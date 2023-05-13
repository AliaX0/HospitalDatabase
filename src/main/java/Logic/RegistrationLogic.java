package Logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import Database.DatabaseContext;
import Model.Patient;

/**
 * <h1>Registration Logic</h1>
 * <p>This class is responsible for all the logic checks related to the registration process.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @version 0.1
 * @since 27/02/2021
 */
public class RegistrationLogic {

    /**
     * Checks if two passwords are identical.
     * @param password1
     * @param password2
     * @return a boolean true if passwords match and false otherwise.
     */
    public boolean checkPassword(char[] password1, char[] password2) {
        if (Arrays.equals(password1,password2)) {
            return true;
        }
        else return false;
    }

    /**
     * Checks if the input string is a valid email address.
     * @param s the email string
     * @return a boolean true if the email is valid and false otherwise.
     */
    public boolean checkEmail(String s) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(s);
        if (m.matches()){
            return true;
        }
        else return false;
    }


    public boolean checkExists (String email, DatabaseContext context) 
    {
        boolean exists = true;

        String s = "SELECT email FROM patient WHERE email = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();

            if (rs.next() == false) 
            {
                exists = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        return exists;
    }


    // Takes the user inputs and creates an instance of a user
    public void createPatient(String firstName, String lastName, Date dateOfBirth, String email, String phoneNumber,
                              Character sex, int nhsNumber, Character prefDocSex, String prefContactMethod,
                            String emergencyContact, String password, DatabaseContext context) throws Exception
    {

        try{
            Patient p;
            p = new Patient (sanitizeString(firstName),sanitizeString(lastName),dateOfBirth,sanitizeString(email), phoneNumber,sex,nhsNumber,prefDocSex,prefContactMethod,emergencyContact);
            p.setPasswordHash(password);
            p.createInstance(context);
        } catch (Exception e){
            throw new Exception("Error creating patient");
        }
    }

    public String sanitizeString(String input) {
        String[] inputSplit = input.split("");
        ArrayList<String> r = new ArrayList<>();
        String finalR = "";

        for (String s : inputSplit) {
            if (s.equals("'")) {
                r.add("''");
            } else {
                r.add(s);
            }
        }

        for(String s : r) {
            finalR += s;
        }
        return finalR;
    }

}
