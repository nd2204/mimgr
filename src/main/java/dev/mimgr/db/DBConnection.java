package dev.mimgr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  private static DBConnection instance = null;
  private Connection connection;

  private String url      = "jdbc:mysql://127.0.0.1:3306/mimgrdb?allowPublicKeyRetrieval=true&useSSL=true";
  private String user     = "mimgr";
  private String password = "mimgr-db";

  private DBConnection() {
    assert instance == null;
    connection = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(url, user, password);
    } catch (SQLException sqlex) {
      System.out.println(sqlex);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    if (connection == null) {
      System.out.println("Không thể kết nối đến cơ sở dữ liệu");
      System.exit(1);
    }
  }

  public static synchronized DBConnection get_instance() {
    if (instance == null) instance = new DBConnection();
    return instance;
  }

  public Connection get_connection() {
    return connection;
  };
}
