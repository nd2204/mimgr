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
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBConnection;
import dev.mimgr.db.DBQueries;
import dev.mimgr.custom.MPasswordField;
import dev.mimgr.custom.MTextField;

public class FormSignUp extends JPanel implements ActionListener {
  MTextField     tf_username;
  MPasswordField pf_password;
  MPasswordField pf_password_confirm;
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
    RoundedPanel input_container = new RoundedPanel();
    input_container.setBackground(m_colors.m_bg_0);
    input_container.setBorder(BorderFactory.createLineBorder(null, 0, true));
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;

    int padding = 40;
    input_container.setLayout(new GridBagLayout());
    c.insets = new Insets(padding, padding, 25, padding);
    c.gridy = 0;
    input_container.add(form_label, c);
    c.insets = new Insets(10, padding, 10, padding);
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
    c.insets = new Insets(5, padding, padding, padding);
    input_container.add(login_button, c);

    this.setLayout(new GridBagLayout());
    this.setBackground(colors.m_bg_dim);
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
    form_label.setFont(FontManager.getFont("RobotoMonoBold", 20f));

    // ========================= Fields =========================

    tf_username = FormBuilder.create_text_field(m_colors, username_placeholder, 30);
    tf_username.setIcon(IconManager.getIcon("user.png", 20, 20, m_colors.m_bg_5), MTextField.ICON_PREFIX);
    pf_password = FormBuilder.create_password_field(m_colors, password_placeholder, 30);
    pf_password.setIcon(IconManager.getIcon("lock_locked.png", 20, 20, m_colors.m_bg_5), MTextField.ICON_PREFIX);
    pf_password_confirm = FormBuilder.create_password_field(m_colors, password_confirm_placeholder, 20);
    pf_password_confirm.setIcon(IconManager.getIcon("lock_locked.png", 20, 20, m_colors.m_bg_5), MTextField.ICON_PREFIX);
    Border rounded_border = FormBuilder.create_rounded_border(m_colors.m_bg_5, 2);

    // ========================= Buttons =========================
    this.signup_button = new JButton("Tạo tài khoản");
    this.signup_button.setFont(FontManager.getFont("RobotoMonoBold", 14f));
    this.signup_button.setBackground(m_colors.m_bg_5);
    this.signup_button.setForeground(m_colors.m_bg_0);
    this.signup_button.setBorder(rounded_border);
    this.signup_button.setFocusable(false);
    this.signup_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.signup_button.addActionListener(this);

    this.login_button = new JButton("Trở lại đăng nhập");
    this.login_button.setFont(FontManager.getFont("RobotoMonoBold", 14f));
    this.login_button.setBackground(m_colors.m_bg_0);
    this.login_button.setForeground(m_colors.m_bg_5);
    this.login_button.setBorder(rounded_border);
    this.login_button.setFocusable(false);
    this.login_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.login_button.addActionListener(this);
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
      String username = FormBuilder.get_textfield_value(tf_username, username_placeholder);
      String password = FormBuilder.get_passwordfield_value(pf_password, password_placeholder);
      String salt = Security.generate_salt(16);
      if (valid_username(username) && valid_password(password)) {
        DBQueries.insert_user(username, Security.hash_string(password + salt), salt);
        PanelManager.unregister_panel("FORM_LOGIN");
        PanelManager.unregister_panel("FORM_SIGNUP");
        return;
      }
    }
  }
}
