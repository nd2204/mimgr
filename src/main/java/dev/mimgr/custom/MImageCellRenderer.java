package dev.mimgr.custom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import dev.mimgr.IconManager;
import dev.mimgr.theme.builtin.ColorScheme;

public class MImageCellRenderer extends JLabel implements TableCellRenderer {
  public MImageCellRenderer(ColorScheme colors) {
    this.colors = colors;
    this.setHorizontalAlignment(SwingConstants.CENTER);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value instanceof ImageIcon) {
      Icon icon = (Icon) value;
      if (icon.getIconHeight() > imageMaxSize) {
        icon =  IconManager.changeIconSize((ImageIcon) value, imageMaxSize, imageMaxSize);
      }
      this.setIcon(icon);
    } else {
      this.setIcon(null);
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
    g2.setColor(colors.m_bg_3);
    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

    g2.dispose();
  }

  private ColorScheme colors;
  private int imageMaxSize = 60;
}



