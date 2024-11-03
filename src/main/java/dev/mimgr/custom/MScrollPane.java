package dev.mimgr.custom;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import dev.mimgr.theme.builtin.ColorScheme;

public class MScrollPane extends JScrollPane {

  public MScrollPane(ColorScheme colors) {
    super();
    this.colors = colors;
    this.setOpaque(false);
    Init();
  }

  public void Init() {
    MScrollBar hsb = new MScrollBar(colors);
    hsb.setOrientation(MScrollBar.HORIZONTAL);
    this.setHorizontalScrollBar(hsb);
    MScrollBar vsb = new MScrollBar(colors);
    this.setVerticalScrollBar(vsb);

    this.setBorder(BorderFactory.createEmptyBorder());
  }

  private ColorScheme colors;
}
