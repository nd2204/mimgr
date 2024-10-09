package dev.mimgr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

class Entry {
  ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_LIGHT_CATPUCCIN);

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  Entry() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 900;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    System.out.printf("%dx%d%n", m_width, m_height);

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    // Main window
    JFrame app = new JFrame();
    app.setLayout(new BorderLayout());
    app.setBackground(colors.m_bg_dim);
    app.setTitle("Mimgr");
    app.setSize(m_width, m_height);
    app.add(new FormSignUp(colors));
    app.setLocation(
      (screen_size.width - app.getWidth()) / 2,
      (screen_size.height - app.getHeight()) / 2
    );
    app.setVisible(true);
    app.addWindowListener(on_close_handler());
  }

  static final WindowListener on_close_handler() {
    return new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    };
  }

  public static void main(String arg[]) {
    new Entry();
  }
}
