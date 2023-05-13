package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Model.Booking;
import Model.Doctor;
import Database.DatabaseContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;

public class ReschedulingLogicTest {
    private static ReschedulingLogic logic;
    private static DatabaseContext context;
    private static Booking booking;
    private static Doctor doctor;

    @BeforeAll
    public static void setup() {
        logic = new ReschedulingLogic();
        context = new DatabaseContext("TestReschedulingLogic");

        //Delete bookings that already exists
        String s = "DELETE FROM booking";
        String s2 = "DELETE FROM doctor";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            ps.executeUpdate();
            PreparedStatement ps2 = context.createPrepStatement(s2);
            ps2.executeUpdate();
        } catch (SQLException e) {
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

        doctor = new Doctor("Test", "Doctor", new Date ("1980/05/15"), "00000000000", 'M', 9988776);
        
        try {
            doctor.createInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Test
    public void getFullTimeStampTest1() {
    LocalDateTime timestamp = LocalDateTime.of(2020, Month.JANUARY,1,12,0);
    LocalDateTime expectedTimeStamp = LocalDateTime.of(2020, Month.JANUARY,1,22,0);
    String time = "10:00";
    assertEquals(logic.getFullTimeStamp(timestamp,time),expectedTimeStamp);
    }

    @Test
    public void getDoctorsTest1() {
        assertEquals("Test Doctor", logic.getDoctors(context)[0]);
    }

    @Test
    public void getDoctorIDTest1() {
        assertEquals(1, logic.getDoctorID("Test Doctor", context));
    }

    @Test
    public void getDoctorIDTest2() {
        assertEquals(0, logic.getDoctorID("Fake Name", context));
    }
}
