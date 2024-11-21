package dev.mimgr.custom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import dev.mimgr.FontManager;
import dev.mimgr.IconManager;
import dev.mimgr.theme.builtin.ColorScheme;

public class DropContainerPanel extends JPanel implements ActionListener {
  public DropContainerPanel(ColorScheme colors) {
    thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    thisPanel.setBackground(colors.m_bg_0);
    thisPanel.setSize(0, 140);
    thisPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

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

    scrollPane = new JScrollPane(
      thisPanel,
      JScrollPane.VERTICAL_SCROLLBAR_NEVER,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    MScrollBar hsb = new MScrollBar();
    hsb.setOrientation(MScrollBarUI.HORIZONTAL);
    scrollPane.setHorizontalScrollBar(hsb);

    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setOpaque(false);

    this.colors = colors;
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_0);
    this.setVisible(false);
    this.add(scrollPane, BorderLayout.CENTER);
  }

  public void addData(Object data) {
    this.setVisible(true);
    if (isEmpty()) {
      thisPanel.add(confirmButton);
    }

    MButton button = null;
    if (data instanceof File file) {
      button = createButton(IconManager.loadIcon(file));
    }
    else if (data instanceof String s) {
      button = createButton(null);
      button.setText(s);
    }
    else if (data instanceof ImageIcon icon) {
      button = createButton(icon);
    }
    else if (data instanceof Path path) {
      button = createButton(IconManager.loadIcon(path.toFile()));
    }

    if (button != null) {
      setup_button(button);
      stagedData.put(button, data);
      thisPanel.add(button);
    }

    System.out.println(stagedData);
    revalidate();
    repaint();
  }

  public void clearData() {
    for (MButton button : this.stagedData.keySet()) {
      thisPanel.remove(button);
    }
    this.stagedData.clear();
    this.setVisible(false);
    revalidate();
    repaint();
  }

  public boolean isEmpty() {
    return stagedData.isEmpty();
  }

  public MButton getConfirmButton() {
    return confirmButton;
  }

  public List<Object> getAllData() {
    ArrayList<Object> dataList = new ArrayList<>();
    for (Object obj : stagedData.values()) {
      dataList.add(obj);
    }
    return dataList;
  }

  public void addActionListener(ActionListener al) {
    allist.add(al);
  }

  public JScrollPane getScrollPaneComponent() {
    return this.scrollPane;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    MButton key = (MButton) e.getSource();
    Object data = stagedData.get(key);
    if (data != null) {
      stagedData.remove(key);
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

  public static MButton createButton(Icon icon) {
    MButton button = new MButton();
    button.setBorderWidth(3);
    if (icon != null) {
      int size = 100 - button.getBorderWidth() * 2;
      button.setIcon(
        IconManager.getRoundedIcon(
          IconManager.resizeByAspectRatio(icon, size, size),
          16
        )
      );
    }
    button.setBorderRadius(16);
    button.setFont(FontManager.getFont("NunitoBold", 14f));
    button.setPreferredSize(new Dimension(100, 100));
    button.setMaximumSize(new Dimension(100, 100));
    button.setMaximumSize(new Dimension(100, 100));
    return button;
  }

  private void setup_button(MButton button) {
    button.addActionListener(this);
    button.setBackground(colors.m_bg_dim);
    button.setForeground(colors.m_fg_0);
    button.setHoverBorderColor(colors.m_red);
    button.setBorderColor(colors.m_bg_5);
  }

  private JScrollPane scrollPane;
  private ArrayList<ActionListener> allist = new ArrayList<>();
  private MButton confirmButton = new MButton("Confirm");
  private JPanel thisPanel = new JPanel();
  private ColorScheme colors;
  private HashMap<MButton, Object> stagedData = new HashMap<>();
}
