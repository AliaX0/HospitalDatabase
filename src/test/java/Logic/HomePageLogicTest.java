package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import Model.Booking;
import Model.Doctor;
import Model.Patient;
import Model.Prescription;
import Database.DatabaseContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomePageLogicTest {
    private static HomePageLogic logic;
    private static DatabaseContext context;
    private static Booking booking;
    private static Doctor doctor;
    private static Patient patient;
    private static Patient patient2;
    private static Prescription prescription1;
    
    @BeforeAll
    public void setup() {
        logic = new HomePageLogic();
        context = new DatabaseContext("TestHomePageLogic");

        //Delete bookings that already exists
        String s = "DELETE FROM booking";
        String s2 = "DELETE FROM doctor";
        String s3 = "DELETE FROM patient";
        String s4 = "DELETE FROM prescription";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.executeUpdate();
            PreparedStatement ps2 = context.createPrepStatement(s2);
            ps2.executeUpdate();
            PreparedStatement ps3 = context.createPrepStatement(s3);
            ps3.executeUpdate();
            PreparedStatement ps4 = context.createPrepStatement(s4);
            ps4.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Create a test patient
        patient = new Patient("Test", "Patient", (new Date("27/01/2022")), "TestEmail", "12345", (new Character('M')), 11223344, (new Character('M')), "email", "54321");
        patient.setPasswordHash("hello");


        patient2 = new Patient("Test", "Patient2", (new Date("27/01/2022")), "TestEmail2", "12365", (new Character('M')), 1122444, (new Character('M')), "email", "54221");
        patient2.setPasswordHash("hello");

        try{
            patient.createInstance(context);
            patient2.createInstance(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //Create a test booking
        doctor = new Doctor("Test", "Doctor", new Date ("1980/05/15"), "00000000000", 'M', 9988776);
        
        try {
            doctor.createInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create test booking with patientID = 1, doctorID = 1 and timeStamp = 2021-03-01T12:00
        LocalDateTime timeStamp = LocalDateTime.of(2021, Month.MARCH,1,12,0);
        booking = new Booking (timeStamp,"test",0,1,1);

        try {
            booking.createInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create a prescription
        prescription1 = new Prescription(new Date ("03/23/2021"), "Paracetamol - 500mg", "4 Weeks",
                        "Take 2 tablets twice a day after eating.", 1, 1);
        try {
            prescription1.createInstance(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckBookings1() {
        assertEquals(true,logic.checkBookings("TestEmail", context) );
    }

    @Test
    public void testCheckBookings2() {
        assertEquals(false, logic.checkBookings("NOTREAL", context));
    }

    @Test
    public void checkPrescriptions1() {
        assertEquals(true, logic.checkPrescriptions("TestEmail", context));
    }

    @Test
    public void checkPrescriptions2() {
        assertEquals(false, logic.checkPrescriptions("TestEmail2", context));
    }
}
