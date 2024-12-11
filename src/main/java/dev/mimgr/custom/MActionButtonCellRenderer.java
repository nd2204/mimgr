package dev.mimgr.custom;

import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * @author nd2204
 */
public class MActionButtonCellRenderer extends JPanel implements TableCellRenderer {
  public MActionButtonCellRenderer(int orientation) {
    this.orientation = orientation;
  }

  public MActionButtonCellRenderer() {
    this(SwingConstants.CENTER);
  }
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    ActionPanel panel = new ActionPanel(orientation);
    if (value instanceof List list) {
      for (Object obj : list) {
        if (obj instanceof JButton button) {
          panel.addButton(button);
        }
      }
    } else if (value instanceof JButton button) {
      panel.addButton(button);
    }

    // Set the background color based on selection
    if (isSelected) {
      panel.setBackground(table.getSelectionBackground());
      panel.setOpaque(true);
    } else {
      panel.setBackground(table.getBackground());
      panel.setOpaque(false);
    }

    return panel;
  }
  private int orientation;
}



