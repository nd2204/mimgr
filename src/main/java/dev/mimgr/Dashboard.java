package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.builtin.ColorScheme;

public class Dashboard extends JPanel {
  Dashboard(ColorScheme colors) {
    super();
    this.colors = colors;
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_dim);

    // Header
    HeaderPanel headerPanel = new HeaderPanel(colors);
    headerPanel.setPreferredSize(new Dimension(this.getWidth(), 65));
    btnToggleSidebar = headerPanel.getToggleSidebarComponent();
    btnToggleSidebar.addActionListener(new HeaderBarListener());
    this.add(headerPanel, BorderLayout.NORTH);

    // Content Panel
    JPanel contentPanel = new JPanel();
    this.add(contentPanel, BorderLayout.CENTER);

    // ContentPanel controller
    sidebarPanel = new SidebarPanel(contentPanel, colors);
    sidebarPanel.setPreferredSize(new Dimension(300, this.getHeight()));
    this.add(sidebarPanel, BorderLayout.WEST);
    {
      final int padding_horizontal = 15;
      final int padding_vertical = 5;
      final int icon_size = 16;
      MButton firstButton;
      Icon home_icon      = IconManager.getIcon("home_1.png", icon_size, icon_size, colors.m_fg_0);
      Icon orders_icon    = IconManager.getIcon("shopping_bag.png", icon_size, icon_size, colors.m_fg_0);
      Icon products_icon  = IconManager.getIcon("tag.png", icon_size, icon_size, colors.m_fg_0);
      Icon analytics_icon = IconManager.getIcon("analytic.png", icon_size, icon_size, colors.m_fg_0);
      Icon accounts_icon  = IconManager.getIcon("account.png", icon_size, icon_size, colors.m_fg_0);
      Icon settings_icon  = IconManager.getIcon("cog.png", icon_size, icon_size, colors.m_fg_0);
      Icon logout_icon    = IconManager.getIcon("export.png", icon_size, icon_size, colors.m_fg_0);
      Icon media_icon     = IconManager.getIcon("image.png", icon_size, icon_size, colors.m_fg_0);

      JSeparator sep = new JSeparator();
      sep.setForeground(colors.m_bg_4);
      sep.setBackground(null);

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.anchor = GridBagConstraints.NORTH;
      c.fill = GridBagConstraints.HORIZONTAL;

      // Top section here
      c.insets = new Insets(20, padding_horizontal, 5, padding_horizontal);
      sidebarPanel.addMenuButton("Home", home_icon, null, c);

      // Menu Buttons
      c.insets = new Insets(padding_vertical, padding_horizontal, padding_vertical, padding_horizontal);
      sidebarPanel.addComponent(sep, c);
      sidebarPanel.addMenuButton("Orders", orders_icon, new FormOrder(colors), c);
      firstButton = sidebarPanel.addMenuButton("Products", products_icon, new FormProduct(), c);
      sidebarPanel.addMenuButton("Analytics", analytics_icon, new FormAnalytic(), c);
      sidebarPanel.addMenuButton("Media", media_icon, new FormMedia(colors), c);

      // Bottom section
      c.weighty = 1.0;
      c.anchor = GridBagConstraints.PAGE_END;
      sidebarPanel.addMenuButton("Account", accounts_icon, new FormAccount(colors), c);

      c.weighty = 0.0;
      sep = new JSeparator();
      sep.setForeground(colors.m_bg_4);
      sep.setBackground(null);
      sidebarPanel.addMenuButton("Settings", settings_icon, null, c);
      sidebarPanel.addComponent(sep, c);

      c.insets = new Insets(padding_vertical, padding_horizontal, 20, padding_horizontal);
      btnLogOut = sidebarPanel.setupMenuButton("Log out", logout_icon);
      sidebarPanel.add(btnLogOut, c);

      SidebarMenuListener sml = new SidebarMenuListener();
      btnLogOut.addActionListener(sml);

      sidebarPanel.setCurrentMenu(firstButton);
    }
  }

  private class SidebarMenuListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnLogOut) {
        PanelManager.get_main_panel().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Entry.registerLoginSignup(colors);
        Entry.removeDashBoard();
        PanelManager.get_main_panel().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        PanelManager.show("FORM_LOGIN");
        LoginService.logoutUser();
      }
    }
  }

  private class HeaderBarListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnToggleSidebar) {
        sidebarPanel.setVisible(!sidebarPanel.isVisible());
      }
    }
  }

  private SidebarPanel sidebarPanel;
  private ColorScheme colors; 
  private MButton btnToggleSidebar;
  private MButton btnLogOut;
}
