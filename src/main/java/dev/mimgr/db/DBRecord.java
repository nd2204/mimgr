package dev.mimgr.db;

public interface DBRecord {
  public String getSelectQuery();
  public String getDeleteQuery(); 
  public String getUpdateQuery(); 
}
