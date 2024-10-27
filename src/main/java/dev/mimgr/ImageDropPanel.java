package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageDropPanel extends JPanel {

    private JLabel imageLabel;
    private static String url;
    private JLabel urlLabel;  // To display the URL of the image

    public ImageDropPanel() {
        setLayout(new BorderLayout());
        imageLabel = new JLabel("Drop an image here", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        imageLabel.setBorder(BorderFactory.createEtchedBorder());

        // Allow drag-and-drop on the panel
        setDragAndDrop();

        add(imageLabel, BorderLayout.CENTER);
    }

    private void setDragAndDrop() {
        // Set up the panel to accept drops
        new DropTarget(this, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                try {
                    Transferable transferable = dtde.getTransferable();

                    // Handle URL drag-and-drop from browser
                    if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        url = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        if (url.startsWith("http")) {
                            displayImageFromUrl(url);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dtde.dropComplete(true);
            }
        });
    }

    private void displayImageFromUrl(String url) {
        try {
            // Load the image from the URL
            @SuppressWarnings("deprecation")
            ImageIcon imageIcon = new ImageIcon(new URL(url));

            // Resize the image to fit 50x50
            Image resizedImage = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);

            // Set the image in the JLabel
            imageLabel.setIcon(new ImageIcon(resizedImage));
            imageLabel.setText("");  // Clear any placeholder text

            // Display the URL below the image
            // urlLabel.setText("Image URL: " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayImage(File file) {
        try {
            // Read the image file
            BufferedImage originalImage = ImageIO.read(file);

            // Resize the image to 50x50
            Image resizedImage = originalImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH);

            // Set the resized image in the JLabel
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            imageLabel.setIcon(imageIcon);
            imageLabel.setText(""); // Clear the default text
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveImageToFile(String urlString, String id) {
        try {
            // Create the "upload" folder if it doesn't exist
            Path uploadDir = Path.of("src/main/resources/upload");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Extract the file name from the URL
            @SuppressWarnings("deprecation")
            URL url = new URL(urlString);
            String fileName = Paths.get(url.getPath()).getFileName().toString();
            String InstrumentName = id + '.' + fileName.substring(fileName.lastIndexOf(".") + 1);
            System.out.println(InstrumentName);

            // Define the path where the image will be saved
            File outputFile = uploadDir.resolve(InstrumentName).toFile();  // Updated line

            // Download and save the image
            BufferedImage image = ImageIO.read(url);
            String fileExtension = InstrumentName.substring(InstrumentName.lastIndexOf(".") + 1);  // Get file extension
            System.out.println(fileExtension);
            ImageIO.write(image, fileExtension, outputFile);

            System.out.println("Image saved to: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImageUrl() {
        return url;
    }
}
