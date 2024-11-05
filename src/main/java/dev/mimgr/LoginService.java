package dev.mimgr;

import java.time.Duration;
import java.time.Instant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.mimgr.db.DBConnection;
import dev.mimgr.db.UserRecord;

public class LoginService {
  public static boolean loginUser(String username, String password, boolean rememberMe) {
    UserRecord userRecord = authenticate(username, password);
    if (userRecord == null) return false;
    if (rememberMe) {
      String token = Security.generateToken();
      Instant expirationTime = Instant.now().plus(Duration.ofHours(1));
      SessionManager.saveSession(token, expirationTime, userRecord.m_id);
    } else {
      SessionManager.clearSession();
    }
    return true;
  }

  private static UserRecord authenticate(String username, String password) {
    Connection con = DBConnection.get_instance().get_connection();
    ResultSet result = UserRecord.selectUserByName(con, username);
    try {
      while (result.next()) {
        UserRecord userRecord = new UserRecord(result);
        if (Security.hash_string(password + userRecord.m_salt).equals(userRecord.m_hash)) {
          return userRecord;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void logoutUser() {
    SessionManager.clearSession();
    System.out.println("User logged out, session cleared.");
  }
}
