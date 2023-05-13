package Logic;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

/**
 * <h1>Util Class</h1>
 * Contains helper methods used by multiple classes
 *
 *  @author Ethan O'Donnell : ejpo2@kent.ac.uk
 *  @version 0.1
 *  @since 23/03/2021
 */
public class Util {
    /**
     * Given a Date as a <code>String</code> from an SQLite database, convert that to into a <code>java.sql.Date</code>
     * @param dateString The date string from an SQLite Database
     * @return Date parsed from the String representation
     */
    public static Date dateFromSQLiteString(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        LocalDate ld = LocalDate.parse(dateString, formatter);
        long offset = ld.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        //Convert Seconds to milliseconds
        offset = offset * 1000;

        return new Date(offset);
    }
}
