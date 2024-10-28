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
    setCheckColor(colors.m_green);
    setBoxColor(colors.m_bg_4);
    setBackground(colors.m_bg_0);
    setSelected((Boolean) value);
    return this;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw the border using the specified color
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(colors.m_bg_4);
    g2.drawLine(0, getHeight(), getWidth(), getHeight()); // Bottom border

    g2.dispose();
  }

  public MCheckBox getCheckBoxComponent() {
    return this;
  }

  private ColorScheme colors;
}

