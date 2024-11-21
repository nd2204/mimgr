package dev.mimgr.custom;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import dev.mimgr.FontManager;
import dev.mimgr.theme.ColorTheme;
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

  public MTable() {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.borderColor = colors.m_bg_3;
    this.headerCellColor = colors.m_bg_dim;
    this.headerCellTextColor = colors.m_grey_0;
    this.tableColor = colors.m_bg_0;
    borders[0] = borders[1] = borders[2] = borders[3] = false;
    this.borders[2] = true;

    this.getTableHeader().setDefaultRenderer(new MTableHeaderRenderer());
    this.getTableHeader().setPreferredSize(new Dimension(0, 45));
    this.setShowGrid(false);
    this.setIntercellSpacing(new Dimension(0, 0));
    this.setBorder(null);
    this.setDefaultRenderer(Object.class, new TableCell());
    this.setGridColor(tableColor);
    this.setBackground(tableColor);
    this.setSelectionBackground(colors.m_bg_1);
    setRowHeight(60);
  }

  public void setup_scrollbar(JScrollPane sp) {
    sp.setVerticalScrollBar(new MScrollBar());
    sp.setHorizontalScrollBar(new MScrollBar());
    JPanel panel = new JPanel();
    panel.setBackground(headerCellColor);
    sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);
  }

  public class TableCell extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean bln1, int row, int col) {
      Component comp = super.getTableCellRendererComponent(table, value, isSelected, bln1, row, col);
      borders[0] = false;
      borders[2] = true;
      comp.setBackground(tableColor);
      comp.setForeground(colors.m_fg_0);
      comp.setFont(FontManager.getFont("NunitoBold", 14f));
      setBorder(new EmptyBorder(0, 5, 0, 5));

      if (isSelected) {
        this.setBackground(table.getSelectionBackground());
        this.setOpaque(true);
      } else {
        this.setBackground(table.getBackground());
        this.setOpaque(false);
      }

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
