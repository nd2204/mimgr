package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MTextArea extends JTextArea implements FocusListener { 
  // Constructor with default settings
  public MTextArea(int rows, int columns) {
    super(rows, columns);
    Init();
  }

  public MTextArea() {
    super();
    Init();
  }

  private void Init() {
    this.setPadding(padding);
    this.setBorder(BorderFactory.createEmptyBorder());
    this.setBorderColor(Color.BLACK);
    this.setInputForeground(Color.BLACK);
    this.setPlaceholderForeground(Color.BLACK);
    this.setPlaceholder("");
    this.setOpaque(false);
    this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    this.addFocusListener(this);
    setOpaque(false); // Make the background transparent for custom painting
  }

  // Override the paintComponent method to customize the drawing
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();

    // Enable anti-aliasing for smooth edges
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw background
    g2d.setColor(getBackground());
    g2d.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius, borderRadius);

    // Draw the text
    super.paintComponent(g2d);

    g2d.setColor(this.borderColor);
    if (isFocusOwner()) {
      if (focusBorderColor == null) {
        g2d.setColor(this.borderColor);
      } else {
        g2d.setColor(this.focusBorderColor);
      }
    }
    g2d.setStroke(new BasicStroke(this.borderWidth));
    g2d.drawRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius, borderRadius);
    g2d.dispose();
  }

  // Customization methods
  public Color getBorderColor() {
    return this.borderColor;
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
    repaint();
  }

  public int getBorderRadius() {
    return this.borderRadius;
  }

  public void setBorderRadius(int radius) {
    this.borderRadius = radius;
    repaint();
  }

  public int getBorderWidth() {
    return this.borderWidth;
  }

  public void setBorderWidth(int width) {
    padding.set(padding.top + borderWidth, padding.left + borderWidth, padding.bottom + borderWidth, padding.right + borderWidth);
    this.setBorder(new EmptyBorder(padding));

    this.borderWidth = width;
    repaint();
  }

  public Insets getPadding() {
    return this.padding;
  }

  public void setPadding(Insets padding) {
    this.padding.set(padding.top + borderWidth, padding.left + borderWidth, padding.bottom + borderWidth, padding.right + borderWidth);
    this.setBorder(new EmptyBorder(padding));
    repaint();
  }

  public Color getFocusBorderColor() {
    return this.focusBorderColor;
  }

  public void setFocusBorderColor(Color color) {
    this.focusBorderColor = color;
    repaint();
  }

  public String getPlaceholder() {
    return this.placeholder;
  }

  public void setPlaceholder(String text) {
    this.placeholder = text;
    this.setText(text);
    repaint();
  }

  public Color getPlaceholderForeground() {
    return this.placeholderForeground;
  }

  public void setPlaceholderForeground(Color color) {
    this.placeholderForeground = color;
    this.setForeground(color);
    repaint();
  }

  public Color getInputForeground() {
    return this.inputForeground;
  }

  public void setInputForeground(Color color) {
    this.setCaretColor(color);
    this.inputForeground = color;
    repaint();
  }

  public String getTextString() {
    String username = this.getText();
    if (username.equals(placeholder)) {
      return "";
    }
    return username;
  }

  @Override
  public void focusLost(FocusEvent e) {
    if (this.getText().isEmpty()) {
      this.setForeground(placeholderForeground);
      this.setText(placeholder);
    }
    repaint();
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (this.getText().equals(placeholder)) {
      this.setForeground(inputForeground);
      this.setText("");
    }
    repaint();
  }

  private String placeholder;
  private Color placeholderForeground;
  private Color inputForeground;
  private Color borderColor;
  private Color focusBorderColor = null;
  private int borderRadius = 0;
  private int borderWidth  = 0;
  private Insets padding = new Insets(10, 20, 20, 20);
}
