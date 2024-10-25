package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

    FontManager.loadFont("Roboto", "Roboto-Regular.ttf", 12f);
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf", 12f);
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf", 12f);
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf", 12f);
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf", 12f);

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    // Register startup panel
    // PanelManager.register_panel(new FormLogin(colors), "FORM_LOGIN");
    // PanelManager.register_panel(new FormSignUp(colors), "FORM_SIGNUP");
    PanelManager.register_panel(new SidebarPanel(colors), "DASHBOARD_SIDEBAR");
    PanelManager.register_panel(new Dashboard(colors), "DASHBOARD");
    PanelManager.register_panel(new FormProduct(colors), "FORM_PRODUCT");
    PanelManager.register_panel(new FormAnalytic(colors), "FORM_ANALYTIC");

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
