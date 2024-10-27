package dev.mimgr;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.builtin.ColorScheme;

public class SidebarPanel extends JPanel implements ActionListener {
  SidebarPanel(ColorScheme colors) {
    this.colors = colors;

    setLayout(new GridBagLayout());
    setBackground(colors.m_bg_dim);

    int padding_horizontal = 15;
    int padding_vertical = 5;
    int icon_size = 16;

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

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.NORTH;
    c.fill = GridBagConstraints.HORIZONTAL;

    // Top section here
    c.insets = new Insets(20, padding_horizontal, 5, padding_horizontal);
    addMenuButton("Home", home_icon, "FORM 1");

    // Menu Buttons
    c.insets = new Insets(padding_vertical, padding_horizontal, padding_vertical, padding_horizontal);
    addComponent(sep);
    addMenuButton("Orders", orders_icon, "FORM_ORDER");
    addMenuButton("Products", products_icon, "FORM_PRODUCT");
    addMenuButton("Analytics", analytics_icon, "FORM_ANALYTIC");
    addMenuButton("Media", media_icon, "FORM_MEDIA");

    // Bottom section
    c.weighty = 1.0;
    c.anchor = GridBagConstraints.PAGE_END;
    addMenuButton("Account", accounts_icon, "FORM_ACCOUNT");

    c.weighty = 0.0;
    sep = new JSeparator();
    sep.setForeground(colors.m_bg_4);
    sep.setBackground(null);
    addMenuButton("Settings", settings_icon, "FORM_SETTING");
    addComponent(sep);

    c.insets = new Insets(padding_vertical, padding_horizontal, 20, padding_horizontal);
    log_out_button = setupMenuButton("Log out", logout_icon);
    this.add(log_out_button, c);
  }

  // Add button to the layout and map that button to a form
  private void addMenuButton(String text, Icon icon, String panelId) {
    MButton button = setupMenuButton(text, icon);
    this.add(button, c);
    button_to_form.put(button, panelId);
    c.gridy = c.gridy + 1;
  }

  private void addComponent(Component comp) {
    this.add(comp, c);
    c.gridy = c.gridy + 1;
  }

  private MButton setupMenuButton(String text, Icon icon) {
    MButton button = new MButton("   " + text, icon);
    button.setPadding(new Insets(5, 20, 5, 20));
    button.setFont(FontManager.getFont("NunitoBold", 16f));
    button.setForeground(colors.m_fg_0);
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBackground(null);
    button.setBorderColor(null);
    button.setHoverBackgroundColor(colors.m_bg_1);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (dashboard_panel == null) {
      dashboard_panel = PanelManager.get_panel("DASHBOARD");
    }

    if (e.getSource() == log_out_button) {
      PanelManager.get_main_panel().setCursor(new Cursor(Cursor.WAIT_CURSOR));
      PanelManager.register_panel(new FormLogin(colors), "FORM_LOGIN");
      PanelManager.register_panel(new FormSignUp(colors), "FORM_SIGNUP");
      PanelManager.unregister_panel("DASHBOARD");
      PanelManager.get_main_panel().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      PanelManager.show("FORM_LOGIN");
    }

    String panelId = button_to_form.get(e.getSource());
    if (panelId == null) return;

    System.out.println("formID: " + panelId);

    JPanel lastPanel = getContentPanel();
    if (lastPanel != null) {
      lastPanel.setVisible(false);
    }

    JPanel formPanel = PanelManager.get_panel(panelId);
    if (formPanel == null) {
      System.err.println("No panel associated with " + panelId);
      return;
    }

    if (pButton != null) {
      pButton.setBackground(null);
      pButton = (MButton) e.getSource();
    } else {
      pButton = (MButton) e.getSource();
    }

    pButton.setBackground(colors.m_bg_0);
    pButton.setHoverBackgroundColor(colors.m_bg_1);
    this.setContentPanel(formPanel);
  }

  private void setContentPanel(JPanel panel) {
    this.content_panel = panel;
    dashboard_panel.add(this.content_panel);
    this.content_panel.setVisible(true);
  }

  private JPanel getContentPanel() {
    return this.content_panel;
  }

  private JPanel dashboard_panel;
  private JScrollPane content_scroll_pane;
  private JPanel content_panel = null;
  private ColorScheme colors;
  private MButton log_out_button;
  private GridBagConstraints c;
  private MButton pButton = null;
  private Map<MButton, String> button_to_form = new HashMap<>();
}
