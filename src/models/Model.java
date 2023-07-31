package models;

import helpers.GameTimer;
import models.datastructures.DataScores;
import models.datastructures.DataWords;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
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
    /*
    hangman_words_ee.db - Estonian words, the leaderboard table is empty
    hangman_words_en.db - English words, the leaderboard table is empty
    hangman_words_ee_test.db - Estonian words, the leaderboard table is NOT empty
     */
    private String databaseFile = "hangman_words_ee.db"; // Default database
    private final String imagesFolder = "images"; // Hangman game images location
    private List<String> imageFiles = new ArrayList<>(); // All images with full folder path
    private String[] cmbNames; // ComboBox categories names (contents)
    private final String chooseCategory = "All categories"; // Default first ComboBox choice
    private DefaultTableModel dtmScores; // Leaderboard DefaultTableModel
    private List<DataScores> dataScores = new ArrayList<>(); // The contents of the entire database table scores
    private int imageId = 0; // Current image id (0..11)
    private String selectedCategory = chooseCategory; // Default all categories as "All categories"
    private List<String> missedLetters = new ArrayList<>();

    public int countMissedWords;
    private String playerName;
    private List<DataWords> dataWords;
    private String[] categories;
    private String wordToGuess;
    private StringBuilder hiddenWord;
    private Connection connection = null;
    private String dbUrl = "jdbc:sqlite:" + databaseFile;
    private DataScores model;
    int timeSeconds;
    int PplayedTimeSeconds;
    //private List<Character> userWord;

    /**
     * During the creation of the model, the names of the categories to be shown in the combobox are known
     */
    public Model() {
        new Database(this);
        dataWords = new ArrayList<>();
        hiddenWord = new StringBuilder(); // Initialize wordNewOfLife here
        wordsSelect();
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
    public void randomWordsFromCmbNamesList (String selectedCategory){
        Random random = new Random();
        //System.out.println("For a test to see current category: " + selectedCategory);
        List<String> guessWordsToList = new ArrayList<>();
        if (selectedCategory.equals("Kõik kategooriad")){
            wordToGuess = dataWords.get(random.nextInt(dataWords.size())).getWord();
            //System.out.println("Test for random word: " + wordToGuess.toUpperCase());
        } else {
            boolean categoryExists = false;
            for (DataWords word : dataWords) {
                if (selectedCategory.equals(word.getCategory())) {
                    categoryExists = true;
                    guessWordsToList.add(word.getWord());
                }
            }

            if (!categoryExists || guessWordsToList.isEmpty()) {
                // Valitud kategooriat ei leitud või selles kategoorias pole sõnu, seega valime juhusliku sõna "Kõik kategooriad" hulgast
                wordToGuess = dataWords.get(random.nextInt(dataWords.size())).getWord();
            } else {
                wordToGuess = guessWordsToList.get(random.nextInt(guessWordsToList.size()));
                System.out.println("Test for random word from current category: " + wordToGuess.toUpperCase());
            }
        }

        this.wordToGuess = wordToGuess.toUpperCase();
        hideLetters();
    }

    private void hideLetters() {
        hiddenWord = new StringBuilder();
        for (int i = 0; i < wordToGuess.length(); i++) {
            hiddenWord.append('_');
        }
        //System.out.println("Test to see is word hidden: " + hiddenWord);
    }

    /**
     * The method reads unique category names from the database and writes them to the cmbNames variable of the model.
     */

    public void wordsSelect() {
        String sql = "SELECT * FROM words ORDER BY category, word";
        List<String> categories = new ArrayList<>(); // NB! See on meetodi sisene muutuja categories!
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            dataWords.clear(); // Tühjenda dataScores list vanadest andmetest
            while (rs.next()) {
                //int id = rs.getInt("id");
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String category = rs.getString("category");
                dataWords.add(new DataWords(id, word, category)); // Lisame tabeli kirje dataWords listi
                categories.add(category);
            }
            // https://howtodoinjava.com/java8/stream-find-remove-duplicates/
            List<String> unique = categories.stream().distinct().collect(Collectors.toList());
            setCorrectCmbNames(unique); // Unikaalsed nimed Listist String[] listi categories

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public List<DataScores> getDataScores() {return dataScores;}



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
    public List<String> getMissedLetters(){
        return missedLetters;
    }

    public void setMissedLetters(List<String> missedLetters) {
        this.missedLetters = missedLetters;
    }





    public void askPlayerName() {
        playerName = JOptionPane.showInputDialog("Sisesta oma nimi");
        if (playerName.length() < 2) {
            askPlayerName();
        }
    }



    public String addSpaceBetween(String word) {
        String[] wordListOfList= word.split("");
        StringJoiner joiner = new StringJoiner(" ");
        for (String words : wordListOfList){
            joiner.add(words);
        }
        return joiner.toString();
    }
    public String getWordToGuess() {return wordToGuess;}
    public String[] getCategories() {return categories;}
    public List<DataWords> setDataWords(List<DataWords> dataWords) {return dataWords;
    }
    private Connection dbConnection() throws SQLException {
        if(connection != null) {  // ühendus on olemas
            connection.close();
        }
        connection = DriverManager.getConnection(dbUrl);
        return connection;
    }

    public StringBuilder getHiddenWord() {
        return hiddenWord;
    }


    public String getPlayerName() {return playerName;
    }

    //public int getTimeSeconds() {return TimeSeconds();}
    public void insertScoreToTable (){
        /**
         * TO-DO example is here https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement/
         * TO-DO example to format dates https://stackoverflow.com/questions/64759668/what-is-the-correct-datetimeformatter-pattern-for-datetimeoffset-column#:~:text=You%20need%20to%20use%20the,SSSSSS%20xxx%20.
         */

        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters) VALUES (?, ?, ?, ?)";
        String removeBrackets = getMissedLetters().toString().replace("[", "").replace("]", "");
        DataScores endTime = new DataScores(LocalDateTime.now(), getPlayerName(), getWordToGuess(), removeBrackets, timeSeconds);

        try {
            Connection conn = this.dbConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String playerTime = endTime.getGameTime().format(formatter);
            preparedStmt.setString(1, playerTime);
            preparedStmt.setString(2, endTime.getPlayerName());
            preparedStmt.setString(3, endTime.getGuessWord());
            preparedStmt.setString(4, endTime.getMissingLetters());
            //preparedStmt.setInt(5, endTime.getTimeSeconds());
            //preparedStmt.setString(5, String.valueOf(endTime.getGameTime()));
            preparedStmt.executeUpdate();
            selectScores();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private void selectScores() {return;}


    public int getCountMissedWords() {
        return countMissedWords;


    }
    public void incrementMissedWords() {
        this.countMissedWords++;
    }
    public void setCountMissedWords(int countMissedWords) {
        this.countMissedWords = countMissedWords;
    }

    public void setHiddenWord(StringBuilder hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    public Image getImageFile() {
        if (imageId < 0 || imageId >= imageFiles.size()) {
            throw new IllegalArgumentException("Invalid imageId: " + imageId);
        }
        String imagePath = imageFiles.get(imageId);
        return new ImageIcon(imagePath).getImage();
    }
}