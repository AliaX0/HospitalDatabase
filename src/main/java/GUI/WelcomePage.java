package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <h1>Welcome Page GUI</h1>
 * <p>This class is responsible for the welcome page GUI.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.2
 * @since 29/03/2021
 */
public class WelcomePage extends PatientAccessFrame {

    // Variable Declaration

    private JPanel panel;

    private JLabel welcomeMessage, textMessage;

    private JButton loginButton, registerButton;

    private final Dimension frameSize = new Dimension(350,175);

    public WelcomePage() {
        super("Welcome Page");
        setUpInterface();
    }

    /**
     * This method initialises the entire welcome page.
     */
    private void setUpInterface() {

        // Initialises the frame.
        setSize(frameSize);
        setMaximumSize(frameSize);
        setMinimumSize(frameSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialises the panel and adds it to the frame.
        panel = new JPanel();
        panel.setLayout(new GridLayout(4,1));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);

        // Initialises the welcome message and adds it to the panel.
        welcomeMessage = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeMessage.setSize(frameSize);
        welcomeMessage.setFont(new Font("Verdana",Font.BOLD,30));
        panel.add(welcomeMessage);

        // Initialises the text message and adds it to the panel.
        textMessage = new JLabel("What would you like to do?", SwingConstants.CENTER);
        panel.add(textMessage);

        // Initialises the login button and adds it to the panel.
        loginButton = new JButton("Login");
        panel.add(loginButton);

        // Initialises the register button and adds it to the panel.
        registerButton = new JButton("Register");
        panel.add(registerButton);

        // Makes the frame visible.
        setVisible(true);

        /**
         * Upon pressing the login button
         * the welcome page will close and the login page will open.
         */
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadLoginPage();
            }
        });

        /**
         * Upon pressing the register button
         * the welcome page will close and the registration page will open.
         */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadRegisterPage();
            }
        });
    }
}
