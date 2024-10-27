package dev.mimgr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.theme.builtin.ColorScheme;

// import static dev.mimgr.UploadService.upload_music_instrument;
public class FormUpload extends JPanel {

    // Declare form components
    JLabel nameLabel, priceLabel, descriptionLabel, quantityLabel, categoryLabel, imageLabel;
    MTextField nameField, priceField, categoryField, quantityField;
    MTextArea descriptionArea;
    MButton browseButton, uploadButton;
    ColorScheme m_colors;
    ImageDropPanel imageDropPanel = new ImageDropPanel();

    // ImageDropPanel imageDropPanel;
    public FormUpload(ColorScheme color) {
        m_colors = color;
        this.setup_form_style();
        JPanel leftSJPanel = new JPanel();
        // Create layout for the form
        setLayout(new GridBagLayout());
        leftSJPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name label and field
        nameLabel = new JLabel("Instrument Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftSJPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        leftSJPanel.add(nameField, gbc);

        // Price label and field
        priceLabel = new JLabel("Price:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        leftSJPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        leftSJPanel.add(priceField, gbc);

        // Description label and text area
        descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        leftSJPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        leftSJPanel.add(scrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Reset to horizontal fill

        // Quantity in stock label and field
        quantityLabel = new JLabel("Quantity in Stock:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        leftSJPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        leftSJPanel.add(quantityField, gbc);

        // Category label and field
        categoryLabel = new JLabel("Category Id:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        leftSJPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        leftSJPanel.add(categoryField, gbc);

        // Image label and file chooser
        imageLabel = new JLabel("Image Path:");
        gbc.gridx = 1;
        // gbc.gridy = 0;
        leftSJPanel.add(imageLabel, gbc);
        add(leftSJPanel);
        add(imageDropPanel);

        // gbc.gridx = 2;
        // add(browseButton, gbc);
        // Browse Button ActionListener for selecting an image
        // browseButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         JFileChooser fileChooser = new JFileChooser();
        //         int result = fileChooser.showOpenDialog(FormUpload.this);
        //         if (result == JFileChooser.APPROVE_OPTION) {
        //             File selectedFile = fileChooser.getSelectedFile();
        //             imagePathField.setText(selectedFile.getAbsolutePath());
        //         }
        //     }
        // });
        // Upload button
        gbc.gridx = 1;
        gbc.gridy = 7;
        leftSJPanel.add(uploadButton, gbc);

        // Upload Button ActionListener to handle form submission
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || categoryField.getText().isEmpty() || imageDropPanel.getImageUrl().isEmpty() || priceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(FormUpload.this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Get the form data
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String description = descriptionArea.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                int category = Integer.parseInt(categoryField.getText());
                String imgUrl = imageDropPanel.getImageUrl();
                String result = UploadService.post_instrument(name, price, description, quantity, category, imgUrl);

                // Display result message
                JOptionPane.showMessageDialog(FormUpload.this, result, "Upload Status", JOptionPane.INFORMATION_MESSAGE);

                if (result.equals("Music instrument uploaded successfully!")) {

                }
            }
        });
    }

    public void setup_form_style() {
        Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);
        // ========================= Fields =========================
        nameField = new MTextField(30);
        nameField.setInputForeground(m_colors.m_fg_0);
        nameField.setBackground(m_colors.m_bg_1);
        nameField.setBorderColor(m_colors.m_bg_4);
        nameField.setFocusBorderColor(m_colors.m_blue);
        nameField.setBorderWidth(2);
        nameField.setFont(font_bold);

        priceField = new MTextField(30);
        priceField.setInputForeground(m_colors.m_fg_0);
        priceField.setBackground(m_colors.m_bg_1);
        priceField.setBorderColor(m_colors.m_bg_4);
        priceField.setFocusBorderColor(m_colors.m_blue);
        priceField.setBorderWidth(2);
        priceField.setFont(font_bold);

        descriptionArea = new MTextArea(10,30);
        descriptionArea.setInputForeground(m_colors.m_fg_0);
        descriptionArea.setBackground(m_colors.m_bg_1);
        descriptionArea.setBorderColor(m_colors.m_bg_4);
        descriptionArea.setFocusBorderColor(m_colors.m_blue);
        descriptionArea.setBorderWidth(2);
        descriptionArea.setFont(font_bold);

        quantityField = new MTextField(30);
        quantityField.setInputForeground(m_colors.m_fg_0);
        quantityField.setBackground(m_colors.m_bg_1);
        quantityField.setBorderColor(m_colors.m_bg_4);
        quantityField.setFocusBorderColor(m_colors.m_blue);
        quantityField.setBorderWidth(2);
        quantityField.setFont(font_bold);

        categoryField = new MTextField(30);
        categoryField.setInputForeground(m_colors.m_fg_0);
        categoryField.setBackground(m_colors.m_bg_1);
        categoryField.setBorderColor(m_colors.m_bg_4);
        categoryField.setFocusBorderColor(m_colors.m_blue);
        categoryField.setBorderWidth(2);
        categoryField.setFont(font_bold);
        
        // ========================= Buttons =========================
        // this.browseButton = new MButton("");
        // this.browseButton.setFont(font_bold);
        // this.browseButton.setBackground(m_colors.m_bg_4);
        // this.browseButton.setBorderColor(m_colors.m_bg_4);
        // this.browseButton.setForeground(m_colors.m_grey_1);
        // this.browseButton.setBorderWidth(2);

        this.uploadButton = new MButton("Submit");
        this.uploadButton.setFont(font_bold);
        this.uploadButton.setBackground(m_colors.m_bg_4);
        this.uploadButton.setBorderColor(m_colors.m_bg_4);
        this.uploadButton.setForeground(m_colors.m_grey_1);
        this.uploadButton.setBorderWidth(2);
    }
}
