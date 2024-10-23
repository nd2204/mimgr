package dev.mimgr.custom;

import java.awt.BasicStroke;
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
    this.setBackground(this.colors.m_bg_0);
  }

  public RoundedPanel() {
    super();
    colors = ColorTheme.get_colorscheme(theme.THEME_LIGHT_DEFAULT);
    this.setBackground(colors.m_bg_0);
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(this.getBackground());
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), this.borderRadius, this.borderRadius);
    g2d.setColor(this.getBackground());
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

  private final ColorScheme colors;
  private int borderRadius = 15;
  private int borderWidth = 1;
}
