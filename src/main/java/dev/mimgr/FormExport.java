package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.db.CategoryRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormExport extends JFrame {
  private double m_aspect_ratio;
  private int m_width;
  private int m_height;

  public MButton getAddProductSubmitButton() {
    return this.btnSubmit;
  }

  public FormExport() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    UploadButtonListener uploadButtonListener = new UploadButtonListener();
    TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();
    // Main window
    ProductExportPanel = new ProductExportPanel();
    btnSubmit = ProductExportPanel.getSubmitComponent();
    btnSubmit.addActionListener(uploadButtonListener);
    btnSubmit.setEnabled(false);
    btnDelete = ProductExportPanel.getDeleteComponent();
    btnDelete.addActionListener(uploadButtonListener);

    cbCategory = ProductExportPanel.getCategoryComponent();
    cbCategoryTextField = (JTextField) cbCategory.getEditor().getEditorComponent();
    cbCategoryTextField.setText("Export all categories");
    cbCategoryTextField.getDocument().addDocumentListener(textFieldListener);
    cbCategoryTextField.addFocusListener(new TextFieldFocusListener("Export all categories", cbCategoryTextField));

    cbProduct = ProductExportPanel.getProductComponent();
    cbProductTextField = (JTextField) cbProduct.getEditor().getEditorComponent();
    cbProductTextField.setText("Export all columns");
    cbProductTextField.getDocument().addDocumentListener(textFieldListener);
    cbProductTextField.addFocusListener(new TextFieldFocusListener("Export all columns", cbProductTextField));

    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Export to CSV file");
    this.setSize(854, m_height);
    this.add(ProductExportPanel);
    this.setLocation(
        (screen_size.width - this.getWidth()) / 2,
        (screen_size.height - this.getHeight()) / 2);
    this.setVisible(true);
    this.requestFocus();
  }

  private class UploadButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnDelete) {
        FormExport.this.dispose();
        return;
      }

      if (e.getSource() == btnSubmit) {

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
      // if (!tfTitle.getText().isEmpty() && !tfPrice.getText().isEmpty() &&
      // !tfStock.getText().isEmpty()) {
      // btnSubmit.setBackground(colors.m_blue);
      // btnSubmit.setBorderColor(colors.m_blue);
      // btnSubmit.setForeground(colors.m_fg_1);
      // btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
      // btnSubmit.setEnabled(true);
      // } else {
      // btnSubmit.setEnabled(false);
      // btnSubmit.setBackground(colors.m_bg_1);
      // btnSubmit.setBorderColor(colors.m_bg_1);
      // btnSubmit.setForeground(colors.m_grey_1);
      // btnSubmit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      // }
    }
  }

  private class TextFieldFocusListener extends FocusAdapter {
    TextFieldFocusListener(String text, JTextField textField) {
      placeholder = text;
      TextField = textField;
    }
    @Override
      public void focusLost(FocusEvent e) {
        if (TextField.getText().isEmpty()) {
          TextField.setText(placeholder);
        }
        repaint();
      }

      @Override
      public void focusGained(FocusEvent e) {
        if (TextField.getText().equals(placeholder)) {
          TextField.setText(placeholder);
        }
        repaint();
      }
      private String placeholder;
      private JTextField TextField;
  }

  public static void main(String arg[]) {
    new FormExport();
  }

  private ProductExportPanel ProductExportPanel;
  private MComboBox<CategoryRecord> cbCategory;
  private JTextField cbCategoryTextField;
  private MComboBox<ProductRecord> cbProduct;
  private JTextField cbProductTextField;
  private MButton btnDelete;
  private MButton btnSubmit;
  private ColorScheme colors;
}
