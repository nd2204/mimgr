package dev.mimgr;

import dev.mimgr.theme.builtin.ColorScheme;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Color;

public class FormBuilder {
  public static JTextField create_text_field(ColorScheme colors, String placeholder, int length) {
    JTextField field = new JTextField(length);
    field.setBackground(colors.m_bg_1);
    field.setForeground(colors.m_bg_5);
    field.setText(placeholder);

    field.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent fe) {
        if (field.getText().equals(placeholder)) {
          field.setText("");
          field.setForeground(colors.m_fg_0);
        }
      }
      public void focusLost(FocusEvent evt) {
        if (field.getText().isEmpty()) {
          field.setText(placeholder);
          field.setForeground(colors.m_bg_5);
        }
      }
    });

    return field;
  };

  public static JPasswordField create_password_field(ColorScheme colors, String placeholder, int length) {
    JPasswordField psswd = new JPasswordField(length);
    psswd.setBackground(colors.m_bg_1);
    psswd.setForeground(colors.m_bg_5);
    psswd.setEchoChar('\0');
    psswd.setText(placeholder);

    psswd.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent fe) {
        if (String.valueOf(psswd.getPassword()).equals(placeholder)) {
          psswd.setEchoChar('*');
          psswd.setText("");
          psswd.setForeground(colors.m_fg_0);
        }
      }

      public void focusLost(FocusEvent evt) {
        if (psswd.getPassword().length == 0) {
          psswd.setEchoChar('\0');
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
}
