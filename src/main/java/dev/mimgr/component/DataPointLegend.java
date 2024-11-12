package dev.mimgr.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import dev.mimgr.FontManager;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class DataPointLegend extends JPanel {
  public DataPointLegend(DataPoint dp) {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.dp = dp;
    this.setVisible(true);
    this.setOpaque(false);
    this.setFont(FontManager.getFont("Nunito", 14f).deriveFont(Font.BOLD));
    FontMetrics metrics = this.getFontMetrics(this.getFont());

    int height = 30;
    int width = metrics.stringWidth(dp.dataLegend) + height;
    this.setMinimumSize(new Dimension(width, height));
    this.setPreferredSize(new Dimension(width, height));
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (dp.dataLegend != null && dp.dataLegend.length() > 0) {
      g2.setFont(FontManager.getFont("Nunito", 14f).deriveFont(Font.BOLD));
      FontMetrics fm = g2.getFontMetrics();

      int legendHeight = (int) (getHeight());

      // int x = (getWidth() - legendWidth) / 2;
      int y = (getHeight() - legendHeight) / 2;

      // g2.setColor(colors.m_bg_1);
      int radius = (int) (legendHeight * 0.2);
      // g2.fillRoundRect(0, y, legendWidth - 1, legendHeight - 1, radius, radius);
      g2.setColor(dp.lineColor);
      g2.fillRoundRect(8, y + 7, legendHeight - 17, legendHeight - 17, radius, radius);

      int textX = legendHeight / 2 + 14;
      int textY = getHeight() / 2 + fm.getAscent() / 2 - fm.getDescent() / 2 - 1;
      g2.setColor(colors.m_fg_0);
      g2.drawString(dp.dataLegend, textX, textY);
    }

    g2.dispose();
  }

  private DataPoint dp;
  private ColorScheme colors;
}

