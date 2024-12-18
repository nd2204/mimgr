package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class RoundedPanel extends JPanel {
  public RoundedPanel() {
    super();
    colors = ColorTheme.getInstance().getCurrentScheme();
    Init();
  }

  private void Init() {
    this.setBackground(colors.m_bg_0);
    this.borderColor = null;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(this.getBackground());
    g2d.fillRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth, getHeight() - borderWidth, this.borderRadius, this.borderRadius);
    if (borderColor != null) {
      g2d.setColor(borderColor);
    }
    g2d.setStroke(new BasicStroke(borderWidth));
    g2d.drawRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth, getHeight() - borderWidth, this.borderRadius, this.borderRadius);
  }

  public void setBorderRadius(int radius) {
    this.borderRadius = radius;
    repaint();
  }

  public void setBorderWidth(int width) {
    this.borderWidth = width;
    repaint();
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
    repaint();
  }

  private final ColorScheme colors;
  private Color borderColor;
  private int borderRadius = 15;
  private int borderWidth = 1;
}
