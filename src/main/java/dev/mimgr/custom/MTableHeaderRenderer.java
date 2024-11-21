package dev.mimgr.custom;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import dev.mimgr.FontManager;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MTableHeaderRenderer extends DefaultTableCellRenderer {
  public MTableHeaderRenderer(int orientation) {
    this.setHorizontalAlignment(orientation);
    Init();
  }

  public MTableHeaderRenderer() {
    this.setHorizontalAlignment(SwingConstants.LEFT);
    Init();
  }

  private void Init() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.borderColor = colors.m_bg_3;
    this.headerCellColor = colors.m_bg_dim;
    this.headerCellTextColor = colors.m_grey_0;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    comp.setBackground(headerCellColor);
    comp.setForeground(headerCellTextColor);
    comp.setFont(FontManager.getFont("NunitoBold", 14f));
    setBorder(new EmptyBorder(0, 5, 0, 5));
    return comp;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw the border using the specified color
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(borderColor);

    // Draw top, left, bottom, and right borders
    g2.drawLine(0, 0, getWidth(), 0); // Top border
    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1); // Bottom border

    g2.dispose();
  }

  public ColorScheme colors;
  public Color tableColor;
  public Color borderColor;
  public Color headerCellColor;
  public Color headerCellTextColor;
}
