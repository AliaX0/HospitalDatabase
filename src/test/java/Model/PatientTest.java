package Model;

import Database.DatabaseContext;
import Logic.Hashing;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PatientTest {

    private DatabaseContext context;
    private Patient testPatient1;

    @BeforeAll
    public void setUp() {
        //Create a testing context
        context = new DatabaseContext("Testing");

        //Clear all rows to keep tests consistent
        String dropAllPatients = "DELETE FROM patient";
        try {
            PreparedStatement ps = context.createPrepStatement(dropAllPatients);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        testPatient1 = new Patient("Test", "Patient", (new Date("27/01/2022")), "TestEmail", "12345", (new Character('M')), 11223344, (new Character('M')), "email", "54321");
        testPatient1.setPasswordHash("hello");
        try {
            testPatient1.createInstance(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void returnPatient1(){
        String selectPatient1 = "SELECT * FROM patient WHERE email = ?";
        try {
            PreparedStatement ps = context.createPrepStatement(selectPatient1);
            ps.setString(1,"TestEmail");
            ResultSet rs = context.executePreparedQuery(ps);
            //TODO:Finish this test
            assertEquals(1,1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void passwordHashTest(){
        String knownGoodPatient1PasswordHash = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";
        if (Hashing.sha256SumString(testPatient1.getPasswordHash()).equals(knownGoodPatient1PasswordHash)){
            fail();
        }
    }
}
