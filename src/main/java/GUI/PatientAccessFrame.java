package GUI;

import javax.swing.*;

/**
 * <h1>PatientAccessFrame Abstract Class</h1>
 *
 * This abstract class is the parent for all frames spawned by the PatientAccess program.
 * It contains methods that may be used by different frames at any one time such as displaying error messages.
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @version 0.1
 * @since 29/03/2021
 */
public abstract class PatientAccessFrame extends JFrame {

    /**
     * Call the default constructor of the super class
     */
    public PatientAccessFrame()
    {
        super();
    }

    /**
     * Create a PatientAccessFrame and set the title
     * Passes the title parameter to the JFrame SuperClass
     * @param title The title of the Frame
     */
    public PatientAccessFrame(String title)
    {
        super(title);
    }

    /**
     * Displays an Error Message to the user.
     * Sets the Parent Component to be <code>this</code> object which ties the error message component to the lifecycle of the calling frame
     * and also prevents the user from modifying this frame until the message is confirmed.
     * @param title the title of the window spawned
     * @param message the contents of the message to the display
     */
    protected void displayErrorMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method to allow a 3rd party class to present an error message to a user
     *
     * Interface method for protected method <code>displayErrorMessage</code>
     *
     * @param title title of the error message
     * @param message error message contents
     */
    public void spawnErrorMessage(String title, String message)
    {
        displayErrorMessage(title,message);
    }

    /**
     * Displays an information message to the user.
     * Should be used in scenarios we wish to inidcate success or we need the let the user know something has happened.
     * Sets the Parent Component to be <code>this</code> object which ties the error message component to the lifecycle of the calling frame
     * and also prevents the user from modifying this frame until the message is confirmed.
     * @param title
     * @param message
     */
    protected void displayInformationMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
