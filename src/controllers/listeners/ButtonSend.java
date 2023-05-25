package controllers.listeners;

import models.Model;
import views.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    /**
     * Konstuktor
     * @param model Model
     * @param view View
     */
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
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

        System.out.println("GuessList to see whats inside: " + guessWord + guessList);  // For testing
        boolean correct = true;  // Set correct to true
        for (int i = 0; i < guessList.length; i++){  // Loop through word to guess
            if (guessList[i].equals(enteredChars)){ // If guessed letter is in word to guess
                model.getWordNewOfLife().setCharAt(i, guessedLetter);  // Set guessed letter
                //view.getLblGuessWord().setText(model.getWordNewOfLife().toString());
                view.getLblResult().setText(model.addSpaceBetween(model.getWordNewOfLife().toString()));
                //System.out.println(model.getWordNewOfLife());
                //System.out.println("What index of:  " + i);

                correct = false;
            }
        }
        if (correct){
            model.getMissedLetters().add(enteredChars);  // Add missed letters to list
            view.getLblError().setForeground(Color.RED);  //Valesti pakutud tähed on punased
        }
        model.setCountMissedWords(model.getMissedLetters().size());  // Set missed words
        view.getLblError().setText("Valesti: " + model.getCountMissedWords() + " täht(e) " + model.getMissedLetters());  // Set missed words
        view.getTxtChar().setText("");  // Set empty text field
        if (!model.getWordNewOfLife().toString().contains("_")) {  // If word is guessed
            model.askPlayerName();  // Ask player name
            model.getPlayerName();  // Get player name
            model.insertScoreToTable();  // Insert score to table
            view.setEndGame();  // Set end game
        }
        if (!(model.getCountMissedWords() < 11)) {  // If missed words are more than 11
            //System.out.println("counter: " + model.getCountMissedWords());
            JOptionPane.showMessageDialog(null, "Kaotasid mängu", "Mäng läbi", JOptionPane.PLAIN_MESSAGE);  // Show message
            view.setEndGame();  // Set end game
        }
    }
}

