package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormCreateOrder extends JFrame {
  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

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

    UploadButtonListener uploadButtonListener = new UploadButtonListener();
    TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();
    // Main window
    orderItemPanel = new OrderItemPanel();
    orderItemPanel.btnSubmit.addActionListener(uploadButtonListener);
    orderItemPanel.btnDelete.addActionListener(uploadButtonListener);

    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Create New Order");
    this.setSize(854, m_height);
    this.add(orderItemPanel);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.requestFocus();
  }

  private class UploadButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == orderItemPanel.btnDelete) {
        FormCreateOrder.this.dispose();
        return;
      }

      if (e.getSource() == orderItemPanel.btnSubmit) {
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

    }
  }

  public static void main(String arg[]) {
    new FormCreateOrder();
  }

  private OrderItemPanel orderItemPanel;
  private ColorScheme colors;
}
