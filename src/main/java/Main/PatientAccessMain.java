package Main;

import GUI.GUIManager;
import Database.DatabaseContext;

/**
 * <h1>PatientAccessMain - Entrypoint of the PatientAccess Program</h1>
 * This class is the entrypoint for the entire program.
 * It is responsible for loading the gui and creating the <code>DatabaseContext</code>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.2
 * @since 30/03/2021
 */
public class PatientAccessMain {

    private static DatabaseContext patientDBContext;

    /**
     * Main Method pointed to by the Manifest
     * <p>
     * Loads the GUI and creates the programs shared <code>DatabaseContext</code>
     * @param args Ignored as not used
     */
    public static void main(String[] args) {
        System.out.println("Main loaded");
        //Creates database in the project root directory currently
        patientDBContext = new DatabaseContext("Development");
        GUIManager.loadWelcomePage();
    }

    /**
     * Getter method for the application wide DB Context
     * @return Handle to sole instance of applications DB context, the interface for database operations.
     */
    public static DatabaseContext getDBContext(){
        return patientDBContext;

    }

}
