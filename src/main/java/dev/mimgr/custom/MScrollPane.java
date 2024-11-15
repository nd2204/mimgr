package dev.mimgr.custom;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class MScrollPane extends JScrollPane {

  public MScrollPane() {
    super();
    this.setOpaque(false);
    Init();
  }

  public void Init() {
    MScrollBar hsb = new MScrollBar();
    hsb.setOrientation(MScrollBar.HORIZONTAL);
    this.setHorizontalScrollBar(hsb);
    MScrollBar vsb = new MScrollBar();
    this.setVerticalScrollBar(vsb);

    this.setBorder(BorderFactory.createEmptyBorder());
  }
}
