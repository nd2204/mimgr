package dev.mimgr;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.GradientPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MPasswordField;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBConnection;
import dev.mimgr.db.UserRecord;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormSignUp extends GradientPanel implements ActionListener, DocumentListener {
  FormSignUp(ColorScheme colors) {
    super(colors.m_green, new Color(0x357b01));
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

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.gridx = 0;
    c.gridy = 3;
    c.weightx = 1.0;
    c.gridwidth = 2;
    input_container.add(pf_password_confirm, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, 10);
    c.gridx = 0;
    c.gridy = 4;
    c.weightx = 1.0;
    c.gridwidth = 1;
    input_container.add(signup_button, c);

    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(10, 0, 10, padding);
    c.gridx = 1;
    c.gridy = 4;
    c.weightx = 0;
    c.weighty = 0.5;
    c.gridwidth = 1;
    input_container.add(show_password_button, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.gridx = 0;
    c.gridy = 5;
    c.weightx = 1.0;
    c.gridwidth = 2;
    JSeparator sep = new JSeparator();
    sep.setForeground(m_colors.m_bg_4);
    sep.setBackground(null);
    input_container.add(sep, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, padding, padding);
    c.gridx = 0;
    c.gridy = 6;
    c.weightx = 1.0;
    c.gridwidth = 2;
    input_container.add(login_button, c);

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

  private void setup_form_style() {
    Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);
    // ========================= Label =========================

    form_label = new JLabel("Đăng ký", JLabel.CENTER);
    form_label.setFont(FontManager.getFont("RobotoMonoBold", 20f));
    form_label.setForeground(m_colors.m_fg_0);

    // ========================= Fields =========================

    tf_username = new MTextField(30);
    tf_username.setInputForeground(m_colors.m_fg_0);
    tf_username.setPlaceholderForeground(m_colors.m_grey_1);
    tf_username.setPlaceholder(username_placeholder);
    tf_username.setBackground(m_colors.m_bg_1);
    tf_username.setBorderColor(m_colors.m_bg_4);
    tf_username.setFocusBorderColor(m_colors.m_green);
    tf_username.setBorderWidth(2);
    tf_username.setFont(font_bold);
    tf_username.setIcon(
      IconManager.getIcon("user.png", 20, 20, m_colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    tf_username.getDocument().addDocumentListener(this);

    pf_password = new MPasswordField(30);
    pf_password.setInputForeground(m_colors.m_fg_0);
    pf_password.setPlaceholderForeground(m_colors.m_grey_1);
    pf_password.setPlaceholder(password_placeholder);
    pf_password.setBackground(m_colors.m_bg_1);
    pf_password.setBorderColor(m_colors.m_bg_4);
    pf_password.setFocusBorderColor(m_colors.m_green);
    pf_password.setBorderWidth(2);
    pf_password.setFont(font_bold);
    pf_password.setIcon(
      IconManager.getIcon("lock_locked.png", 20, 20, m_colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    pf_password.getDocument().addDocumentListener(this);

    pf_password_confirm = new MPasswordField(30);
    pf_password_confirm.setInputForeground(m_colors.m_fg_0);
    pf_password_confirm.setPlaceholderForeground(m_colors.m_grey_1);
    pf_password_confirm.setPlaceholder(password_confirm_placeholder);
    pf_password_confirm.setBackground(m_colors.m_bg_1);
    pf_password_confirm.setBorderColor(m_colors.m_bg_4);
    pf_password_confirm.setFocusBorderColor(m_colors.m_green);
    pf_password_confirm.setBorderWidth(2);
    pf_password_confirm.setFont(font_bold);
    pf_password_confirm.setIcon(
      IconManager.getIcon("lock_locked.png", 20, 20, m_colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    pf_password_confirm.getDocument().addDocumentListener(this);

    // ========================= Buttons =========================
    this.show_password_button = new MButton(IconManager.getIcon("eye_closed.png", 20, 16, m_colors.m_grey_0));
    this.show_password_button.setBorderWidth(2);
    this.show_password_button.setBackground(m_colors.m_bg_0);
    this.show_password_button.setBorderColor(m_colors.m_bg_4);
    this.show_password_button.setForeground(m_colors.m_grey_0);
    this.show_password_button.setHoverBorderColor(m_colors.m_bg_5);
    this.show_password_button.setHoverBackgroundColor(m_colors.m_bg_3);
    this.show_password_button.addActionListener(this);

    this.signup_button = new MButton("Tạo tài khoản");
    this.signup_button.setFont(font_bold);
    this.signup_button.setBackground(m_colors.m_bg_4);
    this.signup_button.setBorderColor(m_colors.m_bg_4);
    this.signup_button.setForeground(m_colors.m_grey_1);
    this.signup_button.setBorderWidth(2);
    this.signup_button.setEnabled(false);
    this.signup_button.addActionListener(this);

    this.login_button = new MButton("Trở lại đăng nhập");
    this.login_button.setFont(font_bold);
    this.login_button.setBorderWidth(2);
    this.login_button.setBackground(m_colors.m_bg_0);
    this.login_button.setBorderColor(m_colors.m_bg_4);
    this.login_button.setForeground(m_colors.m_grey_0);
    this.login_button.setHoverBorderColor(m_colors.m_bg_5);
    this.login_button.setHoverBackgroundColor(m_colors.m_bg_3);
    this.login_button.addActionListener(this);
  }

  private boolean valid_username(String username) {
    try (ResultSet queryResult = UserRecord.selectUserByName(username)) {
      if (queryResult.next()) {
        JOptionPane.showMessageDialog(null, "Tên người dùng đã tồn tại");
        return false;
      }
    } catch (Exception e) {
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

    if (!pf_password_confirm.getTextString().equals(password)) {
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
    if (e.getSource() == show_password_button) {
      if (!isShowingPassword) {
        pf_password.setShowingPassword(true);
        pf_password_confirm.setShowingPassword(true);
        pf_password.setEchoChar('\0');
        pf_password_confirm.setEchoChar('\0');
        show_password_button.setIcon(IconManager.getIcon("eye_opened.png", 20, 16, m_colors.m_green));
        show_password_button.setHoverBorderColor(m_colors.m_green);
      } else {
        pf_password.setShowingPassword(false);
        pf_password_confirm.setShowingPassword(false);
        if (!pf_password.getTextString().isEmpty()) {
          pf_password.setEchoChar('*');
          pf_password_confirm.setEchoChar('*');
        }
        show_password_button.setIcon(IconManager.getIcon("eye_closed.png", 20, 16, m_colors.m_grey_0));
        show_password_button.setHoverBorderColor(m_colors.m_grey_0);
      }
      isShowingPassword = !isShowingPassword;
    }
    if (e.getSource() == login_button) {
      PanelManager.show("FORM_LOGIN");
      return;
    }

    if (e.getSource() == signup_button) {
      String username = tf_username.getTextString();
      String password = pf_password.getTextString();
      String salt = Security.generate_salt(16);
      if (valid_username(username) && valid_password(password)) {
        UserRecord.insertUser(username, Security.hash_string(password + salt), salt, "");
        Entry.removeLoginSignup();
        Entry.registerDashBoard(m_colors);
      }
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
    if (!tf_username.getTextString().isEmpty()
        && !pf_password.getTextString().isEmpty()
        && !pf_password_confirm.getTextString().isEmpty()) {
      this.signup_button.setEnabled(true);
      this.signup_button.setBackground(m_colors.m_green);
      this.signup_button.setBorderColor(m_colors.m_green);
      this.signup_button.setDefaultForeground(m_colors.m_fg_1);
      this.signup_button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    } else {
      this.signup_button.setBackground(m_colors.m_bg_4);
      this.signup_button.setBorderColor(m_colors.m_bg_4);
      this.signup_button.setDefaultForeground(m_colors.m_grey_1);
      this.signup_button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      this.signup_button.setEnabled(false);
    }
  }

  private MTextField     tf_username;
  private MPasswordField pf_password;
  private MPasswordField pf_password_confirm;
  private ColorScheme    m_colors;
  private JLabel         form_label;
  private MButton        login_button;
  private MButton        signup_button;
  private MButton        show_password_button;
  private boolean        isShowingPassword = false;
  private Connection     m_connection;

  private final String username_placeholder = "Tên người dùng";
  private final String password_placeholder = "Mật khẩu";
  private final String password_confirm_placeholder = "Xác nhận mật khẩu";

}
