package dev.mimgr.custom;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import dev.mimgr.component.*;

public class MNumberInputCellRenderer extends JPanel implements TableCellRenderer {
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    panel = new NumberInputPanel();

    if (value instanceof String s) {
      if (s.isEmpty() || s.isBlank() || s == null) {
        panel.setValue(1);
      } else {
        panel.setValue(Integer.parseInt(s));
      }
    }

    // Set the background color based on selection
    if (isSelected) {
      panel.setBackground(table.getSelectionBackground());
      panel.setOpaque(true);
      panel.setVisible(true);
    } else {
      panel.setBackground(table.getBackground());
      panel.setOpaque(false);
      panel.setVisible(false);
    }

    return panel;
  }

  private NumberInputPanel panel;
}




