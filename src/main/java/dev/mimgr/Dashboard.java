package dev.mimgr;

import java.awt.Color;

import javax.swing.JPanel;

import dev.mimgr.theme.builtin.ColorScheme;

public class Dashboard extends JPanel {
  Dashboard(ColorScheme colors) {
    Init();
  }

  private void Init() {
    this.setVisible(false);
  }
}
