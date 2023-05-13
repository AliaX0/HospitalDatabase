package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Properties;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Date;

import Logic.BookingLogic;
import Logic.Login;
import Main.PatientAccessMain;

/**
 * <h1>Booking Page GUI</h1>
 * <p>This class is responsible for the booking page GUI.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @version 0.2
 * @since 29/03/2021
 */

public class BookingPage extends PatientAccessFrame {

    // Variable declaration
    private BookingLogic logic = new BookingLogic();
    private Login loginLogic = new Login();

    private JPanel panel;

    private JLabel reasonLabel, dateLabel, timeLabel, emergencyLabel, doctorLabel;

    private JTextField reason;

    private JComboBox<String> time, doctor;

    private JCheckBox emergency;

    private JButton bookButton, cancelButton;


    public BookingPage(String currentUser){
        super("Booking Page");
        setUpInterface(currentUser);
    }

    private void setUpInterface(String currentUser) {
        // Initialises the frame.
        setSize(350, 300);
        setMinimumSize(new Dimension(350, 300));
        setMaximumSize(new Dimension(350, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialises panel and adds it to frame.
        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panel);

        // Initialises reason label and field and adds it to frame.
        reasonLabel = new JLabel("Reason for visit:");
        panel.add(reasonLabel);
        reason = new JTextField();
        panel.add(reason);

        // Initialises date label and field and adds it to frame.
        dateLabel = new JLabel("Date of visit:");
        panel.add(dateLabel);
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl date = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        panel.add(date);

        // Initialises doctor label and field and adds it to frame.
        doctorLabel = new JLabel("Select Doctor:");
        panel.add(doctorLabel);
        String[] doctorsFromTable = logic.getDoctors(PatientAccessMain.getDBContext());
        doctor = new JComboBox<>(doctorsFromTable);
        panel.add(doctor);

        // Initialises time label and field and adds it to frame.
        timeLabel = new JLabel("Time of visit:");
        panel.add(timeLabel);
        String[] timeSlots = new String[] {"..."};
        time = new JComboBox<>(timeSlots);
        time.setEnabled(false);
        panel.add(time);

        // Initialises emergency label and field and adds it to frame.
        emergencyLabel = new JLabel("Tick if emergency:");
        panel.add(emergencyLabel);
        emergency = new JCheckBox();
        panel.add(emergency);

        // Initialises book button and adds it to frame.
        bookButton = new JButton("Confirm Booking");
        panel.add(bookButton);

        // Initialises cancel button and adds it to frame.
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton);

        // Makes frame visible.
        setVisible(true);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkMandatoryFields()) {  
                    //Checks if a date is selected
                    if (!date.getModel().isSelected()) {
                        displayErrorMessage("User Input Error: Date Empty", "Please select a date");
                    }
                    else {
                        //get the user input of the date
                        int selectedDay = date.getModel().getDay();
                        int selectedMonth = date.getModel().getMonth() + 1;
                        int selectedYear = date.getModel().getYear();

                        //get the user input of the date and convert this to a 'LocalDateTime'
                        Date d = new Date (selectedMonth + "/" + selectedDay  + "/" + selectedYear);
                        LocalDateTime ldtDate = new java.sql.Timestamp(d.getTime()).toLocalDateTime();

                        //get the user input of the time of the booking
                        String chosenTime = (String) time.getSelectedItem();

                        //create the timestamp which will be stored in the booking table
                        LocalDateTime timestamp = logic.getFullTimeStamp(ldtDate, chosenTime);


                        //get the doctorID of the selected doctor
                        int docID = logic.getDoctorID(doctor.getSelectedItem().toString(), PatientAccessMain.getDBContext());

                        String currentUserEmail = loginLogic.getLogin();
                        int patID = logic.getPatientID(loginLogic.getLogin(),PatientAccessMain.getDBContext());

                        if (logic.checkExists(timestamp, patID, docID, PatientAccessMain.getDBContext()) == false) {
                            logic.createBooking(timestamp,reason.getText(),emergency.isSelected(),patID,docID,PatientAccessMain.getDBContext());
                            displayInformationMessage("Booking Succesful", "The booking has been made successfully.");
                            setVisible(false);
                            GUIManager.loadHomePage(currentUser);
                            GUIManager.destroyBookingPage();
                        } else {
                            displayErrorMessage("Error: Cannot book this time slot", "A booking for this time slot with this doctor already exists, please pick another time");
                            //reset form? similar to how we did the registration
                        }
                    }



                }
                else {
                    displayErrorMessage("User Input Error: Reason Empty", "Please provide a reason for your appointment so we can best help you.");
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadHomePage(currentUser);
                GUIManager.destroyBookingPage();
            }
        });

        doctor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Gets the selected doctor.
                String selectedDoctor = (String) doctor.getSelectedItem();
                
                if (selectedDoctor != "...") {
                    // Need a method get the available times for selected doctor
                    // that returns the times as an array of string.
                    String[] doctorTimes = logic.getValidTime(date, selectedDoctor, PatientAccessMain.getDBContext());
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(doctorTimes);
                    time.setModel(model);
                    time.setEnabled(true);
                }
                else {
                    String[] doctorTimes = {"..."};
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(doctorTimes);
                    time.setModel(model);
                    time.setEnabled(false);
                }
            }
        });
    }

    private boolean checkMandatoryFields() {
        if (reason.getText().isEmpty()) {
            return false;
        }
        else return true;
    }

}
