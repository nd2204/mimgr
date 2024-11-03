package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.ColorTheme.theme;
import dev.mimgr.theme.builtin.ColorScheme;

public class RoundedPanel extends JPanel {
  public RoundedPanel(ColorScheme colors) {
    super();
    this.colors = colors;
    Init();
  }

  public RoundedPanel() {
    super();
    colors = ColorTheme.get_colorscheme(theme.THEME_DARK_EVERFOREST);
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
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), this.borderRadius, this.borderRadius);
    if (borderColor != null) {
      g2d.setColor(borderColor);
    }
    g2d.setStroke(new BasicStroke(borderWidth));
    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, this.borderRadius, this.borderRadius);
  }

  public void setBorderRadius(int radius) {
    this.borderRadius = radius;
    repaint();
  }

  public void setBorderWidth(int width) {
    this.borderRadius = width;
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
