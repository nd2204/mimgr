package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

public class MTextField extends JTextField { 
  private Color borderColor = Color.GRAY;
  private int borderRadius = 0;
  private float borderWidth = 1;

  // Constructor with default settings
  public MTextField(int columns) {
    super(columns);
    setOpaque(false); // Make the background transparent for custom painting
    setBorder(null);
  }

  // Override the paintComponent method to customize the drawing
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();

    // Enable anti-aliasing for smooth edges
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw background
    g2d.setColor(getBackground());
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

    // Draw the text
    super.paintComponent(g2d);

    // Draw border
    g2d.setColor(borderColor);
    g2d.setStroke(new BasicStroke(borderWidth));
    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);

    g2d.dispose(); // Dispose the graphics context to release system resources
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
    this.borderWidth = width;
    repaint();
  }
}
