package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRecord {
  public int m_id;
  public String m_name;
  public int m_parent_id;

  public static final String TABLE            = "categories";
  public static final String FIELD_ID         = "category_id";
  public static final String FIELD_PARENT_ID  = "parent_id";
  public static final String FIELD_NAME       = "category_name";

  public static final String QUERY_SELECT_ALL = String.format("SELECT * FROM %s", TABLE);
  public static final String QUERY_SELECT_BY_KEY = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_SELECT_BY_NAME = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_NAME);

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

  public static ResultSet selectAll() {
    return DBQueries.select(QUERY_SELECT_ALL);
  }
  
  public static ResultSet selectByKey(int id) {
    return DBQueries.select(QUERY_SELECT_BY_KEY, id);
  }

  public static ResultSet selectByKey(ProductRecord pr) {
    return DBQueries.select(QUERY_SELECT_BY_KEY, pr.m_id);
  }
  
  public static ResultSet selectByName(String name) {
    return DBQueries.select(QUERY_SELECT_BY_NAME, name);
  }

  public static ResultSet selectByName(ProductRecord pr) {
    return DBQueries.select(QUERY_SELECT_BY_NAME, pr.m_name);
  }
}
