package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Logic.HomePageLogic;
import Main.PatientAccessMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <h1>Home Page GUI</h1>
 * <p>This class is responsible for the home page GUI.</p>
 *
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.3
 * @since 29/03/2021
 */
public class HomePage extends PatientAccessFrame {
    // Variable declaration
    private JPanel panel;
    private final Dimension frameSize = new Dimension(350,200);
    private HomePageLogic logic = new HomePageLogic();
    private JLabel welcomeMessage, user;
    private JButton makeBookingButton, viewBookingButton, viewProfileButton, viewPrescriptionsButton;

    /**
     * Creates the HomePage frame for an authenticated user
     * @param userEmail Email address of the user
     */
    public HomePage(String userEmail)
    {
        super("Home Page");
        setUpInterface(userEmail);
    }

    /**
     * This method initialises the entire home page with the user's email.
     * @param userEmail the user's email.
     */
    private void setUpInterface(String userEmail) {
        // frame.setSize(frameSize);
        setMaximumSize(new Dimension(350,300));
        setMinimumSize(new Dimension(350,300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialises the panel and adds it to the frame.
        panel = new JPanel();
        panel.setLayout(new GridLayout(6,1));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);

        // Initialises the welcome message and adds it to the panel.
        welcomeMessage = new JLabel("Homepage of User:", SwingConstants.CENTER);
        welcomeMessage.setSize(frameSize);
        welcomeMessage.setFont(new Font("Verdana",Font.BOLD,30));
        panel.add(welcomeMessage);

        // Initialises the user email and adds it to the panel.
        user = new JLabel(userEmail,SwingConstants.CENTER);
        user.setSize(frameSize);
        user.setFont(new Font("Verdana",Font.PLAIN,20));
        panel.add(user);

        // Initialises the booking button and adds it to frame.
        makeBookingButton = new JButton("Make Booking");
        panel.add(makeBookingButton);

        // Initialises the view Booking button and adds it to the frame.
        viewBookingButton = new JButton("View Bookings");
        panel.add(viewBookingButton);

        // Initialises the view Prescriptions button and adds it to the frame.
        viewPrescriptionsButton = new JButton("View Prescriptions");
        panel.add(viewPrescriptionsButton);

        // Initialises the view Booking button and adds it to the frame.
        viewProfileButton = new JButton("View Profile");
        panel.add(viewProfileButton);

        // Makes the frame visible.
        setVisible(true);

        /**
         * When the 'Make Booking' button is pressed, the current page is closed and 
         * the new one is opened
         */
        makeBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadBookingPage(userEmail);
                GUIManager.destroyHomePage();
            }
        });

        /**
         * When the 'View Booking' button is pressed, a check is carried out to see if the
         * current user has any bookings, if they do, open the page, or else, tell them they have no bookings
         */
        viewBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (logic.checkBookings(userEmail, PatientAccessMain.getDBContext())) {
                    setVisible(false);
                    GUIManager.loadViewBookings();
                    GUIManager.destroyHomePage();
                } else {
                    displayInformationMessage("No Bookings", "You have no bookings to view");
                }
            }
        });

        /**
         * When the 'View Prescriptions' button is pressed, a check is carried out to see if the
         * current user has any prescriptions, if they do, open the page, or else, tell them they have no current prescriptions.
         */
        viewPrescriptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //if (logic.checkBookings(userEmail, PatientAccessMain.getDBContext())) {
                if (logic.checkPrescriptions(userEmail, PatientAccessMain.getDBContext())) {
                    setVisible(false);
                    GUIManager.loadViewPrescriptions();
                    GUIManager.destroyHomePage();
                } else {
                    displayInformationMessage("No Prescriptions", "You have no prescriptions to view");
                }

            }
        });

        /**
         * Button used to load the profile page of the current user
         */
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadProfilePage(userEmail);
                GUIManager.destroyHomePage();
            }
        });
    }
}
