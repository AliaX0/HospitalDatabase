package Database;

import java.sql.SQLException;

/**
 * <h1>IPatientAccessEntity Interface</h1>
 * Creates a common interface that all models share and must implement.
 * <p>
 * Allows for the creation lists of generic models and creates a tighter coupling of the Model classes.
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @version 0.1
 * @since 27/02.2021
 */
public interface IPatientAccessEntity {
    public void createInstance(DatabaseContext context) throws SQLException, Exception;
    public void updateInstance(DatabaseContext context);
    public void deleteInstance(DatabaseContext context);
}
