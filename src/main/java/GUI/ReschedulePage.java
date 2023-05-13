package GUI;

import Logic.Login;
import Logic.ReschedulingLogic;
import Main.PatientAccessMain;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

/**
 * <h1>Reschedule Page GUI</h1>
 * <p>This class is responsible for the reschedule page GUI.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 12/03/2021
 */
public class ReschedulePage extends PatientAccessFrame {

    private ReschedulingLogic logic = new ReschedulingLogic();

    private JPanel panel;
    private JLabel reasonLabel, dateLabel, newDateLabel, timeLabel, emergencyLabel, doctorLabel, currentDate, doctorNameLabel;
    private JTextField reason;
    private JComboBox<String> time, doctor;
    private JCheckBox emergency;
    private JButton rescheduleButton, cancelButton;

    public ReschedulePage(int bookingID, String date, String reason, String emergency, int doctorID, String doctorName)
    {
        super("Reschedule Page");
        setUpInterface(bookingID, date, reason, emergency, doctorID, doctorName);
    }

    /**
     * This method initialises the entire reschedule page.
     * @param bookingID the booking ID
     * @param visitDate the date of the booking
     * @param visitReason the reason for the visit
     * @param visitEmergency whether the booking is an emergency
     * @param doctorID the ID of the doctor
     * @param doctorName the name of the doctor
     */
    private void setUpInterface(int bookingID, String visitDate, String visitReason, String visitEmergency, int doctorID, String doctorName) {

        // Extracts elements from visitDate
        String year = visitDate.substring(0,4);
        String month = visitDate.substring(5,7);
        String day = visitDate.substring(8,10);
        String hour = visitDate.substring(11,13);
        String minute = visitDate.substring(14,16);

        // Initialises the frame.
        setSize(350, 300);
        setMinimumSize(new Dimension(350, 300));
        setMaximumSize(new Dimension(350, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialises panel and adds it to frame.
        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panel);

        // Initialises reason label and field and adds it to frame.
        reasonLabel = new JLabel("Reason for visit:");
        panel.add(reasonLabel);
        reason = new JTextField(visitReason);
        reason.setEnabled(false);
        panel.add(reason);


        // Initialises current date labels and adds them to frame.
        dateLabel = new JLabel("Current date of visit:");
        panel.add(dateLabel);
        currentDate = new JLabel(year + "-" + month + "-" + day + " at " + hour + ":" + minute);
        panel.add(currentDate);

        // Initialises current doctor labels and adds them to frame.
        doctorLabel = new JLabel("Current doctor:");
        panel.add(doctorLabel);
        doctorNameLabel = new JLabel(doctorName);
        panel.add(doctorNameLabel);

        // Initialises new date label and field and add it to frame.
        newDateLabel = new JLabel("New date of visit:");
        panel.add(newDateLabel);
        JDatePickerImpl datePicker = createDatePicker();
        panel.add(datePicker);

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
        if (visitEmergency.equals("True")) {
            emergency = new JCheckBox("",true);
        }
        else emergency = new JCheckBox();
        panel.add(emergency);


        // Initialises book button and adds it to frame.
        rescheduleButton = new JButton("Confirm Rescheduling");
        panel.add(rescheduleButton);

        // Initialises cancel button and adds it to frame.
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton);

        // Makes frame visible.
        setVisible(true);

        // Gets the available times for doctor upon selecting a day.
        datePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] doctorTimes = logic.getValidTime(datePicker, doctorName, PatientAccessMain.getDBContext());
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(doctorTimes);
                time.setModel(model);
                time.setEnabled(true);
            }
        });

        // Reschedule button functionality.
        rescheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Create a date with what the user has entered
                int selectedDay = datePicker.getModel().getDay();
                int selectedMonth = datePicker.getModel().getMonth() + 1;
                int selectedYear = datePicker.getModel().getYear();
                Date d = new Date (selectedMonth + "/" + selectedDay + "/" + selectedYear);
                LocalDateTime ldtDate = new java.sql.Timestamp(d.getTime()).toLocalDateTime();

                //Get the time the user entered
                String chosenTime = (String) time.getSelectedItem();

                //Create a new timestamp with the user input
                LocalDateTime timestamp = logic.getFullTimeStamp(ldtDate, chosenTime);

                //Get the ID of the chosen doctor
                int docID = logic.getDoctorID(doctorNameLabel.getText(),PatientAccessMain.getDBContext());

                //Update the booking
                logic.changeBooking(bookingID, timestamp, emergency.isSelected(), docID, PatientAccessMain.getDBContext());

                //Tell the user the update was successful and reopen the view bookings window
                displayInformationMessage("Appointment Successfully Rescheduled", "Your appointment has been moved successfully.");
                setVisible(false);
                GUIManager.loadViewBookings();
                GUIManager.destroyReschedulePage();
            }
        });

        // Cancel button functionality.
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                GUIManager.loadViewBookings();
                GUIManager.destroyReschedulePage();
            }
        });
    }

    /**
     * Creates a JDatePicker.
     * @return the created JDatePickerImpl
     */
    public JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

}
