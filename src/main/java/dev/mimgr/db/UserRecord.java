package dev.mimgr.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRecord {
  public UserRecord(ResultSet rs) throws SQLException {
    m_id       = rs.getInt("id");
    m_hash     = rs.getString("hash");
    m_salt     = rs.getString("salt");
    m_username = rs.getString("username");
    m_role     = rs.getString("role");
  }

  public UserRecord(
    int id, String name, String hash, String salt, int role
  ) {
    m_id       = id;
    m_username = name;
    m_hash     = hash;
    m_salt     = salt;
    m_role     = roles[role];
  }

  public static ResultSet selectUserById(Connection con, int id) {
    return DBQueries.select(con, QUERY_SELECT_BY_ID, id);
  }

  public static ResultSet selectUserByName(Connection con, String username) {
    return DBQueries.select(con, QUERY_SELECT_BY_NAME, username);
  }

  public static ResultSet selectUserByRole(Connection con, int role) {
    return DBQueries.select(con, QUERY_SELECT_BY_ROLE, roles[role]);
  }

  public static int insertUser(
    Connection con, String username,
    String hash, String salt, String role
  ) {
    return DBQueries.update(con, QUERY_INSERT, username, hash, salt, role);
  }

  public static int updateUser(Connection con, int id, String hash, String salt, int role) {
    return DBQueries.update(con, QUERY_UPDATE, hash, salt, role, roles[role]);
  }

  public int m_id;
  public String m_username;
  public String m_hash;
  public String m_salt;
  public String m_role;

  public static String TABLE_NAME     = "users";
  public static String FIELD_ID       = "id";
  public static String FIELD_HASH     = "hash";
  public static String FIELD_SALT     = "salt";
  public static String FIELD_USERNAME = "username";
  public static String FIELD_ROLE     = "role";

  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
    TABLE_NAME, FIELD_USERNAME, FIELD_HASH, FIELD_SALT, FIELD_ROLE
  );
  public static final String QUERY_SELECT_BY_NAME = String.format(
    "SELECT * FROM %s WHERE %s=?;",
    TABLE_NAME, FIELD_USERNAME
  );
  public static final String QUERY_SELECT_BY_ID = String.format(
    "SELECT * FROM %s WHERE %s=?;",
    TABLE_NAME, FIELD_ID
  );
  public static final String QUERY_SELECT_BY_ROLE = String.format(
    "SELECT * FROM %s WHERE %s=?;",
    TABLE_NAME, FIELD_ROLE
  );
  public static final String QUERY_UPDATE = String.format(
    "UPDATE %s SET  %s = ?, %s = ?, %s = ? WHERE %s=?;",
    TABLE_NAME, FIELD_HASH, FIELD_SALT, FIELD_ROLE, FIELD_ID
  );

  public static final int ROLE_ADMIN    = 0;
  public static final int ROLE_EMPLOYEE = 1;
  public static final int ROLE_MANAGER  = 2;
  public static final int ROLE_USER     = 3;
  public static final int ROLE_MAXSIZE  = 4;

  private static final String[] roles = {
    "admin", "employee", "manager", "user"
  };
}
