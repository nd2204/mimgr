package dev.mimgr.custom;

import javax.swing.*;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.ColorTheme.theme;
import dev.mimgr.theme.builtin.ColorScheme;
import java.awt.*;

public class RoundedPanel extends JPanel {
  private ColorScheme colors;
  private int borderWidth = 1;

  public RoundedPanel(ColorScheme colors) {
    this.colors = colors;
    setPreferredSize(new Dimension(200, 100));
  }

  public RoundedPanel() {
    colors = ColorTheme.get_colorscheme(theme.THEME_LIGHT_DEFAULT);
    setPreferredSize(new Dimension(200, 100));
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(colors.m_bg_0);
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
    g2d.setColor(colors.m_bg_0);
    g2d.setStroke(new BasicStroke(borderWidth));
    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
  }
}
