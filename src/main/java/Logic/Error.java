package Logic;

import GUI.GUIManager;
import GUI.PatientAccessFrame;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <h1> Logic.Error Class</h1>
 *
 * Error handling logic used by multiple classes
 * Helps pass errors from non GUI components to the GUI so that we can surface errors to the GUI
 *
 */
public class Error {

    /**
     * If <code>GUIManager.getCurrentFrame()</code> is not pointing at a null object, ddraws an error message on the
     * with the applications currently focused frame as the root component
     * <p>
     * Main mechanism to transmit errors to a GUI Prompt, any error caused that a user should be made aware of should
     * that is not GUI input validation should use this method as an interface to the GUI instead of coupling itself
     * directly components
     * @param title The title of the error message
     * @param message The message to provide
     */
    public static void displayErrorInGUI(String title, String message)
    {
        PatientAccessFrame currentFrame = GUIManager.getCurrentFrame();
        if(null != currentFrame){
            currentFrame.spawnErrorMessage(title, message);
        }
    }

    /**
     * Given an <code>Exception</code>, pulls out the stack trace and provides it back as a <code>String</code>
     * @param e <code>Exception</code> object containing the stack trace
     * @return Stack Trace as a <code>String</code>
     */
    public static String getStackTraceStringFromException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }

    public static void showGenericErrorInGUI(Exception e)
    {
        Error.displayErrorInGUI("Uh Oh - Something went wrong :(", Error.getStackTraceStringFromException(e));
    }
}
