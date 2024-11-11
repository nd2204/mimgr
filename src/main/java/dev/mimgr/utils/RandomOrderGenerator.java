package dev.mimgr.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

import dev.mimgr.db.OrderRecord;
import dev.mimgr.db.OrderItemRecord;
import dev.mimgr.db.ProductRecord;

public class RandomOrderGenerator {
  public static void main(String[] args) {
    Instant start = Instant.parse("2020-01-01T00:00:00Z");
    Instant end = Instant.now();
    Random random = new Random();
    for (int i = 0; i < 10; ++i) {
      OrderRecord or = new OrderRecord(0, getRandomInstant(start, end), 0, getRandomPaymentStatus(), getRandomOrderStatus());
      OrderRecord.insert(or);
      System.out.println("[Created Order] " + or);

      int itemCount = random.nextInt(1, 10);
      OrderRecord ordb = OrderRecord.selectByFields(or);
      for (int j = 0; j < itemCount; ++j) {
        createRandomOrderItem(ordb.m_id);
      }
    }
  }

  public static Instant getRandomInstant(Instant start, Instant end) {
    long durationInSeconds = Duration.between(start, end).getSeconds();
    long randomSeconds = (long) (Math.random() * durationInSeconds);
    return start.plusSeconds(randomSeconds);
  }

  public static String getRandomPaymentStatus() {
    Random random = new Random();
    return OrderRecord.paymentStatuses[Math.abs(random.nextInt()) % OrderRecord.ORDER_STATUS_MAX];
  }

  public static String getRandomOrderStatus() {
    Random random = new Random();
    return OrderRecord.orderStatuses[Math.abs(random.nextInt()) % OrderRecord.PAYMENT_STATUS_MAX];
  }

  public static void createRandomOrderItem(int orderId) {
    Random random = new Random();
    ProductRecord randomProduct = getRandomProduct();
    int randomQuantity = random.nextInt(1, 10);
    OrderItemRecord orderItemRecord = new OrderItemRecord(
      0,
      orderId,
      randomProduct.m_id,
      randomQuantity,
      randomProduct.m_price,
      randomProduct.m_price * randomQuantity
    );
    OrderItemRecord.insert(orderItemRecord);
  }

  private static ProductRecord getRandomProduct() {
    ArrayList<ProductRecord> prs = new ArrayList<>(50);
    Random random = new Random();
    try (ResultSet rs = ProductRecord.selectAll()) {
      while (rs.next()) {
        prs.add(new ProductRecord(rs));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return prs.get(random.nextInt(0, prs.size()));
  }
}
