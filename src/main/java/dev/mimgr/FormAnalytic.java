package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.component.DataPoint;
import dev.mimgr.component.LineChart;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormAnalytic extends JPanel {
  public FormAnalytic() {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_bg_0);
    this.setLayout(new BorderLayout());

    List<String> xLabels = new ArrayList<>();
    List<Integer> dataPoints = new ArrayList<>();

    Random random = new Random();
    for (int i = 0; i < 50; ++i) {
      int value = random.nextInt(0, 101);
      dataPoints.add(value);
      xLabels.add(String.valueOf(i));
    }

    DataPoint dataPoint = new DataPoint(dataPoints, xLabels, colors.m_blue);
    dataPoint.dataLegend = "Test test";

    LineChart chart = new LineChart(dataPoint);
    this.add(chart, BorderLayout.CENTER);
    this.add(new DataPointLegend(dataPoint), BorderLayout.SOUTH);

  }

  public class DataPointLegend extends JPanel {
    public DataPointLegend(DataPoint dp) {
      this.colors = ColorTheme.getInstance().getCurrentScheme();
      this.dp = dp;
      this.setMinimumSize(new Dimension(40, 50));
      this.setPreferredSize(new Dimension(40, 30));
      this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2 = (Graphics2D) g.create();

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      if (dp.dataLegend != null && dp.dataLegend.length() > 0) {
        g2.setFont(FontManager.getFont("Nunito", 14f).deriveFont(Font.BOLD));
        FontMetrics fm = g2.getFontMetrics();

        int legendHeight = (int) (getHeight());
        int legendWidth = fm.stringWidth(dp.dataLegend) + legendHeight + 15;
        // int x = (getWidth() - legendWidth) / 2;
        int y = (getHeight() - legendHeight) / 2;

        g2.setColor(colors.m_bg_1);
        int radius = (int) (legendHeight * 0.2);
        g2.fillRoundRect(0, y, legendWidth - 1, legendHeight - 1, radius, radius);
        g2.setColor(dp.lineColor);
        g2.fillRoundRect(8, y + 7, legendHeight - 17, legendHeight - 17, radius, radius);

        int textX = legendHeight / 2 + 14;
        int textY = getHeight() / 2 + fm.getAscent() / 2 - fm.getDescent() / 2 - 1;
        g2.setColor(colors.m_fg_0);
        g2.drawString(dp.dataLegend, textX, textY);
      }

      g2.dispose();
    }

    private DataPoint dp;
    private ColorScheme colors;
  }

  private ColorScheme colors;
}
