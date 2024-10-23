package dev.mimgr;

import dev.mimgr.theme.builtin.ColorScheme;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Color;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.MPasswordField;

public class FormBuilder {
  public static MTextField create_text_field(ColorScheme colors, String placeholder, int length) {
    MTextField field = new MTextField(length);
    field.setBackground(colors.m_bg_1);
    field.setForeground(colors.m_bg_5);
    field.setFont(FontManager.getFont("RobotoMonoBold", 14f));
    field.setText(placeholder);
    field.setBorderWidth(2);
    field.setBorderRadius(15);
    field.setBorderColor(colors.m_bg_5);
    field.setFocusBorderColor(colors.m_accent);

    field.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent fe) {
        if (field.getText().equals(placeholder)) {
          field.setText("");
          field.setForeground(colors.m_fg_0);
          field.setBackground(colors.m_bg_1);
        }
      }
      public void focusLost(FocusEvent evt) {
        if (field.getText().isEmpty()) {
          field.setText(placeholder);
          field.setForeground(colors.m_bg_5);
          field.setBackground(colors.m_bg_1);
        }
      }
    });

    return field;
  };

  public static MPasswordField create_password_field(ColorScheme colors, String placeholder, int length) {
    MPasswordField psswd = new MPasswordField(length);
    psswd.setBackground(colors.m_bg_1);
    psswd.setForeground(colors.m_bg_5);
    psswd.setEchoChar('\0');
    psswd.setText(placeholder);
    psswd.setFont(FontManager.getFont("RobotoMonoBold", 14f));
    psswd.setBorderWidth(2);
    psswd.setBorderRadius(15);
    psswd.setBorderColor(colors.m_bg_5);
    psswd.setFocusBorderColor(colors.m_accent);

    psswd.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent fe) {
        if (String.valueOf(psswd.getPassword()).equals(placeholder)) {
          psswd.setEchoChar('*');
          psswd.setText("");
          psswd.setBackground(colors.m_bg_1);
          psswd.setForeground(colors.m_fg_0);
        }
      }

      public void focusLost(FocusEvent evt) {
        if (psswd.getPassword().length == 0) {
          psswd.setEchoChar('\0');
          psswd.setBackground(colors.m_bg_1);
          psswd.setForeground(colors.m_bg_5);
          psswd.setText(placeholder);
        }
      }
    });

    return psswd;
  };

  public static Border create_rounded_border(Color color, int thickness) {
    Border inner_padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    Border rounded_border = BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(color, thickness, true),
      inner_padding
    );
    return rounded_border;
  }

  public static String get_textfield_value(JTextField textfield, String placeholder) {
    String username = textfield.getText();
    if (username.equals(placeholder)) {
      return "";
    }
    return username;
  }

  public static String get_passwordfield_value(JPasswordField passwordField, String placeholder) {
    String password = String.valueOf(passwordField.getPassword());
    if (password.equals(placeholder)) {
      return "";
    }
    return password;
  }
}
