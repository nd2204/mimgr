package dev.mimgr.custom;

import dev.mimgr.theme.builtin.ColorScheme;

import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DropPanel extends JPanel {
  public DropPanel(ColorScheme colors) {
    this.colors = colors;
    this.setPreferredSize(new Dimension(200, 200));
    this.setBackground(this.colors.m_bg_0);
    this.borderColor = this.colors.m_grey_0;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();

    // Set dashed stroke
    float[] dashPattern = {10, 10}; // Dash length, gap length
    g2d.setStroke(new BasicStroke(
      2,                     // Line width
      BasicStroke.CAP_BUTT,  // End cap
      BasicStroke.JOIN_BEVEL, // Line join style
      0,                     // Miter limit
      dashPattern,           // Dash pattern
      0                      // Dash phase
    ));

    g2d.setColor(borderColor); // Set border color
    g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10); // Draw border with padding
    g2d.dispose();
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
    repaint();
  }

  private ColorScheme colors;
  private Color borderColor;
}

