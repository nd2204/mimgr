package dev.mimgr.db;

import java.sql.DriverManager;

public class MySQLCon extends IDBCon {
  public MySQLCon(String url, String name, String password) {
    this.m_url = url;
    this.m_username = name;
    this.m_username = password;

    try {
      this.m_connection = DriverManager.getConnection(m_url, m_username, m_password);
    } catch (Exception ex) {
      System.out.println(ex);
    }

  }
}
