package dev.mimgr.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class DataPoint {
  public List<Integer> data = new ArrayList<>();
  public List<String> xLabels;
  public Stroke lineStroke;
  public Color lineColor;
  public String dataLegend;

  public DataPoint(List<Integer> dataPoints, List<String> xLabels, Color lineColor, String dataLegend, Stroke lineStroke) {
    Init();
    this.data = dataPoints;
    this.xLabels = xLabels;
    this.lineColor = lineColor;
    this.dataLegend = dataLegend;
    this.lineStroke = lineStroke;
  }

  public DataPoint(List<Integer> dataPoints, List<String> xLabels, Color lineColor, String dataLegend) {
    Init();
    this.data = dataPoints;
    this.xLabels = xLabels;
    this.lineColor = lineColor;
    this.dataLegend = dataLegend;
  }

  public DataPoint(List<Integer> dataPoints, List<String> xLabels, Color lineColor) {
    Init();
    this.data = dataPoints;
    this.xLabels = xLabels;
    this.lineColor = lineColor;
  }

  public DataPoint(List<Integer> dataPoints, List<String> xLabels) {
    Init();
    this.data = dataPoints;
    this.xLabels = xLabels;
  }

  private void Init() {
    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
    this.lineColor = colors.m_blue;
    this.dataLegend = "";
    this.lineStroke = new BasicStroke(1);
  }

  public static Stroke createDashedStroke(int strokeWidth, float dashLen, float gapLen) {
    // Set dashed stroke
    float[] dashPattern = {dashLen, gapLen}; // Dash length, gap length
    return new BasicStroke(
      strokeWidth,            // Line width
      BasicStroke.CAP_BUTT,   // End cap
      BasicStroke.JOIN_BEVEL, // Line join style
      0,                      // Miter limit
      dashPattern,            // Dash pattern
      0                       // Dash phase
    );
  }
};
