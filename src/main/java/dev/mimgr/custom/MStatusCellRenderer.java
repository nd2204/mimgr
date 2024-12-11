package dev.mimgr.custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.FontManager;

public class MStatusCellRenderer extends JPanel implements TableCellRenderer {
  public MStatusCellRenderer() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value instanceof SimpleEntry pair) {
      if (pair.getKey() instanceof String text
        && pair.getValue() instanceof Color color
      ) {
        statusString = text;
        statusBackground = color;
        repaint();
      }
    }
    // Set the background color based on selection
    if (isSelected) {
      this.setBackground(table.getSelectionBackground());
      this.setOpaque(true);
    } else {
      this.setBackground(table.getBackground());
      this.setOpaque(false);
    }

    return this;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (statusString != null && statusString.length() > 0) {
      g2.setFont(FontManager.getFont("Nunito", 14f).deriveFont(Font.BOLD));
      FontMetrics fm = g2.getFontMetrics();

      int statusHeight = (int) (getHeight() * 0.5);
      int statusWidth = fm.stringWidth(statusString) + statusHeight + 15;
      // int x = (getWidth() - statusWidth) / 2;
      int y = (getHeight() - statusHeight) / 2;

      g2.setColor(statusBackground);
      g2.fillRoundRect(0, y, statusWidth - 1, statusHeight - 1, statusHeight, statusHeight);
      g2.setColor(colors.m_fg_1);
      g2.fillRoundRect(5, y + 4, statusHeight - 9, statusHeight - 9, statusHeight, statusHeight);

      int textX = statusHeight / 2 + 14;
      int textY = getHeight() / 2 + fm.getAscent() / 2 - fm.getDescent() / 2 - 1;
      g2.setColor(colors.m_fg_1);
      g2.drawString(statusString, textX, textY);
    }

    g2.setColor(colors.m_bg_1);
    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

    g2.dispose();
  }

  private ColorScheme colors;
  private String statusString = "";
  private Color statusBackground = Color.GRAY;
}
