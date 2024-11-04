package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.ResourceManager;

class Entry extends JFrame {
  Entry() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);


    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    // Register startup panel
    registerDashBoard(colors);
    for (JPanel panel : PanelManager.getAllPanels()) {
      System.out.println(panel);
    }

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

  public static void registerLoginSignup(ColorScheme colors) {
    PanelManager.register_panel(new FormLogin(colors), "FORM_LOGIN");
    PanelManager.register_panel(new FormSignUp(colors), "FORM_SIGNUP");
    PanelManager.show("FORM_LOGIN");
  }

  public static void removeLoginSignup() {
    PanelManager.unregister_panel("FORM_LOGIN");
    PanelManager.unregister_panel("FORM_SIGNUP");
  }

  public static void registerDashBoard(ColorScheme colors) {
    PanelManager.register_panel(new Dashboard(colors), "DASHBOARD");
  }

  public static void removeDashBoard() {
    PanelManager.unregister_panel("DASHBOARD");
  }

  public static void main(String arg[]) {
    new Entry();
  }

  ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_DARK_EVERFOREST);
  ResourceManager resman = ResourceManager.getInstance();

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

}
