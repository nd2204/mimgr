package dev.mimgr.db;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBQueries {
  public static final String sqlPath = "mimgrdb/init.sql";
  private static Connection dbcon = DBConnection.getInstance().getConection();

  public static final String QUERY_SELECT_BY_FIELD = "SELECT * FROM %s WHERE %s = ?";
  public static final String QUERY_SELECT_LIKE_FIELD = "SELECT * FROM %s WHERE %s LIKE ?";

  public static ResultSet selectAllFromTableByField(String table, String field, String value) {
    return DBQueries.select(String.format(DBQueries.QUERY_SELECT_BY_FIELD, table, field), value);
  }

  public static ResultSet selectAllFromTableLikeField(String table, String field, String value) {
    return DBQueries.select(String.format(DBQueries.QUERY_SELECT_BY_FIELD, table, field), value);
  }

  public static int update(String stmt, Object...args) {
    int totalAffectedRows = 0;
    dbcon = DBConnection.getInstance().getConection();
    if (dbcon == null) return 0;
    try (PreparedStatement preparedStatement = dbcon.prepareStatement(stmt)) {
      for (int i = 1; i < args.length + 1; ++i) {
        preparedStatement.setObject(i, args[i-1]);
      }
      totalAffectedRows = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return totalAffectedRows;
  }

  public static int batchUpdate(String stmt, List<Object[]>batchArgs) {
    int totalAffectedRows = 0;
    dbcon = DBConnection.getInstance().getConection();
    if (dbcon == null) return 0;
    try (PreparedStatement preparedStatement = dbcon.prepareStatement(stmt)) {
      // Iterate over each batch of arguments
      for (Object[] args : batchArgs) {
        for (int i = 0; i < args.length; ++i) {
          preparedStatement.setObject(i + 1, args[i]); // Set parameters
        }
        preparedStatement.addBatch(); // Add to batch
      }

      // Execute batch
      int[] batchResults = preparedStatement.executeBatch();

      // Calculate total affected rows
      for (int rows : batchResults) {
        if (rows >= 0) {
          totalAffectedRows += rows; // Add affected rows for successful statements
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return totalAffectedRows;
  }

  public static ResultSet select(String stmt, Object... args) {
    ResultSet resultSet = null;
    dbcon = DBConnection.getInstance().getConection();
    if (dbcon == null) return null;
    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(stmt);
      for (int i = 0; i < args.length; ++i) {
        preparedStatement.setObject(i + 1, args[i]);
      }
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }

  public static void writeSQLToFile(String filePath, String sql) {
    try (FileWriter fileWriter = new FileWriter(filePath, true)) { // Mở tệp ở chế độ ghi nối tiếp (append)
      fileWriter.write(sql + "\n"); // Thêm câu lệnh SQL vào tệp
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
