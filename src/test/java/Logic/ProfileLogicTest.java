package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Database.DatabaseContext;
import Model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileLogicTest {
    private static ProfileLogic logic;
    private static DatabaseContext context;
    private static Patient testPatient;

    @BeforeAll
    public static void setUp(){
        logic = new ProfileLogic();
        context = new DatabaseContext("TestProfileLogic");

        //Clear what is already in the table
        String s = "DELETE FROM patient";
        try {
            PreparedStatement ps = context.createPrepStatement(s);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Add a test patient
        testPatient = new Patient("Test", "Patient", (new Date("27/01/2022")), "TestEmail", "12345", (new Character('M')), 11223344, (new Character('M')), "email", "54321");
        testPatient.setPasswordHash("hello");

        try {
            testPatient.createInstance(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPatientIDTest1() {
        assertEquals(1, logic.getPatientID("TestEmail", context));
    }

    @Test
    public void getPatientIDTest2() {
        assertEquals(0, logic.getPatientID("FakeEmail", context));
    }

    @Test
    public void getPatientInfoTest1() {
        ArrayList<String> expectedAns = new ArrayList<>();
        expectedAns.add("Test"); expectedAns.add("Patient");
        expectedAns.add("Fri Mar 01 00:00:00 GMT 2024");
        expectedAns.add("TestEmail"); expectedAns.add("12345");
        expectedAns.add("M"); expectedAns.add("11223344");
        expectedAns.add("M");
        assertEquals(expectedAns, logic.getPatientInfo("TestEmail", context));
        
    }

    @Test
    public void getPatientInfoTest2() {
        assertEquals(0, logic.getPatientID("FakeEmail", context));
    }
}