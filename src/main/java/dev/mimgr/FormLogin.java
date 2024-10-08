package dev.mimgr;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.BoxLayout;

// import java.nio.file.Paths;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

import dev.mimgr.db.MySQLCon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dev.mimgr.theme.builtin.ColorScheme;

public class FormLogin extends JPanel {
  Connection connection = null;

  JTextField     tf_username;
  JPasswordField pf_password;

  FormLogin(ColorScheme colors) {
    // String DB_URL = "jdbc:mysql://127.0.0.1:3306/admin";
    // String userDB = "root";
    // String passwordDB = "mimgr";
    // connection = new MySQLCon(DB_URL, userDB, passwordDB).get_connection();

    String user_placeholder = "Username";
    String password_placeholder = "Password";

    tf_username = new JTextField();
    tf_username.setBackground(colors.m_bg_3);
    tf_username.setForeground(colors.m_bg_5);
    tf_username.setText(user_placeholder);

    tf_username.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent fe) {
        if (tf_username.getText().equals(user_placeholder)) {
          tf_username.setText("");
          tf_username.setForeground(colors.m_fg_0);
        }

      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        if (tf_username.getText().isEmpty()) {
          tf_username.setText(user_placeholder);
          tf_username.setForeground(colors.m_bg_5);
        }
      }
    });

    pf_password = new JPasswordField();
    pf_password.setBackground(colors.m_bg_3);
    pf_password.setForeground(colors.m_bg_5);
    pf_password.setText(password_placeholder);
    pf_password.setEchoChar('\0');

    pf_password.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent fe) {
        if (String.valueOf(pf_password.getPassword()).equals(password_placeholder)) {
          pf_password.setText("");
          pf_password.setForeground(colors.m_fg_0);
        }

      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        if (pf_password.getPassword().length == 0) {
          pf_password.setForeground(colors.m_bg_5);
          pf_password.setText(password_placeholder);
        }
      }
    });

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_0);

    this.add(tf_username);   //set text field to panel
    this.add(pf_password);   //set text field to panel
  }

  private void authenticate(String username, String password) {
    try {

      PreparedStatement pst = connection.prepareStatement(
        "SELECT hash, salt FROM admins WHERE username = '?'"
      );
      pst.setString(1, username);
      ResultSet rs = pst.executeQuery();

      //if (rs.next()) {
      //  Dashboard page = new Dashboard();
      //  //make page visible to the user
      //  page.setVisible(true);
      //} else {
      //  System.out.println("Please enter valid username and password");
      //}
    } catch (Exception erException) {
      erException.printStackTrace();
    }
  }
}
