package dev.mimgr.custom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import dev.mimgr.theme.builtin.ColorScheme;

public class MCheckBoxCellRenderer extends MCheckBox implements TableCellRenderer {
  public MCheckBoxCellRenderer(ColorScheme colors) {
    this.colors = colors;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    setHorizontalAlignment(SwingConstants.CENTER);
    setSelected((Boolean) value);
    setBorder(null);
    setBorderPainted(false);

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

    // Draw the border using the specified color
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(colors.m_bg_3);
    g2.drawLine(0, getHeight(), getWidth(), getHeight()); // Bottom border

    g2.dispose();
  }

  public MCheckBox getCheckBoxComponent() {
    return this;
  }

  private ColorScheme colors;
}

