package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import Model.Booking;
import Model.Doctor;
import Model.Patient;
import Database.DatabaseContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingLogicTest {
    private static BookingLogic bookingLogic;
    private static DatabaseContext context;
    private static Booking booking;
    private static Doctor doctor;
    private static Patient patient;

    @BeforeAll
    public void setup() {
        bookingLogic = new BookingLogic();
        context = new DatabaseContext("TestBookingLogic");

        //Delete bookings that already exists
        String s = "DELETE FROM booking";
        String s2 = "DELETE FROM doctor";
        String s3 = "DELETE FROM patient";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.executeUpdate();
            PreparedStatement ps2 = context.createPrepStatement(s2);
            ps2.executeUpdate();
            PreparedStatement ps3 = context.createPrepStatement(s3);
            ps3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Create a test patient
        patient = new Patient("Test", "Patient", (new Date("27/01/2022")), "TestEmail", "12345", (new Character('M')), 11223344, (new Character('M')), "email", "54321");
        patient.setPasswordHash("hello");
        try {
            patient.createInstance(context);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFullTimeStampTest1() {
    LocalDateTime timestamp = LocalDateTime.of(2020, Month.JANUARY,1,12,0);
    LocalDateTime expectedTimeStamp = LocalDateTime.of(2020, Month.JANUARY,1,22,0);
    String time = "10:00";
    assertEquals(bookingLogic.getFullTimeStamp(timestamp,time),expectedTimeStamp);
    }

    @Test
    public void getDoctorsTest1() {
        assertEquals("Test Doctor", bookingLogic.getDoctors(context)[0]);
    }


    @Test
    public void getDoctorIDTest1() {
        assertEquals(1, bookingLogic.getDoctorID("Test Doctor", context));
    }

    @Test
    public void getDoctorIDTest2() {
        assertEquals(0, bookingLogic.getDoctorID("Fake Name", context));
    }

    //TODO: need a test for validTime but not sure how to get input for date

    @Test
    public void checkExists1() {
        Date d = new Date (03 + "/" + 01  + "/" + 2021);
        LocalDateTime ldtDate = new java.sql.Timestamp(d.getTime()).toLocalDateTime();
        LocalDateTime timestamp = bookingLogic.getFullTimeStamp(ldtDate, "12:00");
        assertEquals(true,bookingLogic.checkExists(timestamp, 1, 1, context));
    }

    @Test
    public void checkExists2() {
        Date d2 = new Date (01 + "/" + 04 + "/" + 2021);
        LocalDateTime ldtDate2 = new java.sql.Timestamp(d2.getTime()).toLocalDateTime();
        LocalDateTime timestamp2 = bookingLogic.getFullTimeStamp(ldtDate2, "11:00");
        assertEquals(false, bookingLogic.checkExists(timestamp2, 1, 1, context));
    }

    @Test
    public void getBookingsTest1() {
        String email = "TestEmail";
        ArrayList<ArrayList<String>> b = bookingLogic.getBookings(email, context);
        assertEquals(1, b.size());
    }

    @Test
    public void getBookingsTest2() {
        String email = "FakeEmail";
        ArrayList<ArrayList<String>> b = bookingLogic.getBookings(email, context);
        assertEquals(0, b.size());
    }

    @Test
    public void getRowsTest1() {
        int rows = bookingLogic.getRows("TestEmail",context);
        assertEquals(1, rows);
    }

    @Test
    public void getRowsTest2() {
        int rows = bookingLogic.getRows("FakeEmail",context);
        assertEquals(0, rows);
    }

    @Test
    public void checkBookings1() {
        assertEquals(true,bookingLogic.checkBookings("TestEmail", context));
    }

    @Test
    public void checkBookings2() {
        assertEquals(false,bookingLogic.checkBookings("FakeEmail", context));
    }
}
