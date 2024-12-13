package dev.mimgr.utils;

import java.awt.Dimension;
import java.awt.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet; import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId; import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.Timer;

import dev.mimgr.Dashboard;
import dev.mimgr.IconManager;
import dev.mimgr.PanelManager;
import dev.mimgr.component.DataPoint;
import dev.mimgr.custom.MButton;
import dev.mimgr.db.DBQueries;
import dev.mimgr.db.OrderRecord;
import dev.mimgr.db.QueryBuilder;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class Helpers {
  public static char CURRENCY_SYMBOL = '€';

  public static void runProcess(String...commands) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder(commands);
      processBuilder.directory(new java.io.File(System.getProperty("user.home")));
      processBuilder.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void openFileExplorer(Path path) {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) {
      runProcess("explorer", "/select," + path.toString());
    } else if (osName.contains("mac")) {
      runProcess("open", "-R", path.toString());
    } else if (osName.contains("nix")) {
      runProcess("xdg-open", path.getParent().toString());
    }
  }

  public static String formatRelativeDatetime(Instant instant) {
    if (instant == null) return "N/A";
    LocalDateTime now = LocalDateTime.now();
    ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");

    String time = dateTime.format(formatter);

    long daysDifference = ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate());
    long yearDifferencce = ChronoUnit.YEARS.between(dateTime.toLocalDate(), now.toLocalDate());

    if (yearDifferencce == 0) {
      if (daysDifference == 0) {
        return "Today at " + time;
      } else if (daysDifference == 1) {
        return "Yesterday at " + time;
      } else {
        return daysDifference + " days ago at " + time;
      }
    } else if (yearDifferencce == 1) {
      return "Last year";
    } else {
      return yearDifferencce + " years ago";
    }
  }

  public static class MultiClickHandler {
    private Timer clickTimer;
    private int delay; // in milliseconds
    private int clickCount;
    private int baseClickCount;

    public MultiClickHandler(int count, int delay_ms) {
      if (count <= 1) {
        count = 1;
      }
      this.baseClickCount = count - 1;
      this.clickCount = count - 1;
      this.delay = delay_ms;
    }

    public MultiClickHandler(int count) {
      if (count <= 1) {
        count = 1;
      }
      this.baseClickCount = count - 1;
      this.clickCount = count - 1;
      this.delay = 200;
    }

    public boolean isValidClickCount() {
      if (clickCount == baseClickCount) {
        // Start a new timer only for first click
        clickTimer = new Timer(delay, actionEvent -> {
          clickTimer.stop(); // Stop the timer when the delay expires
          clickCount = baseClickCount; // reset the click count back to base
        });
        clickTimer.setRepeats(false);
        clickTimer.start();
      }

      if (clickCount == 0) {
        clickCount = baseClickCount; // reset the click count back to base
        if (clickTimer != null && clickTimer.isRunning()) {
          clickTimer.stop();
          return true;
        }
      }

      clickCount--;
      return false;
    }
  }

  public static String getTimesOfDayString(int hour) {
    if (hour < 12) {
      return "morning";
    } else if (hour < 18) {
      return "afternoon";
    } else {
      return "evening";
    }
  }

  public static String getLocalTimesOfDayString() {
    int hour = LocalTime.now().getHour();
    return getTimesOfDayString(hour);
  }

  /* 
   * @param dayBefore the day offset from today
   * @param offset the offset from the daybefore
   *        result in range [offset -> dayBefore] if negative offset
   *        result in range [dayBefore -> offset] if positive offset
   */
  public static DataPoint getOrdersDataPoint(int dayBefore, int offset) {
    String query = """
    SELECT
    COUNT(*) AS count,
    DATE(order_date) AS order_day
    FROM orders
    WHERE order_date >= DATE_SUB((SELECT MAX(order_date) FROM orders), INTERVAL ? DAY)
    AND order_date <= DATE_SUB((SELECT MAX(order_date) FROM orders), INTERVAL ? DAY)
    GROUP BY order_day
    ORDER BY order_day ASC;
    """;
    DataPoint dataPoint = new DataPoint();
    Instant start = null;
    Instant end = null;
    try (ResultSet rs = DBQueries.select(query, dayBefore + offset, dayBefore)) {

      while (rs.next()) {
        if (rs.isFirst()) {
          start = rs.getTimestamp("order_day").toInstant();
        }
        if (rs.isLast()) {
          end = rs.getTimestamp("order_day").toInstant();
        }
        dataPoint.data.add(rs.getDouble("count"));
        dataPoint.xLabels.add(Helpers.formatInstant(rs.getTimestamp("order_day").toInstant(), "MMM d"));
      }
      if (start == null || end == null) return dataPoint;
      dataPoint.dataLegend = Helpers.formatInstant(start, "MMM d - ") + Helpers.formatInstant(end, "MMM d, yyyy");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return dataPoint;
  }

  /* 
   * @param dayBefore the day offset from today
   * @param offset the offset from the daybefore
   *        result in range [offset -> dayBefore] if negative offset
   *        result in range [dayBefore -> offset] if positive offset
   */
  public static DataPoint getSalesDataPoint(int dayBefore, int offset) {

    String query = String.format("""
      SELECT DATE(order_date) AS order_day, oi.total_price, %s
      FROM orders o
      JOIN
      (
      SELECT
      order_id,
      COUNT(order_item_id) AS total_item,
      SUM(total_price) AS total_price
      FROM order_items
      GROUP BY order_id
      ) AS oi
      ON o.order_id = oi.order_id
      WHERE o.order_date >= DATE_SUB((SELECT MAX(order_date) FROM orders), INTERVAL ? DAY)
      AND o.order_date <= DATE_SUB((SELECT MAX(order_date) FROM orders), INTERVAL ? DAY)
      ORDER BY o.order_date ASC;
      """, OrderRecord.FIELD_PAYMENT_STATUS);

    DataPoint dataPoint = new DataPoint();
    Instant start = null;
    Instant end = null;
    try (ResultSet rs = DBQueries.select(query, dayBefore + offset, dayBefore)) {
      while (rs.next()) {
        if (rs.isFirst()) {
          start = rs.getTimestamp("order_day").toInstant();
        }
        if (rs.isLast()) {
          end = rs.getTimestamp("order_day").toInstant();
        }
        // Skip the row if the order haven't been paid
        if (!rs.getString(OrderRecord.FIELD_PAYMENT_STATUS)
        .equals(OrderRecord.paymentStatuses[OrderRecord.PAYMENT_STATUS_PAID])
      ) continue;
        dataPoint.data.add(rs.getDouble("total_price"));
        dataPoint.xLabels.add(formatInstant(rs.getTimestamp("order_day").toInstant(), "MMM d"));
      }
      // Setup the dataPoint
      if (start == null || end == null) return dataPoint;
      dataPoint.dataLegend = formatInstant(start, "MMM d - ") + formatInstant(end, "MMM d, yyyy");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return dataPoint;
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

  public static int getUnpaidOrderCount() {
    QueryBuilder qb = new QueryBuilder();
    String query = qb.select("COUNT(*)")
    .from(OrderRecord.TABLE)
    .groupby(OrderRecord.FIELD_PAYMENT_STATUS)
    .where(OrderRecord.FIELD_PAYMENT_STATUS + "=?")
    .build();

    ResultSet rs = DBQueries.select(
      query,
      OrderRecord.paymentStatuses[OrderRecord.PAYMENT_STATUS_UNPAID]
    );

    try {
      if (rs == null || !rs.next()) return -1;
      return rs.getInt(1);
    } catch (SQLException e) {
      System.err.println(e);
      return -1;
    }
  }

  public static int getOpenOrderCount() {
    QueryBuilder qb = new QueryBuilder();
    String query = qb.select("COUNT(*)")
    .from(OrderRecord.TABLE)
    .groupby(OrderRecord.FIELD_ORDER_STATUS)
    .where(OrderRecord.FIELD_ORDER_STATUS + "=?")
    .build();

    ResultSet rs = DBQueries.select(
      query,
      OrderRecord.orderStatuses[OrderRecord.ORDER_STATUS_OPEN]
    );

    try {
      if (rs == null || !rs.next()) return -1;
      return rs.getInt(1);
    } catch (SQLException e) {
      System.err.println(e);
      return -1;
    }
  }

  public static String getCurrentTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    System.out.println(LocalDateTime.now().format(formatter));
    return LocalDateTime.now().format(formatter); 
  }

  public static double calculateTotalData(DataPoint dp) {
    return dp.data.stream().reduce(0.0, (d1, d2) -> d1 + d2);
  }

  public static double calculateTotalSales(DataPoint dp) {
    return calculateTotalData(dp);
  }

  public static double calculateTotalOrders(DataPoint dp) {
    return calculateTotalData(dp);
  }

  public static String formatInstant(Instant instant, String format) {
    ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
    return zdt.format(dtf);
  }

  public static MButton createHomeButton() {
    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
    Icon home_icon  = IconManager.getIcon("home_1.png", 16, 16, colors.m_fg_0);
    MButton button = new MButton(home_icon); 
    button.setBorderRadius(15);
    button.setBorderWidth(1);
    button.setMinimumSize(new Dimension(40, 40));
    button.setPreferredSize(new Dimension(40, 40));
    button.setMaximumSize(new Dimension(50, 50));
    button.setBackground(colors.m_bg_0);
    button.setBorderColor(colors.m_bg_1);
    button.addActionListener((e) -> {
      JPanel panel = PanelManager.get_panel("DASHBOARD");
      if (panel instanceof Dashboard dashboard) {
        dashboard.setCurrentMenu(dashboard.menuButtons.get(0));
      }
    });
    return button;
  }
}
