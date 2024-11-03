package dev.mimgr.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQueries {
  public static final String INSERT_USER = "INSERT INTO users (username, hash, salt) VALUES (?, ?, ?)";
  public static final String SELECT_USER = "SELECT hash, salt FROM users WHERE username=?";
  public static final String INSERT_INSTRUMENT = "INSERT INTO products (name, price, description, stock_quantity, category_id) VALUES (?, ?, ?, ?, ?)";
  public static final String SELECT_ALL_INTRUMENTS = "SELECT * FROM products";
  public static final String SELECT_INTRUMENTS = "SELECT * FROM products where name LIKE ?";
  public static final String SELECT_ALL_CATEGORIES = "SELECT category_id, category_name FROM categories WHERE category_name IS NOT NULL";
  public static final String SELECT_ID_CATEGORY = "SELECT category_id FROM categories WHERE category_name=?";
  public static final String SELECT_ALL_IMAGES = "SELECT * FROM images";
  public static final String INSERT_IMAGE = "INSERT INTO images (image_url, image_name, image_caption) VALUES (?, ?, ?)";

  private static Connection dbcon = DBConnection.get_instance().get_connection();

  public static void insert_user(String username, String hash, String salt) {
    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(INSERT_USER);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, hash);
      preparedStatement.setString(3, salt);
      int result = preparedStatement.executeUpdate();
      System.out.println(result + " row(s) affected");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ResultSet select_user(String username) {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_USER);
      preparedStatement.setString(1, username);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }

  public static void insert_product(String name, Double price, String description, int stock_quantity, int category_id) {
    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(INSERT_INSTRUMENT);
      preparedStatement.setString(1, name);
      preparedStatement.setDouble(2, price);
      preparedStatement.setString(3, description);
      preparedStatement.setInt(4, stock_quantity);
      preparedStatement.setInt(5, category_id);

      int result = preparedStatement.executeUpdate();
      System.out.println(result + " row(s) affected");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ResultSet select_all_intruments() {
    ResultSet resultSet = null;
    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_ALL_INTRUMENTS);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }

  public static ResultSet select_intruments(String name) {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_INTRUMENTS);
      preparedStatement.setString(1, '%' + name + '%');
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }

  public static ResultSet select_all_categories() {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_ALL_CATEGORIES);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }


  public static ResultSet select_id_category(String category_name) {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_ID_CATEGORY);
      preparedStatement.setString(1, category_name);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }

  public static void insert_image(String image_url, String image_name, String image_caption) {
    try {
      image_name = image_name.substring(0, image_name.indexOf("."));
      PreparedStatement preparedStatement = dbcon.prepareStatement(INSERT_IMAGE);
      preparedStatement.setString(1, image_url);
      preparedStatement.setString(2, image_name);
      preparedStatement.setString(3, image_caption);

      int result = preparedStatement.executeUpdate();
      System.out.println(result + " row(s) affected");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ResultSet select_all_images() {
    ResultSet resultSet = null;

    try {
      PreparedStatement preparedStatement = dbcon.prepareStatement(SELECT_ALL_IMAGES);
      resultSet = preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return resultSet;
  }
}
