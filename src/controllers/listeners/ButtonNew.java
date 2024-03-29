package controllers.listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNew implements ActionListener {
    private final Model model;
    private final View view;

    /**
     * New Game button constructor.
     * @param model Model
     * @param view View
     */
    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Here is the action that happens when the New Game button is pressed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        view.hideNewButtons(); // Seadistab nupud ja tekstiväljad uue mängu jaoks
        model.setImageId(0);  // Seadistab pildi ID algväärtusele
        ButtonSend.guessedLetters.clear();  // Tühjendab arvatud tähtede nimekirja
        view.getRealDateTime().stop(); // Peatab reaalse aja taimeri

        // Peatab mängu aja taimeri ja lähtestab aja väärtused
        view.getGameTime().stopTimer();
        view.getGameTime().setSeconds(0);
        view.getGameTime().setMinutes(0);
        view.getGameTime().startTimer(); // Alustab mängu aja taimerit
        view.getTxtChar().requestFocus(); // Tegevusliini fookus pärast uue mängu nupu vajutamist
        view.setNewImage(0); // Seadistab uue pildi
        String selectedCategory = view.getCmbCategory().getSelectedItem().toString();
        model.generatedWordFromCategoriesList(selectedCategory); // Genereerib uue sõna valitud kategooria põhjal
        String wordOfNew = model.addSpaceBetween(String.valueOf(model.getWordNewOfLife()));
        view.getLblResult().setText(wordOfNew); // Uuendab kasutajaliidest uue genereeritud sõnaga
    }


}
