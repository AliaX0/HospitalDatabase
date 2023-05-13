package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import Model.Patient;
import Database.DatabaseContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest {
    private static Login login;
    private static DatabaseContext context;
    private static Patient testPatient;

    @BeforeAll
    public void setUp() {
        //Create new db context
        context = new DatabaseContext("TestLoginLogic");

        //Remove what is already in the tables to keep tests consistent
        String s = "DELETE FROM patient";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Add a test Patient to carry out tests with
        testPatient = new Patient("Test", "Patient", (new Date("27/01/2022")), "TestEmail", "12345", (new Character('M')), 11223344, (new Character('M')), "email", "54321");
        testPatient.setPasswordHash("hello");
        try {
            testPatient.createInstance(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkLoginDetails1() {
        String email = "TestEmail";
        String password = "hello";
        assertEquals(login.checkLoginDetails(email, password, context),true);
    }

    @Test
    public void checkLoginDetails2() {
        String email = "TestEmail";
        String password = "notThePassword";
        assertEquals(login.checkLoginDetails(email, password, context),false);
    }
}
