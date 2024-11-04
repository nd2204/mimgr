package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

class FormAddProduct extends JFrame {
  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public FormAddProduct(ColorScheme colors) {
    this.colors = colors;
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    // Main window
    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Upload");
    this.setSize(854, m_height);
    this.add(new UploadPanel(this.colors));
    this.setLocation(
      (screen_size.width - this.getWidth()) / 2,
      (screen_size.height - this.getHeight()) / 2
    );
    this.setVisible(true);
    this.requestFocus();
  }

  public static void main(String arg[]) {
    ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_DARK_EVERFOREST);
    new FormAddProduct(colors);
  }

  private ColorScheme colors;
}
