package Logic;

import Database.DatabaseContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.util.*;

import org.jdatepicker.impl.JDatePickerImpl;

/**
 * <h1>Rescheduling Logic</h1>
 * <p>This class is responsible for the handling the rescheduling of a booking logic.</p>
 *
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 12/03/2021
 */
public class ReschedulingLogic {

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
        //String[] times = {"9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};

        int selectedDocID = getDoctorID(doctor, context);

        int selectedDay = date.getModel().getDay();
        int selectedMonth = date.getModel().getMonth() + 1;
        int selectedYear = date.getModel().getYear();

        int currentID = Login.getLoginID();

        Date d = new Date (selectedMonth + "/" + selectedDay  + "/" + selectedYear);
        LocalDateTime ldtDate = new java.sql.Timestamp(d.getTime()).toLocalDateTime();
        String selectedDate = ldtDate.toString().substring(0,10); 

        String s = "SELECT timestamp, patientID FROM booking WHERE doctorID = ?";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setInt(1,selectedDocID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int pID = rs.getInt("patientID");
                if (pID != currentID) {
                    String sqlDate = rs.getString("timestamp").substring(0,10);
                    if (selectedDate.equals(sqlDate)) {
                        String sqlTime = rs.getString("timestamp").substring(11,16);
                        times.remove(sqlTime);
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
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
     * Will update the current booking with the new user data
     * @param bID - the current booking ID
     * @param ts - the new timestamp
     * @param em - the new value for emergency
     * @param dID - the new DoctorID
     * @param context - this is so we can access all of database features 
     */
    public void changeBooking(Integer bID, LocalDateTime ts, boolean em, Integer dID, DatabaseContext context) {
        String s = "UPDATE booking SET timestamp = ?, emergency = ?, doctorID = ? WHERE bookingID = ?";
        Integer emergVal;

        if (em == true) {
            emergVal = 1;
        } else {
            emergVal = 0;
        }

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.setObject(1, ts);
            ps.setInt(2, emergVal);
            ps.setInt(3, dID);
            ps.setInt(4, bID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
    }

    /**
     * Will check if the time given is past now
     * @param time - the time
     * @return true or false depending of if the time is past now
     */
    public boolean checkIfPastTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }
}
