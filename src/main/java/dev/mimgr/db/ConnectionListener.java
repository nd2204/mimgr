package dev.mimgr.db;

import java.sql.Connection;

public interface ConnectionListener {
  public void onConnected(Connection con);
  public void onClosed();
}
