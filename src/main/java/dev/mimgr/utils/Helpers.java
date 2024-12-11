package dev.mimgr.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.Timer;

public class Helpers {
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
}
