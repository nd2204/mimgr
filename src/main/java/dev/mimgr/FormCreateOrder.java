package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormCreateOrder extends JFrame {
  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public MButton getAddProductSubmitButton() {
    return this.btnSubmit;
  }

  static {
    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");
  }

  public FormCreateOrder() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);


    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    UploadButtonListener uploadButtonListener = new UploadButtonListener();
    TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();
    // Main window
    orderItemPanel = new OrderItemPanel();
    btnSubmit = orderItemPanel.getSubmitComponent();
    btnSubmit.addActionListener(uploadButtonListener);
    btnSubmit.setText("Add Product");
    btnSubmit.setEnabled(false);
    btnDelete = orderItemPanel.getDeleteComponent();
    btnDelete.addActionListener(uploadButtonListener);
    tfPrice = orderItemPanel.getPriceComponent();
    tfPrice.getDocument().addDocumentListener(textFieldListener);
    tfTitle = orderItemPanel.getTitleComponent();
    tfTitle.getDocument().addDocumentListener(textFieldListener);
    tfStock = orderItemPanel.getStockComponent();
    tfStock.getDocument().addDocumentListener(textFieldListener);
    taDescription = orderItemPanel.getDescriptionComponent();
    cbProduct = orderItemPanel.getProductComponent();

    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Add Product");
    this.setSize(854, m_height);
    this.add(orderItemPanel);
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
        FormCreateOrder.this.dispose();
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
      if (!tfTitle.getText().isEmpty() && !tfPrice.getText().isEmpty() && !tfStock.getText().isEmpty()) {
        btnSubmit.setBackground(colors.m_blue);
        btnSubmit.setBorderColor(colors.m_blue);
        btnSubmit.setForeground(colors.m_fg_1);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setEnabled(true);
      } else {
        btnSubmit.setEnabled(false);
        btnSubmit.setBackground(colors.m_bg_1);
        btnSubmit.setBorderColor(colors.m_bg_1);
        btnSubmit.setForeground(colors.m_grey_1);
        btnSubmit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    }
  }

  public static void main(String arg[]) {
    new FormCreateOrder();
  }

  private OrderItemPanel orderItemPanel;
  private MTextArea taDescription;
  private MTextField tfTitle;
  private MTextField tfPrice;
  private MTextField tfStock;
  private MComboBox<ProductRecord> cbProduct;
  private MButton btnDelete;
  private MButton btnSubmit;
  private ColorScheme colors;
}
