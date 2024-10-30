package dev.mimgr.custom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import dev.mimgr.theme.builtin.ColorScheme;

public class MCheckBoxCellEditor extends DefaultCellEditor{
  public MCheckBoxCellEditor(CustomCheckBox checkbox, ColorScheme colors) {
    super(checkbox);
    this.customCheckBox = checkbox;
    // Add custom behavior if needed, e.g., changing appearance on selection
    customCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
      }
    });
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    this.customCheckBox.setSelected(Boolean.TRUE.equals(value));
    if (isSelected) {
      this.customCheckBox.setBackground(table.getSelectionBackground());
    } else {
      this.customCheckBox.setBackground(table.getBackground());
    }
    return this.customCheckBox;
  }

  @Override
  public Object getCellEditorValue() {
    return this.customCheckBox.isSelected();
  }

  public static class CustomCheckBox extends MCheckBox {
    public CustomCheckBox(MCheckBox checkbox, ColorScheme colors) {
      super(checkbox);
      CustomCheckBox.this.colors = colors;
      this.setBoxWidth(this.getBoxWidth() - 1);
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setColor(CustomCheckBox.this.colors.m_bg_3);
      g2.drawLine(0, getHeight(), getWidth(), getHeight());
      g2.dispose();
    }
    private ColorScheme colors;
  }

  private CustomCheckBox customCheckBox;
}
