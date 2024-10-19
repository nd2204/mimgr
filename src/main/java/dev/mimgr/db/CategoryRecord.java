package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRecord {
  public int m_id;
  public String m_name;
  public int m_parent_id;

  public CategoryRecord(int id, String name, int parent_id) {
    m_id = id;
    m_name = name;
    m_parent_id = parent_id;
  }

  public CategoryRecord(ResultSet rs) throws SQLException {
    m_id = rs.getInt("id");
    m_name = rs.getString("category_name");
    m_parent_id = rs.getInt("parent_id");
  }
}
