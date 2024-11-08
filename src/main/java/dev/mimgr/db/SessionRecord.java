package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class SessionRecord {
  public SessionRecord(ResultSet rs) throws SQLException {
    m_id              = rs.getString(FIELD_ID);
    m_value           = rs.getString(FIELD_VALUE);
    m_user_id         = rs.getInt(FIELD_USER_ID);
    m_expiration_time = rs.getTimestamp(FIELD_EXPIRATION).toInstant();
  }

  public static ResultSet selectByToken(String token) {
    return DBQueries.select(QUERY_SELECT_BY_TOKEN, token);
  }

  public static int insert(String id, String token, int userId, Instant expirationTime) {
    int result = DBQueries.update(QUERY_INSERT, id, token, userId, expirationTime);
    return result;
  }

  public static int deleteByToken(String token) {
    int result = DBQueries.update(QUERY_DELETE_SESSION_BY_TOKEN, token);
    return result;
  }

  public String  m_id;
  public String  m_value;
  public int     m_user_id;
  public Instant m_expiration_time;

  public static String TABLE_NAME       = "remember_me_tokens";
  public static String FIELD_ID         = "token_id";
  public static String FIELD_VALUE      = "token_value";
  public static String FIELD_USER_ID    = "user_id";
  public static String FIELD_EXPIRATION = "expiration_time";

  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
    TABLE_NAME, FIELD_ID, FIELD_VALUE, FIELD_USER_ID, FIELD_EXPIRATION
  );
  public static final String QUERY_SELECT_BY_TOKEN = String.format(
    "SELECT * FROM %s WHERE %s = ?",
    TABLE_NAME, FIELD_VALUE
  );
  public static final String QUERY_DELETE_SESSION_BY_TOKEN = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE_NAME, FIELD_VALUE
  );
}
