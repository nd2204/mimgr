package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

/**
 * @author nd2204
 */
public class Dashboard extends JPanel {
  Dashboard() {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_dim);

    // Header
    headerPanel = new HeaderPanel();
    headerPanel.setPreferredSize(new Dimension(this.getWidth(), 65));
    btnToggleSidebar = headerPanel.getToggleSidebarComponent();
    btnToggleSidebar.addActionListener(new HeaderBarListener());
    this.add(headerPanel, BorderLayout.NORTH);

    // Content Panel
    JPanel contentPanel = new JPanel();
    this.add(contentPanel, BorderLayout.CENTER);

    // ContentPanel controller
    sidebarPanel = new SidebarPanel(contentPanel);
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
      menuButtons.add(sidebarPanel.addMenuButton("Home", home_icon, new FormHome(), c));

      // Menu Buttons
      c.insets = new Insets(padding_vertical, padding_horizontal, padding_vertical, padding_horizontal);
      sidebarPanel.addComponent(sep, c);
      menuButtons.add(sidebarPanel.addMenuButton("Orders", orders_icon, new FormOrder(), c));
      menuButtons.add(sidebarPanel.addMenuButton("Products", products_icon, new FormProduct(), c));
      menuButtons.add(sidebarPanel.addMenuButton("Analytics", analytics_icon, new FormAnalytic(), c));
      menuButtons.add(sidebarPanel.addMenuButton("Media", media_icon, new FormMedia(), c));

      // Bottom section
      c.weighty = 1.0;
      c.anchor = GridBagConstraints.PAGE_END;
      formAccount = new FormAccount();
      menuButtons.add(sidebarPanel.addMenuButton("Account", accounts_icon, formAccount, c));
      setButtonRefreshOnClick(formAccount.getUpdateProfileButton());

      c.weighty = 0.0;
      sep = new JSeparator();
      sep.setForeground(colors.m_bg_4);
      sep.setBackground(null);
      // sidebarPanel.addMenuButton("Settings", settings_icon, null, c);
      sidebarPanel.addComponent(sep, c);

      c.insets = new Insets(padding_vertical, padding_horizontal, 20, padding_horizontal);
      btnLogOut = sidebarPanel.setupMenuButton("Log out", logout_icon);
      menuButtons.add(btnLogOut);
      sidebarPanel.add(btnLogOut, c);

      SidebarMenuListener sml = new SidebarMenuListener();
      btnLogOut.addActionListener(sml);

      sidebarPanel.setCurrentMenu(menuButtons.get(0));
    }
  }

  private class SidebarMenuListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnLogOut) {
        PanelManager.get_main_panel().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Entry.registerLoginSignup();
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

  public JButton getSidebarButton(int idx) {
    return menuButtons.get(idx);
  }

  public void setButtonRefreshOnClick(MButton btn) {
    btn.addActionListener((actionEvent) -> {
      new Thread(() -> {
        SwingUtilities.invokeLater(() -> {
          headerPanel.refresh(formAccount.getUsername(), formAccount.getRole());
        });
      }).start();
    });
  }

  public void setCurrentMenu(MButton button) {
    sidebarPanel.setCurrentMenu(button);
  }

  public ArrayList<MButton> menuButtons = new ArrayList<>();
  private SidebarPanel sidebarPanel;
  private ColorScheme colors; 
  private MButton btnToggleSidebar;
  private MButton btnLogOut;
  private FormAccount formAccount;
  private HeaderPanel headerPanel;
}
