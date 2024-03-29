package controllers.listeners;

import helpers.GameTimer;
import models.Model;
import views.View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;




/**
 * Klass nupu Saada täht jaoks
 */
public class ButtonSend implements ActionListener {

    private final Model model;
    private final View view;

    /**
     * Konstruktor
     */

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
        //GameTimer gameTime = view.getGameTime();
        //System.out.println("Constructor gameTime object: " + this.gameTime);
    }

    /**
     * Kui nupule Saada täht vajutatakse, siis kontrollitakse, kas sisestatud täht on õige või vale.
     * Kui täht on õige, siis kuvatakse see sõna juurde. Kui täht on vale, siis kuvatakse see
     * valesti sisestatud tähtede juurde.
     * Kui mängija arvab kõik tähed ära, siis kuvatakse teade, et mängija võitis.
     * Kui mängija arvab 11 korda valesti, siis kuvatakse teade, et mängija kaotas.
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        view.getTxtChar().requestFocus(); // After pressing New Game, the input box becomes active
        String enteredChar = view.getTxtChar().getText().toUpperCase(); // Võtab sisestatud tähe
        if (enteredChar.isEmpty()) { // Kui sisestatud täht on tühi, siis ei tee midagi
            return;
        }

        char guessedLetter = enteredChar.charAt(0); // Võtab sisestatud tähe esimese tähe
        String guessWord = model.getWordToGuess(); // Võtab sõna, mida mängija peab ära arvama
        String[] guessList = guessWord.split(""); // Teeb sõnast massiivi
        System.out.println("GuessList: " + Arrays.toString(guessList));  // Prindib massiivi

        checkGuess(guessList, guessedLetter, enteredChar);  //  Kontrollib, kas sisestatud täht on õige või vale

        view.getTxtChar().setText(""); // Tühjendab sisestatud tähe
        checkGameStatus(); // Kontrollib, kas mängija võitis või kaotas
    }

    /**
     * Kontrollib, kas sisestatud täht on õige või vale
     */
    public void checkGuess(String[] guessList, char guessedLetter, String enteredChar) { // Kontrollib, kas sisestatud täht on õige või vale
        boolean correct = false; // Õige täht on valesti
        for (int i = 0; i < guessList.length; i++) { // Käib läbi massiivi
            if (guessList[i].equals(enteredChar)) { // Kui sisestatud täht on massiivis, siis
                model.getHiddenWord().setCharAt(i, guessedLetter); // Paneb sisestatud tähe õigesse kohta
                view.getLblResult().setText(model.addSpaceBetween(model.getHiddenWord().toString())); // Kuvab sõna koos sisestatud tähega
                correct = true; // Õige täht on õige
            }
        }
        handleIncorrectGuess(enteredChar, correct); // Kontrollib, kas sisestatud täht on õige või vale
        model.setCountMissedWords(model.getMissedLetters().size()); // Paneb valesti arvatud tähtede arvu
        view.getLblError().setText("Valesti: " + model.getCountMissedWords() + " täht(e) " + model.getMissedLetters()); // Kuvab valesti arvatud tähtede arvu
        view.getGameImages().updateImage(); // Uuenda pilti
    }

    /**
     * Kontrollib, kas sisestatud täht on õige või vale
     */

    private void handleIncorrectGuess(String enteredChar, boolean correct) { // Kontrollib, kas sisestatud täht on õige või vale
        if (!correct) {
            model.getMissedLetters().add(enteredChar); // Lisab valesti arvatud tähe
            view.getLblError().setForeground(Color.RED); // Kuvab valesti arvatud tähe punasega
            model.setCountMissedWords(model.getCountMissedWords() + 1);  // Paneb valesti arvatud tähtede arvu
            view.getGameImages().updateImage(); // Uuenda pilti
        } else {
            view.getLblError().setForeground(Color.BLACK);  // Kuvab õigesti arvatud tähe mustaga
        }
    }

    /**
     * Kontrollib, kas mängija võitis või kaotas
     */
    private void checkGameStatus() {
        if (!model.getHiddenWord().toString().contains("_")) {  // Kui sõnas ei ole enam alakriipse, siis

            JOptionPane.showMessageDialog(null, "Võitsid mängu", "Mäng läbi", JOptionPane.PLAIN_MESSAGE); // Kuvab teate, et mängija võitis
            //System.out.println("checkGameStatus gameTime object: " + gameTime);  // Prindib gameTime objekti
            view.getGameTime().stopTimer();  // Peatab mänguaja
            int playedTimeInSeconds = view.getGameTime().getPlayedTimeInSeconds();  // Võtab mängitud aja sekundites
            model.setGametime(playedTimeInSeconds);  // Salvestame mängitud aja siin

            model.askPlayerName(); // Küsib mängija nime
            model.getPlayerName();  // Võtab mängija nime
            model.insertScoreToTable();  // Lisab mängija nime ja mängitud aja tabelisse

            view.setEndGame();  // Lõpetab mängu
            return;

        }
        if (!(model.getCountMissedWords() < 11)) { //kui valesti arvatud tähtede arv on 11 või rohkem, siis
            JOptionPane.showMessageDialog(null, "Kaotasid mängu", "Mäng läbi", JOptionPane.PLAIN_MESSAGE); // Kuvab teate, et mängija kaotas
            view.setEndGame(); // Lõpetab mängu
        }
    }
}
