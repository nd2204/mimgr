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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dev.mimgr.FontManager;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class TotalSalePanel extends RoundedPanel {
  public TotalSalePanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_0);
    this.setMinimumSize(new Dimension(400, 500));
    this.setPreferredSize(new Dimension(400, 500));
    this.setBorder(new EmptyBorder(15, 15, 15, 15));

    InitComponents();

    LineChart chart = new LineChart();
    chart.setMaxXDivision(4);
    chart.setYLabelFormatter((str) -> formatToSuffix(str));
    JPanel legendsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    legendsPanel.setOpaque(false);
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
      lblTotalSales.setText(String.format("€ %.2f", thisMonthSum));
      chart.addDataPoint(dataPoint);
      legendsPanel.add(new DataPointLegend(dataPoint));
    }

    lblGrowthRate.setText(String.format("%s", (int) ((thisMonthSum / lastMonthSum) * 100) + "%"));

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
      detailPanel.add(lblTotalSales, c);

      c.gridx = 1;
      c.weightx = 1.0;
      detailPanel.add(Box.createHorizontalGlue(), c);

      c.insets = new Insets(0, 5, 25, 10);
      c.gridx = 2;
      c.gridy = 1;
      c.weightx = 1.0;
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

  private void InitComponents() {
    lblTitle = new JLabel("Total sales");
    lblTitle.setForeground(colors.m_fg_0);
    lblTitle.setFont(nunito_bold_14);
    lblTitle.setHorizontalAlignment(SwingConstants.LEFT);

    lblTotalSales = new JLabel("NO DATA");
    lblTotalSales.setForeground(colors.m_fg_0);
    lblTotalSales.setFont(nunito_bold_20);
    lblTotalSales.setHorizontalAlignment(SwingConstants.LEFT);
    lblTotalSales.setVerticalAlignment(SwingConstants.CENTER);

    lblGrowthRate = new JLabel();
    lblGrowthRate.setFont(nunito_bold_16);
    lblGrowthRate.setForeground(colors.m_green);
    lblGrowthRate.setHorizontalAlignment(SwingConstants.RIGHT);
    lblGrowthRate.setVerticalAlignment(SwingConstants.CENTER);

    lblChart = new JLabel("SALES OVER TIME");
    lblChart.setFont(nunito_bold_14);
    lblChart.setForeground(colors.m_grey_2);
    lblChart.setHorizontalAlignment(SwingConstants.LEFT);
    lblChart.setVerticalAlignment(SwingConstants.CENTER);
  }
  public static String formatToSuffix(String amountStr) {
    double amount = Double.valueOf(amountStr);
    if (amount >= 1_000_000_000) {
        return String.format("€%.0fB", amount / 1_000_000_000).replaceAll("\\.0B", "B");
    } else if (amount >= 1_000_000) {
        return String.format("€%.0fM", amount / 1_000_000).replaceAll("\\.0M", "M");
    } else if (amount >= 1_000) {
        return String.format("€%.0fK", amount / 1_000).replaceAll("\\.0K", "K");
    } else {
        return String.format("€%.0f", amount);
    }
  }

  private DataPoint getDataPointByMonth(int monthsBefore) {
    String query = """
    SELECT DATE(order_date) AS order_day, oi.total_price
    FROM orders o
    JOIN
      (
        SELECT order_id,
          COUNT(order_item_id) AS total_item,
          SUM(total_price) AS total_price
        FROM order_items
        GROUP BY order_id
      ) AS oi
    ON o.order_id = oi.order_id
    WHERE o.order_date >= DATE_SUB(CURDATE(), INTERVAL ? MONTH)
      AND o.order_date <= DATE_SUB(CURDATE(), INTERVAL ? MONTH)
    ORDER BY o.order_date ASC;
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
        dataPoint.data.add(rs.getDouble("total_price"));
        dataPoint.xLabels.add(formatInstant(rs.getTimestamp("order_day").toInstant(), "MMM d"));
      }
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

  private class GrowthRateLabel extends JLabel {
    public GrowthRateLabel(double rate) {
      super(String.valueOf(rate * 100));
    }
  }

  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private ColorScheme colors;
  private JLabel lblTitle;
  private JLabel lblTotalSales;
  private JLabel lblGrowthRate;
  private JLabel lblChart;
}
