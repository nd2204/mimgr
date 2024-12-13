package dev.mimgr;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.HierarchyEvent;

import dev.mimgr.component.OrderNotificationTableView;
import dev.mimgr.component.QuickActionPanel;
import dev.mimgr.component.TopProductPanel;
import dev.mimgr.component.TotalOrderPanel;
import dev.mimgr.component.TotalSalePanel;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers;
import dev.shader.BuiltinShaders.TriLatticeShader;

public class FormHome extends JPanel {
  public FormHome() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(this.colors.m_bg_dim);

    topProductPanel = new TopProductPanel();
    greetPanel = new GreetPanel();
    totalOrderPanel = new TotalOrderPanel();
    totalSalePanel = new TotalSalePanel();
    quickActionPanel = new QuickActionPanel();
    orderNotification = new OrderNotification();

    this.setLayout(new BorderLayout());
    this.add(new LeftPanel(), BorderLayout.CENTER);
    this.add(new RightPanel(), BorderLayout.EAST);

    quickActionPanel.btnRefresh.addActionListener((ev) -> {
      topProductPanel.refresh();
      greetPanel.refresh();
      totalOrderPanel.refresh();
      totalSalePanel.refresh();
      orderNotification.refresh();
    });

  }

  private class RightPanel extends JPanel {
    public RightPanel() {
      this.setLayout(new GridBagLayout());
      this.setBackground(null);
      this.setOpaque(true);

      GridBagConstraints gc = new GridBagConstraints();
      gc.gridx = 0;
      gc.gridy = 0;
      gc.weighty = 1.0;

      gc.weightx = 1.0;
      gc.fill = GridBagConstraints.BOTH;
      gc.insets = new Insets(20, 0, 20, 20);
      totalSalePanel.legendsPanel.setVisible(false);
      totalSalePanel.setBorderColor(colors.m_bg_1);
      totalSalePanel.setBorderWidth(1);
      this.add(totalSalePanel, gc);
      gc.gridy++;

      gc.insets = new Insets(0, 0, 20, 20);
      totalOrderPanel.legendsPanel.setVisible(false);
      totalOrderPanel.setBorderColor(colors.m_bg_1);
      totalOrderPanel.setBorderWidth(1);
      this.add(totalOrderPanel, gc);
      gc.gridy++;

      gc.weighty = 0.0;
      gc.fill = GridBagConstraints.HORIZONTAL;
      this.add(quickActionPanel, gc);
    }
  }

  private class LeftPanel extends JPanel {
    public LeftPanel() {
      this.setLayout(new GridBagLayout());
      this.setBackground(null);
      this.setOpaque(false);
      GridBagConstraints gc = new GridBagConstraints();
      gc.gridx = 0;
      gc.gridy = 0;

      gc.fill = GridBagConstraints.BOTH;
      gc.insets = new Insets(20, 20, 20, 20);
      gc.weightx = 1.0;
      this.add(greetPanel, gc);

      gc.gridx = 0;
      gc.gridy++;

      gc.insets = new Insets(0, 20, 20, 20);
      this.add(orderNotification, gc);

      gc.gridx = 0;
      gc.gridy++;
      gc.weighty = 1.0;

      this.add(topProductPanel, gc);
    }
  }

  public class OrderNotification extends RoundedPanel {
    public OrderNotification() {
      this.setLayout(new GridBagLayout());
      this.setBorderRadius(15);
      this.setBorderWidth(1);
      this.setBackground(colors.m_bg_0);
      this.setBorderColor(colors.m_bg_1);
      GridBagConstraints gc = new GridBagConstraints();
      gc.insets = new Insets(10, 10, 10, 10);
      gc.fill = GridBagConstraints.BOTH;
      gc.weightx = gc.weighty = 1.0;
      orderNotificationTableView = new OrderNotificationTableView();
      this.setVisible(false);

      refresh();

      this.add(orderNotificationTableView, gc);
    }

    public void refresh() {
      orderNotificationTableView.clearNotification();
    {
        int count = Helpers.getUnpaidOrderCount();
        if (count > 0) {
          orderNotificationTableView.addNotification(
            count + " orders have payments that need to be captured",
            "Go to orders",
          () -> {
              JPanel panel = PanelManager.get_panel("DASHBOARD");
              if (panel instanceof Dashboard dashboard) {
                dashboard.setCurrentMenu(dashboard.menuButtons.get(1));
              }
            }
          );
        }
      }

    {
        int count = Helpers.getOpenOrderCount();
        if (count > 0) {
          orderNotificationTableView.addNotification(
            count + " orders need to be fulfilled",
            "View orders",
          () -> {
              JPanel panel = PanelManager.get_panel("DASHBOARD");
              if (panel instanceof Dashboard dashboard) {
                dashboard.setCurrentMenu(dashboard.menuButtons.get(1));
              }
            }
          );
        }
      }
      this.setVisible(!orderNotificationTableView.isEmpty());
    }

    private OrderNotificationTableView orderNotificationTableView;
  }

  public class GreetPanel extends AnimatedPanel {
    public GreetPanel() {
      super(new GreetPanelShader(), 200, 100);
      this.setLayout(new GridBagLayout());
      this.setMinimumSize(new Dimension(200, 250));
      this.setPreferredSize(new Dimension(200, 250));
      this.setMaximumSize(new Dimension(200, 300));
      this.setBorderRadius(15);
      this.setBorderWidth(2);
      this.setBackground(null);
      this.setBorderColor(colors.m_bg_1);

      GridBagConstraints gc = new GridBagConstraints();

      gc.fill = GridBagConstraints.BOTH;
      gc.anchor = GridBagConstraints.FIRST_LINE_START;

      gc.gridwidth = GridBagConstraints.REMAINDER;
      gc.insets = new Insets(30, 30, 0, 30);
      gc.gridx = 0;
      gc.gridy = 0;

      gc.weightx = 1.0;
      lblGreet = new JLabel("");
      lblGreet.setForeground(colors.m_fg_0);
      lblGreet.setFont(nunito_bold_26);
      this.add(lblGreet, gc);

      gc.insets = new Insets(5, 30, 0, 30);
      gc.gridx = 0;
      gc.gridy++;

      ta1 = new JTextArea("Here's what's happening\nwith your store today");
      ta1.setEditable(false);
      ta1.setLineWrap(true);
      ta1.setWrapStyleWord(true);
      ta1.setOpaque(false);
      ta1.setForeground(colors.m_grey_2);
      ta1.setFont(nunito_bold_22);
      this.add(ta1, gc);

      gc.gridwidth = 1;
      gc.weightx = 0.0;
      gc.insets = new Insets(30, 30, 0, 30);
      gc.gridx = 0;
      gc.gridy++;

      lblTodaysTotalSales = new JLabel("Today's total sales");
      lblTodaysTotalSales.setForeground(colors.m_grey_0);
      lblTodaysTotalSales.setFont(nunito_bold_18);
      this.add(lblTodaysTotalSales, gc);
      gc.gridx++;

      lblTodaysTotalOrders = new JLabel("Today's total orders");
      lblTodaysTotalOrders.setForeground(colors.m_grey_0);
      lblTodaysTotalOrders.setFont(nunito_bold_18);
      this.add(lblTodaysTotalOrders, gc);

      gc.insets = new Insets(0, 30, 30, 30);
      gc.gridx = 0;
      gc.gridy++;

      lblTodaysTotalSalesValue = new JLabel(Helpers.CURRENCY_SYMBOL + " 0.00");
      lblTodaysTotalSalesValue.setForeground(colors.m_fg_0);
      lblTodaysTotalSalesValue.setFont(nunito_bold_26);
      this.add(lblTodaysTotalSalesValue, gc);
      gc.gridx++;

      lblTodaysTotalOrdersValue = new JLabel("0");
      lblTodaysTotalOrdersValue.setForeground(colors.m_fg_0);
      lblTodaysTotalOrdersValue.setFont(nunito_bold_26);
      this.add(lblTodaysTotalOrdersValue, gc);

      this.addHierarchyListener(e -> {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
          if (GreetPanel.this.isShowing()) {
            GreetPanel.this.resume();
          } else {
            GreetPanel.this.pause();
          }
        }
      });

      this.refresh();
      this.start();
    }

    public void refresh() {
      double totalSales = Helpers.calculateTotalSales(
        Helpers.getSalesDataPoint(0, 1)
      );
      double totalOrders = Helpers.calculateTotalSales(
        Helpers.getOrdersDataPoint(0, 1)
      );
      String greetString = String.format(
        "Good %s, %s.",
        Helpers.getLocalTimesOfDayString(),
        SessionManager.getCurrentUser().m_username
      );
      lblGreet.setText(greetString);
      lblTodaysTotalSalesValue.setText(String.format("%c%.2f",Helpers.CURRENCY_SYMBOL, totalSales));
      lblTodaysTotalOrdersValue.setText(String.format("%.0f", totalOrders));
    }

    public JLabel lblTodaysTotalSales;
    public JLabel lblTodaysTotalSalesValue;

    public JLabel lblTodaysTotalOrders;
    public JLabel lblTodaysTotalOrdersValue;

    public JLabel lblGreet;
    public JTextArea ta1;
  }

  private TopProductPanel   topProductPanel;
  private GreetPanel        greetPanel;
  private TotalOrderPanel   totalOrderPanel;
  private TotalSalePanel    totalSalePanel;
  private QuickActionPanel  quickActionPanel;
  private OrderNotification orderNotification;

  private Font nunito_bold_18 = FontManager.getFont("NunitoBold", 18f);
  private Font nunito_bold_22 = FontManager.getFont("NunitoBold", 22f);
  private Font nunito_bold_26 = FontManager.getFont("NunitoBold", 26f);

  private ColorScheme colors;
}

class GreetPanelShader extends TriLatticeShader {
  public GreetPanelShader() {
    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
    mult = 4.0f;

    int rgb1 = colors.m_bg_1.getRGB();
    col.x = ((rgb1 >> 16) & 255) / 255.0f;
    col.y = ((rgb1 >> 8) & 255) / 255.0f;
    col.z = ((rgb1 >> 0) & 255) / 255.0f;

    int rgb2 = colors.m_bg_0.getRGB();
    lineCol.x = ((rgb2 >> 16) & 255) / 255.0f;
    lineCol.y = ((rgb2 >> 8) & 255) / 255.0f;
    lineCol.z = ((rgb2 >> 0) & 255) / 255.0f;
  }
}
