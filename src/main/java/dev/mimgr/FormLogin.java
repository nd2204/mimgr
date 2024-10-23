package dev.mimgr;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.MPasswordField;

public class FormLogin extends JPanel implements ActionListener {
  MTextField     tf_username;
  MPasswordField pf_password;
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
    input_container.add(remember, c);
    c.gridy = 4;
    input_container.add(login_button, c);
    c.gridy = 5;
    JSeparator sep = new JSeparator();
    sep.setForeground(m_colors.m_bg_2);
    input_container.add(sep, c);
    c.gridy = 6;
    c.insets = new Insets(5, padding, padding, padding);
    input_container.add(signup_button, c);

    this.setLayout(new GridBagLayout());
    this.setBackground(m_colors.m_bg_dim);
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.weighty = 1.0;
    this.add(input_container, c);
    this.requestFocus();
    this.setVisible(false);
  }

  private boolean is_valid_credential(String username, String password) {
    String salt;
    ResultSet result = DBQueries.select_user(username);
    try {
      while (result.next()) {
        salt = result.getString("salt");
        if (Security.hash_string(password + salt).equals(result.getString("hash"))) {
          return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void setup_form_style() {
    // ========================= Label =========================

    form_label = new JLabel("Đăng nhập", JLabel.CENTER);

    form_label.setFont(FontManager.getFont("RobotoMonoBold", 20f));

    // ========================= Fields =========================

    tf_username = FormBuilder.create_text_field(m_colors, username_placeholder, 30);
    tf_username.setIcon(IconManager.getIcon("user.png", 20, 20, m_colors.m_bg_5), MPasswordField.ICON_PREFIX);
    pf_password = FormBuilder.create_password_field(m_colors, password_placeholder, 30);
    pf_password.setIcon(IconManager.getIcon("lock_locked.png", 20, 20, m_colors.m_bg_5), MPasswordField.ICON_PREFIX);
    Border rounded_border = FormBuilder.create_rounded_border(m_colors.m_bg_5, 2);

    // ========================= Buttons =========================

    this.remember = new JCheckBox("Nhớ phiên đăng nhập");
    this.remember.setFont(FontManager.getFont("RobotoMonoBold", 12f));
    remember.setBackground(null);

    this.login_button = new JButton("Đăng nhập");
    this.login_button.setFont(FontManager.getFont("RobotoMonoBold", 14f));
    this.login_button.setBackground(m_colors.m_bg_5);
    this.login_button.setForeground(m_colors.m_bg_0);
    this.login_button.setBorder(rounded_border);
    this.login_button.setFocusable(false);
    this.login_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.login_button.addActionListener(this);

    this.signup_button = new JButton("Đăng ký");
    this.signup_button.setFont(FontManager.getFont("RobotoMonoBold", 14f));
    this.signup_button.setBackground(m_colors.m_bg_0);
    this.signup_button.setForeground(m_colors.m_bg_5);
    this.signup_button.setBorder(rounded_border);
    this.signup_button.setFocusable(false);
    this.signup_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
