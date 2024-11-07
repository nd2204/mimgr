package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.db.CategoryRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

class FormAddProduct extends JFrame {
  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public MButton getAddProductSubmitButton() {
    return this.btnSubmit;
  }

  public FormAddProduct(ColorScheme colors) {
    this.colors = colors;
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    UploadButtonListener uploadButtonListener = new UploadButtonListener();
    TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();
    // Main window
    uploadPanel = new UploadPanel(colors);
    btnSubmit = uploadPanel.getSubmitComponent();
    btnSubmit.addActionListener(uploadButtonListener);
    btnSubmit.setText("Add Product");
    btnDelete = uploadPanel.getDeleteComponent();
    btnDelete.addActionListener(uploadButtonListener);
    tfPrice = uploadPanel.getPriceComponent();
    tfPrice.getDocument().addDocumentListener(textFieldListener);
    tfTitle = uploadPanel.getTitleComponent();
    tfTitle.getDocument().addDocumentListener(textFieldListener);
    tfStock = uploadPanel.getStockComponent();
    tfStock.getDocument().addDocumentListener(textFieldListener);
    taDescription = uploadPanel.getDescriptionComponent();
    cbCategory = uploadPanel.getCategoryComponent();
    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Add Product");
    this.setSize(854, m_height);
    this.add(uploadPanel);
    this.setLocation(
      (screen_size.width - this.getWidth()) / 2,
      (screen_size.height - this.getHeight()) / 2
    );
    this.setVisible(true);
    this.requestFocus();
  }

  private class UploadButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnDelete) {
        System.out.println("Exiting?");
        FormAddProduct.this.dispose();
        return;
      }

      if (e.getSource() == btnSubmit) {
        String name = tfTitle.getTextString();
        double price = Double.parseDouble(tfPrice.getTextString());
        String description = taDescription.getTextString();
        int stock_quantity = Integer.parseInt(tfStock.getTextString());
        int category_id = get_category_id((String) cbCategory.getSelectedItem());
        System.out.println(category_id);
        if (category_id == 0) {
          JOptionPane.showMessageDialog(null, "Not valid category name");
        }
        else {
          ProductRecord.insert(name, price, description, stock_quantity, category_id);
          JOptionPane.showMessageDialog(null, "Success");
          FormAddProduct.this.dispose();
        }
        return;
      }
    }
  }

  private class TextFieldDocumentListener implements DocumentListener {
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
      if (!tfTitle.getText().isEmpty() && !tfPrice.getText().isEmpty() && !tfStock.getText().isEmpty()) {
        btnSubmit.setBackground(colors.m_blue);
        btnSubmit.setBorderColor(colors.m_blue);
        btnSubmit.setForeground(colors.m_fg_1);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setEnabled(true);
      } else {
        btnSubmit.setEnabled(false);
        btnSubmit.setBackground(colors.m_bg_4);
        btnSubmit.setBorderColor(colors.m_bg_4);
        btnSubmit.setForeground(colors.m_grey_1);
        btnSubmit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    }
  }

  private int get_category_id(String category_name) {
    ResultSet queryResult = CategoryRecord.selectByName(category_name);
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


  public static void main(String arg[]) {
    ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.THEME_DARK_EVERFOREST);
    new FormAddProduct(colors);
  }

  private UploadPanel uploadPanel;
  private MTextArea taDescription;
  private MTextField tfTitle;
  private MTextField tfPrice;
  private MTextField tfStock;
  private MComboBox<String> cbCategory;
  private MButton btnDelete;
  private MButton btnSubmit;
  private ColorScheme colors;
}
