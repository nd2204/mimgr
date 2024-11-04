package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.mimgr.db.DBQueries;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

class FormEditProduct extends JFrame {
  ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_DARK_EVERFOREST);

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public FormEditProduct(ProductRecord pr) {
    Init();
    currentUploadPanel.add(createProductEditPanel(pr));
  }

  public FormEditProduct(ProductRecord[] prList) {
    Init();
    for (ProductRecord pr : prList) {
      currentUploadPanel.add(String.valueOf(pr.m_id), currentUploadPanel);
    }
  }

  private UploadPanel createProductEditPanel(ProductRecord pr) {
    UploadPanel uploadPanel = new UploadPanel(colors);
    uploadPanel.getTitleComponent().setText(pr.m_name);
    uploadPanel.getDescriptionComponent().setText(pr.m_description);
    uploadPanel.getStockComponent().setText(String.valueOf(pr.m_stock_quantity));
    uploadPanel.getPriceComponent().setText(String.valueOf(pr.m_price));
    uploadPanel.getLabelComponent().setText("Edit Product");
    idToPanel.put(pr.m_id, uploadPanel);
    return uploadPanel;
  }

  private void Init() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    mainPanel = new JPanel(new BorderLayout());
    uploadPanelsSwitcher = new CardLayout();
    currentUploadPanel = new JPanel(uploadPanelsSwitcher);

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
    this.setLocation(
      (screen_size.width - this.getWidth()) / 2,
      (screen_size.height - this.getHeight()) / 2
    );
    this.add(mainPanel);
    mainPanel.add(currentUploadPanel, BorderLayout.CENTER);
    mainPanel.setVisible(true);

    this.setVisible(true);
    this.requestFocus();
  }

  public static void main(String arg[]) {
    try {
      ResultSet rs = DBQueries.select_all_intruments();
      if (!rs.next()) return;
      ProductRecord pr = new ProductRecord(rs);
      new FormEditProduct(pr);
    } catch (SQLException e) {
    }
  }

  private CardLayout uploadPanelsSwitcher;
  private JPanel currentUploadPanel, mainPanel;
  private HashMap<Integer, UploadPanel> idToPanel = new HashMap<>();
}
