package dev.mimgr.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import javax.swing.JPanel;

import dev.mimgr.FontManager;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class LineChart extends JPanel {
  public LineChart(DataPoint dataPoint) {
    InitComponents();
    this.setMinimumSize(new Dimension(400, 250));
    this.setPreferredSize(new Dimension(400, 250));
    this.addDataPoint(dataPoint);
  }

  public LineChart() {
    InitComponents();
  }

  private void InitComponents() {
    this.dataPointList = new ArrayList<>();
    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_bg_0);
    this.chartBackground = colors.m_bg_0;
    this.chartAxisLabel = colors.m_grey_0;
    this.chartAxisColor = colors.m_bg_2;
    this.maxYDivision = 3;
    this.maxXDivision = 2;
    this.yLabelsFormatter = (s) -> s;

    // Define margins and axis padding
    this.chartPadding = 30;
    this.labelPadding = 30;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();

    // Background color
    g2.setColor(chartBackground);
    g2.fillRect(chartPadding + labelPadding, chartPadding, width - 2 * chartPadding - labelPadding, height - 2 * chartPadding - labelPadding);
    g2.setFont(FontManager.getFont("NunitoBold", 14f));

    int maxDataPointValue = getMaxDataPointValue(dataPointList);
    DataPoint longestDataPointList = getLongestDataPointList(dataPointList);
    DataPoint newestDataPointList = dataPointList.getLast();

    drawHorizontalDataPointAxisLine(g2, width, height, longestDataPointList, maxDataPointValue);
    drawVerticalDataPointAxisLine(g2, width, height, newestDataPointList, newestDataPointList.xLabels);
    for (DataPoint dataPoints : dataPointList) {
      drawDataPointLine(g2, width, height, dataPoints, maxDataPointValue);
    }

    g2.dispose();
  }

  private void drawHorizontalDataPointAxisLine(Graphics2D g2, int width, int height, DataPoint dataPoint, int maxDataPointValue) {
    // Create hatch marks for y-axis and x-axis.
    for (int i = 0; i < maxYDivision + 1; i++) {
      int x0 = chartPadding + labelPadding;
      int y0 = height - ((i * (height - chartPadding * 2 - labelPadding)) / maxYDivision + chartPadding + labelPadding);
      int y1 = y0;
      if (!dataPoint.data.isEmpty()) {
        if (flagIsOn(FLAG_DRAW_HORIZONTAL_DATAPOINT_LINE)) {
          g2.setColor(chartAxisColor);
          g2.drawLine(chartPadding + labelPadding + 1, y0, width - chartPadding, y1);
        }
        g2.setColor(chartAxisLabel);
        String yLabel = ((int) ((maxDataPointValue * (i  / (float) maxYDivision)) * 100)) / 100.0 + "";
        yLabel = yLabelsFormatter.apply(yLabel);
        FontMetrics metrics = g2.getFontMetrics();
        int labelWidth = metrics.stringWidth(yLabel) + 10;
        g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 5);
      }
    }
  }

  private void drawVerticalDataPointAxisLine(Graphics2D g2, int width, int height, DataPoint dataPoint, List<String> xLabels) {
    int dataPointSize = dataPoint.data.size();
    int dataPointPerSegment = dataPointSize / maxXDivision;
    for (int i = 0; i < dataPointSize; i++) {
      if (dataPointSize <= 1) break;
      int x0 = i * (width - chartPadding * 2 - labelPadding) / (dataPointSize - 1) + chartPadding + labelPadding;
      int x1 = x0;
      int y0 = height - chartPadding - labelPadding;
      // Draw Label and line only in subdivision range
      if (i % dataPointPerSegment == 0) {
        if (flagIsOn(FLAG_DRAW_VERTICAL_DATAPOINT_LINE)) {
          g2.setColor(chartAxisColor);
          g2.drawLine(x0, height - chartPadding - labelPadding - 1, x1, chartPadding);
        }
        g2.setColor(chartAxisLabel);
        if (i < xLabels.size()) {
          String xLabel = xLabels.get(i);
          FontMetrics metrics = g2.getFontMetrics();
          int labelWidth = metrics.stringWidth(xLabel);
          g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 10);
        }
      }
    }
  }

  private void drawDataPointLine(Graphics2D g2, int width, int height, DataPoint dataPoint, int maxDataPointValue) {
    // Draw the line chart
    List<Point> dp = new ArrayList<>();
    int dataPointSize = dataPoint.data.size();
    g2.setColor(dataPoint.lineColor);
    for (int i = 0; i < dataPointSize - 1; i++) {
      int value1 = dataPoint.data.get(i).intValue();
      int value2 = dataPoint.data.get(i+1).intValue();
      int x1 = i * (width - chartPadding * 2 - labelPadding) / (dataPointSize - 1) + chartPadding + labelPadding;
      int y1 = height - ((value1 * (height - chartPadding * 2 - labelPadding)) / maxDataPointValue + chartPadding + labelPadding);
      int x2 = (i + 1) * (width - chartPadding * 2 - labelPadding) / (dataPointSize - 1) + chartPadding + labelPadding;
      int y2 = height - ((value2 * (height - chartPadding * 2 - labelPadding)) / maxDataPointValue + chartPadding + labelPadding);
      dp.add(new Point(x1, y1));
      if (i + 1 == dataPointSize - 1) {
        dp.add(new Point(x2, y2));
      }
    }

    List<Point> points;
    if (flagIsOn(FLAG_DRAW_SMOOTH_DATAPOINT)) {
      points = calculateSpline(dp, 1000);
    } else {
      points = dp;
    }

    for (int i = 0; i < points.size() - 1; i++) {
      Point p1 = points.get(i);
      Point p2 = points.get(i + 1);
      g2.setStroke(dataPoint.lineStroke);
      if (flagIsOn(FLAG_DRAW_DATAPOINT_POINT)) {
        if (i > 0) g2.fillOval(p1.x - 3, p1.y - 3, 6, 6);
      }
      if (flagIsOn(FLAG_DRAW_DATAPOINT_LINE)) {
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
      }
    }
  }

  // Cubic spline interpolation calculation
  private List<Point> calculateSpline(List<Point> points, int numSamplesPerSegment) {
    List<Point> splinePoints = new ArrayList<>();

    for (int i = 0; i < points.size() - 1; i++) {
      Point p0 = (i == 0) ? points.get(i) : points.get(i - 1);
      Point p1 = points.get(i);
      Point p2 = points.get(i + 1);
      Point p3 = (i + 2 < points.size()) ? points.get(i + 2) : points.get(i + 1);

      for (int j = 0; j <= numSamplesPerSegment; j++) {
        double t = j / (double) numSamplesPerSegment;
        double tt = t * t;
        double ttt = tt * t;

        // Cubic Hermite spline formula for X and Y
        double q1 = -0.5 * ttt + tt - 0.5 * t;
        double q2 = 1.5 * ttt - 2.5 * tt + 1.0;
        double q3 = -1.5 * ttt + 2.0 * tt + 0.5 * t;
        double q4 = 0.5 * ttt - 0.5 * tt;

        int x = (int) (p0.x * q1 + p1.x * q2 + p2.x * q3 + p3.x * q4);
        int y = (int) (p0.y * q1 + p1.y * q2 + p2.y * q3 + p3.y * q4);

        splinePoints.add(new Point(x, y));
      }
    }

    return splinePoints;
  }

  private int getMaxDataPointValue(List<DataPoint> dataPoints) {
    double max = Integer.MIN_VALUE;
    for (DataPoint dp : dataPoints) {
      for (double value : dp.data) {
        max = Math.max(max, value);
      }
    }
    return Double.valueOf(max * 1.1).intValue();
  }

  private DataPoint getLongestDataPointList(List<DataPoint> dataPoints) {
    List<DataPoint> dataPointsList = new ArrayList<>(dataPoints);
    dataPointsList.sort(Comparator.<DataPoint>comparingInt(dp -> dp.data.size()).reversed());
    return dataPointsList.getFirst();
  }

  public void setChartBackground(Color color) {
    this.chartBackground = color;
    repaint();
  }

  public void setDrawFlags(int flags) {
    this.flags = this.flags | flags;
    repaint();
  }

  public void unsetDrawFlags(int flags) {
    this.flags = this.flags ^ flags;
    repaint();
  }

  private boolean flagIsOn(int flag) {
    return (flags & flag) != 0;
  }

  public void addDataPoint(DataPoint dp) {
    if (dataPointList.size() <= 5) {
      if (!dp.data.isEmpty()) {
        this.dataPointList.add(dp);
        this.setVisible(true);
      } else {
        this.setVisible(false);
      }
    } else {
      System.err.println("Maximum data point list reached");
    }
    revalidate();
    repaint();
  }

  public void setYLabelFormatter(Function<String, String> format) {
    this.yLabelsFormatter = format; 
    repaint();
  }

  public void setMaxYDivision(int MaxDivision) {
    this.maxYDivision = MaxDivision; 
    repaint();
  }

  public void setMaxXDivision(int MaxDivision) {
    this.maxXDivision = MaxDivision; 
    repaint();
  }

  public static final int FLAG_DRAW_VERTICAL_DATAPOINT_LINE = 1;
  public static final int FLAG_DRAW_HORIZONTAL_DATAPOINT_LINE = 1 << 1;
  public static final int FLAG_DRAW_SMOOTH_DATAPOINT = 1 << 2;
  public static final int FLAG_DRAW_DATAPOINT_POINT = 1 << 3;
  public static final int FLAG_DRAW_DATAPOINT_LINE = 1 << 4;

  private int flags = FLAG_DRAW_HORIZONTAL_DATAPOINT_LINE 
    | FLAG_DRAW_VERTICAL_DATAPOINT_LINE
    | FLAG_DRAW_DATAPOINT_LINE;

  private Function<String, String> yLabelsFormatter;
  private List<DataPoint> dataPointList;
  private Color chartBackground;
  private Color chartAxisLabel;
  private Color chartAxisColor;
  private int maxYDivision;
  private int maxXDivision;
  private int chartPadding;
  private int labelPadding;
}
