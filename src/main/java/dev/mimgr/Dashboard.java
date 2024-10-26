package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import dev.mimgr.theme.builtin.ColorScheme;

public class Dashboard extends JPanel {
  Dashboard(ColorScheme colors) {
    super();
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_dim);

    // Header
    HeaderPanel headerPanel = new HeaderPanel(colors);
    headerPanel.setPreferredSize(new Dimension(this.getWidth(), 60));
    this.add(headerPanel, BorderLayout.NORTH);

    // Sidebar
    // Sidebar also control the content panel
    SidebarPanel sidebarPanel = (SidebarPanel) PanelManager.get_panel("DASHBOARD_SIDEBAR");
    sidebarPanel.setPreferredSize(new Dimension(300, this.getHeight()));
    this.add(sidebarPanel, BorderLayout.WEST);

    // JButton button = new JButton("5");
    // c.fill = GridBagConstraints.HORIZONTAL;
    // c.ipady = 0;       //reset to default
    // c.weighty = 1.0;   //request any extra vertical space
    // c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    // c.insets = new Insets(10,0,0,0);  //top padding
    // c.gridx = 1;       //aligned with button 2
    // c.gridwidth = 2;   //2 columns wide
    // c.gridy = 2;       //third row
    // this.add(button, c);
  }
}
