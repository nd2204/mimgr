package dev.mimgr.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import dev.mimgr.db.OrderRecord;
import dev.mimgr.db.OrderItemRecord;
import dev.mimgr.db.ProductRecord;

public class RandomOrderGenerator {
  public static void main(String[] args) {
    // Instant start = Instant.parse("2023-01-01T00:00:00Z");
    createRandomOrder(30, () -> getRandomThis30DayInstant());
    createRandomOrder(30, () -> getRandomPrevious30DayInstant());
  }

  public static void createRandomOrder(int count, Supplier<Instant> timestampGen) {
    Random random = new Random();

    ArrayList<OrderRecord> ors = new ArrayList<>();
    ArrayList<OrderItemRecord> oirs = new ArrayList<>();

    for (int i = 0; i < count; ++i) {
      ors.add(new OrderRecord(0, timestampGen.get(), 0, getRandomPaymentStatus(), getRandomOrderStatus()));
    }
    OrderRecord.insert(ors);

    for (OrderRecord or : ors) {
      System.out.println("[Created Order] " + or);
      int itemCount = random.nextInt(1, 5);
      for (int j = 0; j < itemCount; ++j) {
        OrderItemRecord oir = createRandomOrderItem(OrderRecord.selectByFields(or).m_id);
        System.out.println(oir);
        oirs.add(oir);
      }
    }

    OrderItemRecord.insert(oirs);
  }

  public static Instant getRandomPrevious30DayInstant() {
    Instant end = Instant.now().minus(Duration.ofDays(30)).truncatedTo(ChronoUnit.SECONDS);
    return getRandomInstant(end.minus(Duration.ofDays(30)).truncatedTo(ChronoUnit.SECONDS), end);
  }

  public static Instant getRandomThis30DayInstant() {
    Instant end = Instant.now();
    return getRandomInstant(end.minus(Duration.ofDays(30)).truncatedTo(ChronoUnit.SECONDS), end);
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

  public static OrderItemRecord createRandomOrderItem(int orderId) {
    Random random = new Random();
    ProductRecord randomProduct = getRandomProduct();
    int randomQuantity = random.nextInt(1, 5);
    return new OrderItemRecord(
      0,
      orderId,
      randomProduct.m_id,
      randomQuantity,
      randomProduct.m_price,
      randomProduct.m_price * randomQuantity
    );
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
