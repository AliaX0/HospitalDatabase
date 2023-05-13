package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

import Logic.Login;
import Logic.Error;
import Main.PatientAccessMain;

/**
 * <h1>Login Page GUI</h1>
 * <p>This class is responsible for the login page GUI.</p>
 *
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.2
 * @since 12/03/2021
 */
public class LoginPage extends PatientAccessFrame {

    // Variable declaration.
    private JPanel panel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JTextField email;
    private JPasswordField password;
    private JButton loginButton;
    private JButton registerButton;
    private JButton cancelButton;

    public LoginPage(){
        super("Login Page");
        setupInterface();
    }

    /**
     * This method initialises the entire login page.
     */
    private void setupInterface() {
        // Initialises the frame.
        setSize(300, 150);
        setMinimumSize(new Dimension(300, 150));
        setMaximumSize(new Dimension(300, 150));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialises the panel and adds it to frame.
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panel);

        // Creates and adds email label.
        emailLabel = new JLabel("Email:");
        panel.add(emailLabel);

        // Creates and adds email field.
        email = new JTextField(7);
        panel.add(email);

        // Creates and adds password label.
        passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        // Creates and adds password field.
        password = new JPasswordField(7);
        panel.add(password);

        // Creates and adds login button.
        loginButton = new JButton("Login");
        panel.add(loginButton);

        // Creates and adds register button.
        registerButton = new JButton("Register");
        panel.add(registerButton);

        //Creates and adds cancel button.
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton);

        /**
         * Upon pressing the login button, the credentials will be checked.
         * If the credentials match the login page will close and the home page will open.
         */
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("Email is: " + email.getText());
                System.out.println("Password is: " + Arrays.toString(password.getPassword()));

                if (Login.checkLoginDetails(email.getText(), new String(password.getPassword()), PatientAccessMain.getDBContext())) {
                    setVisible(false);
                    Login.setLoggedIn(email.getText());
                    setVisible(false);
                    GUIManager.loadHomePage(email.getText());
                } else {
                    Error.displayErrorInGUI("Error: You cannot log in", "You have not entered the correct infomation");
                    email.setText("");
                    password.setText("");
                }
            }
        });

        /**
         * Upon pressing the register button, the login page will close
         * and the registration page will open.
         */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                GUIManager.loadRegisterPage();
            }
        });

        /**
         * Upon pressing the cancel button the login page will close
         * and the welcome page will open.
         */
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                GUIManager.loadWelcomePage();
            }
        });

        // Makes the whole frame visible.
        setVisible(true);
    }

}
