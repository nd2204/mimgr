package dev.mimgr.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLCon extends IDBCon {
  public MySQLCon(String url, String name, String password) {
    this.m_url = url;
    this.m_username = name;
    this.m_password = password;
    this.m_connection = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      this.m_connection = DriverManager.getConnection(m_url, m_username, m_password);
    } catch (SQLException sqlex) {
      System.out.println(sqlex);
    } catch (Exception ex) {
      System.out.println(ex);
    }

    assert this.m_connection != null : "Connection cannot be established";
  }
}
