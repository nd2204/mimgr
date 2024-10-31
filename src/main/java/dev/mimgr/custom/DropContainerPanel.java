package dev.mimgr.custom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import dev.mimgr.FontManager;
import dev.mimgr.theme.builtin.ColorScheme;

public class DropContainerPanel extends JPanel implements ActionListener {
  public DropContainerPanel(ColorScheme colors) {
    thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    thisPanel.setBackground(colors.m_bg_0);
    thisPanel.setSize(0, 140);
    thisPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

    JScrollPane sp = new JScrollPane(
      thisPanel,
      JScrollPane.VERTICAL_SCROLLBAR_NEVER,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    // MScrollBar vsb = new MScrollBar(colors);
    // sp.setVerticalScrollBar(vsb);

    MScrollBar hsb = new MScrollBar(colors);
    hsb.setOrientation(MScrollBarUI.HORIZONTAL);
    sp.setHorizontalScrollBar(hsb);

    sp.setBorder(BorderFactory.createEmptyBorder());
    sp.setOpaque(false);

    this.colors = colors;
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_0);
    this.setVisible(false);
    this.add(sp, BorderLayout.CENTER);
  }

  public void addData(Object data) {
    if (!isVisible()) {
      this.setVisible(true);
    }
    if (isEmpty()) {
      confirmButton.setFont(FontManager.getFont("NunitoBold", 14f));
      confirmButton.setBackground(colors.m_bg_dim);
      confirmButton.setForeground(colors.m_bg_3);
      confirmButton.setBorderColor(colors.m_bg_3);
      confirmButton.setBorderWidth(2);
      confirmButton.setHoverBorderColor(colors.m_green);
      confirmButton.setHoverBackgroundColor(colors.m_green);
      confirmButton.setHoverForegroundColor(colors.m_fg_1);
      confirmButton.setPreferredSize(new Dimension(100, 100));
      confirmButton.setMaximumSize(new Dimension(100, 100));
      confirmButton.setMaximumSize(new Dimension(100, 100));
      confirmButton.addActionListener(this);
      thisPanel.add(confirmButton);
    }
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
      thisPanel.add(button);
    }

    System.out.println(dataMap);
    revalidate();
    repaint();
  }

  public void clearData() {
    for (MButton button : this.dataMap.keySet()) {
      thisPanel.remove(button);
    }
    this.dataMap.clear();
    this.setVisible(false);
    revalidate();
    repaint();
  }

  private void setup_button(MButton button) {
    button.setBackground(null);
    button.setForeground(colors.m_fg_0);
    button.setBorderWidth(2);
    button.setFont(FontManager.getFont("NunitoBold", 14f));
    button.setPreferredSize(new Dimension(100, 100));
    button.setMaximumSize(new Dimension(100, 100));
    button.setMaximumSize(new Dimension(100, 100));
    button.setHoverBorderColor(colors.m_red);
    button.setBorderColor(colors.m_bg_5);
    button.addActionListener(this);
  }

  public boolean isEmpty() {
    return dataMap.isEmpty();
  }

  public JButton getConfirmButton() {
    return confirmButton;
  }

  public List<Object> getAllData() {
    ArrayList<Object> dataList = new ArrayList<>();
    for (Object obj : dataMap.values()) {
      dataList.add(obj);
    }
    return dataList;
  }

  public void addActionListener(ActionListener al) {
    allist.add(al);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    MButton key = (MButton) e.getSource();
    Object data = dataMap.get(key);
    if (data != null) {
      dataMap.remove(key);
      thisPanel.remove(key);
    }
    if (this.isEmpty()) {
      this.setVisible(false);
    }
    for (ActionListener al : allist) {
      al.actionPerformed(e);
    }
    revalidate();
    repaint();
  }

  private ArrayList<ActionListener> allist = new ArrayList<>();
  private MButton confirmButton = new MButton("Confirm");
  private JPanel thisPanel = new JPanel();
  private ColorScheme colors;
  private HashMap<MButton, Object> dataMap = new HashMap<>();
}