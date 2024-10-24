package dev.mimgr;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBConnection;
import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.GradientPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MPasswordField;
import dev.mimgr.custom.MCheckBox;

public class FormLogin extends GradientPanel implements ActionListener, DocumentListener {
  FormLogin(ColorScheme colors) {
    super(new Color(0x1379a9), colors.m_blue);
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
    this.setBackground(null);
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.weighty = 1.0;
    this.add(input_container, c);
    this.setVisible(false);
    this.form_label.requestFocus();
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
    Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);
    // ========================= Label =========================

    form_label = new JLabel("Đăng nhập", JLabel.CENTER);
    form_label.setFont(FontManager.getFont("RobotoMonoBold", 20f));
    form_label.setForeground(m_colors.m_fg_0);

    // ========================= Fields =========================

    tf_username = new MTextField(30);
    tf_username.setInputForeground(m_colors.m_fg_0);
    tf_username.setPlaceholder(username_placeholder);
    tf_username.setBackground(m_colors.m_bg_1);
    tf_username.setBorderColor(m_colors.m_bg_4);
    tf_username.setFocusBorderColor(m_colors.m_blue);
    tf_username.setBorderWidth(2);
    tf_username.setFont(font_bold);
    tf_username.setIcon(
      IconManager.getIcon("user.png", 20, 20, m_colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    tf_username.setPlaceholderForeground(m_colors.m_grey_1);
    tf_username.getDocument().addDocumentListener(this);

    pf_password = new MPasswordField(30);
    pf_password.setInputForeground(m_colors.m_fg_0);
    pf_password.setPlaceholderForeground(m_colors.m_grey_1);
    pf_password.setPlaceholder(password_placeholder);
    pf_password.setBackground(m_colors.m_bg_1);
    pf_password.setBorderColor(m_colors.m_bg_4);
    pf_password.setFocusBorderColor(m_colors.m_blue);
    pf_password.setBorderWidth(2);
    pf_password.setFont(font_bold);
    pf_password.setIcon(
      IconManager.getIcon("lock_locked.png", 20, 20, m_colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    pf_password.getDocument().addDocumentListener(this);

    // ========================= Buttons =========================

    this.remember = new MCheckBox("Nhớ phiên đăng nhập");
    this.remember.setFocusable(false);
    this.remember.setFont(font_bold);
    this.remember.setBackground(m_colors.m_bg_1);
    this.remember.setForeground(m_colors.m_grey_2);
    this.remember.setBoxColor(m_colors.m_bg_5);
    this.remember.setBoxHoverColor(m_colors.m_grey_0);
    this.remember.setCheckColor(m_colors.m_blue);

    this.login_button = new MButton("Đăng nhập");
    this.login_button.setFont(font_bold);
    this.login_button.setBackground(m_colors.m_bg_4);
    this.login_button.setBorderColor(m_colors.m_bg_4);
    this.login_button.setForeground(m_colors.m_grey_1);
    this.login_button.setBorderWidth(2);
    this.login_button.setEnabled(false);
    this.login_button.addActionListener(this);

    this.signup_button = new MButton("Đăng ký");
    this.signup_button.setFont(font_bold);
    this.signup_button.setBorderWidth(2);
    this.signup_button.setBackground(m_colors.m_bg_0);
    this.signup_button.setBorderColor(m_colors.m_bg_4);
    this.signup_button.setForeground(m_colors.m_grey_0);
    this.signup_button.setHoverBorderColor(m_colors.m_bg_5);
    this.signup_button.setHoverBackgroundColor(m_colors.m_bg_3);
    this.signup_button.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == tf_username) {
      System.out.println("Changed");
    }

    if (e.getSource() == login_button) {
      String username = tf_username.getTextString();
      String password = pf_password.getTextString();

      if (DBConnection.get_instance().get_connection() == null) {
        JOptionPane.showMessageDialog(null, "FATAL: Cannot connect to database");
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

  @Override
  public void insertUpdate(DocumentEvent e) {
    checkFields();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    checkFields();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    checkFields();
  }

  private void checkFields() {
    if (!tf_username.getTextString().isEmpty() && !pf_password.getTextString().isEmpty()) {
      this.login_button.setBackground(m_colors.m_blue);
      this.login_button.setBorderColor(m_colors.m_blue);
      this.login_button.setForeground(m_colors.m_fg_1);
      this.login_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.login_button.setEnabled(true);
    } else {
      this.login_button.setEnabled(false);
      this.login_button.setBackground(m_colors.m_bg_4);
      this.login_button.setBorderColor(m_colors.m_bg_4);
      this.login_button.setForeground(m_colors.m_grey_1);
      this.login_button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
  }

  private MTextField     tf_username;
  private MPasswordField pf_password;
  private ColorScheme    m_colors;
  private JLabel         form_label;
  private MButton        login_button;
  private MButton        signup_button;
  private MCheckBox      remember;

  private final String username_placeholder = "Tên người dùng";
  private final String password_placeholder = "Mật khẩu";
}
