package dev.mimgr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.nio.file.Paths;
import java.io.*;
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
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER; 
    gbc.weighty = 1.0;

    String user_placeholder = "Username";
    String password_placeholder = "Password";

    tf_username = new JTextField(20);
    tf_username.setBackground(colors.m_bg_1);
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

    pf_password = new JPasswordField(20);
    pf_password.setBackground(colors.m_bg_1);
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

    Border inner_padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    Border roundedBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(colors.m_bg_5, 1, true),
        inner_padding
    );

    tf_username.setBorder(roundedBorder);
    pf_password.setBorder(roundedBorder);

    JButton login_button = new JButton("Login");
    JPanel input_container = new JPanel();
    JLabel login_label = new JLabel("Login", JLabel.CENTER);
    // input_container.setPreferredSize(new Dimension(200, 100));
    input_container.setLayout(new BoxLayout(input_container, BoxLayout.Y_AXIS));
    input_container.add(login_label);
    input_container.add(Box.createVerticalStrut(20));
    input_container.add(tf_username);
    input_container.add(Box.createVerticalStrut(20));
    input_container.add(pf_password);
    input_container.add(Box.createVerticalStrut(20));
    input_container.add(login_button);

    File root = new File(".");
    for (File file : root.listFiles()) {
      System.out.println(file.toString());
    }

    this.setBackground(colors.m_bg_0);
    this.add(input_container);
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
