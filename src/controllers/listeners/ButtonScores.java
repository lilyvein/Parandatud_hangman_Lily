package controllers.listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Button Leaderboard functionality (implements ActionListener)
 */
public class ButtonScores implements ActionListener {
    private final Model model;
    private final View view;
    private final String[] header = new String[] {"Date", "Name", "Word", "Letters", "Game time"}; // Leaderboard header
    private final DefaultTableModel dtm = new DefaultTableModel(header,0); // Empty table with header
    private final JTable table = new JTable(dtm);
    private JDialog dialogScore; // Small Dialog window

    public ButtonScores(Model model, View view) {
        this.model = model;
        this.view = view;
        model.setDtmScores(dtm);
        createDialog(); // Create a window with a table header but no content
    }

    /**
     * Teeb dialog-i akna ja paneb tabeli sinna. Kuna seda on vaja korduvalt kasutada,
     */
    private void createDialog() {
        dialogScore = new JDialog();
        dialogScore.setPreferredSize(new Dimension(530,180));

        // Table first column width
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        // Table not editable https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
        table.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer(); // To center an object
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(center); // Center the fourth column

        dialogScore.add(new JScrollPane(table)); // Scrollbar
        dialogScore.setTitle("Leaderboard"); // Dialog title text
        dialogScore.pack();
        dialogScore.setLocationRelativeTo(null); // Center on screen
        dialogScore.setModal(true); // The main window cannot be clicked
    }

    /**
     * Button Leaderboard clicked
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new Database(model).selectScores(); // Read the data from the database List again
        if(model.getDataScores().size() > 0) { // if data in array
            view.updateScoresTable(); // Update leaderboard
            dialogScore.setVisible(true); // Show dialog window
            // view.getGameBoard().getTxtChar().requestFocus(); // Not needed here
        } else {
            JOptionPane.showMessageDialog(view, "Play first! The leaderboard is empty!");
        }
    }
}
