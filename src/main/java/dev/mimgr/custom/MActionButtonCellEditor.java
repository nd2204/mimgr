package dev.mimgr.custom;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.DefaultCellEditor;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MActionButtonCellEditor extends DefaultCellEditor {
  public MActionButtonCellEditor() {
    super(new JCheckBox());
    this.colors = ColorTheme.getInstance().getCurrentScheme();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  {
    ActionPanel panel = new ActionPanel();
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

  private class ActionPanel extends JPanel {
    public ActionPanel() {
      this.setLayout(new GridBagLayout());
      c.gridx = 0;
      c.gridy = 0;
      c.insets = new Insets(0, 0, 0, 5);
    }

    public void addButton(JButton button) {
      this.add(button, c);
      c.gridx++;
    }

    GridBagConstraints c = new GridBagConstraints();

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2 = (Graphics2D) g.create();
      g2.setColor(colors.m_bg_3);
      g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

      g2.dispose();
    }
  }

  @Override
  public Object getCellEditorValue() {
    List<JButton> buttons_copy = new ArrayList<>(buttons);
    buttons.clear();
    return buttons_copy;
  }

  private ColorScheme colors;
  private List<JButton> buttons = new ArrayList<>();
}



