package Database;

import Logic.Error;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <h1>DatabaseContext</h1>
 * Class that represents the DB Context actions should be performed in and
 * configures and manages the lifecycle of a single database instance
 * <p>
 * Runs with the assumption that it is the sole instance of its class currently running in the program
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @author Rahul Mistry : rm721@kent.ac.uk
 * @version 0.2
 * @since 18/03/2021
 */
public class DatabaseContext {

    //Semantic versioning numbers for this instance of the database.
    private static final int dbVersionMajor = 0;
    private static final int dbVersionMinor = 0;
    private static final int dbVersionPatch = 4;

    private String dbURL;
    private String dbName;
    private Connection sqliteConnection;

    /**
     * Constructor for a DatabaseContext
     * The DBName will determine the name of the DB in the path
     * @param DBName - Name of DB to create/access sans file extension (Production NOT Production.db)
     */
    public DatabaseContext(String DBName) {
        this.dbName = DBName;
        this.dbURL = "jdbc:sqlite:".concat(DBName).concat(".db");
        System.out.println(this.dbURL);
        initDB();
    }

    /**
     * Responsible for configuring and starting the SQLite Database
     */
    private void initDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            sqliteConnection = DriverManager.getConnection(dbURL);

            if(!checkPath()){
                createSchema();
                updateVersion();
            }

            if(!checkVersion()){

                deleteDBFile(); //TODO: Need to set up connection pooling to centralise connections and kill them to delete db
                createSchema();
                updateVersion();
            }



        } catch (ClassNotFoundException | SQLException ex){
            ex.printStackTrace();
            Error.showGenericErrorInGUI(ex);
        }
    }

    /**
     * Public Interface to pass an SQL Statement to commit to DB
     * @param statement - a string representing the desired SQL operation
     */
    public void commitStatement(String statement) throws SQLException
    {
        runStatement(statement);
    }

    /**
     * Private Handler for running SQL Statements.
     * Implements the behaviour for commitStatement
     * Closes the connection to the database when statements are completed
     * @param statement <code>String</code> representing the the SQL statment to run
     */
    private void runStatement(String statement) throws SQLException
    {
            Connection c = sqliteConnection;
            Statement s = c.createStatement();
            s.execute(statement);
    }

    public ResultSet runQuery(String statement) throws SQLException
    {
        Connection c = sqliteConnection;
        Statement s = c.createStatement();
        s.execute(statement);
        return s.getResultSet();
    }

    /**
     * Handles the creation of creating a Prepared Statement
     * to allow us to pass a parameter to an SQL statement 
     * we wish to carry out
     * @param statement <code>String</code> representing the SQL to create a prepared statement from
     * @return <code>PreparedStatement</code> created from the supplied SQL and associated with the <code>DatbaseContexts</code>
     *         DB Connection
     * @throws SQLException
     */
    public PreparedStatement createPrepStatement(String statement) throws SQLException
    {
            Connection c = sqliteConnection;
            PreparedStatement ps = c.prepareStatement(statement);
            return ps;
    }

    /**
     * Execute a query expecting no output (UPDATE, DELETE, INSERT .etc)
     * @param ps <code>PreparedStatement</code> representing the query you want to run
     * @throws SQLException
     */
    public void executePreparedUpdate(PreparedStatement ps) throws SQLException
    {
            ps.executeUpdate();
    }

    /**
     * Execute a query for which you expect a given output as a ResultSet
     * @param ps <code>PreparedStatement</code> representing the query you want to run
     * @return ResultSet with the result of your query.
     * @throws SQLException
     */
    public ResultSet executePreparedQuery(PreparedStatement ps) throws SQLException
    {
            return ps.executeQuery();
    }

    /**
     * Patient Table Schema
     * @return SQL for Patient Table Schema
     */
    private String patientTable()
    {
        return "CREATE TABLE IF NOT EXISTS patient ( \n"
                            + "patientID integer UNIQUE PRIMARY KEY, \n"
                            + "firstName varchar(20) NOT NULL, \n"
                            + "lastName varchar(20) NOT NULL, \n"
                            + "dob date NOT NULL, \n"
                            + "email varchar NOT NULL, \n"
                            + "phoneNumber varchar NOT NULL, \n"
                            + "passwordHash varchar NOT NULL, \n"
                            + "passwordSalt varchar(5) NOT NULL, \n"
                            + "sex char(1) CHECK (sex IN ('M','F','O')) NOT NULL, \n"
                            + "nhsNumber integer UNIQUE NOT NULL, \n"
                            + "preferredDoctorSex char(1) CHECK (preferredDoctorSex IN ('M','F','O')) NOT NULL, \n"
                            + "preferredContactMethod varchar CHECK (preferredContactMethod IN ('email','text')) NOT NULL, \n"
                            + "emergencyNumber varchar NOT NULL \n"
                            + ");";
    }

    /**
     * Doctor Table Schema
     * @return SQL for Doctor Table Schema
     */
    private String doctorTable ()
    {
        return "CREATE TABLE IF NOT EXISTS doctor ( \n"
                             + "doctorID integer UNIQUE PRIMARY KEY, \n"
                             + "firstName varchar(20) NOT NULL, \n"
                             + "lastName varchar(20) NOT NULL, \n"
                             + "dob date NOT NULL, \n"
                             + "phoneNumber varchar NOT NULL, \n"
                             + "sex char(1) CHECK (sex IN ('M','F','O')) NOT NULL, \n"
                             + "nhsNumber integer UNIQUE NOT NULL \n"
                             + ");";
    }

    /**
     * Booking Table Scheme
     * @return SQL for Booking Table Schema
     */
    private String bookingTable ()
    {
        return "CREATE TABLE IF NOT EXISTS booking ( \n"
                             + "bookingID integer UNIQUE PRIMARY KEY, \n"
                             + "timestamp varchar NOT NULL, \n"
                             + "reason varchar NOT NULL, \n"
                             + "emergency integer CHECK (emergency IN ('1','0')) NOT NULL, \n"
                             + "patientID integer NOT NULL, \n"
                             + "doctorID integer NOT NULL, \n"
                             + "FOREIGN KEY (patientID) REFERENCES patient (patientID),"
                             + "FOREIGN KEY (doctorID) REFERENCES doctor (doctorID) \n"
                             + ");";
    }

    /**
     * Prescription Table Schema
     * @return SQL for Prescription Table Schema
     */
    private String prescriptionTable ()
    {
        return "CREATE TABLE IF NOT EXISTS prescription ( \n"
                + "prescriptionID integer UNIQUE PRIMARY KEY, \n"
                + "pDate date NOT NULL, \n"
                + "pDescription varchar NOT NULL, \n"
                + "pPeriod varchar NOT NULL, \n"
                + "pInstructions varchar NOT NULL, \n"
                + "patientID integer NOT NULL, \n"
                + "doctorID integer NOT NULL, \n"
                + "FOREIGN KEY (patientID) REFERENCES patient (patientID),"
                + "FOREIGN KEY (doctorID) REFERENCES doctor (doctorID) \n"
                + ");";
    }

    /**
     * Version Table Schema
     * @return SQL for Version Table Schema
     */
    private String versionTable ()
    {
        return "CREATE TABLE IF NOT EXISTS version ( \n"
                + "versionID integer UNIQUE PRIMARY KEY, \n"
                + "major integer(3) NOT NULL, \n"
                + "minor integer(3) NOT NULL, \n"
                + "patch integer(3) NOT NULL \n"
                + ");";
    }

    /**
     * Helper method to crete table schema
     * @throws SQLException
     */
    private void createSchema() throws SQLException
    {
        runStatement(patientTable());
        runStatement(doctorTable());
        runStatement(bookingTable());
        runStatement(versionTable());
        runStatement(prescriptionTable());
    }

    /**
     * Helper method to set the version tables initial row to match the Database version
     */
    private void updateVersion() throws SQLException {
        String statement = "INSERT INTO version (major, minor, patch) VALUES (?, ?, ?);";
        PreparedStatement ps = createPrepStatement(statement);
        ps.setInt(1,DatabaseContext.dbVersionMajor);
        ps.setInt(2,DatabaseContext.dbVersionMinor);
        ps.setInt(3,DatabaseContext.dbVersionPatch);
        executePreparedUpdate(ps);
    }

    /**
     * Helper function to return whether the database is the correct version
     * @return True if the database version matches the DB Context Version, False on an exception or when the Version does not match the DB Context Version
     */
    private boolean checkVersion()
    {
        String statement = "SELECT major, minor, patch FROM version;";
        boolean correctVersion = false;
        try{
            PreparedStatement ps = createPrepStatement(statement);
            ResultSet rs = executePreparedQuery(ps);
            int[] resultVersion = {rs.getInt(1), rs.getInt(2), rs.getInt(3)};
            if (resultVersion[0] == DatabaseContext.dbVersionMajor && resultVersion[1] == DatabaseContext.dbVersionMinor && resultVersion[2] == DatabaseContext.dbVersionPatch){
                correctVersion = true;
            }
        } catch (SQLException e) {
            //TODO: Better way of handling this than just swallowing an excepetion
            //Stack Trace swallowed to make built jar output clean
            //e.printStackTrace();
        }
        return correctVersion;
    }

    /**
     * Checks to see if the Database Exists in the given path
     * @return True if it exists, false if it does not
     */
    private boolean checkPath()
    {
        boolean exists = false;
        try {
            Path dbPath = FileSystems.getDefault().getPath(dbName.concat(".db"));
            exists = Files.exists(dbPath);
        } catch (Exception e){
            e.printStackTrace(); //TODO: Narrow Down the exceptions caught here
            Error.showGenericErrorInGUI(e);
        }

        return exists;
    }

    /**
     * Deletes the DB file from the file system
     */
    private void deleteDBFile(){
        try {
            Path dbPath = FileSystems.getDefault().getPath(dbName.concat(".db"));
            //TODO: Find a better method of detecting a fresh DB that isn't this obscure size check
            if(Files.exists(dbPath) && Files.size(dbPath) > 0){
                Files.delete(dbPath);
            }
        } catch (Exception e){
            e.printStackTrace(); //TODO: Narrow Down the exceptions caught here
            Error.showGenericErrorInGUI(e);
        }
    }
}
	
	
	
		