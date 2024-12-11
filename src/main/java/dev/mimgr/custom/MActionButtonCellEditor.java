package dev.mimgr.custom;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.DefaultCellEditor;

public class MActionButtonCellEditor extends DefaultCellEditor {
  public MActionButtonCellEditor() {
    super(new JCheckBox());
  }

  public MActionButtonCellEditor(int orientation) {
    super(new JCheckBox());
    this.orientation = orientation;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  {
    ActionPanel panel = new ActionPanel(orientation);
    if (value instanceof List<?> list) {
      for (Object obj : list) {
        if (obj instanceof JButton button) {
          panel.addButton(button);
          buttons.add(button);
          button.addActionListener((e) -> fireEditingStopped());
        }
      }
    } else if (value instanceof JButton button) {
      panel.addButton(button);
      buttons.add(button);
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


  @Override
  public Object getCellEditorValue() {
    List<JButton> buttons_copy = new ArrayList<>(buttons);
    buttons.clear();
    return buttons_copy;
  }

  private List<JButton> buttons = new ArrayList<>();
  private int orientation = SwingConstants.CENTER;
}



