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

public class FormSignUp extends JPanel implements ActionListener {
  JTextField     tf_username;
  JPasswordField pf_password;
  JPasswordField pf_password_confirm;
  ColorScheme    m_colors;
  JLabel         form_label;
  JButton        login_button;
  JButton        signup_button;
  Connection     connection;

  FormSignUp(ColorScheme colors) {
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

    form_label = new JLabel("Sign Up", JLabel.CENTER);

    // ========================= Fields =========================

    tf_username = FormBuilder.create_text_field(m_colors, "Username", 20);
    pf_password = FormBuilder.create_password_field(m_colors, "Password", 20);
    pf_password_confirm = FormBuilder.create_password_field(m_colors, "Confirm password", 20);
    Border rounded_border = FormBuilder.create_rounded_border(m_colors.m_bg_5, 2);

    this.tf_username.setBorder(rounded_border);
    this.pf_password.setBorder(rounded_border);
    this.pf_password_confirm.setBorder(rounded_border);

    // ========================= Buttons =========================
    this.signup_button = new JButton("Create Account");
    this.signup_button.setBackground(m_colors.m_bg_5);
    this.signup_button.setForeground(m_colors.m_bg_1);
    this.signup_button.setBorder(rounded_border);
    this.signup_button.addActionListener(this);

    this.login_button = new JButton("Back to login");
    this.login_button.setBackground(m_colors.m_bg_0);
    this.login_button.setForeground(m_colors.m_bg_5);
    this.login_button.setBorder(rounded_border);
    this.login_button.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == login_button) {
      PanelManager.show("FORM_LOGIN");
      return;
    }
    if (e.getSource() == signup_button) {
      PanelManager.show("FORM_LOGIN");
      return;
    }
  }
}
