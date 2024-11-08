package dev.mimgr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.DatabaseMetaData;

public class DBConnection {

  public static synchronized DBConnection getInstance() {
    return DBConnectionHolder.INSTANCE;
  }

  public synchronized Connection getConection() {
    if (connection == null || !isConnectionValid()) {
      startReconnectThread();
    }
    return connection;
  };


  public void addConnectionListener(ConnectionListener listener) {
    listeners.add(listener);
    if (connection != null) {
      listener.onConnected(connection);
    }
  }

  public void closeConnection() {
    try {
      if (isConnectionValid()) {
        this.connection.close();
        notifyListenerClosed();
        this.connection = null;
        stopReconnectThread();
      }
    } catch (SQLException ex) {
      System.err.println(ex);
    }
  }

  public String getConnectionInfo() {
    if (connection == null) {
      return "No active database connection.";
    }

    try {
      DatabaseMetaData metaData = connection.getMetaData();
      String dbProductName = metaData.getDatabaseProductName();
      String dbProductVersion = metaData.getDatabaseProductVersion();
      String driverName = metaData.getDriverName();
      String driverVersion = metaData.getDriverVersion();
      String url = metaData.getURL();
      String userName = metaData.getUserName();

      return String.format(
        "Connected to %s %s\nDriver: %s %s\nURL: %s\nUser: %s",
        dbProductName, dbProductVersion,
        driverName, driverVersion,
        url, userName
      );
    } catch (SQLException e) {
      return "Unable to retrieve connection information: " + e.getMessage();
    }
  }

  private void tryConnect() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(url, user, password);
      System.out.println("Connection Info:\n" + getConnectionInfo() + "\n");
      notifyListenerConnected();
    } catch (SQLException sqlex) {
      System.err.println("Connection Failed.");
      // System.err.println(sqlex);
    } catch (Exception ex) {
      System.err.println(ex);
    }
  }

   private void startReconnectThread() {
    if (reconnectThread != null && reconnectThread.isAlive()) {
      return; // Already running
    }
    reconnectThread = new Thread(() -> {
      reconnecting = true;
      while (reconnecting) {
        try {
          System.out.println("Attempting to reconnect...");
          tryConnect();
          if (connection != null && isConnectionValid()) {
            System.out.println("Reconnected successfully.");
            stopReconnectThread();
          }
          Thread.sleep(5000); // Attempt to reconnect every 5 seconds
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
      }
    });
    reconnectThread.setDaemon(true);
    reconnectThread.start();
  }

  private boolean isConnectionValid() {
    try {
      if (connection != null && connection.isValid(2)) {
        return true;
      }
    } catch (SQLException ex) {
      System.err.println("Connection validation failed: " + ex.getMessage());
    }
    return false;
  }

  private void stopReconnectThread() {
    reconnecting = false;
    if (reconnectThread != null) {
      reconnectThread.interrupt();
    }
  }


  private void notifyListenerConnected() {
    for (ConnectionListener listener : listeners) {
      listener.onConnected(connection);
    }
  }

  private void notifyListenerClosed() {
    for (ConnectionListener listener : listeners) {
      listener.onClosed();
    }
  }

  private class DBConnectionHolder {
    public static final DBConnection INSTANCE = new DBConnection();
  }

  private DBConnection() {
    connection = null;
    tryConnect();
    if (connection == null) {
      startReconnectThread();
    }
  }

  private boolean reconnecting;
  private Thread reconnectThread;
  private Connection connection;
  private String url      = "jdbc:mysql://127.0.0.1:3306/mimgrdb?allowPublicKeyRetrieval=true&useSSL=true";
  private String user     = "mimgr";
  private String password = "mimgr-db";
  private List<ConnectionListener> listeners = new ArrayList<>();
}
