package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryRecord {
  public int m_id;
  public String m_name;
  public int m_parent_id;
  public ArrayList<CategoryRecord> childs;

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
    childs = new ArrayList<>();
  }

  public void addChild(CategoryRecord cr) {
    this.childs.add(cr);
  }

  @Override
  public String toString() {
    return m_name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CategoryRecord) {
      CategoryRecord other = (CategoryRecord) obj;
      return this.m_id == other.m_id;  // Match by ID
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(m_id);  // Make sure the hash code is based on ID
  }

  public CategoryRecord(ResultSet rs) throws SQLException {
    m_id = rs.getInt(FIELD_ID);
    m_name = rs.getString(FIELD_NAME);
    m_parent_id = rs.getInt(FIELD_PARENT_ID);
    childs = new ArrayList<>();
  }

  public static ResultSet selectAll() {
    return DBQueries.select(QUERY_SELECT_ALL);
  }

  public static CategoryRecord selectByKey(int id) {
    try (ResultSet rs = DBQueries.select(QUERY_SELECT_BY_KEY, id)) {
      if (!rs.next()) return null;
      return new CategoryRecord(rs);
    } catch (SQLException ex) {
      return null;
    }
  }

  public static CategoryRecord selectByName(String name) {
    try (ResultSet rs = DBQueries.select(QUERY_SELECT_BY_NAME, name)) {
      if (!rs.next()) return null;
      return new CategoryRecord(rs);
    } catch (SQLException ex) {
      return null;
    }
  }
}
