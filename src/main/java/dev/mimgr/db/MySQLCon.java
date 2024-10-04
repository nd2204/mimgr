package dev.mimgr.db;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLCon extends IDBCon {
    public MySQLCon(String url, String name, String password) {
         try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            // handle the error
        }
        Class.forName("");
        this.m_url = url;
        this.m_username = name;
        this.m_username = name;
    }
}
