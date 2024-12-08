package dev.mimgr.custom;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import dev.mimgr.component.NumberInputPanel;

public class MNumberInputCellEditor extends AbstractCellEditor implements TableCellEditor {
  @Override
  public Object getCellEditorValue() {
    return panel.tfNumber.getTextString();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  {
    panel = new NumberInputPanel();
    panel.tfNumber.addActionListener((e) -> { stopCellEditing(); });
    panel.minus.addActionListener(new NumberModifier(i -> i - 1));
    panel.plus.addActionListener(new NumberModifier(i -> i + 1));

    if (value instanceof String s) {
      if (!(s.isEmpty() || s.isBlank() || s == null)) {
        panel.setValue(Integer.parseInt(s));
      }
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

  private class NumberModifier implements ActionListener {
    public NumberModifier(Function<Integer, Integer> modifier) {
      this.modifier = modifier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      int value = panel.getValue();
      int modified_value = modifier.apply(value);
      panel.setValue(modified_value);
      stopCellEditing();
    }
    private Function<Integer, Integer> modifier;
  }

  private NumberInputPanel panel;
}




