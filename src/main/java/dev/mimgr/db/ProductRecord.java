package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRecord {
  public int m_id;
  public String m_name;
  public double m_price;
  public String m_description;
  public int m_stock_quantity;
  public int m_category_id;
  public int m_image_id;

  public static final String TABLE                 = "products";
  public static final String FIELD_ID              = "product_id";
  public static final String FIELD_NAME            = "name";
  public static final String FIELD_PRICE           = "price";
  public static final String FIELD_DESCRIPTION     = "description";
  public static final String FIELD_STOCK_QUANTITY  = "stock_quantity";
  public static final String FIELD_CATEGORY_ID     = "category_id";
  public static final String FIELD_IMAGE_ID        = "image_id";

  public static final String QUERY_SELECT_ALL = String.format("SELECT * FROM %s", TABLE);
  public static final String QUERY_SELECT_BY_KEY = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_SELECT_BY_NAME = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_NAME);
  public static final String QUERY_SELECT_LIKE_NAME = String.format("SELECT * FROM %s WHERE %s LIKE ?", TABLE, FIELD_NAME);

  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
    TABLE, FIELD_NAME, FIELD_PRICE, FIELD_DESCRIPTION, FIELD_STOCK_QUANTITY, FIELD_CATEGORY_ID, FIELD_IMAGE_ID
  );
  public static final String QUERY_DELETE_BY_KEY = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE, FIELD_ID
  );
  public static final String QUERY_UPDATE_BY_KEY = String.format(
    "UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
    TABLE, FIELD_NAME, FIELD_PRICE, FIELD_DESCRIPTION, 
    FIELD_STOCK_QUANTITY, FIELD_CATEGORY_ID, FIELD_IMAGE_ID, FIELD_ID
  );


  public ProductRecord(ResultSet rs) throws SQLException {
    m_id             = rs.getInt(FIELD_ID);
    m_name           = rs.getString(FIELD_NAME);
    m_description    = rs.getString(FIELD_DESCRIPTION);
    m_price          = rs.getDouble(FIELD_PRICE);
    m_stock_quantity = rs.getInt(FIELD_STOCK_QUANTITY);
    m_category_id    = rs.getInt(FIELD_CATEGORY_ID);
    m_image_id       = rs.getInt(FIELD_IMAGE_ID);
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

  public static ResultSet selectLikeName(String name) {
    return DBQueries.select(QUERY_SELECT_LIKE_NAME, '%' + name + '%');
  }

  public static ResultSet selectLikeName(ProductRecord pr) {
    return DBQueries.select(QUERY_SELECT_LIKE_NAME, '%' + pr.m_name + '%');
  }

  public static int delete(ProductRecord pr) {
    return DBQueries.update(QUERY_DELETE_BY_KEY, pr.m_id);
  }

  public static int delete(int id) {
    return DBQueries.update(QUERY_DELETE_BY_KEY, id);
  }

  public static int insert(String name, Double price, String description, int stock_quantity, int category_id, int image_id) {
    int result = DBQueries.update(
      QUERY_INSERT, name, price, description,
      stock_quantity, category_id,
      (image_id < 0) ? null : image_id
    );
    return result;
  }

  public static int insert(ProductRecord pr) {
    int result = DBQueries.update(
      QUERY_INSERT, pr.m_name, pr.m_price, pr.m_description,
      pr.m_stock_quantity, pr.m_category_id,
      (pr.m_image_id < 0) ? null : pr.m_image_id
    );
    return result;
  }

  public static int update(String name, Double price, String description, int stock_quantity, int category_id, int image_id, int product_id) {
    return DBQueries.update(QUERY_UPDATE_BY_KEY, name, price, description, stock_quantity, category_id, image_id, product_id);
  }

  public static int update(ProductRecord pr) {
    return DBQueries.update(QUERY_UPDATE_BY_KEY, pr.m_name, pr.m_price, pr.m_description, pr.m_stock_quantity, pr.m_category_id, pr.m_id);
  }

  public ProductRecord(
    int id, String name, String description,
    double price, int stock_quantity, int category_id, int image_id
  ) throws SQLException {
    m_id             = id;
    m_name           = name;
    m_description    = description;
    m_price          = price;
    m_stock_quantity = stock_quantity;
    m_category_id    = category_id;
    m_image_id       = image_id;
  }
}
