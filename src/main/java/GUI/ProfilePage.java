package GUI;

import Logic.Login;
import Logic.ProfileLogic;
import Main.PatientAccessMain;
import Model.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * <h1>Profile Page GUI</h1>
 * <p>This class is responsible for the profile page GUI.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @version 0.2
 * @since 30/03/2021
 */

public class ProfilePage extends PatientAccessFrame {

    private JLabel firstNameLabel, lastNameLabel, dobLabel, phoneLabel,
            nhsLabel, genderLabel, firstName, lastName,
            dob, phone, nhs, gender, emailLabel, email, prefSexOfDoctorLabel;

    private JPanel panel;

    private JButton changeDoctorButton, backButton, applyChangesButton;

    private JComboBox<String> prefSexOfDoctor;

    private String[] doctorList;

    private Dimension frameSize = new Dimension(400,250);

    private ProfileLogic logic = new ProfileLogic();

    public ProfilePage()
    {
        super("Profile Page");
        setUpInterface(Login.getLogin());
    }

    /**
     * This method initialises the entire profile page for the given user.
     * @param userEmail the current user's email address
     */
    public void setUpInterface(String userEmail) {

        ArrayList<String> details = logic.getPatientInfo(userEmail, PatientAccessMain.getDBContext());

        String pName = details.get(0);
        String pLastName = details.get(1);
        String[] pDob = details.get(2).split(" ");
        String pEmail = details.get(3);
        String pPhone = details.get(4);
        String pGender = details.get(5);
        String pNhs = details.get(6);
        String pDoctor = details.get(7);
        System.out.println(pDoctor);

        setSize(frameSize);
        setMaximumSize(frameSize);
        setMinimumSize(frameSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(9,3));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);

        firstNameLabel = new JLabel("First Name: ");
        firstName = new JLabel(pName);
        panel.add(firstNameLabel);
        panel.add(firstName);

        insertSpacer();

        lastNameLabel = new JLabel("Last Name: ");
        lastName = new JLabel(pLastName);
        panel.add(lastNameLabel);
        panel.add(lastName);

        insertSpacer();

        dobLabel = new JLabel("Date of Birth: ");
        dob = new JLabel(pDob[0] + " " + pDob[1] + " " + pDob[2] + " " + pDob[5]);
        panel.add(dobLabel);
        panel.add(dob);

        insertSpacer();

        emailLabel = new JLabel("Email: ");
        email = new JLabel(pEmail);
        panel.add(emailLabel);
        panel.add(email);

        insertSpacer();

        genderLabel = new JLabel("Gender: ");
        gender = new JLabel(pGender);
        panel.add(genderLabel);
        panel.add(gender);

        insertSpacer();

        phoneLabel = new JLabel("Phone Number: ");
        phone = new JLabel(pPhone);
        panel.add(phoneLabel);
        panel.add(phone);

        insertSpacer();

        nhsLabel = new JLabel("NHS Number: ");
        nhs = new JLabel(pNhs);
        panel.add(nhsLabel);
        panel.add(nhs);

        insertSpacer();

        prefSexOfDoctorLabel = new JLabel("Preferred Sex of Doctor:");
        String[] genderOfDoctor = new String[] {"M","F","O"};
        prefSexOfDoctor = new JComboBox<>(genderOfDoctor);
        prefSexOfDoctor.setSelectedIndex(getIndex(pDoctor,genderOfDoctor));
        prefSexOfDoctor.setEnabled(false);
        //TODO: Set the first value of the drop down to be the answer the user already has
        //this value is stored in pDoctor but what I didn't doesn't work for some reason
        prefSexOfDoctor.setSelectedItem(pDoctor);
        panel.add(prefSexOfDoctorLabel);
        panel.add(prefSexOfDoctor);

        changeDoctorButton = new JButton("Change");
        panel.add(changeDoctorButton);

        applyChangesButton = new JButton("Apply Changes");
        panel.add(applyChangesButton);

        backButton = new JButton("Back");
        panel.add(backButton);

        setVisible(true);

        changeDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prefSexOfDoctor.setEnabled(true);
            }
        });

        applyChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logic.updateSex(userEmail, prefSexOfDoctor.getSelectedItem().toString(), PatientAccessMain.getDBContext());
                displayInformationMessage("Preferred Doctor Sex Updated", ("Preferred doctor sex has been updated to: " + prefSexOfDoctor.getSelectedItem().toString()));
                setVisible(false);
                GUIManager.destroyProfilePage();
                GUIManager.loadProfilePage(userEmail);

            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadHomePage(userEmail);
            }
        });

    }

    /**
     * Adds a spacer JLabel to the GUI
     */
    private void insertSpacer() {
        JLabel spacer = new JLabel(" ");
        panel.add(spacer);
    }

    /**
     * Gets the input string's index in the list.
     * @param s the name of the doctor.
     * @return list index.
     */
    private int getIndex(String s, String[] list) {
        int index = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(s)) {
                index = i;
            }
        }
        return index;
    }
}
