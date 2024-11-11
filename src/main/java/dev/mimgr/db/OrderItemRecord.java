package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRecord {
  public int     m_id;
  public int     m_order_id;
  public int     m_product_id;
  public int     m_quantity;
  public double  m_unit_price;
  public double  m_total_price;

  public static final String TABLE                 = "order_items";
  public static final String FIELD_ID              = "order_item_id";
  public static final String FIELD_ORDER_ID        = "order_id";
  public static final String FIELD_PRODUCT_ID      = "product_id";
  public static final String FIELD_QUANTITY        = "quantity";
  public static final String FIELD_UNIT_PRICE      = "unit_price";
  public static final String FIELD_TOTAL_PRICE     = "total_price";

  public static final String QUERY_SELECT_ALL = String.format("SELECT * FROM %s", TABLE);
  public static final String QUERY_SELECT_BY_KEY = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_SELECT_BY_ORDER_ID = String.format(
    "SELECT * FROM %s WHERE %s = ?",
    TABLE,
    FIELD_ORDER_ID
  );

  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
    TABLE, FIELD_ORDER_ID, FIELD_PRODUCT_ID, FIELD_QUANTITY, FIELD_UNIT_PRICE, FIELD_TOTAL_PRICE
  );
  public static final String QUERY_DELETE_BY_KEY = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE, FIELD_ID
  );

  public OrderItemRecord(ResultSet rs) throws SQLException {
    m_id          = rs.getInt(FIELD_ID);
    m_order_id    = rs.getInt(FIELD_ORDER_ID);
    m_product_id  = rs.getInt(FIELD_PRODUCT_ID);
    m_quantity    = rs.getInt(FIELD_QUANTITY);
    m_unit_price  = rs.getDouble(FIELD_UNIT_PRICE);
    m_total_price = rs.getDouble(FIELD_TOTAL_PRICE);
  }

  public OrderItemRecord(int id, int orderId, int productId, int quanity, double unitPrice, double totalPrice) {
    m_id          = id;
    m_order_id    = orderId;
    m_product_id  = productId;
    m_quantity    = quanity;
    m_unit_price  = unitPrice;
    m_total_price = totalPrice;
  }


  public static ResultSet selectAll() {
    return DBQueries.select(QUERY_SELECT_ALL);
  }

  public static ResultSet selectByKey(int id) {
    return DBQueries.select(QUERY_SELECT_BY_KEY, id);
  }

  public static ResultSet selectByOrderId(int id) {
    return DBQueries.select(QUERY_SELECT_BY_ORDER_ID, id);
  }

  public static ResultSet selectByKey(OrderItemRecord pr) {
    return DBQueries.select(QUERY_SELECT_BY_KEY, pr.m_id);
  }

  public static int delete(OrderItemRecord pr) {
    return DBQueries.update(QUERY_DELETE_BY_KEY, pr.m_id);
  }

  public static int delete(int id) {
    return DBQueries.update(QUERY_DELETE_BY_KEY, id);
  }

  public static int insert(int orderId, int productId, int quantity, double unit, double total) {
    return DBQueries.update(QUERY_INSERT, orderId, productId, quantity, unit, total);
  }

  public static int insert(OrderItemRecord oir) {
    return DBQueries.update(
      QUERY_INSERT,
      oir.m_order_id,
      oir.m_product_id,
      oir.m_quantity,
      oir.m_unit_price,
      oir.m_total_price
    );
  }
}
