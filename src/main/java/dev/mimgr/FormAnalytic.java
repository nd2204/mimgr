package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dev.mimgr.component.DataPoint;
import dev.mimgr.component.DataPointLegend;
import dev.mimgr.component.LineChart;
import dev.mimgr.component.TotalSalePanel;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormAnalytic extends JPanel {
  public FormAnalytic() {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_bg_dim);
    this.setLayout(new GridBagLayout());

    InitComponents();

    GridBagConstraints c = new GridBagConstraints();
    int padding = 25;
    c.insets = new Insets(25, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(lblAnalytic, c);

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.HORIZONTAL;

    c.insets = new Insets(0, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 0.5;
    // c.weighty = 1.0;
    c.gridwidth = 1;
    this.add(new TotalSalePanel(), c);

    c.insets = new Insets(0, 0, 25, padding);
    c.gridx = 1;
    this.add(new TotalOrderPanel(), c);
  }

  private void InitComponents() {
    this.lblAnalytic = new JLabel("Analytics Dashboard");
    this.lblAnalytic.setFont(nunito_bold_20);
    this.lblAnalytic.setForeground(colors.m_fg_0);
  }


  public class TotalOrderPanel extends RoundedPanel {
    public TotalOrderPanel() {
      this.colors = ColorTheme.getInstance().getCurrentScheme();
      this.setLayout(new BorderLayout());
      this.setBackground(colors.m_bg_0);
      this.setMinimumSize(new Dimension(400, 500));
      this.setPreferredSize(new Dimension(400, 500));
      this.setBorder(new EmptyBorder(15, 15, 15, 15));

      List<String> xLabels = new ArrayList<>();
      List<Double> dataPoints = new ArrayList<>();

      Random random = new Random();
      for (int i = 0; i < 50; ++i) {
        double value = random.nextDouble(0, 101);
        dataPoints.add(value);
        xLabels.add(String.valueOf(i));
      }

      DataPoint dataPoint = new DataPoint(dataPoints, xLabels, colors.m_blue);
      dataPoint.dataLegend = "Test";

      // Legends
      JPanel legendsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
      legendsPanel.add(new DataPointLegend(dataPoint));
      legendsPanel.setOpaque(false);

      LineChart chart = new LineChart(dataPoint);
      this.add(chart, BorderLayout.CENTER);
      this.add(legendsPanel, BorderLayout.SOUTH);
    }
    private ColorScheme colors;
  }

  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private ColorScheme colors;
  private JLabel lblAnalytic;
}
