package dev.mimgr.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRecord {
  public int m_id;
  public String m_name;
  public double m_price;
  public String m_description;
  public int m_stock_quantity;
  public int m_category_id;

  public static final String TABLE                 = "products";
  public static final String FIELD_ID              = "product_id";
  public static final String FIELD_NAME            = "name";
  public static final String FIELD_PRICE           = "price";
  public static final String FIELD_DESCRIPTION     = "description";
  public static final String FIELD_STOCK_QUANTITY  = "stock_quantity";
  public static final String FIELD_CATEGORY_ID     = "category_id";

  public static final String QUERY_SELECT_ALL = String.format("SELECT * FROM %s", TABLE);
  public static final String QUERY_SELECT_BY_KEY = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
    TABLE, FIELD_NAME, FIELD_PRICE, FIELD_DESCRIPTION, FIELD_STOCK_QUANTITY, FIELD_CATEGORY_ID
  );
  public static final String QUERY_DELETE_BY_KEY = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE, FIELD_ID
  );
  public static final String QUERY_UPDATE_BY_KEY = String.format(
    "UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
    TABLE, FIELD_NAME, FIELD_PRICE, FIELD_DESCRIPTION, FIELD_STOCK_QUANTITY, FIELD_CATEGORY_ID, FIELD_ID
  );


  public ProductRecord(ResultSet rs) throws SQLException {
    m_id             = rs.getInt(FIELD_ID);
    m_name           = rs.getString(FIELD_NAME);
    m_description    = rs.getString(FIELD_DESCRIPTION);
    m_price          = rs.getDouble(FIELD_PRICE);
    m_stock_quantity = rs.getInt(FIELD_STOCK_QUANTITY);
    m_category_id    = rs.getInt(FIELD_CATEGORY_ID);
  }

  public static ResultSet selectAll(Connection con) {
    return DBQueries.select(con, QUERY_SELECT_ALL);
  }

  public static int delete(Connection con, ProductRecord pr) {
    return DBQueries.update(con, QUERY_DELETE_BY_KEY, pr.m_id);
  }

  public static int delete(Connection con, int id) {
    return DBQueries.update(con, QUERY_DELETE_BY_KEY, id);
  }

  public static int insert(Connection con, String name, Double price, String description, int stock_quantity, int category_id) {
    return DBQueries.update(con, QUERY_INSERT, name, price, description, stock_quantity, category_id);
  }

  public static int insert(Connection con, ProductRecord pr) {
    return DBQueries.update(con, QUERY_INSERT, pr.m_name, pr.m_price, pr.m_description, pr.m_stock_quantity, pr.m_category_id);
  }

  public static int update(Connection con, String name, Double price, String description, int stock_quantity, int category_id, int product_id) {
    return DBQueries.update(con, QUERY_UPDATE_BY_KEY, name, price, description, stock_quantity, category_id, product_id);
  }

  public static int update(Connection con, ProductRecord pr) {
    return DBQueries.update(con, QUERY_UPDATE_BY_KEY, pr.m_name, pr.m_price, pr.m_description, pr.m_stock_quantity, pr.m_category_id, pr.m_id);
  }

  public ProductRecord(
    int id, String name, String description,
    double price, int stock_quantity, int category_id
  ) throws SQLException {
    m_id             = id;
    m_name           = name;
    m_description    = description;
    m_price          = price;
    m_stock_quantity = stock_quantity;
    m_category_id    = category_id;
  }
}
