package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRecord {
  public int m_id;
  public String m_username;
  public String m_hash;
  public String m_salt;

  public UserRecord(ResultSet rs) throws SQLException {
    m_id   = rs.getInt("product_id");
    m_hash = rs.getString("name");
    m_salt = rs.getString("description");
  }

  public UserRecord(
    int id, String name, String hash, String salt
  ) throws SQLException {
    m_id       = id;
    m_username = name;
    m_hash     = hash;
    m_salt     = salt;
  }
}
