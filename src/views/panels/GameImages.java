package views.panels;

import models.Model;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that creates an image panel and at the same time reads the images in the image folder into an array
 */
public class GameImages extends JPanel {
    private final Model model;
    private JLabel lblImage; // A label that contains an image

    /**
     * A class constructor that is executed only at the time of order creation
     * @param model Model
     */
    public GameImages(Model model) {
        this.model = model;
        this.setBackground(new Color(195, 220, 255)); // Image panel background color
        this.setPreferredSize(new Dimension(130,130)); // Image panel size
        readImagesFolder(); // Reads the image files into an array

        lblImage = new JLabel(); // Creates a label for the image
        // Creates an icon for the image (use image hang11.png)
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().get(model.getImageFiles().size()-1));
        // Sets the image to a label
        lblImage.setIcon(imageIcon);
        // Adds a label to this panel (GameImages)
        add(lblImage);
    }

    /**
     * Reads the image files (all files!) into an array and the content is also added to the model.
     * There might be a better solution for this
     */
    private void readImagesFolder() {
        File folder = new File(model.getImagesFolder());
        File[] files = folder.listFiles(); // The File object has a listFiles method
        List<String> imageFiles = new ArrayList<>(); // Empty array for image locations
        for(File file: files) {
            imageFiles.add(file.getAbsolutePath()); // Each image is added to an array full of a long folder path
        }
        model.setImageFiles(imageFiles); // We set up the necessary array in the model
    }

    /**
     *
     * @return
     */
    public JLabel updateImage() {
        int missedCount = model.getCountMissedWords();
        // Kontrollige, et ei ületata maksimaalset piltide arvu
        if (missedCount <= 11) {
            String imagePath = model.getImageFiles().get(missedCount);
            ImageIcon imageIcon = new ImageIcon(imagePath);
            lblImage.setIcon(imageIcon);
        }
        return lblImage;
    }

    /**
     * Returns the label of the image
     * @return label of the image
     */
    public JLabel getLblImage() {
        return lblImage;
    }
}
