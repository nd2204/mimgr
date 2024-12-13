package dev.mimgr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.mimgr.component.NotificationPopup;
import dev.mimgr.component.TotalOrderPanel;
import dev.mimgr.component.TotalSalePanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers;
import dev.mimgr.utils.RandomOrderGenerator;

public class FormAnalytic extends JPanel implements ActionListener {
  public FormAnalytic() {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_bg_dim);
    this.setLayout(new GridBagLayout());

    InitComponents();

    GridBagConstraints c = new GridBagConstraints();
    int padding = 25;

    // ---------
    c.gridx = 0;
    c.gridy = 0;
    // ---------

    c.insets = new Insets(20, 25, 20, 15);
    this.add(Helpers.createHomeButton(), c);
    c.gridx++;

    c.insets = new Insets(25, 0, 5, padding);
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(lblAnalytic, c);
    c.gridx++;

    c.insets = new Insets(20, 5, 5, 5);
    this.add(btnRefresh, c);
    c.gridx++;

    this.add(btnGenerateRandom, c);
    c.gridx++;

    // ---------
    c.gridx = 0;
    c.gridy++;
    // ---------

    c.insets = new Insets(25, padding, 25, padding);
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill = GridBagConstraints.BOTH;
    this.add(analyticView, c);
    c.gridx++;

    // ---------
    c.gridx = 0;
    c.gridy++;
    // ---------

    c.weighty = 1.0;
    this.add(Box.createVerticalGlue(), c);
  }

  public class AnalyticView extends JPanel {
    public AnalyticView() {
      this.setBackground(null);
      this.setOpaque(false);
      this.setLayout(new GridBagLayout());

      totalOrderPanel = new TotalOrderPanel();
      totalSalePanel = new TotalSalePanel();

      gc.gridx = 0;
      gc.gridy = 0;

      gc.anchor = GridBagConstraints.CENTER;
      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.insets = new Insets(0, 0, 25, 25);
      gc.weightx = 1.0;
      gc.gridwidth = 1;
      this.add(totalSalePanel, gc);
      gc.gridx++;

      gc.insets = new Insets(0, 0, 25, 0);
      this.add(totalOrderPanel, gc);
      gc.gridx++;
    }

    public void refresh() {
      totalSalePanel.refresh();
      totalOrderPanel.refresh();
    }

    private GridBagConstraints gc = new GridBagConstraints();
    private TotalSalePanel totalSalePanel;
    private TotalOrderPanel totalOrderPanel;
  }

  private void InitComponents() {
    this.lblAnalytic = new JLabel("Analytics Dashboard");
    this.lblAnalytic.setFont(nunito_bold_20);
    this.lblAnalytic.setForeground(colors.m_fg_0);

    this.btnGenerateRandom = new MButton("Generate Random Orders");
    this.btnGenerateRandom.setBackground(colors.m_bg_dim);
    this.btnGenerateRandom.setBorderColor(colors.m_bg_4);
    this.btnGenerateRandom.setHoverBackgroundColor(colors.m_bg_1);
    this.btnGenerateRandom.setHoverBorderColor(colors.m_bg_3);
    this.btnGenerateRandom.setClickBackgroundColor(colors.m_bg_0);
    this.btnGenerateRandom.setFont(nunito_extrabold_14);
    this.btnGenerateRandom.setIcon(IconManager.getIcon("upload.png", 16, 16, colors.m_grey_0));
    this.btnGenerateRandom.setForeground(colors.m_grey_0);
    this.btnGenerateRandom.addActionListener(this);

    this.btnRefresh = new MButton("Refresh");
    this.btnRefresh.setBackground(colors.m_bg_dim);
    this.btnRefresh.setBorderColor(colors.m_bg_4);
    this.btnRefresh.setHoverBackgroundColor(colors.m_bg_1);
    this.btnRefresh.setHoverBorderColor(colors.m_bg_3);
    this.btnRefresh.setClickBackgroundColor(colors.m_bg_0);
    this.btnRefresh.setFont(nunito_extrabold_14);
    this.btnRefresh.setIcon(IconManager.getIcon("refresh_2.png", 14, 16, colors.m_grey_0));
    this.btnRefresh.setForeground(colors.m_grey_0);
    this.btnRefresh.addActionListener(this);

    this.analyticView = new AnalyticView();
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    if (ev.getSource() == btnRefresh) {
      analyticView.refresh();
    }

    if (ev.getSource() == btnGenerateRandom) {
      Thread thread = new Thread(() -> {
        RandomOrderGenerator.createRandomOrder(30, () -> RandomOrderGenerator.getRandomThis30DayInstant());
        PanelManager.createPopup(new NotificationPopup("Generated 30 orders of this month", NotificationPopup.NOTIFY_LEVEL_DEBUG, 5000));
        RandomOrderGenerator.createRandomOrder(30, () -> RandomOrderGenerator.getRandomPrevious30DayInstant());
        PanelManager.createPopup(new NotificationPopup("Generated 30 orders of previous month", NotificationPopup.NOTIFY_LEVEL_DEBUG, 5000));
        SwingUtilities.invokeLater(() -> {
          analyticView.refresh();
        });
      });
      thread.setDaemon(true);
      thread.start();
    }
  }

  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private MButton btnRefresh;
  private MButton btnGenerateRandom;

  private AnalyticView analyticView;
  private ColorScheme colors;
  private JLabel lblAnalytic;
}
