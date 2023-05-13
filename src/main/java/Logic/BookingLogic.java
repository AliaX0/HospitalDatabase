package Logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.util.*;
import java.util.ArrayList;

import org.jdatepicker.impl.JDatePickerImpl;

import Database.DatabaseContext;
import Model.Booking;

/**
 * <h1>Booking Logic</h1>
 * <p>This class is responsible for the handling the booking logic.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 12/03/2021
 */
public class BookingLogic {

    public HashMap<String,Long[]> timeMap = new HashMap<>();

    // Creates a time map that returns a long based on a string input.
    private void setupTimeMap() {
        timeMap.put("9:00",new Long[]{9L, 0L});
        timeMap.put("10:00",new Long[]{10L, 0L});
        timeMap.put("11:00",new Long[]{11L, 0L});
        timeMap.put("12:00",new Long[]{12L, 0L});
        timeMap.put("13:00",new Long[]{13L, 0L});
        timeMap.put("14:00",new Long[]{14L, 0L});
        timeMap.put("15:00",new Long[]{15L, 0L});
        timeMap.put("16:00",new Long[]{16L, 0L});
    }


    /**
     * Return a timestamp with the chosen date and time
     * @param timeStamp - the user selected date with time set to midnight
     * @param timeString - the user selected time
     * @return timeStamp - a timestamp with the user selected date and time
     */
    public LocalDateTime getFullTimeStamp(LocalDateTime timeStamp, String timeString) {
        setupTimeMap();
        timeStamp = timeStamp.plusHours(timeMap.get(timeString)[0]);
        timeStamp = timeStamp.plusMinutes(timeMap.get(timeString)[1]);
        return timeStamp;
    }

    /**
     * This will get the list of all the doctors currently in the database
     * @param context - this is so we can access all of database features
     * @return doctors - this is an array of doctors which will be used in a drop down list
     */
    public String[] getDoctors(DatabaseContext context) 
    {
        String s = "SELECT firstName, lastName From doctor";
        ArrayList<String> docs = new ArrayList<>();

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                docs.add(firstName + " " + lastName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        Object[] array = docs.toArray();
        int length = array.length;
        String[] doctors = new String[length];

        for (int i = 0; i<length; i++) {
            doctors[i] = (String) array[i];
        }   

        return doctors;

    }

    /**
     * Returns the doctorID of the chosen doctor
     * @param doctor - the name of the user chosen doctor
     * @param context - this is so we can access all of database features
     * @return id - the ID of the chosen doctor
     */
    public int getDoctorID (String doctor, DatabaseContext context) {
        int id = 0;
        String fName = doctor.split(" ")[0];
        String lName = doctor.split(" ")[1];
        String s = "SELECT doctorID FROM doctor WHERE firstName = ? AND lastName = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1, fName);
            ps.setString(2,lName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                id = rs.getInt("doctorID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

        return id;


    }
    
    /**
     * Creates a booking
     * @param timestamp - the time and date of the booking
     * @param reason - the reason for the booking
     * @param emergency - if the booking is an emergency or not
     * @param patientID - the patient attached to the booking
     * @param doctorID - the doctor attached to the booking
     * @param context - this is so we can access all of database features
     */
    public void createBooking(LocalDateTime timestamp, String reason, boolean emergency,
                            Integer patientID, Integer doctorID, DatabaseContext context)
    {
        //converts bool value to 0 or 1 accordingly so it can be stored in the database
        Integer emergencyCheck;
        if (emergency == true) {
            emergencyCheck = 1;
        } else {
            emergencyCheck = 0;
        }

        String[] reasonSplit = reason.split("");
        ArrayList<String> r = new ArrayList<>();
        String finalR = "";

        for (String s : reasonSplit) {
            if (s.equals("'")) {
                r.add("''");
            } else {
                r.add(s);
            }
        }

        for(String s : r) {
            finalR += s;
        }


        Booking b;
        b = new Booking (timestamp,finalR,emergencyCheck,patientID,doctorID);
        try {
            b.createInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
    }

    /**
     * Removes a booking from the database
     * @param bookingID - which booking to remove
     * @param context - this is so we can access all of database features
     */
    public void removeBooking(Integer bookingID, DatabaseContext context)
    {
        String s = "DELETE FROM booking WHERE bookingID = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setInt(1,bookingID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
    }

    /**
     * Gets a list of times the chosen doctor is free
     * @param date - chosen date for booking
     * @param doctor - chosen doctor for booking
     * @param context - this is so we can access all of database features
     * @return times - an array of times the chosen doctor is free on the chosen date
     */
    public String[] getValidTime(JDatePickerImpl date, String doctor, DatabaseContext context) {

        ArrayList<String> times = new ArrayList<>();
        times.add("09:00");times.add("10:00");times.add("11:00");times.add("12:00");
        times.add("13:00");times.add("14:00");times.add("15:00");times.add("16:00");

        int selectedDocID = getDoctorID(doctor, context);

        int selectedDay = date.getModel().getDay();
        int selectedMonth = date.getModel().getMonth() + 1;
        int selectedYear = date.getModel().getYear();

        Date d = new Date (selectedMonth + "/" + selectedDay  + "/" + selectedYear);
        LocalDateTime ldtDate = new java.sql.Timestamp(d.getTime()).toLocalDateTime();
        String selectedDate = ldtDate.toString().substring(0,10);

        String s = "SELECT timestamp FROM booking WHERE doctorID = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setInt(1,selectedDocID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String sqlDate = rs.getString("timestamp").substring(0,10);
                if (selectedDate.equals(sqlDate)) {
                    String sqlTime = rs.getString("timestamp").substring(11,16);
                    times.remove(sqlTime);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[] array = times.toArray();
        int length = array.length;
        String[] timesFree = new String[length];

        for (int i = 0; i < length; i++) {
            timesFree[i] = (String) array[i];
        }

        return timesFree;

    }

    /**
     * Checks if a booking already exists
     * @param ldt - date of current booking
     * @param patientID - the ID of the one creating the booking
     * @param doctorID - the chosen doctor
     * @param context - this is so we can access all of database features
     * @return exists - boolean value to show if the booking already exists or not
     */
    public boolean checkExists(LocalDateTime ldt, int patientID, int doctorID, DatabaseContext context) {
        boolean exists = true;

        String s = "SELECT timestamp, patientID, doctorID FROM booking WHERE timestamp = ? AND patientID = ? AND doctorID = ?";
        
        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setString(1, ldt.toString());
            ps.setInt(2,patientID);
            ps.setInt(3,doctorID);
            ResultSet rs = ps.executeQuery();

            if (rs.next() == false) {
                exists = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exists = true;
        }

        return exists;
    }

    /**
     * Returns the current user ID
     * @param email - currently logged in user
     * @param context - this is so we can access all of database features
     * @return patID - the ID of the current user
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
     * Returns an ArrayList with the current user bookings
     * @param email - currently logged in user
     * @param context - this is so we can access all of database features
     * @return bookings - an ArrayList of bookings
     */
    public ArrayList<ArrayList<String>> getBookings (String email, DatabaseContext context) {

        ArrayList<ArrayList<String>> bookings = new ArrayList<>();

        int patID = getPatientID(email, context);

        String s = "SELECT * FROM booking b LEFT JOIN doctor d ON b.doctorID = d.doctorID WHERE b.patientID = ? ORDER BY timestamp";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setInt(1,patID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ArrayList<String> tempBooking = new ArrayList<>();
                Integer bID = rs.getInt("bookingID");
                String timestamp = rs.getString("timestamp");
                String reason = rs.getString("reason");
                Integer emergency = rs.getInt("emergency");
                Integer dID = rs.getInt("doctorID");
                String dName = rs.getString("firstName") + " " + rs.getString("lastName");
                
                String e;

                if (emergency == 1) {
                    e = "True";
                } else {
                    e = "False";
                }
                tempBooking.add(Integer.toString(bID));
                tempBooking.add(timestamp);
                tempBooking.add(reason);
                tempBooking.add(e);
                tempBooking.add(Integer.toString(dID));
                tempBooking.add(dName);

                bookings.add(tempBooking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;

    }

    /**
     * Counts how many bookings a user has
     * @param email - currently logged in user
     * @param context - this is so we can access all of database features
     * @return rows - number of bookings
     */
    public int getRows(String email, DatabaseContext context) {
        int rows = 0;

        int patID = getPatientID(email, context);

        String s = "SELECT COUNT() as total FROM booking WHERE patientID = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setInt(1,patID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rows = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rows;

    }

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
        }

        return check;
    }
}

