package dev.mimgr;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

// import java.nio.file.Paths;
// import java.io.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// import dev.mimgr.db.MySQLCon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormLogin extends JPanel implements ActionListener {
  JTextField     tf_username;
  JPasswordField pf_password;
  ColorScheme    m_colors;
  JLabel         form_label;
  JButton        login_button;
  JButton        signup_button;
  JCheckBox      remember;

  private final String username_placeholder = "Tên người dùng";
  private final String password_placeholder = "Mật khẩu";

  FormLogin(ColorScheme colors) {
    m_colors = colors;

    this.setup_form_style();

    GridBagConstraints c = new GridBagConstraints();
    JPanel input_container = new JPanel();
    input_container.setBackground(m_colors.m_bg_0);
    input_container.setBorder(BorderFactory.createLineBorder(null, 0, true));
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;

    input_container.setLayout(new GridBagLayout());
    c.insets = new Insets(20, 20, 10, 20);
    c.gridy = 0;
    input_container.add(form_label, c);
    c.insets = new Insets(10, 20, 10, 20);
    c.gridy = 1;
    input_container.add(tf_username, c);
    c.gridy = 2;
    input_container.add(pf_password, c);
    c.gridy = 3;
    input_container.add(remember, c);
    c.gridy = 4;
    input_container.add(login_button, c);
    c.gridy = 5;
    JSeparator sep = new JSeparator();
    sep.setForeground(m_colors.m_bg_2);
    input_container.add(sep, c);
    c.gridy = 6;
    c.insets = new Insets(5, 20, 20, 20);
    input_container.add(signup_button, c);

    this.setLayout(new GridBagLayout());
    this.setBackground(colors.m_bg_0);
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.weighty = 1.0;
    this.add(input_container, c);
    this.setVisible(false);
  }

  private boolean is_valid_credential(String username, String password) {
    String salt;

    ResultSet result = DBQueries.select_user(username);
    try {
      salt = result.getString("salt");
      if (Security.hash_string(password + salt).equals(result.getString("hash"))) {
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void setup_form_style() {
    // ========================= Label =========================

    form_label = new JLabel("Login", JLabel.CENTER);

    // ========================= Fields =========================

    tf_username = FormBuilder.create_text_field(m_colors, username_placeholder, 20);
    pf_password = FormBuilder.create_password_field(m_colors, password_placeholder, 20);
    Border rounded_border = FormBuilder.create_rounded_border(m_colors.m_bg_5, 2);

    this.tf_username.setBorder(rounded_border);
    this.pf_password.setBorder(rounded_border);

    // ========================= Buttons =========================

    this.remember = new JCheckBox("Nhớ phiên đăng nhập");
    remember.setBackground(null);

    this.login_button = new JButton("Đăng nhập");
    this.login_button.setBackground(m_colors.m_bg_5);
    this.login_button.setForeground(m_colors.m_bg_1);
    this.login_button.setBorder(rounded_border);
    this.login_button.addActionListener(this);

    this.signup_button = new JButton("Đăng ký");
    this.signup_button.setBackground(m_colors.m_bg_0);
    this.signup_button.setForeground(m_colors.m_bg_5);
    this.signup_button.setBorder(rounded_border);
    this.signup_button.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == login_button) {
      String username = FormBuilder.get_textfield_value(tf_username, username_placeholder);
      String password = FormBuilder.get_passwordfield_value(pf_password, password_placeholder);
      if (username.length() == 0) {
        JOptionPane.showMessageDialog(null, "Tên người dùng không được để trống");
        return;
      }

      if (password.length() == 0) {
        JOptionPane.showMessageDialog(null, "Mật khẩu không được để trống");
        return;
      }

      if (is_valid_credential(username, password)) {
        PanelManager.unregister_panel("FORM_LOGIN");
        PanelManager.unregister_panel("FORM_SIGNUP");
      }
      return;
    }
    if (e.getSource() == signup_button) {
      PanelManager.show("FORM_SIGNUP");
      return;
    }
  }
}
