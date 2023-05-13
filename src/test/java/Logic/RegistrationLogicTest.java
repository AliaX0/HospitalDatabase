package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import Database.DatabaseContext;
import Model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationLogicTest {
    private static RegistrationLogic registrationLogic;
    private static DatabaseContext context;
    private static Patient testPatient;

    @BeforeAll
    public static void setUp(){
        registrationLogic = new RegistrationLogic();
        context = new DatabaseContext("TestRegLogic");

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
    public void checkPasswordTest1(){
        char[] password1 = {'a','b','c','d'};
        char[] password2 = {'a','b','c','d'};
        assertEquals(registrationLogic.checkPassword(password1,password2), true);
    }

    @Test
    public void checkPasswordTest2(){
        char[] password1 = {'a','b','c','d'};
        char[] password2 = {'a','b','c','e'};
        assertEquals(registrationLogic.checkPassword(password1,password2), false);
    }

    @Test
    public void checkEmailTest1(){
        String email = "abcd@gmail.com";
        assertEquals(registrationLogic.checkEmail(email),true);
    }

    @Test
    public void checkEmailTest2(){
        String email = "abcd@gmail";
        assertEquals(registrationLogic.checkEmail(email),false);
    }

    @Test
    public void checkExists1() {
        String email = "TestEmail";
        assertEquals(registrationLogic.checkExists(email,context),true);
    }

    @Test
    public void checkExists2() {
        String email = "NotTheSameEmail";
        assertEquals(registrationLogic.checkExists(email,context),false);
    }
}
