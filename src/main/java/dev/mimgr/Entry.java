package dev.mimgr;

import java.awt.*;
import java.awt.event.*;
// import javax.swing.*;
import dev.mimgr.theme.*;
import dev.mimgr.theme.builtin.ColorScheme;

class Entry {
  ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_DARK_DEFAULT);

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public Entry() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 900;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    System.out.printf("%dx%d%n", m_width, m_height);

    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
    Panel panel = new Panel();
    panel.setBackground(colors.m_bg_0);
    panel.setBounds(0, 0, m_width / 3, m_height);

    Frame frame = new Frame();
    frame.add(panel);
    frame.setBackground(colors.m_bg_dim);
    frame.setTitle("Mimgr");
    frame.setSize(m_width, m_height);

    frame.setLocation(
      (screen_size.width - frame.getWidth()) / 2,
      (screen_size.height - frame.getHeight()) / 2
    );

    frame.setLayout(null);
    frame.setVisible(true);
    frame.addWindowListener(on_close_handler());
  }

  static final WindowListener on_close_handler() {
    return new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    };
  }

  public static void main(String[] args) {
    new Entry();
  }
}
