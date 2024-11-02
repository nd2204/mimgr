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
    m_id       = rs.getInt("id");
    m_hash     = rs.getString("hash");
    m_salt     = rs.getString("salt");
    m_username = rs.getString("username");
    m_role     = rs.getString("role");
  }

  public UserRecord(
    int id, String name, String hash, String salt, int role
  ) throws SQLException {
    if (role < 0 || role > ROLE_MAXSIZE) {
      throw new SQLException();
    }
    m_id       = id;
    m_username = name;
    m_hash     = hash;
    m_salt     = salt;
    m_role     = roles[role];
  }

  public static final int ROLE_ADMIN    = 0;
  public static final int ROLE_EMPLOYEE = 1;
  public static final int ROLE_MANAGER  = 2;
  public static final int ROLE_USER     = 3;

  private static final int ROLE_MAXSIZE  = 4;
  private static final String[] roles = {
    "admin", "employee", "manager", "user"
  };
}
