package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRecord {
  public int m_id;
  public String m_name;
  public String m_caption;
  public String m_url;
  public String m_created_at;
  public int m_author;

  public ImageRecord(ResultSet rs) throws SQLException {
    m_id             = rs.getInt("id");
    m_name           = rs.getString("image_name");
    m_caption        = rs.getString("image_caption");
    m_url            = rs.getString("image_url");
    m_created_at     = rs.getString("image_created_at");
    m_author         = rs.getInt("image_author");
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
}
