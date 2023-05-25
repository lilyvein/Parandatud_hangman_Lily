package main;

import controllers.Controller;
import models.Model;
import views.View;

import javax.swing.*;

/**
 * This is the class that is invoked
 */
public class AppMain {

    /**
     * A method that creates the necessary objects for playing. This means model, GUI, etc.
     */
    private static void start() {
        Model model = new Model(); // Create Model
        // Creates the main window and all the necessary panels after that with the labels and text fields of the
        // buttons there
        View view = new View(model);
        new Controller(model, view); // For real-time, game-time and button functionality

        view.pack(); // Adjusts the objects in the main window to "fit"
        view.setLocationRelativeTo(null); // Main window in the center of the screen
        view.setVisible(true); // Makes the main window visible. From here on, the user can click on the main window
    }

    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppMain::start);
    }
}
