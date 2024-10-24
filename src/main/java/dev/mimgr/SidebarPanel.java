package dev.mimgr;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.builtin.ColorScheme;

public class SidebarPanel extends JPanel implements ActionListener{
  SidebarPanel(ColorScheme colors) {
    this.colors = colors;
    setLayout(new GridBagLayout());
    setBackground(colors.m_bg_dim);

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
    c.insets = new Insets(20, 10, 10, 10);

    // Menu Buttons
    c.insets = new Insets(5, 10, 5, 10);
    this.add(sep, c);
    addMenuButton("Home",      IconManager.getIcon("home.png"        , 16, 16, colors.m_fg_0), "FORM 1");
    addMenuButton("Orders",    IconManager.getIcon("shopping_bag.png", 16, 16, colors.m_fg_0), "FORM 2");
    addMenuButton("Products",  IconManager.getIcon("tag.png"         , 16, 16, colors.m_fg_0), "FORM 3");
    addMenuButton("Analytics", IconManager.getIcon("analytic.png"    , 16, 16, colors.m_fg_0), "FORM 4");

    // Bottom section
    c.weighty = 1.0;
    c.anchor = GridBagConstraints.PAGE_END;
    this.add(sep, c);

    c.gridy += 1;
    c.weighty = 0.0;
    c.insets = new Insets(5, 10, 20, 10);
    log_out_button = setupMenuButton("Log out", IconManager.getIcon("export.png", 16, 16, colors.m_fg_0));
    this.add(log_out_button, c);
  }

  // Add button to the layout and map that button to a form
  private void addMenuButton(String text, Icon icon, String formId) {
    MButton button = setupMenuButton(text, icon);
    this.add(button, c);
    button_to_form.put(button, formId);
    c.gridy = c.gridy + 1;
  }

  private MButton setupMenuButton(String text, Icon icon) {
    MButton button = new MButton("   " + text, icon);
    button.setFont(FontManager.getFont("NunitoBold", 16f));
    button.setForeground(colors.m_fg_0);
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBackground(null);
    button.setBorderColor(null);
    button.setHoverBackgroundColor(colors.m_bg_4);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == log_out_button) {
      PanelManager.get_main_panel().setCursor(new Cursor(Cursor.WAIT_CURSOR));
      PanelManager.register_panel(new FormLogin(colors), "FORM_LOGIN");
      PanelManager.register_panel(new FormSignUp(colors), "FORM_SIGNUP");
      PanelManager.unregister_panel("DASHBOARD");
      PanelManager.get_main_panel().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      PanelManager.show("FORM_LOGIN");
    }

    String formId = button_to_form.get(e.getSource());
    if (formId != null) {
      System.out.println("formID:" + formId);
      if (pButton != null) {
        pButton.setBackground(null);
        pButton = (MButton) e.getSource();
      } else {
        pButton = (MButton) e.getSource();
        pButton.setBackground(colors.m_bg_2);
      }
    }
  }

  private ColorScheme colors;
  private MButton log_out_button;
  private GridBagConstraints c;
  private MButton pButton = null;
  private Map<MButton, String> button_to_form = new HashMap<>();
}
