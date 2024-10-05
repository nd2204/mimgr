package dev.mimgr;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import dev.mimgr.theme.ColorTheme;
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
    panel.setBackground(colors.m_grey_0);
    panel.setBounds(0, 0, m_width / 3, m_height);

    TextField url = new TextField();
    url.setBounds(50, 50, 200, 50);
    TextField user = new TextField();
    user.setBounds(50, 100, 200, 50);
    TextField pass = new TextField();
    pass.setBounds(50, 150, 200, 50);

    Button button = new Button("Connect MySQL");
    button.setBounds(50, 500, 100, 30);

    Label resultLabel = new Label();
    resultLabel.setBounds(50, 300, 300, 30);
    resultLabel.setBackground(colors.m_grey_0);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          // Lấy giá trị từ TextField
          String DB_URL = "jdbc:mysql://localhost:3306";
          String userName = user.getText();
          String password = pass.getText();
          String sql = "select user from mysql.user where user = '" + userName + "'";
          try {
            Connection connection = DriverManager.getConnection(DB_URL, userName, password);
            // Connection connection = mySQLCon.get_connection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
              System.out.println("True");
            }
            else {
              System.out.println("False");
            }
            // while (rs.next()) {
            //   resultLabel.setText("You entered: " + rs.getString(1));
            // }
          } catch (Exception erException) {
            erException.printStackTrace();
          }

          // Hiển thị giá trị trên Label
          // resultLabel.setText("You entered: " + inputText);
      }
  });

    Frame frame = new Frame();
    // frame.add(url);
    frame.add(user);
    frame.add(pass);
    frame.add(button);
    frame.add(panel);
    // frame.add(resultLabel);
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
