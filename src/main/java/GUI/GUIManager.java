package GUI;

/**
 * <h1>GUI Manager</h1>
 * <p>This class is responsible for loading up and displaying all the GUI pages.</p>
 *
 * @author Dominykas Sliuzas : ds725@kent.ac.uk
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.3
 * @since 29/03/2021
 */
public class GUIManager {

    /*
     * This is the current frame that is loaded in the GUI.
     * Whenever the GUI manager is called this field should be updated. This field allows us to tie error messages produced by non-gui code to bind an error message to the current message.
     */
    private static PatientAccessFrame currentFrame;

    /*Fields that will store an instance of a frame so that it can be reused rather than always respawning new frames
    * We will let the Garbage Collector handle the dynamic frames by making sure we null out their reference with a destroy method.
    */
    private static WelcomePage welcomePage;
    private static LoginPage loginPage;
    private static RegisterPage registerPage;
    private static HomePage homePage;
    private static BookingPage bookingPage;
    private static ReschedulePage reschedulePage;
    private static ViewBookings viewBookings;
    private static ViewPrescriptions viewPrescriptions;
    private static ProfilePage profilePage;


    /**
     * Returns a handle to the currently drawn frame as a <code>PatientAccessFrame</code>
     * @return
     */
    public static PatientAccessFrame getCurrentFrame()
    {
        return currentFrame;
    }

    /**
     * Displays the welcome page.
     */
    public static void loadWelcomePage() {
        if(null == welcomePage){
            welcomePage = new WelcomePage();
        }else{
            welcomePage.setVisible(true);
        }

        currentFrame = welcomePage;
    }

    /**
     * Displays the login page.
     */
    public static void loadLoginPage() {
        if(null == loginPage){
            loginPage = new LoginPage();
        } else {
            loginPage.setVisible(true);
        }

        currentFrame = loginPage;
    }

    /**
     * Displays the registration page.
     */
    public static void loadRegisterPage() {
        if(null == registerPage){
            registerPage = new RegisterPage();
        }else {
            assert true : "We should never reach this block - means that the registration page is not being destroyed after use somewhere";
            registerPage.setVisible(true);
        }

        currentFrame = registerPage;
    }

    /**
     * Destroy the reference to RegisterPage so that next time register is called a new Frame is served
     * Prevents context from old registrations leaking to new registrations
     */
    public static void destroyRegisterPage()
    {
        registerPage = null;
    }

    /**
     * Displays the home page with the user's email.
     * Assumes that logging out means closing the application
     * @param userEmail the user's email.
     */
    public static void loadHomePage(String userEmail)
    {
        if(null == homePage){
            homePage = new HomePage(userEmail);
        } else {
            assert true : "We should never reach this - means that the homepage isn't being destroyed when it's dismissed somewhere";
            homePage.setVisible(true);
        }

        currentFrame = homePage;
    }

    /**
     * Destroy the reference to the HomePage
     * Prevents old HomePage context from polluting a new login
     */
    public static void destroyHomePage()
    {
        homePage = null;
    }
    
    /**
     * Displays the create a new booking page with the user's email.
     * @param userEmail the user's email
     */

    public static void loadBookingPage(String userEmail) {
        if (null == bookingPage){
            bookingPage = new BookingPage(userEmail);
        } else {
            assert true : "We should never reach this - means that the booking page isn't being destroyed when it's dismissed somewhere";
            bookingPage.setVisible(true);
        }

        currentFrame = bookingPage;
    }

    /**
     * Destroy the reference to the bookingPage
     * Prevents an old booking context polluting a new booking
     */
    public static void destroyBookingPage()
    {
        bookingPage = null;
    }


    /**
     * Displays the reschedule page for a specific booking.
     * @param bookingID the booking ID
     * @param date the date of the booking
     * @param reason the reason for the visit
     * @param emergency whether the booking is an emergency
     * @param doctorID the ID of the doctor
     * @param doctorName the name of the doctor
     */
    public static void loadReschedulePage(int bookingID, String date, String reason, String emergency, int doctorID, String doctorName) {
        if(null == reschedulePage){
            reschedulePage = new ReschedulePage(bookingID, date, reason, emergency, doctorID, doctorName);
        } else {
            assert true : "We shouldn't reach this - previous reference to reschedule page isn't being destroyed somehwere";
            reschedulePage.setVisible(true);
        }
        currentFrame = reschedulePage;
    }

    /**
     * Destroy the reference to the reschedule page
     * Prevents an old reschedule context polluting that of a new reschedule
     */
    public static void destroyReschedulePage()
    {
        reschedulePage = null;
    }

    /**
     * Displays the current bookings page with the user's email.
     */
    public static void loadViewBookings()
    {
        if(null == viewBookings) {
            viewBookings = new ViewBookings();
        } else {
            assert true : "We shouldn't reach this - Previous view Bookings hasn't been destroyed";
            viewBookings.setVisible(true);
        }

        currentFrame = viewBookings;
    }

    /**
     * Destroy viewBookings Reference
     * Allows bookings to be refreshed.
     */
    public static void destroyViewBookings()
    {
        viewBookings = null;
    }

    /**
     * Displays the current prescriptions page with the user's email.
     */
    public static void loadViewPrescriptions() {
        if (null == viewPrescriptions){
            viewPrescriptions = new ViewPrescriptions();
        } else {
            viewPrescriptions.setVisible(true);
        }

        currentFrame = viewPrescriptions;
    }

    /**
     * Destroy viewPrescriptions
     * Would be called in case of logout - there is no way currently for a prescription to be updated whilst a user is logged in
     */
    public static void destroyViewPrescriptions()
    {
        viewPrescriptions = null;
    }

    /**
     * Displays the profile of the current user
     * @param userEmail the user's email
     */
    public static void loadProfilePage(String userEmail) {
        if(null == profilePage){
            profilePage = new ProfilePage();
        } else {
            profilePage.setVisible(true);
        }

        currentFrame = profilePage;
    }

    /**
     * Destroy ProfilePage Reference
     * Allows ProfilePage to be refreshed.
     */
    public static void destroyProfilePage()
    {
        profilePage = null;
    }
}
