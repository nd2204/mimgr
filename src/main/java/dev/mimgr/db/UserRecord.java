package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRecord {
  public int m_id;
  public String m_username;
  public String m_hash;
  public String m_salt;
  public String m_role;

  public UserRecord(ResultSet rs) throws SQLException {
    m_id       = rs.getInt("user_id");
    m_hash     = rs.getString("hash");
    m_salt     = rs.getString("salt");
    m_username = rs.getString("user_name");
    m_role     = rs.getString("user_role");
  }

  public UserRecord(
    int id, String name, String hash, String salt, String role
  ) throws SQLException {
    m_id       = id;
    m_username = name;
    m_hash     = hash;
    m_salt     = salt;
    m_role     = role;
  }

  public static final String ROLE_ADMIN = "admin";
  public static final String ROLE_EMPLOYEE = "employee";
  public static final String ROLE_MANAGER = "manager";
  public static final String ROLE_USER = "user";
}
