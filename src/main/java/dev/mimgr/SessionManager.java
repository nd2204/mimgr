package dev.mimgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Properties;

import dev.mimgr.component.NotificationPopup;
import dev.mimgr.db.SessionRecord;
import dev.mimgr.db.UserRecord;
import dev.mimgr.utils.ResourceManager;

public class SessionManager {
  public static void saveSession(String token, Instant expirationTime, int userId) {
    Properties properties = new Properties();
    properties.setProperty(TOKEN_KEY, token);
    SessionRecord.insert(Security.generateToken(), token, userId, expirationTime);

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

    try (ResultSet resultSet = SessionRecord.selectByToken(token)) {
      if (resultSet == null) return null;
      if (!resultSet.next()) return null;

      SessionRecord sr = new SessionRecord(resultSet);
      if (Instant.now().isBefore(sr.m_expiration_time)) {
        ResultSet rs = UserRecord.selectUserById(sr.m_user_id);
        if (!rs.next()) return null;
        UserRecord ur = new UserRecord(rs);
        setCurrentUser(ur);
        return ur;
      } else {
        System.err.println("NO TOKEN FOUND WITH THIS DEVICE TOKEN");
        clearSession();
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
      SessionRecord.deleteByToken(getToken());
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
    System.out.println("Logged in as user: " + ur.m_username + " as role: " + ur.m_role);
    PanelManager.createPopup(new NotificationPopup("Chào mừng trở lại " + ur.m_username, NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
  }

  private static ResourceManager rm = ResourceManager.getInstance();
  private static UserRecord currentUser = null;
  private static final File TOKEN_FILE = rm.getAppDataDir().resolve("TOKEN").toFile();
  private static final String TOKEN_KEY = "rememberMeToken";
}

