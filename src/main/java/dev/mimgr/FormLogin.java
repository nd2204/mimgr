package dev.mimgr;

import java.awt.Component;
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
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.shader.BuiltinShaders.*;
import dev.mimgr.custom.MTextField;
import dev.mimgr.component.NotificationPopup;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MPasswordField;
import dev.mimgr.custom.MCheckBox;

public class FormLogin extends AnimatedPanel implements ActionListener, DocumentListener, KeyListener {
  FormLogin() {
    super(new LoginShader(), Entry.m_width, Entry.m_height);
    // super(new Color(0x1379a9), ColorTheme.getInstance().getCurrentScheme().m_blue);
    colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_bg_dim);

    Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);
    // =======================================================
    // Setup Appearance
    // =======================================================

    form_label = new JLabel("Đăng nhập", JLabel.CENTER);
    form_label.setFont(FontManager.getFont("RobotoMonoBold", 20f));
    form_label.setForeground(colors.m_fg_0);

    tf_username = new MTextField(30);
    tf_username.setInputForeground(colors.m_fg_0);
    tf_username.setPlaceholder(username_placeholder);
    tf_username.setBackground(colors.m_bg_1);
    tf_username.setBorderColor(colors.m_bg_4);
    tf_username.setFocusBorderColor(colors.m_blue);
    tf_username.setBorderWidth(2);
    tf_username.setFont(font_bold);
    tf_username.setIcon(
      IconManager.getIcon("user.png", 20, 20, colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    tf_username.setPlaceholderForeground(colors.m_grey_1);
    tf_username.getDocument().addDocumentListener(this);

    pf_password = new MPasswordField(30);
    pf_password.setInputForeground(colors.m_fg_0);
    pf_password.setPlaceholderForeground(colors.m_grey_1);
    pf_password.setPlaceholder(password_placeholder);
    pf_password.setBackground(colors.m_bg_1);
    pf_password.setBorderColor(colors.m_bg_4);
    pf_password.setFocusBorderColor(colors.m_blue);
    pf_password.setBorderWidth(2);
    pf_password.setFont(font_bold);
    pf_password.setIcon(
      IconManager.getIcon("lock_locked.png", 20, 20, colors.m_grey_1),
      MPasswordField.ICON_PREFIX
    );
    pf_password.getDocument().addDocumentListener(this);

    this.remember = new MCheckBox("Nhớ phiên đăng nhập");
    this.remember.setFocusable(false);
    this.remember.setFont(font_bold);
    this.remember.setBackground(colors.m_bg_1);
    this.remember.setForeground(colors.m_grey_2);
    this.remember.setBoxColor(colors.m_bg_5);
    this.remember.setBoxHoverColor(colors.m_grey_0);
    this.remember.setCheckColor(colors.m_blue);
    this.remember.setHorizontalAlignment(SwingConstants.LEFT);
    this.remember.setBoxSelectedColor(colors.m_blue);
    this.remember.setBackground(null);

    this.show_password_button = new MButton(IconManager.getIcon("eye_closed.png", 20, 16, colors.m_grey_0));
    this.show_password_button.setBorderWidth(2);
    this.show_password_button.setBackground(colors.m_bg_0);
    this.show_password_button.setBorderColor(colors.m_bg_4);
    this.show_password_button.setForeground(colors.m_grey_0);
    this.show_password_button.setHoverBorderColor(colors.m_bg_5);
    this.show_password_button.setHoverBackgroundColor(colors.m_bg_3);
    this.show_password_button.addActionListener(this);
    // this.show_password_button.setBackground(null);
    // this.show_password_button.setIcon(IconManager.getIcon("eye_closed.png", 20, 16, colors.m_blue));
    // this.show_password_button.setBorderColor(null);


    this.btnLogin = new MButton("Đăng nhập");
    this.btnLogin.setFont(font_bold);
    this.btnLogin.setBackground(colors.m_bg_4);
    this.btnLogin.setBorderColor(colors.m_bg_4);
    this.btnLogin.setForeground(colors.m_grey_1);
    this.btnLogin.setDefaultForeground(colors.m_grey_1);
    this.btnLogin.setBorderWidth(2);
    this.btnLogin.setEnabled(false);
    this.btnLogin.addActionListener(this);

    this.btnSignup = new MButton("Đăng ký");
    this.btnSignup.setFont(font_bold);
    this.btnSignup.setBorderWidth(2);
    this.btnSignup.setBackground(colors.m_bg_0);
    this.btnSignup.setBorderColor(colors.m_bg_4);
    this.btnSignup.setForeground(colors.m_grey_0);
    this.btnSignup.setHoverBorderColor(colors.m_bg_5);
    this.btnSignup.setHoverBackgroundColor(colors.m_bg_3);
    this.btnSignup.addActionListener(this);

    // =======================================================
    // Setup Layout
    // =======================================================
    GridBagConstraints c = new GridBagConstraints();

    RoundedPanel input_container = new RoundedPanel();
    input_container.setBackground(colors.m_bg_0);
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
    input_container.add(btnLogin, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(10, padding, 10, padding);
    c.weightx = 0.5;
    c.gridy = 5;
    JSeparator sep = new JSeparator();
    sep.setForeground(colors.m_bg_4);
    sep.setBackground(null);
    input_container.add(sep, c);

    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(10, padding, padding, padding);
    c.gridy = 6;
    c.gridx = 0;
    c.weightx = 1.0;
    input_container.add(btnSignup, c);

    this.setLayout(new GridBagLayout());
    this.setBackground(null);
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.weighty = 1.0;
    this.add(input_container, c);

    this.start();
    this.setVisible(false);

    for (Component comp : input_container.getComponents()) {
      comp.addKeyListener(this);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == show_password_button) {
      if (!isShowingPassword) {
        pf_password.setEchoChar('\0');
        show_password_button.setIcon(IconManager.getIcon("eye_opened.png", 20, 16, colors.m_blue));
        show_password_button.setHoverBorderColor(colors.m_blue);
      } else {
        if (!pf_password.getTextString().isEmpty()) {
          pf_password.setEchoChar('*');
        }
        show_password_button.setIcon(IconManager.getIcon("eye_closed.png", 20, 16, colors.m_grey_0));
        show_password_button.setHoverBorderColor(colors.m_grey_0);
      }
      isShowingPassword = !isShowingPassword;
      pf_password.setShowingPassword(isShowingPassword);
    }

    if (e.getSource() == btnLogin) {
      tryLogin();
    }

    if (e.getSource() == btnSignup) {
      this.stop();
      PanelManager.register_panel(new FormSignUp(), "FORM_SIGNUP");
      PanelManager.show("FORM_SIGNUP");
      PanelManager.unregister_panel("FORM_LOGIN");
      return;
    }
  }

  private void tryLogin() {
    String username = tf_username.getTextString();
    String password = pf_password.getTextString();

    if (DBConnection.getInstance().getConection() == null) {
      PanelManager.createPopup(new NotificationPopup(
        "Cannot connect to database.",
        NotificationPopup.NOTIFY_LEVEL_ERROR,
        3000
      ));
      return;
    }

    if (LoginService.loginUser(username, password, remember.isSelected())) {
      this.stop();
      Entry.removeLoginSignup();
      Entry.registerDashBoard();
    } else {
      PanelManager.createPopup(new NotificationPopup(
        "Tài khoản hoặc mật khẩu không hợp lệ",
        NotificationPopup.NOTIFY_LEVEL_WARNING,
        3000
      ));
    }

    return;
  }

  private void checkFields() {
    if (!tf_username.getTextString().isEmpty() && !pf_password.getTextString().isEmpty()) {
      this.btnLogin.setBackground(colors.m_blue);
      this.btnLogin.setBorderColor(colors.m_blue);
      this.btnLogin.setForeground(colors.m_fg_1);
      this.btnLogin.setDefaultForeground(colors.m_fg_1);
      this.btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.btnLogin.setEnabled(true);
    } else {
      this.btnLogin.setEnabled(false);
      this.btnLogin.setBackground(colors.m_bg_4);
      this.btnLogin.setBorderColor(colors.m_bg_4);
      this.btnLogin.setForeground(colors.m_grey_1);
      this.btnLogin.setDefaultForeground(colors.m_grey_1);
      this.btnLogin.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER && btnLogin.isEnabled()) {
      tryLogin();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void insertUpdate(DocumentEvent e) { checkFields(); }

  @Override
  public void removeUpdate(DocumentEvent e) { checkFields(); }

  @Override
  public void changedUpdate(DocumentEvent e) { checkFields(); }

  private MTextField     tf_username;
  private MPasswordField pf_password;
  private ColorScheme    colors;
  private JLabel         form_label;
  private MButton        btnLogin;
  private MButton        btnSignup;
  private MButton        show_password_button;
  private boolean        isShowingPassword = false;
  private MCheckBox      remember;

  private final String username_placeholder = "Tên người dùng";
  private final String password_placeholder = "Mật khẩu";
}

class LoginShader extends TriLatticeShader {
  public LoginShader() {
    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
    int rgb1 = colors.m_bg_dim.getRGB();
    col.x = ((rgb1 >> 16) & 255) / 255.0f;
    col.y = ((rgb1 >> 8) & 255) / 255.0f;
    col.z = ((rgb1 >> 0) & 255) / 255.0f;

    int rgb2 = colors.m_bg_1.getRGB();
    lineCol.x = ((rgb2 >> 16) & 255) / 255.0f;
    lineCol.y = ((rgb2 >> 8) & 255) / 255.0f;
    lineCol.z = ((rgb2 >> 0) & 255) / 255.0f;
  }
}

