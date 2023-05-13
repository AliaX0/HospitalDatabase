package Model;

import Database.DatabaseContext;
import Database.IPatientAccessEntity;
import Logic.Error;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * <h1>Booking Model</h1>
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 23/03/2021
 */
public class Booking implements IPatientAccessEntity{
    private Integer bookingID;
    private LocalDateTime timestamp;
    private String reason;
    private Integer emergency;
    private Integer patientID;
    private Integer doctorID;

    public Booking (LocalDateTime timestamp, String reason, Integer emergency,
                    Integer patientID, Integer doctorID)
    {
        this.timestamp = timestamp;
        this.reason = reason;
        this.emergency = emergency;
        this.patientID = patientID;
        this.doctorID = doctorID;
    }

    /**
     * takes values of the current booking and creates a booking in the 
     * booking table by creating a SQL statement and executing it
     * @throws Exception
     */
    public void createInstance(DatabaseContext context) throws SQLException {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO booking (timestamp,reason, emergency, patientID, doctorID)");
        sb.append("VALUES ");
        sb.append("('".concat(timestamp.toString()).concat("', "));
        sb.append("'".concat(reason).concat("', "));
        sb.append("'".concat(emergency.toString()).concat("', "));
        sb.append("'".concat(patientID.toString()).concat("', "));
        sb.append("'".concat(doctorID.toString()).concat("');"));

        String statement = sb.toString();

        try {
            context.commitStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Your booking cannot be made");
        }
    }


    public void updateInstance(DatabaseContext context) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void deleteInstance(DatabaseContext context) {

        String statement = "DELETE FROM booking WHERE bookingID = ?";

        try { 
            PreparedStatement ps = context.createPrepStatement(statement);
            ps.setInt(1,bookingID);
            context.executePreparedUpdate(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }

    }

}
