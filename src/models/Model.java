package models;

import helpers.GameTimer;
import models.datastructures.DataScores;
import models.datastructures.DataWords;
import views.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
/**
 * A model where the entire logic of the game must take place
 */
public class Model {


    private final String databaseFile = "hangman_words_ee.db"; // Default database
    private List<String> imageFiles = new ArrayList<>(); // All images with full folder path
    private String[] cmbNames; // ComboBox categories names (contents)
    private final String chooseCategory = "All categories"; // Default first ComboBox choice
    private DefaultTableModel dtmScores; // Leaderboard DefaultTableModel
    private List<DataScores> dataScores = new ArrayList<>(); // The contents of the entire database table scores
    private int imageId = 0; // Current image id (0..11)
    private String selectedCategory = chooseCategory; // Default all categories as "All categories"

    private List<String> missedLetters = new ArrayList<>(); //
    public int countMissedWords; // arvatud sõna
    private String playerName; // mängija nimi
    private final List<DataWords> dataWords; // sõnad
    private String[] categories; // kategooriad
    private String wordToGuess;  // sõna, mida arvata
    private StringBuilder hiddenWord; // Peidetud random sõna
    private Connection connection = null;  // andmebaasi ühendus

    private int gametime;

    /**
     * Konstruktor
     */
    public Model() {
        new Database(this);  // andmebaas
        dataWords = new ArrayList<>();  // sõnad listis
        hiddenWord = new StringBuilder();  // peidetud sõna
        wordsSelect(); // sõnad
        //this.gameTime = new GameTimer(new View());  // mängu aeg

    }


    private Connection dbConnection() throws SQLException {
        if(connection != null) {
            connection.close();
        }
        // andmebaasi url
        String dbUrl = "jdbc:sqlite:" + databaseFile;
        connection = DriverManager.getConnection(dbUrl);
        return connection;
    }


    /**
     * Sets the content to match the ComboBox. Adds "All categories" and unique categories obtained from the database.
     * @param unique all unique categories from database
     */
    public void setCorrectCmbNames(List<String> unique) {
        cmbNames = new String[unique.size()+1];
        cmbNames[0] = chooseCategory; // First choice before categories
        int x = 1;
        for(String category : unique) {
            cmbNames[x] = category;
            x++;
        }
    }

    /**
     * All ComboBox contents
     * @return ComboBox contents
     */
    public String[] getCmbNames() {
        return cmbNames;
    }

    /**
     * Sets a new DefaultTableModel
     * @param dtmScores DefaultTableModel
     */
    public void setDtmScores(DefaultTableModel dtmScores) {
        this.dtmScores = dtmScores;
    }

    /**
     * ALl leaderbaord content
     * @return List<DataScores>
     */
    public List<DataScores> getDataScores() {
        return dataScores;
    }

    /**
     * Sets the new content of the leaderboard
     * @param dataScores List<DataScores>
     */
    public void setDataScores(List<DataScores> dataScores) {
        this.dataScores = dataScores;
    }

    /**
     * Returns the configured database file
     * @return databaseFile
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * Returns the default table model (DefaultTableModel)
     * @return DefaultTableModel
     */
    public DefaultTableModel getDtmScores() {
        return dtmScores;
    }

    /**
     * Returns the images folder
     * @return String
     */
    public String getImagesFolder() {
        // Hangman game images location
        String imagesFolder = "images";
        return imagesFolder;
    }

    /**
     * Sets up an array of new images
     * @param imageFiles List<String>
     */
    public void setImageFiles(List<String> imageFiles) {
        this.imageFiles = imageFiles;
    }

    /**
     * An array of images
     * @return List<String>
     */
    public List<String> getImageFiles() {
        return imageFiles;
    }

    /**
     * The id of the current image
     * @return id
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * Sets the new image id
     * @param imageId id
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
        hideLetters();   // chati pakutud
    }

    /**
     * Returns the selected category
     * @return selected category
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Sets a new selected category
     * @param selectedCategory new category
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * Saame random sõna
     */
    public void randomWordsFromCmbNamesList (String selectedCategory){
        Random random = new Random();
        List<String> guessWordsToList = new ArrayList<>();
        if (selectedCategory.equals("Kõik kategooriad")){
            wordToGuess = dataWords.get(random.nextInt(dataWords.size())).getWord();
        } else {
            boolean categoryExists = false;
            for (DataWords word : dataWords) {
                if (selectedCategory.equals(word.getCategory())) {
                    categoryExists = true;
                    guessWordsToList.add(word.getWord());
                }
            }
            if (!categoryExists || guessWordsToList.isEmpty()) {
                wordToGuess = dataWords.get(random.nextInt(dataWords.size())).getWord();
            } else {
                wordToGuess = guessWordsToList.get(random.nextInt(guessWordsToList.size()));
            }
        }
        this.wordToGuess = wordToGuess.toUpperCase();
        hideLetters();
    }

    /**
     * Peidab sõnad
     */
    private void hideLetters() {
        hiddenWord = new StringBuilder();
        for (int i = 0; i < wordToGuess.length(); i++) {
            hiddenWord.append('_');
        }
    }

    /**
     * SELECT lause tabeli words sisu lugemiseks ja info dataWords listi lisamiseks
     */
    public void wordsSelect() {
        String sql = "SELECT * FROM words ORDER BY category, word";
        List<String> categories = new ArrayList<>();
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            dataWords.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String category = rs.getString("category");
                dataWords.add(new DataWords(id, word, category));
                categories.add(category);
            }
            List<String> unique = categories.stream().distinct().collect(Collectors.toList());
            setCorrectCmbNames(unique);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Tasgastab valesti sisestatud tähed
     * @return String
     */
    public List<String> getMissedLetters(){
        return missedLetters;
    }

    /**
     * Tagastab arvatud sõna
     */
    public void setMissedLetters(List<String> missedLetters) {
        this.missedLetters = missedLetters;
    }

    /**
     * Küsib mängija nime
     */
    public void askPlayerName() {
        playerName = JOptionPane.showInputDialog("Sisesta oma nimi");
        if (playerName.length() < 2) {
            askPlayerName();
        }
    }


    /**
     * Meetod, mis lisab sõna tähtede vahele tühikud.
     *
     * @param word Sisendina antud sõna, millele tühikud lisatakse.
     * @return Muudetud sõna, kus tähtede vahele on lisatud tühikud.
     */
    public String addSpaceBetween(String word) {  // Jagame sõna tähtedeks ja paneme need massiivi
        String[] wordListOfList= word.split("");  // Loome StringJoiner objekti, et ühendada tähti tühikutega
        StringJoiner joiner = new StringJoiner(" ");  // Kasutame StringJoinerit tähtede ühendamiseks tühikutega
        for (String words : wordListOfList){ // Siin liigume läbi igat tähte massiivis
            joiner.add(words);  // Ning lisame iga tähe StringJoinerisse
        }
        return joiner.toString(); // Lõpuks tagastame muudetud sõna, kus tähtede vahele on lisatud tühikud
    }

    /**
     * Tagastab sõna, mida arvata
     */
    public String getWordToGuess() {return wordToGuess;}


    /**
     * Tagastab suvalise valitud sõna selliselt, et keskmised sõnad on peidetud "_" alla.
     */
    public StringBuilder getHiddenWord() {
        return hiddenWord;
    }

    /**
     * Tagastab mängija nime
     */
    public String getPlayerName() {return playerName;}

    /**
     * Sisestab mängija andmed edetabelisse
     */
    public void insertScoreToTable (){

        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?,?, ?, ?,?)";
        String removeBrackets = getMissedLetters().toString().replace("[", "").replace("]", "");
        DataScores endTime = new DataScores(LocalDateTime.now(), getPlayerName(), getWordToGuess(), removeBrackets, getGametime());

        try {
            Connection conn = this.dbConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String playerTime = endTime.getGameTime().format(formatter);
            preparedStmt.setString(1, playerTime);
            preparedStmt.setString(2, endTime.getPlayerName());
            preparedStmt.setString(3, endTime.getGuessWord());
            preparedStmt.setString(4, endTime.getMissingLetters());
            preparedStmt.setInt(5, getGametime());

            preparedStmt.executeUpdate();
            selectScores();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tagastab edetabeli
     */
    private void selectScores() {}


    /**
     * Tagastab arvatamata sõna
     */
    public int getCountMissedWords() {
        return countMissedWords;
    }

    /**
     * Tagastab arvatud sõna
     */
    public void setCountMissedWords(int countMissedWords) {
        this.countMissedWords = countMissedWords;
    }

    /**
     * Tagastab arvata sõna
     */
    public void setHiddenWord(StringBuilder hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    /**
     * Saab mängu aja sekundites
     */
    public int getGametime() {
        return gametime;
    }
    /**
     * Määrab mängu aja sekundites
     */

    public void setGametime(int gametime) {
        this.gametime = gametime;
    }
}

