package dev.mimgr;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.builtin.ColorScheme;

public class HeaderPanel extends JPanel {
  HeaderPanel(ColorScheme colors) {
    this.setBackground(colors.m_bg_0);
    this.setLayout(new GridBagLayout());

    btnToggleSidebar = new MButton(IconManager.getIcon("tripple_dash.png", 20, 18, colors.m_grey_0));
    btnToggleSidebar.setBackground(null);
    btnToggleSidebar.setBorderWidth(0);
    btnToggleSidebar.setBorderColor(null);
    btnToggleSidebar.setOpaque(false);
    btnToggleSidebar.setBorderPainted(false);
    btnToggleSidebar.setAlignmentX(SwingConstants.LEFT);

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(20, 10, 20, 20);
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;

    this.add(btnToggleSidebar, c);

    c.gridx = 1;
    c.weightx = 1.0;
    this.add(Box.createVerticalGlue(), c);
  }

  public MButton getToggleSidebarComponent() {
    return this.btnToggleSidebar;
  }

  MButton btnToggleSidebar;
}
