package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
interface RecordMapper<T> {
  T map(ResultSet rs) throws SQLException;
}

public class RecordUtil {
  public static <T> List<T> getAllRecords(ResultSet rs, RecordMapper<T> mapper) {
    List<T> ret = new ArrayList<>();
    try {
      while (rs.next()) {
        ret.add(mapper.map(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ret;
  }
}
