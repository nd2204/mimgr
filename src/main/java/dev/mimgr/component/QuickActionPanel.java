package dev.mimgr.component;

import javax.swing.Icon;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;

import dev.mimgr.Dashboard;
import dev.mimgr.FontManager;
import dev.mimgr.IconManager;
import dev.mimgr.PanelManager;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class QuickActionPanel extends RoundedPanel {
  public QuickActionPanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new GridBagLayout());
    this.setBorderRadius(15);
    this.setBorderWidth(1);
    this.setBackground(colors.m_bg_0);
    this.setBorderColor(colors.m_bg_1);

    this.setMinimumSize(new Dimension(100, 80));
    this.setPreferredSize(new Dimension(this.getPreferredSize().width, 80));
    this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

    final int icon_size = 16;
    Icon orders_icon    = IconManager.getIcon("shopping_bag.png", icon_size, icon_size, colors.m_fg_0);
    Icon products_icon  = IconManager.getIcon("tag.png", icon_size, icon_size, colors.m_fg_0);
    Icon analytics_icon = IconManager.getIcon("analytic.png", icon_size, icon_size, colors.m_fg_0);
    Icon accounts_icon  = IconManager.getIcon("account.png", icon_size, icon_size, colors.m_fg_0);
    Icon media_icon     = IconManager.getIcon("image.png", icon_size, icon_size, colors.m_fg_0);
    Icon refresh_icon   = IconManager.getIcon("Refresh.png", icon_size, icon_size, colors.m_fg_0);

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.0;

    BiConsumer<MButton, Integer> setup_button = (button, idx) -> {
      button.setBorderRadius(15);
      button.setBorderWidth(1);
      button.setPreferredSize(new Dimension(50, 50));
      button.setBackground(colors.m_bg_dim);
      button.setBorderColor(colors.m_bg_1);
      button.addActionListener((e) -> {
        if (idx < 0) return;
        JPanel panel = PanelManager.get_panel("DASHBOARD");
        if (panel instanceof Dashboard dashboard) {
          dashboard.setCurrentMenu(dashboard.menuButtons.get(idx));
        }
      });
    };

    btnOrder = new MButton(orders_icon);
    btnProduct = new MButton(products_icon);
    btnAnalytics = new MButton(analytics_icon);
    btnMedia = new MButton(media_icon);
    btnAccount = new MButton(accounts_icon);
    btnRefresh = new MButton(refresh_icon);

    setup_button.accept(btnOrder, 1);
    setup_button.accept(btnProduct, 2);
    setup_button.accept(btnAnalytics, 3);
    setup_button.accept(btnMedia, 4);
    setup_button.accept(btnAccount, 5);
    setup_button.accept(btnRefresh, -1);

    c.insets = new Insets(10, 10, 10, 0);
    this.add(btnRefresh, c);
    c.gridx++;

    c.insets = new Insets(0, 5, 0, 0);
    this.add(btnOrder, c);
    c.gridx++;

    this.add(btnProduct, c);
    c.gridx++;

    this.add(btnAnalytics, c);
    c.gridx++;

    this.add(btnMedia, c);
    c.gridx++;

    c.insets = new Insets(10, 5, 10, 10);
    this.add(btnAccount, c);
    c.gridx++;
  }

  private MButton btnOrder;
  private MButton btnProduct;
  private MButton btnAnalytics;
  private MButton btnMedia;
  private MButton btnAccount;
  public MButton btnRefresh;

  private ColorScheme colors;
}
