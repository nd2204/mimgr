package dev.mimgr;

import java.awt.CardLayout;
import java.awt.Component;
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
import javax.swing.SwingConstants;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.builtin.ColorScheme;

/**
 *
 * @author dn200
 */
public class SidebarPanel extends JPanel implements ActionListener {
  SidebarPanel(JPanel contentPanel, ColorScheme colors) {
    this.colors = colors;
    this.contentPanel = contentPanel;
    this.contentPanelSwitcher = new CardLayout();
    this.contentPanel.setLayout(contentPanelSwitcher);

    setLayout(new GridBagLayout());
    setBackground(colors.m_bg_dim);
  }

  // Add button to the layout and map that button to a form
  public void addMenuButton(String text, Icon icon, JPanel panel, GridBagConstraints c) {
    MButton button = setupMenuButton(text, icon);
    this.add(button, c);
    buttonToPanel.put(button, panel);
    if (panel != null) {
      this.contentPanel.add(panel, String.valueOf(button.hashCode()));
    }
    c.gridy = c.gridy + 1;
  }

  public void addComponent(Component comp, GridBagConstraints c) {
    this.add(comp, c);
    c.gridy = c.gridy + 1;
  }

  public MButton setupMenuButton(String text, Icon icon) {
    MButton button;
    if (icon == null) {
      button = new MButton("   " + text);
    } else {
      button = new MButton("   " + text, icon);
    }

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
    // Skip if the pressed menu is the current menu

    if (e.getSource() instanceof MButton sourceButton) {
      if (sourceButton == pButton) {
        return;
      }

      JPanel formPanel = buttonToPanel.get(sourceButton);

      if (formPanel == null) {
        System.err.println("No panel associated with " + sourceButton.getName());
        pButton = null;
        return;
      }

      if (pButton != null) {
        pButton.setBackground(null);
      }
      pButton = sourceButton;
      pButton.setBackground(colors.m_bg_0);
      pButton.setHoverBackgroundColor(colors.m_bg_1);
      setCurrentMenu(e.getSource().hashCode());
    }
  }

  public void setCurrentMenu(int buttonHashCode) {
    this.contentPanelSwitcher.show(this.contentPanel, String.valueOf(buttonHashCode));
  }

  public void removeMenuButton(MButton button) {
    this.contentPanel.remove(buttonToPanel.get(button));
    this.remove(button);
  }

  public MButton[] getAllMenuButtons() {
    return this.buttonToPanel
      .keySet()
      .toArray(new MButton[this.buttonToPanel.keySet().size()]);
  }

  private CardLayout contentPanelSwitcher;
  private JPanel contentPanel;
  private JScrollPane contentScrollPane;
  private ColorScheme colors;
  private MButton pButton = null;
  private Map<MButton, JPanel> buttonToPanel = new HashMap<>();
}
