package dev.mimgr.custom;

import java.awt.FlowLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import dev.mimgr.theme.builtin.ColorScheme;

public class DropContainerPanel extends JPanel implements ActionListener {
  public DropContainerPanel(ColorScheme colors) {
    this.setLayout(new FlowLayout());
  }

  public void addData(Object data) {
    MButton button = null;
    if (data instanceof String) {
      button = new MButton((String) data);
    }
    else if (data instanceof ImageIcon) {
      button = new MButton((ImageIcon) data);
    }
    else if (data instanceof File) {
    }
    else if (data instanceof List) {
      List<?> dataList = (List<?>) data;
      for (Object d : dataList) {
        if (d instanceof File) {
          addData((File) d);
        }
      }
    }
    if (button != null) {
      setup_button(button);
      dataMap.put(button, data);
      this.add(button);
    }
  }

  public void addImageData(ImageIcon data) {
  }

  private void setup_button(MButton button) {
    button.setBackground(null);
    button.setForeground(colors.m_fg_0);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    MButton key = (MButton) e.getSource();
    Object data = dataMap.get(key);
    if (data != null) {
      dataMap.remove(key);
    }
  }

  private ColorScheme colors;
  private HashMap<MButton, Object> dataMap = new HashMap<>();
}
