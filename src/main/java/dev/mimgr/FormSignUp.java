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

import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.db.DBConnection;
import dev.mimgr.db.DBQueries;

public class FormSignUp extends JPanel implements ActionListener {
  JTextField     tf_username;
  JPasswordField pf_password;
  JPasswordField pf_password_confirm;
  ColorScheme    m_colors;
  JLabel         form_label;
  JButton        login_button;
  JButton        signup_button;
  Connection     m_connection;

  private final String username_placeholder = "Tên người dùng";
  private final String password_placeholder = "Mật khẩu";
  private final String password_confirm_placeholder = "Xác nhận mật khẩu";

  FormSignUp(ColorScheme colors) {
    m_colors = colors;
    m_connection = DBConnection.get_instance().get_connection();


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
    input_container.add(pf_password_confirm, c);
    c.gridy = 4;
    input_container.add(signup_button, c);
    c.gridy = 5;
    JSeparator sep = new JSeparator();
    sep.setForeground(m_colors.m_bg_2);
    input_container.add(sep, c);
    c.gridy = 6;
    c.insets = new Insets(5, 20, 20, 20);
    input_container.add(login_button, c);

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

  private void setup_form_style() {
    // ========================= Label =========================

    form_label = new JLabel("Đăng ký", JLabel.CENTER);

    // ========================= Fields =========================

    tf_username = FormBuilder.create_text_field(m_colors, username_placeholder, 20);
    pf_password = FormBuilder.create_password_field(m_colors, password_placeholder, 20);
    pf_password_confirm = FormBuilder.create_password_field(m_colors, password_confirm_placeholder, 20);
    Border rounded_border = FormBuilder.create_rounded_border(m_colors.m_bg_5, 2);

    this.tf_username.setBorder(rounded_border);
    this.pf_password.setBorder(rounded_border);
    this.pf_password_confirm.setBorder(rounded_border);

    // ========================= Buttons =========================
    this.signup_button = new JButton("Tạo tài khoản");
    this.signup_button.setBackground(m_colors.m_bg_5);
    this.signup_button.setForeground(m_colors.m_bg_1);
    this.signup_button.setBorder(rounded_border);
    this.signup_button.addActionListener(this);

    this.login_button = new JButton("Trở lại đăng nhập");
    this.login_button.setBackground(m_colors.m_bg_0);
    this.login_button.setForeground(m_colors.m_bg_5);
    this.login_button.setBorder(rounded_border);
    this.login_button.addActionListener(this);
  }

  private String get_username() {
    String username = tf_username.getText();
    if (username == username_placeholder) {
      return "";
    }
    return username;
  }

  private String get_password() {
    String password = String.valueOf(pf_password.getPassword());
    if (password.equals(password_placeholder)) {
      return "";
    }
    return password;
  }

  private boolean valid_username(String username) {
    if (DBQueries.select_user(username) != null) {
      JOptionPane.showMessageDialog(null, "Tên người dùng đã tồn tại");
      return false;
    }

    if (!username.matches("^[a-zA-Z0-9._]{5,20}$")) {
      JOptionPane.showMessageDialog(
        null,
        "Tên người dùng phải chứa từ 5-20 ký tự"
      );
      return false; 
    }

    if (!username.matches("^(?!.*[_.]{2}).*$")) {
      JOptionPane.showMessageDialog(
        null,
        "Tên không được bao gồm hai dấu (_) hoặc (.) liền nhau"
      );
      return false; 
    }

    if (!username.matches("^[^_.].*[^_.]$")) {
      JOptionPane.showMessageDialog(
        null,
        "Tên không được bắt đầu bằng ký tự gạch dưới (_) hoặc dấu chấm (.)"
      );
      return false; 
    }

    return true;
  }

  private boolean valid_password(String password) {
    if (password.length() < 8) {
      JOptionPane.showMessageDialog(
        null,
        "Mật khẩu phải chứa ít nhất 8 ký tự"
      );
      return false;
    }

    if (!password.matches(".*[!@#$%^&*]")) {
      JOptionPane.showMessageDialog(
        null,
        "Mật khẩu phải chứa ít nhất 1 trong các ký tự đặc biệt: !,@,#,$,%,^,&,*"
      );
      return false; 
    }

    if (!String.valueOf(pf_password_confirm.getPassword()).equals(password)) {
      JOptionPane.showMessageDialog(
        null,
        "Mật khẩu không trùng khớp"
      );
      return false; 
    }

    return true;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == login_button) {
      PanelManager.show("FORM_LOGIN");
      return;
    }

    if (e.getSource() == signup_button) {
      String username = get_username();
      String password = get_password();
      String salt = Security.generate_salt(16);
      if (valid_username(username) && valid_password(password)) {
        DBQueries.insert_user(username, Security.hash_string(password + salt), salt);
        PanelManager.show("FORM_LOGIN");
        return;
      }
    }
  }
}
