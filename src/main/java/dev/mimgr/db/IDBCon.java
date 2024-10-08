package dev.mimgr.db;

import java.sql.Connection;

public abstract class IDBCon {
  protected String m_url;
  protected String m_username;
  protected String m_password;
  protected Connection m_connection = null;

  public Connection get_connection() {
    return this.m_connection; 
  }
}
