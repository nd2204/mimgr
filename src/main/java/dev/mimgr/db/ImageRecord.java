package dev.mimgr.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRecord {
  public int m_id;
  public String m_name;
  public String m_caption;
  public String m_url;
  public String m_created_at;
  public int m_author;

  public static final String TABLE            = "images";
  public static final String FIELD_ID         = "image_id";
  public static final String FIELD_NAME       = "image_name";
  public static final String FIELD_CAPTION    = "image_caption";
  public static final String FIELD_URL        = "image_url";
  public static final String FIELD_CREATED_AT = "image_created_at";
  public static final String FIELD_AUTHOR     = "image_author";

  public static final String QUERY_SELECT_ALL = String.format("SELECT * FROM %s", TABLE);
  public static final String QUERY_SELECT_BY_KEY = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
    TABLE, FIELD_NAME, FIELD_URL, FIELD_CAPTION, FIELD_AUTHOR
  );
  public static final String QUERY_DELETE_BY_KEY = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE, FIELD_ID
  );

  public ImageRecord(ResultSet rs) throws SQLException {
    m_id             = rs.getInt(FIELD_ID);
    m_name           = rs.getString(FIELD_NAME);
    m_caption        = rs.getString(FIELD_CAPTION);
    m_url            = rs.getString(FIELD_URL);
    m_created_at     = rs.getString(FIELD_CREATED_AT);
    m_author         = rs.getInt(FIELD_AUTHOR);
  }

  public ImageRecord(
    int id, String name, String caption, String url,
    String created_at, int author
  ) throws SQLException {
    m_id             = id;
    m_name           = name;
    m_caption        = caption;
    m_url            = url;
    m_created_at     = created_at;
    m_author         = author;
  }

  public static ResultSet selectAll(Connection con) {
    return DBQueries.select(con, QUERY_SELECT_ALL);
  }

  public static int delete(Connection con, ImageRecord ir) {
    return DBQueries.update(con, QUERY_DELETE_BY_KEY, ir.m_id);
  }

  public static int delete(Connection con, int id) {
    return DBQueries.update(con, QUERY_DELETE_BY_KEY, id);
  }

  public static int insert(Connection con, String image_url, String image_name, String image_caption, String image_author) {
    image_name = image_name.substring(0, image_name.indexOf("."));
    return DBQueries.update(con, QUERY_INSERT, image_url, image_name, image_caption, image_author);
  }

  public static int insert(Connection con, ImageRecord ir) {
    ir.m_name = ir.m_name.substring(0, ir.m_name.indexOf("."));
    return DBQueries.update(con, QUERY_INSERT, ir.m_url, ir.m_name, ir.m_caption, ir.m_author);
  }
}
