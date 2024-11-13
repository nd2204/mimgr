package dev.mimgr.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
}
