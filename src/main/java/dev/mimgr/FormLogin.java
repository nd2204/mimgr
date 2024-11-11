package dev.mimgr;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBConnection;
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

    Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);
    // =======================================================
    // Setup Appearance
    // =======================================================

    form_label = new JLabel("Đăng nhập", JLabel.CENTER);
    form_label.setFont(FontManager.getFont("RobotoMonoBold", 20f));
    form_label.setForeground(m_colors.m_fg_0);

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

    this.remember = new MCheckBox("Nhớ phiên đăng nhập");
    this.remember.setFocusable(false);
    this.remember.setFont(font_bold);
    this.remember.setBackground(m_colors.m_bg_1);
    this.remember.setForeground(m_colors.m_grey_2);
    this.remember.setBoxColor(m_colors.m_bg_5);
    this.remember.setBoxHoverColor(m_colors.m_grey_0);
    this.remember.setCheckColor(m_colors.m_blue);
    this.remember.setHorizontalAlignment(SwingConstants.LEFT);
    this.remember.setBoxSelectedColor(m_colors.m_blue);
    this.remember.setBackground(null);

    this.show_password_button = new MButton(IconManager.getIcon("eye_closed.png", 20, 16, m_colors.m_grey_0));
    this.show_password_button.setBorderWidth(2);
    this.show_password_button.setBackground(m_colors.m_bg_0);
    this.show_password_button.setBorderColor(m_colors.m_bg_4);
    this.show_password_button.setForeground(m_colors.m_grey_0);
    this.show_password_button.setHoverBorderColor(m_colors.m_bg_5);
    this.show_password_button.setHoverBackgroundColor(m_colors.m_bg_3);
    this.show_password_button.addActionListener(this);
    // this.show_password_button.setBackground(null);
    // this.show_password_button.setIcon(IconManager.getIcon("eye_closed.png", 20, 16, m_colors.m_blue));
    // this.show_password_button.setBorderColor(null);


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

    // =======================================================
    // Setup Layout
    // =======================================================
    GridBagConstraints c = new GridBagConstraints();

    RoundedPanel input_container = new RoundedPanel();
    input_container.setBackground(m_colors.m_bg_0);
    input_container.setBorder(BorderFactory.createLineBorder(null, 0, true));

    int padding = 40;

    input_container.setLayout(new GridBagLayout());

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(padding, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.0;
    c.gridwidth = 2;
    input_container.add(form_label, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1.0;
    c.gridwidth = 2;
    input_container.add(tf_username, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1.0;
    c.gridwidth = 2;
    input_container.add(pf_password, c);

    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(10, padding, 10, 0);
    c.gridx = 0;
    c.gridy = 3;
    c.weightx = 1.0;
    c.weighty = 0.5;
    c.gridwidth = 1;
    input_container.add(remember, c);

    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(10, 0, 10, padding);
    c.gridx = 1;
    c.gridy = 3;
    c.weightx = 0;
    c.weighty = 0.5;
    c.gridwidth = 1;
    input_container.add(show_password_button, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.gridx = 0;
    c.gridy = 4;
    c.weightx = 1.0;
    c.gridwidth = 2;
    input_container.add(login_button, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.weightx = 0.5;
    c.gridy = 5;
    JSeparator sep = new JSeparator();
    sep.setForeground(m_colors.m_bg_4);
    sep.setBackground(null);
    input_container.add(sep, c);

    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(10, padding, padding, padding);
    c.gridy = 6;
    c.gridx = 0;
    c.weightx = 1.0;
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
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == show_password_button) {
      if (!isShowingPassword) {
        pf_password.setEchoChar('\0');
        show_password_button.setIcon(IconManager.getIcon("eye_opened.png", 20, 16, m_colors.m_blue));
        show_password_button.setHoverBorderColor(m_colors.m_blue);
      } else {
        if (!pf_password.getTextString().isEmpty()) {
          pf_password.setEchoChar('*');
        }
        show_password_button.setIcon(IconManager.getIcon("eye_closed.png", 20, 16, m_colors.m_grey_0));
        show_password_button.setHoverBorderColor(m_colors.m_grey_0);
      }
      isShowingPassword = !isShowingPassword;
      pf_password.setShowingPassword(isShowingPassword);
    }

    if (e.getSource() == login_button) {
      tryLogin();
    }
    if (e.getSource() == signup_button) {
      PanelManager.show("FORM_SIGNUP");
      return;
    }
  }

  private void tryLogin() {
      String username = tf_username.getTextString();
      String password = pf_password.getTextString();

      if (DBConnection.getInstance().getConection() == null) {
        JOptionPane.showMessageDialog(null, "FATAL: Cannot connect to database");
        return;
      }

      if (LoginService.loginUser(username, password, remember.isSelected())) {
        Entry.removeLoginSignup();
        Entry.registerDashBoard(m_colors);
      } else {
        JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không hợp lệ");
      }

      return;
  }

  private void checkFields() {
    if (!tf_username.getTextString().isEmpty() && !pf_password.getTextString().isEmpty()) {
      this.login_button.setBackground(m_colors.m_blue);
      this.login_button.setBorderColor(m_colors.m_blue);
      this.login_button.setDefaultForeground(m_colors.m_fg_1);
      this.login_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.login_button.setEnabled(true);
    } else {
      this.login_button.setEnabled(false);
      this.login_button.setBackground(m_colors.m_bg_4);
      this.login_button.setBorderColor(m_colors.m_bg_4);
      this.login_button.setDefaultForeground(m_colors.m_grey_1);
      this.login_button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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

  private MTextField     tf_username;
  private MPasswordField pf_password;
  private ColorScheme    m_colors;
  private JLabel         form_label;
  private MButton        login_button;
  private MButton        signup_button;
  private MButton        show_password_button;
  private boolean        isShowingPassword = false;
  private MCheckBox      remember;

  private final String username_placeholder = "Tên người dùng";
  private final String password_placeholder = "Mật khẩu";
}
