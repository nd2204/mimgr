package dev.mimgr;

import javax.swing.JPanel;

import dev.mimgr.theme.builtin.ColorScheme;

public class FormOrder extends JPanel {
  public FormOrder(ColorScheme colors) {
    this.colors = colors;
    this.setBackground(this.colors.m_bg_0);
  }

  private ColorScheme colors;
}
