package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.ColorTheme.theme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MTextField extends JTextField { 
  private ColorScheme colors;
  private Color borderColor;
  private Color focusBorderColor;
  private int borderRadius = 15;
  private int borderWidth = 1;
  private Insets padding = new Insets(10, 10, 10, 10);

  // Constructor with default settings
  public MTextField(int columns) {
    super(columns);
    Init();
  }

  public MTextField() {
    super();
    Init();
  }

  private void Init() {
    this.colors = ColorTheme.get_colorscheme(theme.THEME_LIGHT_DEFAULT);
    padding.set(padding.top + borderWidth, padding.left + borderWidth, padding.bottom + borderWidth, padding.right + borderWidth);
    this.setBorder(new EmptyBorder(padding));
    this.setBorderColor(null);
    this.borderColor = colors.m_fg_0;
    this.focusBorderColor = colors.m_accent;
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
      g2d.setColor(this.focusBorderColor);
    }
    g2d.setStroke(new BasicStroke(this.borderWidth));
    g2d.drawRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius, borderRadius);

    g2d.dispose();
  }

  // Customization methods
  public void setBorderColor(Color color) {
    this.borderColor = color;
    repaint();
  }

  public void setBorderRadius(int radius) {
    this.borderRadius = radius;
    repaint();
  }

  public void setBorderWidth(int width) {
    padding.set(padding.top + borderWidth, padding.left + borderWidth, padding.bottom + borderWidth, padding.right + borderWidth);
    this.setBorder(new EmptyBorder(padding));

    this.borderWidth = width;
    repaint();
  }

  public void setPadding(int left, int top, int right, int bottom) {
    padding.set(top + borderWidth, left + borderWidth, bottom + borderWidth, right + borderWidth);
    this.setBorder(new EmptyBorder(padding));
  }

  public void setFocusBorderColor(Color color) {
    this.focusBorderColor = color;
  }
}
