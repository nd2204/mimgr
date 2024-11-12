
package dev.mimgr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class OrderRecord {
  public int     m_id;
  public Instant m_date;
  public double  m_total;
  public String  m_order_status;
  public String  m_payment_status;

  public static final String TABLE                 = "orders";
  public static final String FIELD_ID              = "order_id";
  public static final String FIELD_DATE            = "order_date";
  public static final String FIELD_TOTAL           = "total_amount";
  public static final String FIELD_ORDER_STATUS    = "order_status";
  public static final String FIELD_PAYMENT_STATUS  = "payment_status";

  public static final String QUERY_SELECT_ALL = String.format("SELECT * FROM %s", TABLE);
  public static final String QUERY_SELECT_BY_KEY = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, FIELD_ID);
  public static final String QUERY_SELECT_BY_FIELDS = String.format(
    "SELECT * FROM %s WHERE %s = ? AND %s = ? AND %s = ? AND %s = ?",
    TABLE,
    FIELD_DATE, FIELD_TOTAL, FIELD_ORDER_STATUS, FIELD_PAYMENT_STATUS
  );
  public static final String QUERY_SELECT_LIKE = String.format(
    """
    SELECT *
    FROM %s
    WHERE %s LIKE ?
      OR %s LIKE ?
      OR CAST(%s AS CHAR) LIKE ?
      OR CAST(%s AS CHAR) LIKE ?
      OR CAST(%s AS CHAR) LIKE ?
    """,
    TABLE, FIELD_PAYMENT_STATUS, FIELD_ORDER_STATUS, FIELD_DATE, FIELD_TOTAL, FIELD_ID
  );

  public static final int ORDER_STATUS_OPEN = 0;
  public static final int ORDER_STATUS_ARCHIVED = 1;
  public static final int ORDER_STATUS_CLOSED = 2;
  public static final int ORDER_STATUS_MAX = 3;
  public static final String[] orderStatuses = {
    "Open", "Archived", "Closed"
  };

  public static final int PAYMENT_STATUS_PAID = 0;
  public static final int PAYMENT_STATUS_UNPAID = 1;
  public static final int PAYMENT_STATUS_PENDING = 2;
  public static final int PAYMENT_STATUS_MAX = 3;
  public static final String[] paymentStatuses = {
    "Paid", "Unpaid", "Pending"
  };

  public static final String QUERY_INSERT = String.format(
    "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
    TABLE, FIELD_DATE, FIELD_TOTAL, FIELD_ORDER_STATUS, FIELD_PAYMENT_STATUS
  );
  public static final String QUERY_DELETE_BY_KEY = String.format(
    "DELETE FROM %s WHERE %s = ?",
    TABLE, FIELD_ID
  );

  public OrderRecord(ResultSet rs) throws SQLException {
    m_id             = rs.getInt(FIELD_ID);
    m_date           = rs.getTimestamp(FIELD_DATE).toInstant();
    m_total          = rs.getDouble(FIELD_TOTAL);
    m_order_status   = rs.getString(FIELD_ORDER_STATUS);
    m_payment_status = rs.getString(FIELD_PAYMENT_STATUS);
  }

  public OrderRecord(
    int id, Instant date, double total, String paymentStatus, String orderStatus
  ) {
    m_id             = id;
    m_date           = date;
    m_total          = total;
    m_order_status   = orderStatus;
    m_payment_status = paymentStatus;
  }

  @Override
  public String toString() {
    return
      m_date + ", " +
      m_order_status + ", " +
      m_payment_status;
  }

  public static ResultSet selectAll() {
    return DBQueries.select(QUERY_SELECT_ALL);
  }

  public static ResultSet selectByKey(int id) {
    return DBQueries.select(QUERY_SELECT_BY_KEY, id);
  }

  public static ResultSet selectLike(String target) {
    target = '%' + target + '%';
    return DBQueries.select(QUERY_SELECT_LIKE, target, target, target, target, target);
  }

  public static ResultSet selectByKey(OrderRecord or) {
    return DBQueries.select(QUERY_SELECT_BY_KEY, or.m_id);
  }

  public static OrderRecord selectByFields(OrderRecord or) {
    ResultSet rs = DBQueries.select(
      QUERY_SELECT_BY_FIELDS,
      or.m_date, or.m_total, or.m_order_status, or.m_payment_status
    );
    try {
      if (rs.next()) {
        return new OrderRecord(rs);
      }
    } catch (SQLException e) {}
    return null;
  }

  public static int delete(OrderRecord or) {
    return DBQueries.update(QUERY_DELETE_BY_KEY, or.m_id);
  }

  public static int delete(int id) {
    return DBQueries.update(QUERY_DELETE_BY_KEY, id);
  }

  public static int insert(Instant date, double total, String paymentStatus, String orderStatus) {
    int result = DBQueries.update(QUERY_INSERT, date, total, orderStatus, paymentStatus);
    return result;
  }

  public static int insert(OrderRecord pr) {
    int result = DBQueries.update(QUERY_INSERT, pr.m_date, pr.m_total, pr.m_order_status, pr.m_payment_status);
    return result;
  }
}
