package dev.mimgr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import dev.mimgr.custom.MTextField;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

class Entry extends Frame {
  ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_LIGHT_DEFAULT);

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  Entry() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 900;
    m_height = (int) ((float) m_width / m_aspect_ratio);


    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    // Register startup panel
    PanelManager.register_panel(new FormLogin(colors), "FORM_LOGIN");
    PanelManager.register_panel(new FormSignUp(colors), "FORM_SIGNUP");

    JFrame frame = new JFrame("Custom Component Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new FlowLayout());

    // Create and customize the custom component
    MTextField customTextField = new MTextField(20);
    customTextField.setBackground(Color.WHITE); // Set the background color
    customTextField.setBorderColor(Color.BLUE); // Custom border color
    customTextField.setBorderRadius(15);        // Rounded corners
    customTextField.setBorderWidth(2);          // Custom border width
    customTextField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set custom font

    // Add the custom component to the frame
    frame.add(customTextField);

    // Display frame
    frame.pack();
    frame.setVisible(true);

    // Main window
    this.setLayout(new BorderLayout());
    this.setBackground(colors.m_bg_dim);
    this.setTitle("Mimgr");
    this.setSize(m_width, m_height);
    this.add(PanelManager.get_main_panel());
    this.setLocation(
    (screen_size.width - this.getWidth()) / 2,
    (screen_size.height - this.getHeight()) / 2
    );
    this.setVisible(true);
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
