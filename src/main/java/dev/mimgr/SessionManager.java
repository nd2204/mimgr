package dev.mimgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Properties;

import dev.mimgr.db.DBConnection;
import dev.mimgr.db.DBQueries;
import dev.mimgr.db.UserRecord;
import dev.mimgr.utils.ResourceManager;

public class SessionManager {
  public static void addRememberMeToken(String token, Instant expirationTime, int userId) {
    DBQueries.update(
      "INSERT INTO remember_me_tokens (token_id, user_id, token_value, expiration_time) VALUES (?, ?, ?, ?)",
      Security.generateToken(), userId, token, expirationTime 
    );
  }

  public static void saveSession(String token, Instant expirationTime, int userId) {
    Properties properties = new Properties();
    properties.setProperty(TOKEN_KEY, token);
    addRememberMeToken(token, expirationTime, userId);

    try (FileOutputStream outputStream = new FileOutputStream(TOKEN_FILE)) {
      properties.store(outputStream, "User session data");
    } catch (IOException e) {
      e.printStackTrace();
    }
 
    System.out.println("SAVED TOKEN TO: " + TOKEN_FILE.toString());
  }

  // Load session if a valid token is present and has not expired
  //
  public static UserRecord loadSession() {
    String token = getToken();
    if (token == null || token.isEmpty()) {
      System.err.println(TOKEN_FILE.toString() + " Not exist, Skipping...");
      return null;
    }

    try (ResultSet resultSet = DBQueries.select(SELECT_SESSION_QUERY, token)) {
      if (resultSet.next()) {
          Instant expirationTime = resultSet.getTimestamp("expiration_time").toInstant();
        if (Instant.now().isBefore(expirationTime)) {
          int id = resultSet.getInt("user_id");
          ResultSet rs = UserRecord.selectUserById(id);
          if (rs.next()) {
            return new UserRecord(rs);
          } else {
            return null;
          }
        } else {
          System.err.println("NO TOKEN FOUND WITH THIS DEVICE TOKEN");
          clearSession();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static String getToken() {
    Properties properties = new Properties();
    if (TOKEN_FILE.exists()) {
      try (FileInputStream inputStream = new FileInputStream(TOKEN_FILE)) {
        properties.load(inputStream);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return properties.getProperty(TOKEN_KEY);
    }
    return null;
  }

  public static void clearSession() {
    try {
      int row = DBQueries.update(DELETE_SESSION_QUERY, getToken());
      Files.deleteIfExists(TOKEN_FILE.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static UserRecord getCurrentUser() {
    return currentUser;
  }

  public static void setCurrentUser(UserRecord ur) {
    currentUser = ur;
  }

  private static final String SELECT_SESSION_QUERY = 
    "SELECT user_id, expiration_time FROM remember_me_tokens WHERE token_value = ?";
  private static final String DELETE_SESSION_QUERY = 
    "DELETE FROM remember_me_tokens WHERE token_value = ?";

  private static ResourceManager rm = ResourceManager.getInstance();
  private static UserRecord currentUser = null;
  private static final File TOKEN_FILE = rm.getAppDataDir().resolve("TOKEN").toFile();
  private static final String TOKEN_KEY = "rememberMeToken";
}

