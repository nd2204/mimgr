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
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

/**
 * @author nd2204
 */
public class SidebarPanel extends RoundedPanel implements ActionListener {
  SidebarPanel(JPanel contentPanel) {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.contentPanel = contentPanel;
    this.contentPanelSwitcher = new CardLayout();
    this.contentPanel.setLayout(contentPanelSwitcher);
    // this.thisPanel = new JPanel();
    this.setupButtonStyle = (button) -> this.setDefaultButtonStyle(button);
    this.setupSelectedButtonStyle = (button) -> this.setDefaultSelectedButtonStyle(button);

    setLayout(new GridBagLayout());
    setBackground(colors.m_bg_dim);
  }

  // Add button to the layout and map that button to a form
  public MButton addMenuButton(String text, Icon icon, JPanel panel, GridBagConstraints c) {
    MButton button = setupMenuButton(text, icon);
    this.add(button, c);
    buttonToPanel.put(button, panel);
    if (panel != null) {
      this.contentPanel.add(panel, String.valueOf(button.hashCode()));
    }
    c.gridy++;
    return button;
  }

  // Same as addMenuButton but didn't add to layout
  public MButton addDetachedButton(String text, Icon icon, JPanel panel) {
    MButton button = setupMenuButton(text, icon);
    buttonToPanel.put(button, panel);
    if (panel != null) {
      this.contentPanel.add(panel, String.valueOf(button.hashCode()));
    }
    return button;
  }

  public void addComponent(Component comp, GridBagConstraints c) {
    this.add(comp, c);
    c.gridy++;
  }

  public MButton setupMenuButton(String text, Icon icon) {
    MButton button;
    if (icon == null) {
      button = new MButton("   " + text);
    } else {
      button = new MButton("   " + text, icon);
    }
    this.setupButtonStyle.accept(button);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Skip if the pressed menu is the current menu

    if (e.getSource() instanceof MButton sourceButton) {
      JPanel formPanel = buttonToPanel.get(sourceButton);
      if (formPanel == null) {
        System.err.println("No panel associated with " + sourceButton.getName());
        pButton = null;
        return;
      }
      setCurrentMenu(sourceButton);
    }
  }

  public void setCurrentMenu(MButton button) {
    // Set old button background transparent
    if (this.pButton != null) {
      this.setupButtonStyle.accept(this.pButton);
    }
    this.pButton = button;
    this.setupSelectedButtonStyle.accept(this.pButton);
    this.contentPanelSwitcher.show(this.contentPanel, String.valueOf(button.hashCode()));
  }

  private void setDefaultButtonStyle(MButton button) {
    button.setPadding(new Insets(5, 20, 5, 20));
    button.setFont(FontManager.getFont("NunitoBold", 16f));
    button.setForeground(colors.m_fg_0);
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBackground(null);
    button.setBorderColor(null);
    button.setHoverBackgroundColor(colors.m_bg_1);
  }

  private void setDefaultSelectedButtonStyle(MButton button) {
    button.setPadding(new Insets(5, 20, 5, 20));
    button.setFont(FontManager.getFont("NunitoBold", 16f));
    button.setForeground(colors.m_fg_0);
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderColor(null);
    button.setBackground(colors.m_bg_0);
    button.setHoverBackgroundColor(colors.m_bg_1);
  }

  public void removeMenuButton(MButton button) {
    this.contentPanel.remove(buttonToPanel.get(button));
    this.buttonToPanel.remove(button);
    if (!buttonToPanel.isEmpty()) {
      setCurrentMenu(buttonToPanel.keySet().iterator().next());
    }
    button.setVisible(false);
    revalidate();
    repaint();
  }

  public MButton[] getAllMenuButtons() {
    return this.buttonToPanel
      .keySet()
      .toArray(new MButton[this.buttonToPanel.keySet().size()]);
  }

  public int getMenuButtonCount() {
    return this.buttonToPanel.size();
  }

  public Consumer<MButton> setupButtonStyle = null;
  public Consumer<MButton> setupSelectedButtonStyle = null;

  private CardLayout contentPanelSwitcher;
  private JPanel contentPanel;
  private ColorScheme colors;
  private MButton pButton = null;
  private Map<MButton, JPanel> buttonToPanel = new HashMap<>();
}
