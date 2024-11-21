package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.mimgr.SessionManager;

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
  public static final String QUERY_SELECT_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_SELECT_BY_FIELD = "SELECT * FROM %s WHERE %s = ?";
  public static final String QUERY_SELECT_LIKE_NAME = String.format("SELECT * FROM %s WHERE %s LIKE ?", TABLE, FIELD_NAME);
  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
    TABLE, FIELD_NAME, FIELD_URL, FIELD_CAPTION, FIELD_AUTHOR
  );
  public static final String QUERY_DELETE_BY_ID = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE, FIELD_ID
  );
  public static final String QUERY_UPDATE_BY_KEY = String.format(
    "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
    TABLE, FIELD_NAME, FIELD_CAPTION, FIELD_URL, 
    FIELD_AUTHOR, FIELD_ID
  );
  public static final String QUERY_SELECT_ALL_NEWEST = String.format(
    "SELECT * FROM %s ORDER BY %s DESC",
    TABLE, FIELD_CREATED_AT
  );
  public static final String QUERY_SELECT_ALL_OLDEST = String.format(
    "SELECT * FROM %s ORDER BY %s ASC",
    TABLE, FIELD_CREATED_AT
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

  public static ResultSet selectAll() {
    return DBQueries.select(QUERY_SELECT_ALL);
  }

  public static ImageRecord selectByName(String name) {
    name = name.substring(0, name.indexOf("."));
    try (ResultSet rs = DBQueries.select(
      String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_NAME),
      name)
    ) {
      if (rs == null || !rs.next()) return null;
      return new ImageRecord(rs);
    } catch (SQLException ex) {
      return null;
    }
  }

  public static ResultSet selectByField(String field, String value) {
    return DBQueries.select(String.format(QUERY_SELECT_BY_FIELD, TABLE, field), value);
  }

  public static ImageRecord selectById(int id) {
    try (ResultSet rs = DBQueries.select(QUERY_SELECT_BY_ID, id)) {
      if (rs == null || !rs.next()) return null;
      return new ImageRecord(rs);
    } catch (SQLException ex) {
      return null;
    }
  }

  public static ResultSet selectLikeName(String name) {
    return DBQueries.select(QUERY_SELECT_LIKE_NAME, '%' + name + '%');
  }

  public static int delete(ImageRecord ir) {
    return DBQueries.update(QUERY_DELETE_BY_ID, ir.m_id);
  }

  public static int delete(int id) {
    return DBQueries.update(QUERY_DELETE_BY_ID, id);
  }

  public static int insert(String image_url, String image_name, String image_caption, int image_author) {
    image_name = image_name.substring(0, image_name.indexOf("."));
    int result = DBQueries.update(QUERY_INSERT, image_name, image_url, image_caption, image_author);
    return result;
  }

  public static int insert(ImageRecord ir) {
    ir.m_name = ir.m_name.substring(0, ir.m_name.indexOf("."));
    int result = DBQueries.update(
      QUERY_INSERT,
      ir.m_name,
      ir.m_url,
      ir.m_caption,
      SessionManager.getCurrentUser().m_id,
      ir.m_id
    );
    return result;
  }

  public static int update(String image_name, String image_caption, String image_url, int image_author, int image_id) {
    image_name = image_name.substring(0, image_name.indexOf("."));
    int result = DBQueries.update(QUERY_UPDATE_BY_KEY, image_name, image_caption, image_url, image_author, image_id);
    return result;
  }

  public static int update(ImageRecord ir) {
    ir.m_name = ir.m_name.substring(0, ir.m_name.indexOf("."));
    int result = DBQueries.update(
      QUERY_UPDATE_BY_KEY,
      ir.m_name,
      ir.m_caption,
      ir.m_url,
      SessionManager.getCurrentUser().m_id,
      ir.m_id
    );
    return result;
  }

  public static String getImageAuthor(ImageRecord ir) {
    try (ResultSet rs = UserRecord.selectUserById(ir.m_author)) {
      if (rs.next()) {
        UserRecord ur = new UserRecord(rs);
        return ur.m_username;
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
    return "N/A";
  }

  public static ResultSet selectAllNewest() {
    return DBQueries.select(QUERY_SELECT_ALL_NEWEST);
  }

  public static ResultSet selectAllOldest() {
    return DBQueries.select(QUERY_SELECT_ALL_OLDEST);
  }
}
