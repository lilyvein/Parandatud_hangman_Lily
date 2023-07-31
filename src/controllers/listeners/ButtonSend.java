package controllers.listeners;

import models.Model;
import views.View;
import views.panels.GameImages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klass nupu Saada täht jaoks
 */
public class ButtonSend implements ActionListener {
    /**
     * Mudel
     */
    private Model model;
    /**
     * View
     */
    private View view;

    int imageId = 0;

    private List imageFiles;
    private GameImages gameImages;

    /**
     * Konstuktor
     * @param model Model
     * @param view View
     */
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
        this.gameImages = new GameImages(this.model);

    }

    /**
     * Kui kliikida nupul Saada täht
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus tekstikastile

        String enteredChars = view.getTxtChar().getText().toUpperCase();  // Get entered chars
        char guessedLetter = enteredChars.charAt(0);  // Get guessed letter
        String guessWord = model.getWordToGuess(); // Get word to guess
        String[] guessList = guessWord.split("");  // Split word to guess
        System.out.println("GuessList to see whats inside: " + guessWord.toString());


        boolean correct = false;  // Set correct to true
        for (int i = 0; i < guessList.length; i++){  // Loop through word to guess
            if (guessList[i].equals(enteredChars)){ // If guessed letter is in word to guess
                model.getHiddenWord().setCharAt(i, guessedLetter);  // Set guessed letter
                view.getLblResult().setText(model.addSpaceBetween(model.getHiddenWord().toString()));
                //System.out.println(model.getHiddenWord());

                correct = true;
            }
        }
        if (!correct) {
            model.getMissedLetters().add(enteredChars);  // Add missed letters to list
            view.getLblError().setForeground(Color.RED);

            // Increase the count of wrong guesses
            model.setCountMissedWords(model.getCountMissedWords() + 1);
            if (model.getCountMissedWords() < model.getImageFiles().size()) {
                ImageIcon imageIcon = new ImageIcon(model.getImageFiles().get(model.getCountMissedWords()));
                gameImages.getLblImage().setIcon(imageIcon);

            } else {
                view.setEndGame();
            }
        }


        model.setCountMissedWords(model.getMissedLetters().size());  // Set missed words
        view.getLblError().setText("Valesti: " + model.getCountMissedWords() + " täht(e) " + model.getMissedLetters());  // Set missed words
        view.getTxtChar().setText("");  // Set empty text field

        if (!model.getHiddenWord().toString().contains("_")) {  // If word is guessed
            model.askPlayerName();  // Ask player name
            model.getPlayerName();  // Get player name
            model.insertScoreToTable();  // Insert score to table
            view.setEndGame();  // Set end game
        }
        if (!(model.getCountMissedWords() < 7)) {   // If missed words are more than 11
            //System.out.println("counter: " + model.getCountMissedWords());
            JOptionPane.showMessageDialog(null, "Kaotasid mängu", "Mäng läbi", JOptionPane.PLAIN_MESSAGE);  // Show message
            view.setEndGame();  // Set end game
        }
        if (model.getMissedLetters().isEmpty()) {
            //System.out.println("GuessList to see whats inside: " + Arrays.toString(guessList));  // For testing

        }
    }


    private int getMissedCount() {
        return model.getCountMissedWords();
    }
}