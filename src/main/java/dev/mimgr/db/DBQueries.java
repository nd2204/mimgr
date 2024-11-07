package dev.mimgr.db;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQueries {
  public static String sqlPath = "mimgrdb/init.sql";
  private static Connection dbcon = DBConnection.get_instance().get_connection();

  /*
   * General db insert function
   * @param con Database connection
   * @return int
   */
  public static int update(String stmt, Object... args) {
    int result = 0;
    dbcon = DBConnection.get_instance().get_connection();
    try (PreparedStatement preparedStatement = dbcon.prepareStatement(stmt)) {
      for (int i = 1; i < args.length + 1; ++i) {
        preparedStatement.setObject(i, args[i-1]);
      }
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static ResultSet select(String stmt, Object... args) {
    ResultSet resultSet = null;
    dbcon = DBConnection.get_instance().get_connection();
    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(stmt);
      for (int i = 1; i < args.length + 1; ++i) {
        preparedStatement.setObject(i, args[i-1]);
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
