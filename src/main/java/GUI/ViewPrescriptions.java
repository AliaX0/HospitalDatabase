package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import Main.PatientAccessMain;
import java.util.*;
import Logic.Login;
import Logic.PrescriptionLogic;
import Model.Prescription;

/**
 * <h1>View Prescriptions Page</h1>
 * <p>This class displays all the current prescriptions.</p>
 *
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @author Ethan O'Donnell
 * @version 0.2
 * @since 29/03/2021
 */
public class ViewPrescriptions extends PatientAccessFrame {

    // Variables
    private JPanel panel;
    private JLabel dateColumn, descriptionColumn, instructionsColumn, periodColumn;
    private JButton returnButton;
    private PrescriptionLogic prescriptionLogic = new PrescriptionLogic();

    public ViewPrescriptions()
    {
        super("Current Prescriptions");
        setUpInterface(Login.getLogin());
    }

    /** Start up
    *   @param userEmail the user's email.
    */
    private void setUpInterface(String userEmail){

            // Initialises the frame upon start up.
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Initialises the p ArrayList of ArrayList<String> and gets the List of Prescriptions from PrescriptionLogic
            ArrayList<Prescription> p = prescriptionLogic.getUserPrescriptions(Login.getLoginID(), PatientAccessMain.getDBContext());   

            // number of prescriptions
            final int x = p.size();
   
            // Sets the mininum and maximum frame size.
            setMinimumSize(new Dimension(600,600));
            setMaximumSize(new Dimension(600,600));

            // Initialises the panel and adds it to the frame. 
            panel = new JPanel(new GridLayout(0, 4));
            panel.setBorder(new EmptyBorder(10,10,10,10));
            add(panel);

            // Initialises the dateColumn, dosageColumn, quantityColumn, doctorColumn;
            dateColumn = new JLabel("Date");
            descriptionColumn = new JLabel("Description");
            instructionsColumn = new JLabel("Instructions");
            periodColumn = new JLabel("Period");

            // Adds the dateColumn, dosageColumn, quantityColumn, doctorColumn label to the panel.
            panel.add(dateColumn);
            panel.add(descriptionColumn);
            panel.add(instructionsColumn);
            panel.add(periodColumn);
  
            // Iterates through the array list for every prescription object and adds the necessary values.
            for (Prescription prescription : p){
                        panel.add(new JLabel(""+(prescription.getPrescriptionDate())));
                        panel.add(new JLabel(""+(prescription.getPrescriptionDescription())));
                        panel.add(new JLabel(""+(prescription.getPrescriptionInstructions())));
                        panel.add(new JLabel(""+(prescription.getPrescriptionPeriod())));
            }

            // Initialises the return button and adds it to the panel
            // returnButton function
            returnButton = new JButton ("Return");
            panel.add(returnButton);
            returnButton.addActionListener(new ActionListener() {
                    @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            // Return to Homepage.
                            setVisible(false);
                            GUIManager.loadHomePage(Login.getLogin());
                        }
            });  
                     
        // Makes the frame visible.
        setVisible(true);
    }       
}