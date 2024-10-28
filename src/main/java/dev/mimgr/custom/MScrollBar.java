package dev.mimgr.custom;

import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JScrollBar;
import dev.mimgr.theme.builtin.ColorScheme;


// Custom ScrollBar
public class MScrollBar extends JScrollBar {
  public MScrollBar(ColorScheme colors) {
    this.colors = colors;
    setUI(new MScrollBarUI());
    setPreferredSize(new Dimension(5, 5));
    setForeground(this.colors.m_grey_0);
    setUnitIncrement(20);
    setOpaque(false);
    setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  private ColorScheme colors;
}
