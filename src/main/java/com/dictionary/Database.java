package com.dictionary;
import com.commandline.*;
import java.sql.*;


public class Database {
    public Connection databaseLink;

    /**
     * Get database connection from a database.
     * @return database link.
     */
    public Connection getDatabaseConnection() {
        String databaseName = "dictionary";
        String databaseUser = "root";
        String databasePassword = "Long24062004";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            //Logger.getLogger(Database.class.getName()).warning("SQLException: " + e.getMessage());
            e.printStackTrace();
        }

        return databaseLink;
    }

    /**
     * Get data from database and add to current local dictionary.
     * Must be called after {@link Database#getDatabaseConnection()}.
     */
    public void getDataFromDatabase() {
        var query = "SELECT * FROM tbl_edict";
        try {
            Statement statement = databaseLink.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String target = queryOutput.getString("word");
                String explain = queryOutput.getString("detail");
                Dictionary.words.add(new Word(target, explain));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close database connection.
     */
    public void closeDatabaseConnection() {
        try {
            databaseLink.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
