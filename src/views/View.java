package views;

import controllers.listeners.ButtonSend;
import helpers.GameTimer;
import helpers.RealDateTime;
import models.Model;
import models.datastructures.DataScores;
import views.panels.GameBoard;
import views.panels.GameResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class creates a main window (extends JFrame)
 */
public class View extends JFrame {
    private final Model model;
    private GameBoard gameBoard; // Top panel
    private GameResult gameResult; // Bottom panel
    private final RealDateTime realDateTime; // Real Date Time
    private final GameTimer gameTime; // Game time

    /**
     * Main window JFrame
     * @param model The already created model
     */
    public View(Model model) {
        this.model = model; // Use the model you made in AppMain

        setupFrame(); // set the JFrame properties
        setupPanels(); // sets and places panels (two) on this frame (JFrame)

        realDateTime = new RealDateTime(this); // Create real time
        realDateTime.start(); // Start real time. This not good place for start!

        gameTime = new GameTimer(this); // Creates an empty object when creating a frame
    }

    /**
     * Let's set the JFrame properties
     */
    private void setupFrame() {
        this.setTitle("Hangman 2023"); // Main window title text
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Makes the main window closeable
        this.setLayout(new BorderLayout()); // Sets a new layout for the window
        this.setMinimumSize(new Dimension(590,250));
    }

    /**
     * Creates panels and the objects needed for them on the main window
     */
    private void setupPanels() {
        gameBoard = new GameBoard(model); // Creates a top panel
        gameResult = new GameResult(); // Creates a bottom panel

        this.add(gameBoard, BorderLayout.NORTH); // Places the panel according to BorderLayout
        this.add(gameResult, BorderLayout.CENTER); // Places the panel according to BorderLayout
    }
    // All methods register* in file Controller.java
    /**
     * Take the leaderboard button from gameBoard and add an actionListener to the button
     * @param al actionListener
     */
    public void registerButtonScores(ActionListener al) {
        gameBoard.getBtnScore().addActionListener(al);
    }

    /**
     * Take the New Game button from the game board and add an actionListener to the button
     * @param al actionListener
     */
    public void registerButtonNew(ActionListener al) {
        gameBoard.getBtnNew().addActionListener(al);
    }

    /**
     * Nupu "Saada täht" funktsionaalsuseks
     * @param al ActionListener
     */
    public void registerButtonSend(ActionListener al) {gameBoard.getBtnSend().addActionListener(al);}

    /**
     * Take the game pause button from the game board and add an actionListener to the button
     * @param al actionListener
     */
    public void registerButtonCancel(ActionListener al) {
        gameBoard.getBtnCancel().addActionListener(al);
    }

    /**
     * Take a ComboBox from the game board and add an itemListener
     * @param il itemListener
     */
    public void registerComboBoxChange(ItemListener il) {
        gameBoard.getCmbCategory().addItemListener(il);
    }

    /**
     * Update the leaderboard table (DefaultTableModel)
     */
    public void updateScoresTable() {
        model.getDtmScores().setRowCount(0);
        for(DataScores ds : model.getDataScores()) {
            String gameTime = ds.gameTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            String name = ds.playerName();
            String word = ds.word();
            String chars = ds.missedLetters();
            int timeSeconds = ds.timeSeconds();
            String humanTime = convertSecToMMSS(timeSeconds);
            model.getDtmScores().addRow(new Object[] {gameTime, name, word, chars, humanTime});
        }
    }

    /**
     * Converts full seconds to minutes and seconds. 100 sec => 01:30 (1 min 30 sec)
     * @param seconds full seconds
     * @return 00:00 in format
     */
    private String convertSecToMMSS(int seconds) {
        int min = (seconds / 60);
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    /**
     * Show new game button and other button changes. There is no game
     */
    public void showNewButton() {
        gameBoard.getBtnNew().setEnabled(true); // Enable New Game button
        gameBoard.getCmbCategory().setEnabled(true); // // Enable ComboBox
        gameBoard.getBtnSend().setEnabled(false); // Disable Send button
        gameBoard.getBtnCancel().setEnabled(false); // Disable Cancel button
        gameBoard.getTxtChar().setEnabled(false); // Disable Input text field
        this.setNewImage(model.getImageFiles().size()-1); // Show the last picture from the list of pictures
    }

    /**
     * Hide new game button and other button changes. The game is on.
     */
    public void hideNewButtons() {
        gameBoard.getBtnNew().setEnabled(false); // Disable New button
        gameBoard.getCmbCategory().setEnabled(false); // // Disable ComboBox
        gameBoard.getBtnSend().setEnabled(true); // Enable Send button
        gameBoard.getBtnCancel().setEnabled(true); // Enable Cancel button
        gameBoard.getTxtChar().setEnabled(true); // Enable Input text field
    }
    /**
     * Return the time label located on the gameBoard (top panel)
     * @return label
     */
    public JLabel getLblTime() {
        return gameBoard.getLblTime();
    }

    /**
     * Return the guessed word label located in the gameResult panel (bottom panel)
     * @return label
     */
    public JLabel getLblResult() {
        return gameResult.getLblResult();
    }

    /**
     * Returns the GameBoard panel (top panel)
     * @return gameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns the real time
     * @return RealDateTime
     */
    public RealDateTime getRealDateTime() {
        return realDateTime;
    }

    /**
     * Reaturns the game time
     * @return GameTimer
     */
    public GameTimer getGameTime() {
        return gameTime;
    }

    /**
     * Set a new image according to the image number
     * @param id image id (0..11)
     */
    public void setNewImage(int id) {
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().get(id));
        gameBoard.getGameImages().getLblImage().setIcon(imageIcon);
    }

    /**
     * Returns an input box
     * @return JTextField
     */
    public JTextField getTxtChar() {
        return gameBoard.getTxtChar();
    }

    /**
     * Tagastab comboboxi
     * @return JCombobox
     */
    public JComboBox<String> getCmbCategory() {return gameBoard.getCmbCategory();}

    /**
     * Tagastab uue mängu nupu
     * @return JButton
     */
    public JButton getBtnNew() {
        return gameBoard.getBtnNew();
    }
    /**
     * Tagastab nupu Saada tähte nupu
     * @return JButton
     */
    public JButton getBtnSend() {
        return gameBoard.getBtnSend();
    }

    /**
     * Tagastab Cancel nupu
     * @return JButton
     */
    public JButton getBtnCancel() {
        return gameBoard.getBtnCancel();
    }

    /**
     * Tagsatab lbalei mis sisaldab vigast infot
     * @return JLabel
     */
    public JLabel getLblError() {
        return gameBoard.getLblError();
    }

    /**
     * Seadistab mängu ALGSEISU nuppude ja tekstiväljadega seoses. See kutsuda siis, kui kogu mängu info on olemas.
     */
    public void setStartGame() {
        getCmbCategory().setEnabled(false); // Comboboxi ei saa valida
        getBtnNew().setEnabled(false); // Mängimise ajal ei saa uut mängu alustada
        getTxtChar().setEnabled(true); // Tähte saab sisestada
        getBtnSend().setEnabled(true); // Saada täht nuppu saab kasutada
        getBtnCancel().setVisible(true);   // Mängu saab katkestada
        //getLblWrongInfo().setText("Valesti 0 täht(e). "); // Muuda vigade teavitus vaikimisi tekstiks
        //getLblWrongInfo().setForeground(Color.RED); // Muuda teksti värv vaikimsii mustaks
    }

    /**
     * Seadistab mängu LÕPPSEISU nuppude ja tekstiväljadega seoses. See kustuda siis kui mängu lõpp tulemus on teada
     * ja mäng on KINDLASTI lõppenud
     */
    public void setEndGame() {
        getCmbCategory().setEnabled(true); // Comboboxi saab  valida
        getBtnNew().setEnabled(true); // Saab uut mängu alustada
        getTxtChar().setEnabled(false); // Tähte ei saa sisestada
        getBtnSend().setEnabled(false); // Saada täht nuppu ei saa kasutada
        getBtnCancel().setVisible(false);  // Mängu ei saa enam katkestada
        getTxtChar().setText("");   // Sisestatud tähe tühjendamine
        getLblError().setText("Valesti 0 täht(e). "); // Muuda vigade teavitus vaikimisi tekstiks
        model.setMissedLetters(new ArrayList<>());
        //getGameTime();
        getLblError().setForeground(Color.BLACK); // Muuda teksti värv vaikimsii mustaks
    }

    public void imageUpdate(Image iconImage, String s) { return; }

}
