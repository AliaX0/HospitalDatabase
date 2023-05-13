package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import Logic.*;
import Logic.Error;
import Main.PatientAccessMain;

/**
 * <h1>Registration Page GUI</h1>
 * <p>This class is responsible for the registration page GUI.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author  Rahul Mistry : rm721@kent.ac.uk
 * @author  Lia Wilkinson : lw517@kent.ac.uk
 * @author  Ethan O'Donnell : ejpo2@kent.ac.uk
 * @version 0.2
 * @since 29/03/2021
 */
public class RegisterPage extends PatientAccessFrame {

    // Variable declaration.
    private RegistrationLogic logic = new RegistrationLogic();

    private JPanel panel;

    private JLabel nameLabel, lastNameLabel, emailLabel, dobLabel, genderLabel,
            passwordLabel, cPasswordLabel, phoneNumLabel, addressLabel, nhsNumLabel,
            prefSexOfDoctorLabel, notificationLabel, emergencyLabel;

    private JTextField name, lastName, email, dob, phone, address, nhsNum, emergency;

    private JPasswordField password, cPassword;

    private JComboBox<String> notification;

    private JComboBox<Character> gender, prefSexOfDoctor;

    private JButton registerButton, cancelButton;


    public RegisterPage(){
        super("Registration Page");
        setUpInterface();
    }

    /**
     * This method initialises the entire registration page.
     */
    private void setUpInterface() {
        // Initialises the frame.
        setSize(500, 600);
        setMinimumSize(new Dimension(500,600));
        setMaximumSize(new Dimension(500,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialises panel and adds it to frame.
        panel = new JPanel();
        panel.setLayout(new GridLayout(15,2));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);

        // Initialises name label and field and adds it to frame.
        nameLabel = new JLabel("First Name:");
        panel.add(nameLabel);
        name = new JTextField();
        panel.add(name);

        // Initialises last name label and field and adds it to frame.
        lastNameLabel = new JLabel("Last Name:");
        panel.add(lastNameLabel);
        lastName = new JTextField();
        panel.add(lastName);

        // Initialises email label and field and adds it to frame.
        emailLabel = new JLabel("Email Address:");
        panel.add(emailLabel);
        email = new JTextField();
        panel.add(email);

        // Initialises DOB label and field and adds it to frame.
        dobLabel = new JLabel("Date of Birth:");
        panel.add(dobLabel);
        dob = new JTextField("dd/mm/yyyy");
        panel.add(dob);

        // Initialises gender label and field and adds it to frame.
        genderLabel = new JLabel("Gender:");
        panel.add(genderLabel);
        Character[] genders = new Character[] {'M','F','O'};
        gender = new JComboBox<>(genders);
        panel.add(gender);

        // Initialises password label and field and adds it to frame.
        passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);
        password = new JPasswordField();
        panel.add(password);

        // Initialises confirm password label and field and adds it to frame.
        cPasswordLabel = new JLabel("Confirm Password:");
        panel.add(cPasswordLabel);
        cPassword = new JPasswordField();
        panel.add(cPassword);

        // Initialises phone number label and field and adds it to frame.
        phoneNumLabel = new JLabel("Phone Number:");
        panel.add(phoneNumLabel);
        phone = new JTextField();
        panel.add(phone);

        // Initialises address label and field and adds it to frame.
        addressLabel = new JLabel("Home Address:");
        panel.add(addressLabel);
        address = new JTextField();
        panel.add(address);

        // Initialises NHS number label and field and adds it to frame.
        nhsNumLabel = new JLabel("NHS Number:");
        panel.add(nhsNumLabel);
        nhsNum = new JTextField();
        panel.add(nhsNum);

        // Initialises preferred sex of doctor label and field and adds it to frame.
        prefSexOfDoctorLabel = new JLabel("Preferred Sex of Doctor:");
        panel.add(prefSexOfDoctorLabel);
        Character[] genderOfDoctor = new Character[] {'M','F','O'};
        prefSexOfDoctor = new JComboBox<>(genderOfDoctor);
        panel.add(prefSexOfDoctor);

        // Initialises notification type label and field and adds it to frame.
        notificationLabel = new JLabel("Notification preference:");
        panel.add(notificationLabel);
        String[] notificationType = new String[] {"text","email"};
        notification = new JComboBox<>(notificationType);
        panel.add(notification);

        // Initialises emergency number label and field and adds it to frame.
        emergencyLabel = new JLabel("Emergency Phone Number: ");
        panel.add(emergencyLabel);
        emergency = new JTextField();
        panel.add(emergency);

        // Initialises and adds the register button.
        registerButton = new JButton("Register");
        panel.add(registerButton);

        // Initialises and adds the cancel button.
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton);

        // Makes whole frame visible.
        setVisible(true);

        /**
         * Upon pressing the register button, all the filled in fields will be checked.
         * If all the fields were entered correctly, the registration page will close
         * and the login page will open.
         * Otherwise a corresponding prompt will pop up on screen.
         */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkMandatoryFields()) {
                    if (logic.checkEmail(email.getText())) {
                        if(logic.checkPassword(password.getPassword(),cPassword.getPassword())) {
                            if (!logic.checkExists(email.getText(), PatientAccessMain.getDBContext()))
                            {
                                    try{
                                        logic.createPatient(name.getText(), lastName.getText(), new Date(dob.getText()),
                                                email.getText(), phone.getText(),
                                                (gender.getSelectedItem()).toString().charAt(0), Integer.parseInt(nhsNum.getText()),
                                                (prefSexOfDoctor.getSelectedItem()).toString().charAt(0),
                                                String.valueOf(notification.getSelectedItem()), emergency.getText(),
                                                new String(password.getPassword()), PatientAccessMain.getDBContext());
                                        displayInformationMessage("Registration Successful", "You have successfully registered!\nYou can now login to the PatientAccess System.");
                                        setVisible(false);
                                        GUIManager.loadLoginPage();
                                        GUIManager.destroyRegisterPage();
                                    } catch (Exception e){
                                        e.printStackTrace();
                                        Error.showGenericErrorInGUI(e);
                                        resetForm();
                                    }
                            } else 
                            {
                                displayErrorMessage("Input Error: Email Already Exists", "The email you have entered already exists.\nPlease enter a new email or login with the existing address.");
                                resetForm();
                            }
                        } else
                        displayErrorMessage("Input Error: Passwords did not match", "The passwords you entered did not match.\nPlease re-enter your password and try again.");
                        resetForm();
                    }
                    else {
                        displayErrorMessage("Input Error: Invalid Email entered", "The email you have entered is not a valid email address.\nPlease enter an email using the format: user@emailprovider.com");
                        resetForm();
                    }
                }
                else {
                    displayErrorMessage("Input Error: Fill in all required fields", "Some required information is missing.\nPlease make sure that all required fields have been filled out.");
                    resetForm();
                }
            }
        });

        /**
         * Upon pressing the cancel button, the registration page will close
         * and the welcome page will open.
         */
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadWelcomePage();
                GUIManager.destroyRegisterPage();
            }
        });
    }


    /**
     * Resets the form to its original state.
     */
    public void resetForm() {
        
        name.setText("");
        lastName.setText("");
        email.setText("");
        dob.setText("dd/mm/yyyy");
        phone.setText("");
        address.setText("");
        nhsNum.setText("");
        emergency.setText("");
        password.setText("");
        cPassword.setText("");
        notification.setSelectedIndex(0);
        gender.setSelectedIndex(0);
        prefSexOfDoctor.setSelectedIndex(0);
    }

    /**
     * Checks if all the mandatory fields of the registration form are filled in.
     * @return a true if all fields were filled in and false otherwise.
     */
    public boolean checkMandatoryFields() {
        if (name.getText().isEmpty()) {
            return false;
        }
        else if (lastName.getText().isEmpty()) {
            return false;
        }
        else if (dob.getText().isEmpty()) {
            return false;
        }
        else if (email.getText().isEmpty()) {
            return false;
        }
        else if (phone.getText().isEmpty()) {
            return false;
        }
        else if (password.getPassword().length == 0) {
            return false;
        }
        else if (cPassword.getPassword().length == 0) {
            return false;
        }
        else if (address.getText().isEmpty()) {
            return false;
        }
        else return true;
    }

}
