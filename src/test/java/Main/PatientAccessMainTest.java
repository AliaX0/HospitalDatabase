package Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Main.PatientAccessMain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PatientAccessMainTest {
    private static PatientAccessMain patientAccessMain;

    @BeforeAll
    public static void setUp(){
        patientAccessMain = new PatientAccessMain();
    }
}
