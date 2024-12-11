package dev.mimgr.custom;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.theme.ColorTheme;


public class MCheckBoxHeader extends JPanel implements TableCellRenderer {
  public MCheckBoxHeader(MTable table, int checkBoxColumnIdx) {
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.selectAllCheckBox = new MCheckBox();
    this.selectAllCheckBox.setBorder(null);
    this.selectAllCheckBox.setBorderPainted(false);
    this.selectAllCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    // Add listener to update the column checkboxes
    selectAllCheckBox.addActionListener(e -> {
      boolean isSelected = selectAllCheckBox.isSelected();
      for (int row = 0; row < table.getRowCount(); row++) {
        table.setValueAt(isSelected, row, checkBoxColumnIdx);
      }
    });
    // Add mouse listener to the header so that the checkbox is clickable
    if (table.getTableHeader() != null) {
      table.getTableHeader().addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          // Get the column at the mouse click position
          int column = table.columnAtPoint(e.getPoint());
          if (column == checkBoxColumnIdx) {
            selectAllCheckBox.setSelected(!selectAllCheckBox.isSelected());
            selectAllCheckBox.getActionListeners()[0].actionPerformed(null);
            table.getTableHeader().repaint();
          }
        }
      });
    }
    this.add(selectAllCheckBox);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    this.setBackground(colors.m_bg_dim);
    this.setOpaque(true);
    return this;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw the border using the specified color
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(colors.m_bg_3);
    g2.drawLine(0, 0, getWidth(), 0);
    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

    g2.dispose();
  }

  public MCheckBox getCheckBoxComponent() {
    return this.selectAllCheckBox;
  }

  private ColorScheme colors;
  private final MCheckBox selectAllCheckBox;
}

