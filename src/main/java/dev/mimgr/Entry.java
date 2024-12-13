package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dev.mimgr.db.DBConnection;
import dev.mimgr.db.UserRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.utils.ResourceManager;

public class Entry extends JFrame {
  Entry() {

    // Main window
    // this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 480));
    this.setTitle("Mimgr");
    this.setSize(m_width, m_height);
    this.add(PanelManager.get_main_panel(), BorderLayout.CENTER);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.requestFocus();
    this.addWindowListener(on_close_handler());
  }

  static final WindowListener on_close_handler() {
    return new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        DBConnection.getInstance().closeConnection();
        System.exit(0);
      }
    };
  }

  public static void registerLoginSignup() {
    PanelManager.register_panel(new FormLogin(), "FORM_LOGIN");
    PanelManager.show("FORM_LOGIN");
  }

  public static void removeLoginSignup() {
    PanelManager.unregister_panel("FORM_LOGIN");
    PanelManager.unregister_panel("FORM_SIGNUP");
  }

  public static void registerDashBoard() {
    PanelManager.register_panel(new Dashboard(), "DASHBOARD");
    PanelManager.show("DASHBOARD");
  }

  public static void removeDashBoard() {
    PanelManager.unregister_panel("DASHBOARD");
  }

  public static void main(String arg[]) {
    ColorTheme theme = ColorTheme.getInstance();
    theme.setColorScheme(ColorTheme.THEME_LIGHT_DEFAULT);

    String osName = System.getProperty("os.name").toLowerCase();
    String osVersion = System.getProperty("os.version");
    String osArch = System.getProperty("os.arch");

    System.out.println("Operating System: " + osName);
    System.out.println("Version: " + osVersion);
    System.out.println("Architecture: " + osArch);
    System.out.println();

    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");

    SwingUtilities.invokeLater(() -> {
      // Register startup panel
      UserRecord ur = SessionManager.loadSession();
      if (ur == null) {
        registerLoginSignup();
      } else {
        registerDashBoard();
      }

      new Entry();
    });
  }

  ResourceManager resman = ResourceManager.getInstance();

  public static double m_aspect_ratio = 16.0f / 11.0f;
  public static int m_width = 1280;
  public static int m_height = (int) ((float) m_width / m_aspect_ratio);
}
