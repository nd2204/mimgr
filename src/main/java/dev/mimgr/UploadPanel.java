package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.builtin.ColorScheme;

public class UploadPanel extends JPanel implements ActionListener, DocumentListener {

    UploadPanel(ColorScheme colors) {
        super();
        this.setLayout(new BorderLayout());
        this.setBackground(colors.m_bg_dim);
        m_colors = colors;
        this.setup_form_style();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name label and field
        nameLabel = new JLabel("Instrument Name:");
        nameLabel.setForeground(m_colors.m_fg_0);
        nameLabel.setFont(font_bold);
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(nameLabel, gbc);

        gbc.gridx = 1;
        this.add(nameField, gbc);

        // Price label and field
        priceLabel = new JLabel("Price:");
        priceLabel.setForeground(m_colors.m_fg_0);
        priceLabel.setFont(font_bold);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(priceLabel, gbc);
        gbc.gridx = 1;
        this.add(priceField, gbc);

        // Description label and text area
        descriptionLabel = new JLabel("Description:");
        descriptionLabel.setForeground(m_colors.m_fg_0);
        descriptionLabel.setFont(font_bold);
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new MScrollPane(descriptionArea);
        this.add(scrollPane, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL; // Reset to horizontal fill
        // Quantity in stock label and field
        quantityLabel = new JLabel("Quantity in Stock:");
        quantityLabel.setForeground(m_colors.m_fg_0);
        quantityLabel.setFont(font_bold);
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(quantityLabel, gbc);

        gbc.gridx = 1;
        this.add(quantityField, gbc);

        // Category label and field
        categoryLabel = new JLabel("Category Id:");
        categoryLabel.setForeground(m_colors.m_fg_0);
        categoryLabel.setFont(font_bold);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(categoryLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(categoryField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        this.add(uploadButton, gbc);
    }

    public void setup_form_style() {

        // ========================= Fields =========================
        nameField = new MTextField(30);
        nameField.setInputForeground(m_colors.m_fg_0);
        nameField.setBackground(m_colors.m_bg_1);
        nameField.setBorderColor(m_colors.m_bg_4);
        nameField.setFocusBorderColor(m_colors.m_blue);
        nameField.setBorderWidth(2);
        nameField.setFont(font_bold);
        nameField.getDocument().addDocumentListener(this);

        priceField = new MTextField(30);
        priceField.setInputForeground(m_colors.m_fg_0);
        priceField.setBackground(m_colors.m_bg_1);
        priceField.setBorderColor(m_colors.m_bg_4);
        priceField.setFocusBorderColor(m_colors.m_blue);
        priceField.setBorderWidth(2);
        priceField.setFont(font_bold);
        priceField.getDocument().addDocumentListener(this);

        descriptionArea = new MTextArea(10, 30);
        descriptionArea.setInputForeground(m_colors.m_fg_0);
        descriptionArea.setBackground(m_colors.m_bg_1);
        descriptionArea.setBorderColor(m_colors.m_bg_4);
        descriptionArea.setFocusBorderColor(m_colors.m_blue);
        descriptionArea.setBorderWidth(2);
        descriptionArea.setFont(font_bold);
        descriptionArea.getDocument().addDocumentListener(this);

        quantityField = new MTextField(30);
        quantityField.setInputForeground(m_colors.m_fg_0);
        quantityField.setBackground(m_colors.m_bg_1);
        quantityField.setBorderColor(m_colors.m_bg_4);
        quantityField.setFocusBorderColor(m_colors.m_blue);
        quantityField.setBorderWidth(2);
        quantityField.setFont(font_bold);
        quantityField.getDocument().addDocumentListener(this);

        // categoryField = new MComboBox(30);
        String[] options = get_category_names();
        categoryField = new MComboBox<>(options, m_colors);
        // txt = (JTextField) categoryField.getEditor().getEditorComponent();
        // txt.setBorder(BorderFactory.createCompoundBorder(
        //   new LineBorder(m_colors.m_bg_5, 1),
        //   new EmptyBorder(new Insets(10, 20, 10, 20))
        // ));

        // ========================= Buttons =========================
        this.uploadButton = new MButton("Submit");
        this.uploadButton.setFont(font_bold);
        this.uploadButton.setBackground(m_colors.m_bg_4);
        this.uploadButton.setBorderColor(m_colors.m_bg_4);
        this.uploadButton.setForeground(m_colors.m_grey_1);
        this.uploadButton.setBorderWidth(2);
        this.uploadButton.setEnabled(false);
        this.uploadButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton) {
          String name = nameField.getTextString();
          double price = Double.parseDouble(priceField.getTextString());
          String description = descriptionArea.getTextString();
          int stock_quantity = Integer.parseInt(quantityField.getTextString());
          int category_id = get_category_id(txt.getText());
          String image_url = "";
          System.out.println(category_id);
          if (category_id == 0) {
            JOptionPane.showMessageDialog(null, "Not valid category name");
          }
          else {
            JOptionPane.showMessageDialog(null, "Success");
            DBQueries.insert_product(name, price, description, stock_quantity, category_id, image_url);
          }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkFields();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkFields();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkFields();
    }

    private void checkFields() {
        if (!nameField.getText().isEmpty() && !priceField.getText().isEmpty() && !quantityField.getText().isEmpty() && !descriptionArea.getText().isEmpty()) {
            this.uploadButton.setBackground(m_colors.m_blue);
            this.uploadButton.setBorderColor(m_colors.m_blue);
            this.uploadButton.setForeground(m_colors.m_fg_1);
            this.uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.uploadButton.setEnabled(true);
        } else {
            this.uploadButton.setEnabled(false);
            this.uploadButton.setBackground(m_colors.m_bg_4);
            this.uploadButton.setBorderColor(m_colors.m_bg_4);
            this.uploadButton.setForeground(m_colors.m_grey_1);
            this.uploadButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private String[] get_category_names() {
      ResultSet queryResult = DBQueries.select_all_categories();
      List<String> nameList = new ArrayList<>();
      try {
        while (queryResult.next()) {
          nameList.add(queryResult.getString("category_name"));
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      String[] namesArray = nameList.toArray(new String[0]);
      return namesArray;
    }

    private int get_category_id(String category_name) {
      ResultSet queryResult = DBQueries.select_id_category(category_name);
      int id_result = 0;
      try {
        while (queryResult.next()) {
          id_result = queryResult.getInt("category_id");
        }
        return id_result;
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return id_result;
    }

    // Declare form components
    JLabel nameLabel, priceLabel, descriptionLabel, quantityLabel, categoryLabel, imageLabel;
    MTextField nameField, priceField, quantityField;
    JTextField txt;
    MComboBox<String> categoryField;
    MTextArea descriptionArea;
    MButton uploadButton;
    ColorScheme m_colors;
    Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);
}
