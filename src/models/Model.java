package models;

import models.datastructures.DataScores;
import models.datastructures.DataWords;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.View;
import java.sql.SQLException;
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
    private final String databaseFile = "hangman_words_ee_test.db"; // Default database
    private final String imagesFolder = "images"; // Hangman game images location
    private List<String> imageFiles = new ArrayList<>(); // All images with full folder path
    private String[] cmbNames; // ComboBox categories names (contents)
    private final String chooseCategory = "All categories"; // Default first ComboBox choice
    private DefaultTableModel dtmScores; // Leaderboard DefaultTableModel
    private List<DataScores> dataScores = new ArrayList<>(); // The contents of the entire database table scores
    private List<DataWords> dataWords;
    private List<String> missedLetters = new ArrayList<>();
    private int imageId = 0; // Current image id (0..11)
    private String selectedCategory = chooseCategory; // Default all categories as "All categories"
    private String wordToGuess;
    private StringBuilder wordNewOfLife;
    private String playerName;
    public int countMissedWords;

    /**
     * During the creation of the model, the names of the categories to be shown in the combobox are known
     */
    public Model() {
        new Database(this);
        dataWords = new ArrayList<>();
        fetchWordsFromDatabase(); // Populate the dataWords list with data from the database
    }

    /**
     * Sets the content to match the ComboBox. Adds "All categories" and unique categories obtained from the database.
     * @param unique all unique categories from database
     */
    public void setCorrectCmbNames(List<String> unique) {
        if (unique == null || unique.isEmpty()) {
            cmbNames = new String[]{chooseCategory}; // If unique list is empty, create a one-element array with default choice
        } else {
            cmbNames = new String[unique.size() + 1];
            cmbNames[0] = chooseCategory; // First choice before categories
            int x = 1;
            for (String category : unique) {
                cmbNames[x] = category;
                x++;
            }
        }
    }

    /**
     * Fetch words from the database and populate dataWords list
     */
    public void fetchWordsFromDatabase() {
        try {
            Database database = new Database(this);
            dataWords = database.getWords(); // Get words from the database and populate the dataWords list
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
    public StringBuilder getWordNewOfLife() {
        return wordNewOfLife;
    }
    public String getWordToGuess() {
        return wordToGuess;
    }

    public int getCountMissedWords() {
        return countMissedWords;
    }
    public void setCountMissedWords(int countMissedWords) {
        this.countMissedWords = countMissedWords;
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
    public List<String> getMissedLetters(){
        return missedLetters;
    }
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets a new selected category
     * @param selectedCategory new category
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
    public void generatedWordFromCategoriesList(String selectedCategory) {
        if (dataWords == null || dataWords.isEmpty()) {
            // Handle the case when dataWords is null or empty
            // For example, show an error message or choose a default word
            return;
        }
        Random random = new Random();
        List<String> guessWordsToList = new ArrayList<>();
        if (selectedCategory.equals("All categories")) {
            wordToGuess = dataWords.get(random.nextInt(dataWords.size())).getWord();
        } else {
            for (DataWords word : dataWords) {
                if (selectedCategory.equals(word.getCategory())) {
                    guessWordsToList.add(word.getWord());
                }
            }
            if (guessWordsToList.isEmpty()) {
                // Handle the case when no words match the selected category
                // For example, show an error message or choose a default word
                return;
            }
            wordToGuess = guessWordsToList.get(random.nextInt(guessWordsToList.size()));
        }
        this.wordToGuess = wordToGuess.toUpperCase();
        hideLetters();
        //System.out.println(wordToGuess);
    }
    private void hideLetters() {
        StringBuilder correct = new StringBuilder(wordToGuess);
        for (int i = 0; i < wordToGuess.length(); i++) {
            correct.setCharAt(i, '_');
        }
        this.wordNewOfLife = correct;
        //System.out.println("Testsona: " + wordNewOfLife);

    }
    public void setMissedLetters(List<String> missedLetters) {
        this.missedLetters = missedLetters;
    }
    public String addSpaceBetween(String word) {
        //@TO-DO
        /**
         * @ something to try https://stackoverflow.com/questions/41953388/java-split-and-trim-in-one-shot
         * Don't use trim method not going to work as needed
         */
        String[] wordListOfList= word.split("");
        StringJoiner joiner = new StringJoiner(" ");
        for (String words : wordListOfList){
            joiner.add(words);
        }
        return joiner.toString();
    }
    public void askPlayerName() {
        playerName = JOptionPane.showInputDialog("Sisesta oma nimi");
        if (playerName.length() < 2) {
            askPlayerName();

        }
    }

}


