package dev.mimgr.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQueries {
  public static final String INSERT_USER = "INSERT INTO users (username, hash, salt) VALUES (?, ?, ?)";
  public static final String SELECT_USER = "SELECT hash, salt FROM users WHERE username=?";

  private static Connection dbcon = DBConnection.get_instance().get_connection();

  public static void insert_user(String username, String hash, String salt) {
    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(INSERT_USER);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, hash);
      preparedStatement.setString(3, salt);
      int result = preparedStatement.executeUpdate();
      System.out.println(result + " row(s) affected");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ResultSet select_user(String username) {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_USER);
      preparedStatement.setString(1, username);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }
}
