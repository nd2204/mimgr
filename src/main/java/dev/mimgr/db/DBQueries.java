package dev.mimgr.db;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQueries {
  private static String sqlPath = "mimgrdb/init.sql";
  public static final String SELECT_USER = "SELECT hash, salt FROM users WHERE username=?";
  public static final String SELECT_INSTRUMENTS = "SELECT * FROM products where name LIKE ?";
  public static final String SELECT_ID_CATEGORY = "SELECT category_id FROM categories WHERE category_name=?";
  public static final String SELECT_SESSION = "SELECT * FROM sessions WHERE session_id = ?";

  public static final String INSERT_INSTRUMENT = "INSERT INTO products (name, price, description, stock_quantity, category_id) VALUES (?, ?, ?, ?, ?)";
  public static final String INSERT_USER = "INSERT INTO users (username, hash, salt) VALUES (?, ?, ?)";
  public static final String INSERT_IMAGE = "INSERT INTO images (image_url, image_name, image_caption) VALUES (?, ?, ?)";
  public static final String INSERT_SESSION = "INSERT INTO session (image_url, image_name, image_caption) VALUES (?, ?, ?)";

  public static final String SELECT_ALL_INSTRUMENTS = "SELECT * FROM products";
  public static final String SELECT_ALL_CATEGORIES = "SELECT category_id, category_name FROM categories WHERE category_name IS NOT NULL";
  public static final String SELECT_ALL_IMAGES = "SELECT * FROM images";

  private static Connection dbcon = DBConnection.get_instance().get_connection();
  /*
   * General db insert function
   * @param con Database connection
   * @return int
   */
  public static int update(Connection con, String stmt, Object... args) {
    int result = 0;
    try (PreparedStatement preparedStatement = con.prepareStatement(stmt)) {
      for (int i = 1; i < args.length + 1; ++i) {
        preparedStatement.setObject(i, args[i-1]);
      }
      result = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static ResultSet select(Connection con, String stmt, Object... args) {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = con.prepareStatement(stmt);
      for (int i = 1; i < args.length + 1; ++i) {
        preparedStatement.setObject(i, args[i-1]);
      }
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }

  public static void insert_user(String username, String hash, String salt) {
    int result = update(dbcon, INSERT_USER, username, hash, salt);
    System.out.println(result + " row(s) affected");

    // Ghi câu lệnh SQL vào file init.sql
    if (result > 0) {
      writeSQLToFile(sqlPath, String.format(
        "INSERT INTO users (username, hash, salt) VALUES ('%s', '%s', '%s');",
        username, hash, salt)
      );
    }
  }

  public static void insert_product(String name, Double price, String description, int stock_quantity, int category_id) {
    int result = update(dbcon, INSERT_INSTRUMENT, name, price, description, stock_quantity, category_id);
    System.out.println(result + " row(s) affected");

    // Ghi câu lệnh SQL vào file init.sql
    if (result > 0) {
      writeSQLToFile(sqlPath, String.format(
        "INSERT INTO products (name, price, description, stock_quantity, category_id) VALUES ('%s', %.2f, '%s', %d, %d);",
        name, price, description, stock_quantity, category_id)
      );
    }
  }

  public static void insert_image(String image_url, String image_name, String image_caption) {
    image_name = image_name.substring(0, image_name.indexOf("."));
    int result = update(dbcon, INSERT_IMAGE, image_url, image_name, image_caption);

    // Ghi câu lệnh SQL vào file init.sql
    if (result > 0) {
      writeSQLToFile(sqlPath, String.format(
        "INSERT INTO images (image_url, image_name, image_caption) VALUES ('%s', '%s', '%s');",
        image_url, image_name, image_caption)
      );
    }
  }

  public static ResultSet select_user(String username) {
    return select(dbcon, SELECT_USER, username);
  }

  public static ResultSet select_all_intruments() {
    return select(dbcon, SELECT_ALL_INSTRUMENTS);
  }

  public static ResultSet select_intruments(String name) {
    return select(dbcon, SELECT_INSTRUMENTS, '%' + name + '%');
  }

  public static ResultSet select_all_categories() {
    return select(dbcon, SELECT_ALL_CATEGORIES);
  }

  public static ResultSet select_id_category(String category_name) {
    return select(dbcon, SELECT_ID_CATEGORY, category_name);
  }

  public static ResultSet select_all_images() {
    return select(dbcon, SELECT_ALL_IMAGES);
  }

  private static void writeSQLToFile(String filePath, String sql) {
    try (FileWriter fileWriter = new FileWriter(filePath, true)) { // Mở tệp ở chế độ ghi nối tiếp (append)
      fileWriter.write(sql + "\n"); // Thêm câu lệnh SQL vào tệp
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
