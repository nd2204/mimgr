package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

class Entry extends JFrame {
  ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_LIGHT_EVERFOREST);

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  Entry() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    FontManager.loadFont("Roboto", "/fonts/Roboto-Regular.ttf", 12f);
    FontManager.loadFont("RobotoBold", "/fonts/Roboto-Bold.ttf", 12f);
    FontManager.loadFont("RobotoMonoBold", "/fonts/RobotoMono-Bold.ttf", 12f);
    FontManager.loadVariableFont("MontserratBold", "/fonts/Montserrat-VariableFont_wght.ttf", Font.BOLD, 12f);

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    // Register startup panel
    PanelManager.register_panel(new FormLogin(colors), "FORM_LOGIN");
    PanelManager.register_panel(new FormSignUp(colors), "FORM_SIGNUP");
    // PanelManager.register_panel(new Dashboard(colors), "DASHBOARD");

    // Main window
    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 480));
    this.setTitle("Mimgr");
    this.setSize(m_width, m_height);
    this.add(PanelManager.get_main_panel());
    this.setLocation(
      (screen_size.width - this.getWidth()) / 2,
      (screen_size.height - this.getHeight()) / 2
    );
    this.setVisible(true);
    this.requestFocus();
    this.addWindowListener(on_close_handler());

    //     PanelManager.show("FORM_LOGIN");
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
