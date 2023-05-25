package models;

import models.datastructures.DataScores;
import models.datastructures.DataWords;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class for interacting with and querying a database.
 */
public class Database {
    private Connection connection = null;
    private final String databaseUrl;
    private String databaseFile;
    private final Model model;
    private List<String> missedLetters = new ArrayList<>();
    private int timeSeconds;

    /**
     * A database constructor that is always invoked when an object is created.     *
     *
     * @param model Model
     */
    public Database(Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile();
        this.selectUniqueCategories(); // ComboBox needs categories from the table
        this.databaseFile = this.databaseFile;
        this.selectScores(); // Leaderboard needs data from the table
    }

    /**
     * Database connection
     * @return Connection
     * @throws SQLException throws error on console.
     */
    private Connection dbConnection() throws SQLException {
        // https://stackoverflow.com/questions/13891006/
        if(connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(databaseUrl);
        return connection;
    }

    /**
     * The method reads unique category names from the database and writes them to the cmbNames variable of the model.
     */
    public void selectUniqueCategories() {
        String sql = "SELECT DISTINCT(category) as category FROM words ORDER BY category";
        List<String> categories = new ArrayList<>();
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String category = rs.getString("category");
                categories.add(category);
            }
            model.setCorrectCmbNames(categories); // writes unique categories to the cmbNames variable of the model
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method reads the entire leaderboard content from the database and writes it to the model's dataScores
     * variable
     */
    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername";
        List<DataScores> data = new ArrayList<>();
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.getDataScores().clear();
            while(rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                data.add(new DataScores(playerTime, playerName, guessWord, wrongChar, timeSeconds));
            }
            model.setDataScores(data); // Write dataScore in the model variable

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void wordsSelect (){
        String sql = "SELECT * FROM words ORDER BY category, word";
        List<String> categories = new ArrayList<>();
        List<DataWords> dataWords = new ArrayList<>();
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String category = rs.getString("category");
                dataWords.add(new DataWords(id, word, category));
                categories.add(category);  // add category to array list
            }
            // https://howtodoinjava.com/java8/stream-find-remove-duplicates/
            List<String> unique = categories.stream().distinct().collect(Collectors.toList());
            model.setDataWords(dataWords);
            model.setCorrectCmbNames(unique);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void checkDatabaseConnection() {
        try {
            Connection conn = this.dbConnection();
            if (conn != null) {
                System.out.println("Ühendus andmebaasiga on edukas!");
                conn.close();
            } else {
                System.out.println("Andmebaasiga ühenduse loomine ebaõnnestus!");
            }
        } catch (SQLException e) {
            System.out.println("Andmebaasiga ühenduse loomine ebaõnnestus! Viga: " + e.getMessage());
        }
    }
}


