package Logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Model.*;
import Database.DatabaseContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

public class PrescriptionLogicTest {

    private static DatabaseContext context;
    private static PrescriptionLogic logic = new PrescriptionLogic();
    private static Doctor doctor1, doctor2, doctor3, doctor4;
    private static Patient patient1, patient2, patient3, patient4;
    private static Prescription prescription1, prescription2;

    @BeforeAll
    public static void setUp() {
        context = new DatabaseContext("PrescriptionLogicTest");

        //Create test doctors
        String s = "DELETE FROM doctor;";

        try {
            PreparedStatement ps = context.createPrepStatement(s);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        doctor1 = new Doctor ("Bruce", "Wayne", new Date ("02/15/1970"), "07711223344", 'M', new Integer (11223344));
        doctor2 = new Doctor ("Barry", "Allen", new Date ("03/07/1983"), "07711223343", 'M', new Integer (99887766));
        doctor3 = new Doctor ("Diana", "Prince", new Date ("07/20/1975"), "07711237444", 'F', new Integer (55446633));
        doctor4 = new Doctor ("Harleen", "Quinzel", new Date ("09/29/1985"), "07711237444",'F',new Integer(88227733));

        try {
            doctor1.createInstance(context);
            doctor2.createInstance(context);
            doctor3.createInstance(context);
            doctor4.createInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create test patients
        String s2 =  "DELETE FROM patient;";
        try {
            PreparedStatement ps2 = context.createPrepStatement(s2);
            context.executePreparedUpdate(ps2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        patient1 = new Patient("Jeremy", "Kyle", new Date ("08/11/1982"), "jkyle123@gmail.com", "07465473123",
                'M', 11445768, 'M', "email", "07263457123" );
        patient2 = new Patient("John", "Parker", new Date ("02/03/1993"), "jparker@outlook.com", "07145645312",
                'M', 45623478, 'M', "email", "07112245113" );
        patient3 = new Patient("Emma", "Watson", new Date ("12/15/1988"), "emma@watson.org", "07112523331",
                'F', 55678913, 'F', "email", "07336451123" );
        patient4 = new Patient("Emilia", "Clarke", new Date ("03/22/1990"), "gotfan88@hotmail.co.uk", "07554612333",
                'F', 22446677, 'F', "text", "07112254653" );

        patient1.setPasswordHash("HelloWorld");
        patient2.setPasswordHash("HelloWorld");
        patient3.setPasswordHash("HelloWorld");
        patient4.setPasswordHash("HelloWorld");

        try {
            patient1.createInstance(context);
            patient2.createInstance(context);
            patient3.createInstance(context);
            patient4.createInstance(context);
        } catch (SQLException e){
            e.printStackTrace();
        }

        //Create test prescriptions
        String s3 =  "DELETE FROM prescription;";
        try {
            PreparedStatement ps3 = context.createPrepStatement(s3);
            context.executePreparedUpdate(ps3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        prescription1 = new Prescription(new Date ("03/23/2021"), "Paracetamol - 500mg", "4 Weeks",
                "Take 2 tablets twice a day after eating.", 1, 2);
        prescription2 = new Prescription(new Date ("03/23/2021"), "Penicillin - 250mg", "3 Weeks",
                "Take 2 tablets twice a day after eating.", 4, 3);

        try {
            prescription1.createInstance(context);
            prescription2.createInstance(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserPrescriptionsTest1() {
        ArrayList<Prescription> p = logic.getUserPrescriptions(2, context);
        assertEquals(1, p.size());
    }

}
