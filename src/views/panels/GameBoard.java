package views.panels;

import helpers.TextFieldLimit;
import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This class makes the GameBoard panel (top panel) (extends JPanel)
 */
public class GameBoard extends JPanel {
    private final Model model;
    /**
     * This panel is on top of the JFrame and the buttons, labels and text field on the top side are placed on top of
     * this panel
     */
    private final JPanel pnlComponents = new JPanel(new GridBagLayout());
    private GameImages gameImages; // Image panel
    /**
     * GridBagLayout settings
     */
    private final GridBagConstraints gbc = new GridBagConstraints();
    private JLabel lblTime; // Label for time (game time or realtime)
    private JLabel lblError; // Label for error text: Wrong 0 letter(s)
    private JTextField txtChar; // Input for letter
    private JButton btnNew; // Button New Game
    private JButton btnScore; // Button Leaderboard
    private JButton btnCancel; // Button Cancel game
    private JButton btnSend; // Button Send letter
    private JComboBox<String> cmbCategory; // category ComboBox

    /**
     * Class constructor. Always do this part when creating an object. An object is created only once.
     * @param model A previously made model
     */
    public GameBoard(Model model, View view) {
        this.model = model; // The GameBoard panel needs access to the model

        this.setLayout(new FlowLayout(FlowLayout.LEFT)); // This panel layout
        this.setBackground(new Color(255, 255, 160)); // this panel background color

        //pnlComponents.setBackground(new Color(240, 160, 170));
        pnlComponents.setBorder(new EmptyBorder(5,5,5,5));

        gbc.anchor = GridBagConstraints.WEST;   // Text in a left-aligned cell
        gbc.insets = new Insets(2,5,2,5); // Empty space around the panel
        gbc.fill = GridBagConstraints.BOTH;     // Stretch the component wide in the cell

        populatePanel(); // Places all necessary objects on the panel

        gameImages = new GameImages(model); // Creates a panel of image

        this.add(pnlComponents);
        this.add(gameImages);
    }

    /**
     * All objects placed on the panel (JLabel, JButton, JTextField, JComboBox)
     */
    private void populatePanel(){
        // First Line
        lblTime = new JLabel("Here comes the right time or game time", JLabel.CENTER);
        gbc.gridx = 0; // Veerg
        gbc.gridy = 0; // rida
        gbc.gridwidth = 3; // A label over three columns
        pnlComponents.add(lblTime, gbc);

        // Second Line
        JLabel lblCategory = new JLabel("Category");
        gbc.gridx = 0; // Veerg
        gbc.gridy = 1; // rida
        gbc.gridwidth = 1; // Back one column wide
        pnlComponents.add(lblCategory, gbc);

        cmbCategory = new JComboBox<>(model.getCmbNames());
        gbc.gridx = 1; // Veerg
        gbc.gridy = 1; // rida
        pnlComponents.add(cmbCategory, gbc);

        btnNew = new JButton("New Game");
        gbc.gridx = 2; // Veerg
        gbc.gridy = 1; // rida
        pnlComponents.add(btnNew, gbc);

        // Third line
        JLabel lblChar = new JLabel("Input character");
        gbc.gridx = 0; // Veerg
        gbc.gridy = 2; // rida
        pnlComponents.add(lblChar, gbc);

        /*
         * TextField all time focus
         * https://stackoverflow.com/questions/4640138/setting-the-focus-to-a-text-field
         */
        txtChar = new JTextField("", 14) {
            @Override
            public void addNotify() {
                super.addNotify();
                requestFocus();
            }
        };
        txtChar.setEnabled(false); // Can't input text (letter)
        txtChar.setHorizontalAlignment(JTextField.CENTER); // Write in the middle of the cell
        txtChar.setDocument(new TextFieldLimit(1)); // One letter into TextField (helpers/TextFieldLimit)
        gbc.gridx = 1; // Veerg
        gbc.gridy = 2; // rida
        pnlComponents.add(txtChar, gbc);

        btnSend = new JButton("Send");
        btnSend.setEnabled(false);
        gbc.gridx = 2; // Veerg
        gbc.gridy = 2; // rida
        pnlComponents.add(btnSend, gbc);

        // Fourth line
        lblError = new JLabel("Wrong 0 letter(s)");
        gbc.gridx = 0; // Veerg
        gbc.gridy = 3; // rida
        gbc.gridwidth = 3; // Over three columns
        pnlComponents.add(lblError, gbc);

        // Fifth line
        btnCancel = new JButton("Cancel the game");
        btnCancel.setEnabled(false);
        gbc.gridx = 0; // Veerg
        gbc.gridy = 4; // rida
        gbc.gridwidth = 1; // Back one column wide
        pnlComponents.add(btnCancel, gbc);

        btnScore = new JButton("Leaderboard");
        gbc.gridx = 1; // Veerg
        gbc.gridy = 4; // rida
        pnlComponents.add(btnScore, gbc);
    }

    /**
     * Let's take a time label.
     * @return a real-time or game-time label to view
     */
    public JLabel getLblTime() {
        return lblTime;
    }

    /**
     * Let's take a button leaderboard
     * @return button
     */
    public JButton getBtnScore() {
        return btnScore;
    }

    /**
     * Let's take a letter in the input box
     * @return textfield
     */
    public JTextField getTxtChar() {
        return txtChar;
    }

    /**
     * Let's take a button new game
     * @return nutton
     */
    public JButton getBtnNew() {
        return btnNew;
    }

    /**
     * Let's take a button cancel game
     * @return button
     */
    public JButton getBtnCancel() {
        return btnCancel;
    }

    /**
     * Let's take a button letter send
     * @return button
     */
    public JButton getBtnSend() {
        return btnSend;
    }

    /**
     * Let's take a ComboBox for categories
     * @return ComboBox
     */
    public JComboBox<String> getCmbCategory() {
        return cmbCategory;
    }

    /**
     * Return the GameImage panel
     * @return GameImage
     */
    public GameImages getGameImages() {
        return gameImages;
    }
    /**
     * Tagastab JLabeli mis sisaldab vigade ingot
     * @return JLabel
     */
    public JLabel getLblError() {return lblError;}

}
