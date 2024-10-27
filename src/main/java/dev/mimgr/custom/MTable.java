package dev.mimgr.custom;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import dev.mimgr.FontManager;
import dev.mimgr.theme.builtin.ColorScheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MTable extends JTable {
  public MTable(Object[][] data, String[] column) {
    super(data, column);
  }

  public MTable(ColorScheme colors) {
    super();
    this.colors = colors;
    this.borderColor = colors.m_bg_3;
    this.headerCellColor = colors.m_bg_0;
    this.headerCellTextColor = colors.m_grey_0;
    this.tableColor = headerCellColor;
    borders[0] = borders[1] = borders[2] = borders[3] = false;
    this.borders[2] = true;

    getTableHeader().setDefaultRenderer(new TableHeader());
    getTableHeader().setPreferredSize(new Dimension(0, 45));
    this.setDefaultRenderer(Object.class, new TableCell());
    this.setGridColor(tableColor);
    this.setBackground(tableColor);
    setRowHeight(60);
  }

  // public void setup_scrollbar(JScrollPane sp) {
  //   sp.setVerticalScrollBar(new JScrollPane());
  //   JPanel panel = new JPanel();
  //   panel.setBackground(headerCellColor);
  //   sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);
  // }

  private class TableHeader extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object o, boolean bln, boolean bln1, int i, int i1) {
      Component comp = super.getTableCellRendererComponent(table, o, bln, bln1, i, i1);
      comp.setBackground(headerCellColor);
      comp.setForeground(headerCellTextColor);
      comp.setFont(FontManager.getFont("NunitoBold", 14f));
      borders[2] = true;
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
      if (borders[0]) g2.drawLine(0, 0, getWidth(), 0); // Top border
      if (borders[1]) g2.drawLine(0, 0, 0, getHeight()); // Left border
      if (borders[2]) g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2); // Bottom border
      if (borders[3]) g2.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight()); // Right border

      g2.dispose();
    }
  }

  public class TableCell extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean bln, boolean bln1, int row, int col) {
      Component comp = super.getTableCellRendererComponent(table, value, bln, bln1, row, col);
      comp.setBackground(tableColor);
      comp.setForeground(colors.m_fg_0);
      comp.setFont(FontManager.getFont("Nunito", 14f));
      setSelectionBackground(colors.m_bg_1);
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
      if (borders[0]) g2.drawLine(0, 0, getWidth(), 0); // Top border
      if (borders[1]) g2.drawLine(0, 0, 0, getHeight()); // Left border
      if (borders[2]) g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1); // Bottom border
      if (borders[3]) g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight()); // Right border

      g2.dispose();
    }
  }

  public ColorScheme colors;
  public Color tableColor;
  public Color borderColor;
  public Color headerCellColor;
  public Color headerCellTextColor;
  public boolean[] borders = new boolean[4];
}
