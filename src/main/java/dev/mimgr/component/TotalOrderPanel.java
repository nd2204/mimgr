package dev.mimgr.component;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dev.mimgr.FontManager;
import dev.mimgr.IconManager;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class TotalOrderPanel extends RoundedPanel {
  public TotalOrderPanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_0);
    this.setMinimumSize(new Dimension(400, 500));
    this.setPreferredSize(new Dimension(400, 500));
    this.setBorder(new EmptyBorder(15, 15, 15, 15));

    InitComponents();

    JPanel detailPanel = new JPanel(new GridBagLayout());
    detailPanel.setBackground(colors.m_bg_0);
    {
      GridBagConstraints c = new GridBagConstraints();
      c.insets = new Insets(0, 5, 5, 10);
      c.gridx = 0;
      c.gridy = 0;
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      detailPanel.add(lblTitle, c);

      c.insets = new Insets(0, 5, 15, 10);
      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 1.0;
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      detailPanel.add(lblTotalOrder, c);

      c.gridx = 1;
      c.weightx = 1.0;
      detailPanel.add(Box.createHorizontalGlue(), c);

      c.insets = new Insets(0, 5, 25, 10);
      c.gridx = 2;
      c.gridy = 1;
      c.weightx = 1.0;
      c.weighty = 1.0;
      c.anchor = GridBagConstraints.FIRST_LINE_END;
      detailPanel.add(lblGrowthRate, c);

      c.insets = new Insets(0, 5, 0, 10);
      c.gridx = 0;
      c.gridy = 2;
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      detailPanel.add(lblChart, c);
    }

    this.add(detailPanel, BorderLayout.NORTH);
    this.add(chart, BorderLayout.CENTER);
    this.add(legendsPanel, BorderLayout.SOUTH);
  }

  private void initChart() {
    double thisMonthSum;
    double lastMonthSum;
    {
      DataPoint dataPoint = getDataPointByMonth(1);
      dataPoint.lineColor = colors.m_bg_5;
      dataPoint.lineStroke = DataPoint.createDashedStroke(2, 4 ,4);
      lastMonthSum = dataPoint.data.stream().reduce(0.0, (d1, d2) -> d1 + d2);
      chart.addDataPoint(dataPoint);
      legendsPanel.add(new DataPointLegend(dataPoint));
    }
    {
      DataPoint dataPoint = getDataPointByMonth(0);
      thisMonthSum = dataPoint.data.stream().reduce(0.0, (d1, d2) -> d1 + d2);
      lblTotalOrder.setText(String.format("%d", Double.valueOf(thisMonthSum).intValue()));
      chart.addDataPoint(dataPoint);
      legendsPanel.add(new DataPointLegend(dataPoint));
    }

    lblGrowthRate.setRate(thisMonthSum / lastMonthSum);
  }

  public void refresh() {
    chart.clear();
    legendsPanel.removeAll();
    initChart();
    revalidate();
    repaint();
  }

  private void InitComponents() {
    lblTitle = new JLabel("Total orders");
    lblTitle.setForeground(colors.m_fg_0);
    lblTitle.setFont(nunito_bold_14);
    lblTitle.setHorizontalAlignment(SwingConstants.LEFT);

    lblTotalOrder = new JLabel("NO DATA");
    lblTotalOrder.setForeground(colors.m_fg_0);
    lblTotalOrder.setFont(nunito_extrabold_22);
    lblTotalOrder.setHorizontalAlignment(SwingConstants.LEFT);
    lblTotalOrder.setVerticalAlignment(SwingConstants.CENTER);

    lblGrowthRate = new GrowthRateLabel();
    lblGrowthRate.setFont(nunito_bold_16);
    lblGrowthRate.setForeground(colors.m_green);
    lblGrowthRate.setHorizontalAlignment(SwingConstants.RIGHT);
    lblGrowthRate.setVerticalAlignment(SwingConstants.CENTER);

    lblChart = new JLabel("ORDERS OVER TIME");
    lblChart.setFont(nunito_bold_14);
    lblChart.setForeground(colors.m_grey_2);
    lblChart.setHorizontalAlignment(SwingConstants.LEFT);
    lblChart.setVerticalAlignment(SwingConstants.CENTER);

    chart = new LineChart();
    chart.setMaxXDivision(4);

    legendsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    legendsPanel.setOpaque(false);

    initChart();
  }

  private DataPoint getDataPointByMonth(int monthsBefore) {
    String query = """
    SELECT
      COUNT(*) AS count,
      DATE(order_date) AS order_day
    FROM orders
    WHERE order_date >= DATE_SUB((SELECT MAX(order_date) FROM orders), INTERVAL ? MONTH)
      AND order_date <= DATE_SUB((SELECT MAX(order_date) FROM orders), INTERVAL ? MONTH)
    GROUP BY order_day
    ORDER BY order_day ASC;
    """;
    DataPoint dataPoint = new DataPoint();
    Instant start = null;
    Instant end = null;
    try (ResultSet rs = DBQueries.select(query, monthsBefore + 1, monthsBefore)) {

      while (rs.next()) {
        if (rs.isFirst()) {
          start = rs.getTimestamp("order_day").toInstant();
        }
        if (rs.isLast()) {
          end = rs.getTimestamp("order_day").toInstant();
        }
        dataPoint.data.add(rs.getDouble("count"));
        dataPoint.xLabels.add(formatInstant(rs.getTimestamp("order_day").toInstant(), "MMM d"));
      }
      if (start == null || end == null) return dataPoint;
      dataPoint.dataLegend = formatInstant(start, "MMM d - ") + formatInstant(end, "MMM d, yyyy");
      dataPoint.lineColor = colors.m_blue;
      dataPoint.lineStroke = new BasicStroke(2);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return dataPoint;
  }

  private String formatInstant(Instant instant, String format) {
    ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
    return zdt.format(dtf);
  }

  public class GrowthRateLabel extends JLabel {
    public GrowthRateLabel() {
      this.setFont(nunito_bold_16);
    }

    public void setRate(double rate) {
      if (rate < 1.0) {
        this.setText(String.format("%d%s", (int) ((1.0 - rate) * 100), "%"));
        this.setIcon(IconManager.getIcon("down_arrow.png", 14, 16, colors.m_red));
        this.setForeground(colors.m_red);
      } else {
        this.setText(String.format("%d%s", (int) ((rate - 1.0) * 100), "%"));
        this.setIcon(IconManager.getIcon("up_arrow.png", 14, 16, colors.m_green));
        this.setForeground(colors.m_green);
      }
    }

    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
  }

  private Font nunito_extrabold_22 = FontManager.getFont("NunitoExtraBold", 22f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);

  private JPanel legendsPanel;
  private LineChart chart;
  private ColorScheme colors;
  private JLabel lblTitle;
  private JLabel lblTotalOrder;
  private GrowthRateLabel lblGrowthRate;
  private JLabel lblChart;
}
