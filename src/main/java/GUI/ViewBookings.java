package GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import Main.PatientAccessMain;
import java.util.*;
import Logic.BookingLogic;
import Logic.Login;

/**
 * <h1>View Booking Page</h1>
 * <p>This class is displays all the current bookings and has an additional rescheduling option.</p>
 *
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.3
 * @since 30/03/2021
 */
public class ViewBookings extends PatientAccessFrame {

    // Variables
    private JPanel panel;
    private JLabel dateColumn, reasonColumn, emergencyColumn, doctorColumn, rescheduleColumn, cancelColumn;
    private JButton rescheduleButton, cancelButton, returnButton;
    private BookingLogic logic = new BookingLogic();

    public ViewBookings(){
        super("Current Bookings");
        setUpInterface(Login.getLogin());
    }

    /** Start up
    *   @param userEmail the user's email.
    */
    private void setUpInterface(String userEmail){

        // Initialises the frame upon start up.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Retrieves the number of rows from Login Logic getRows() method
        final int x = logic.getRows(Login.getLogin(), PatientAccessMain.getDBContext());
        setMinimumSize(new Dimension(600,300));
        setMaximumSize(new Dimension(600,300));

        // Initialises the panel and adds it to the frame.
        panel = new JPanel(new GridLayout(0, 6));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);

        // Initialises the dateColumn, reasonColumn, emergencyColumn, doctorColumn, rescheduleColumn and cancelColumn label
        dateColumn = new JLabel("Date");
        reasonColumn = new JLabel("Reason");
        emergencyColumn = new JLabel("Emergency");
        doctorColumn = new JLabel("Doctor");
        rescheduleColumn = new JLabel("");
        cancelColumn = new JLabel("");

        // Adds the dateColumn, reasonColumn, emergencyColumn, doctorColumn, rescheduleColumn and cancelColumn label to the panel.
        panel.add(dateColumn);
        panel.add(reasonColumn);
        panel.add(emergencyColumn);
        panel.add(doctorColumn);
        panel.add(rescheduleColumn);
        panel.add(cancelColumn);

        // Initialises the b ArrayList of ArrayList<String> and gets the List of Bookings from BookingLogic
        ArrayList<ArrayList<String>> b = logic.getBookings(Login.getLogin(), PatientAccessMain.getDBContext());

        // Iterates through the ArrayList<String> for the index of b
        for (ArrayList<String> booking : b) {               

            // Retrieves parts of the ArrayList at that part of b       
            Integer bookingID = Integer.parseInt(booking.get(0));
            String timestamp = booking.get(1).toString();               
            String reason = booking.get(2);
            String emergency = booking.get(3);
            Integer dID = Integer.parseInt(booking.get(4));
            String dName = booking.get(5);

            // Initialises the labels, which will contain the timestamp, reason, emergency and doctor's name,
            // and adds it to the panel.
            panel.add(new JLabel((booking.get(1))));
            panel.add(new JLabel((booking.get(2))));
            panel.add(new JLabel((booking.get(3))));
            panel.add(new JLabel((booking.get(5))));

            // Initialises the reschedule button and adds it to the panel.
            rescheduleButton =new JButton("Reschedule");
            panel.add(rescheduleButton);

            // rescheduleButton function
            rescheduleButton.addActionListener(new ActionListener() {      
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    // Removes current frame and loads the reschedule page, passing the required variables.
                    setVisible(false);
                    GUIManager.loadReschedulePage(bookingID,timestamp,reason,emergency,dID,dName);
                    GUIManager.destroyBookingPage();
                };              
            });                        
                    
            // Initialises the cancel button and adds it to the panel.
            cancelButton = new JButton("Cancel");
            panel.add(cancelButton);
            
            // cancelButton function
            cancelButton.addActionListener(new ActionListener() {
                @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        // Passes the required variables to the removeBooking() method to be deleted in the database.
                        logic.removeBooking(bookingID, PatientAccessMain.getDBContext());
                        // Reloads the current page.
                        setVisible(false);
                        if (logic.checkBookings(userEmail, PatientAccessMain.getDBContext())) {
                            GUIManager.destroyViewBookings();
                            GUIManager.loadViewBookings();

                        } else {
                            GUIManager.loadHomePage(Login.getLogin());
                            GUIManager.destroyViewBookings();
                            displayInformationMessage("Booking Successfully Removed", "The booking has been removed");
                        }
                    }
                }); 
                        
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
                        GUIManager.destroyViewBookings();
                    }
                });             


        // Makes the frame visible.
        setVisible(true);
    }       
}