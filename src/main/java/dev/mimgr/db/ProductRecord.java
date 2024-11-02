package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRecord {
  public int m_id;
  public String m_name;
  public String m_description;
  public String m_image_url;
  public double m_price;
  public int m_stock_quantity;
  public int m_category_id;

  public ProductRecord(ResultSet rs) throws SQLException {
    m_id             = rs.getInt("product_id");
    m_name           = rs.getString("name");
    m_description    = rs.getString("description");
    m_price          = rs.getDouble("price");
    m_stock_quantity = rs.getInt("stock_quantity");
    m_category_id    = rs.getInt("category_id");
  }

  public ProductRecord(
    int id, String name, String description, String url,
    double price, int stock_quantity, int category_id
  ) throws SQLException {
    m_id             = id;
    m_name           = name;
    m_description    = description;
    m_image_url      = url;
    m_price          = price;
    m_stock_quantity = stock_quantity;
    m_category_id    = category_id;
  }
}
