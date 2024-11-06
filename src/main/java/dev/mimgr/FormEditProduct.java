package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
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
    productRecords = new ArrayList<>();
    productRecords.add(pr);
    Init();
  }

  public FormEditProduct(Iterable<ProductRecord> prList) {
    productRecords = new ArrayList<>();
    for (ProductRecord pr : prList) {
      productRecords.add(pr);
    }
    Init();
  }

  public FormEditProduct(ProductRecord[] prList) {
    productRecords = new ArrayList<>(Arrays.asList(prList));
    Init();
  }

  private void Init() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(colors.m_bg_dim);
    currentUploadPanel = new JPanel();
    sidebarPanel = new SidebarPanel(currentUploadPanel, colors);
    sidebarPanel.setBackground(colors.m_bg_0);
    sidebarPanel.setPreferredSize(new Dimension(300, this.getHeight()));
    sidebarPanel.setupButtonStyle = (button) -> {
      button.setPadding(new Insets(10, 0, 10, 10));
      button.setFont(FontManager.getFont("NunitoBold", 16f));
      button.setForeground(colors.m_fg_0);
      button.setBackground(colors.m_bg_1);
      button.setBorderColor(null);
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setHoverBackgroundColor(colors.m_bg_2);
      button.setPreferredSize(new Dimension(220, button.getPreferredSize().height));
    };
    sidebarPanel.setupSelectedButtonStyle = (button) -> {
      button.setFont(FontManager.getFont("NunitoBold", 16f));
      button.setForeground(colors.m_fg_0);
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setBorderColor(null);
      button.setBackground(colors.m_bg_dim);
    };
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = gc.gridy = 0;
    gc.weightx = gc.weighty = 1.0;
    gc.insets = new Insets(20, 10, 20, 10);
    gc.fill = GridBagConstraints.BOTH;
    MScrollPane sp = new MScrollPane(colors);
    JPanel buttonMenu = new ButtonMenu();
    sp.add(buttonMenu);
    sp.setViewportView(buttonMenu);
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sidebarPanel.addComponent(sp, gc);

    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");

    // Main window
    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Upload");
    this.setSize(1154, m_height);
    this.setLocationRelativeTo(null);
    this.add(mainPanel);
    mainPanel.add(currentUploadPanel, BorderLayout.CENTER);
    mainPanel.add(sidebarPanel, BorderLayout.WEST);
    mainPanel.add(new TopPanel(), BorderLayout.NORTH);
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

  private UploadPanel createProductEditPanel(ProductRecord pr) {
    UploadPanel uploadPanel = new UploadPanel(colors);
    return uploadPanel;
  }

  private class TopPanel extends JPanel {
    public TopPanel() {
      this.setLayout(new FlowLayout(FlowLayout.LEFT));
      this.setBackground(colors.m_bg_dim);
      JLabel lblEditProduct = new JLabel("Edit Product");
      lblEditProduct.setFont(FontManager.getFont("NunitoExtraBold", 20f));
      lblEditProduct.setAlignmentX(JLabel.LEFT_ALIGNMENT);
      lblEditProduct.setAlignmentY(JLabel.CENTER_ALIGNMENT);
      lblEditProduct.setBorder(new EmptyBorder(20, 20, 20, 20));
      lblEditProduct.setForeground(colors.m_fg_0);
      this.add(lblEditProduct);
    }
  }

  private class ButtonMenu extends JPanel {
    public ButtonMenu() {
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      // this.setPreferredSize(new Dimension(280, this.getPreferredSize().height));
      final int padding_horizontal = 0;
      final int padding_vertical = 5;

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.fill = GridBagConstraints.HORIZONTAL;

      UploadPanel panel;
      MButton firstMenuBtn = null;
      for (ProductRecord pr : productRecords) {
        panel = createProductEditPanel(pr);
        MButton button = sidebarPanel.addDetachedButton(pr.m_name, null, panel);
        if (pr == productRecords.getFirst()) {
          c.insets = new Insets(0, padding_horizontal, padding_vertical, padding_horizontal);
          firstMenuBtn = button;
        }
        else if (pr == productRecords.getLast()) {
          c.insets = new Insets(padding_vertical, padding_horizontal, 0, padding_horizontal);
        } else {
          c.insets = new Insets(padding_vertical, padding_horizontal, padding_vertical, padding_horizontal);
        }
        new EditButtonListener(button, panel, pr);
        this.add(button , c);
        c.gridy++;
      }

      sidebarPanel.setCurrentMenu(firstMenuBtn);
      // Dummy component to anchor buttons to the top and prevent infinite panel growth
      c.weighty = 1.0;
      this.add(Box.createVerticalGlue(), c);
    }
  }

  private class EditButtonListener implements ActionListener {
    public EditButtonListener(MButton sb, UploadPanel panel, ProductRecord pr) {
      this.btnDelete = panel.getDeleteComponent();
      this.btnSubmit = panel.getSubmitComponent();
      this.sidebarButton = sb;
      this.panel = panel;


      tfTitle = panel.getTitleComponent();
      tfPrice = panel.getPriceComponent();
      taDescription = panel.getDescriptionComponent();
      tfStock = panel.getStockComponent();
      cbCategory = panel.getCategoryComponent();
      product_id = pr.m_id;

      TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();
      tfPrice.getDocument().addDocumentListener(textFieldListener);
      tfTitle.getDocument().addDocumentListener(textFieldListener);
      tfStock.getDocument().addDocumentListener(textFieldListener);
      taDescription.getDocument().addDocumentListener(textFieldListener);

      tfTitle.setText(pr.m_name);
      tfStock.setText(String.valueOf(pr.m_stock_quantity));
      tfPrice.setText(String.valueOf(pr.m_price));
      taDescription.setText(pr.m_description);

      panel.getDeleteComponent().setText("Delete Product");
      panel.getLabelComponent().setVisible(false);
      panel.setProductRecord(pr);

      Init();
    }

    private void Init() {
      btnSubmit = panel.getSubmitComponent();
      btnSubmit.addActionListener(this);
      btnDelete = panel.getDeleteComponent();
      btnDelete.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnDelete) {
        System.out.println("Exiting?");
        sidebarPanel.removeMenuButton(sidebarButton);
        return;
      }

      if (e.getSource() == btnSubmit) {
        String name = tfTitle.getTextString();
        double price = Double.parseDouble(tfPrice.getTextString());
        String description = taDescription.getTextString();
        int stock_quantity = Integer.parseInt(tfStock.getTextString());
        int category_id = get_category_id((String) cbCategory.getSelectedItem());
        
        System.out.println(category_id);
        if (category_id == 0) {
          JOptionPane.showMessageDialog(null, "Not valid category name");
        }
        else {
          JOptionPane.showMessageDialog(null, "Success");
          DBQueries.update_product(name, price, description, stock_quantity, category_id, product_id);
        }
        return;
      }
    }

    private class TextFieldDocumentListener implements DocumentListener {
    @Override
    public void insertUpdate(DocumentEvent e) {
      checkFields();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      checkFields();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      checkFields();
    }

    private void checkFields() {
      if (!tfTitle.getText().isEmpty() && !tfPrice.getText().isEmpty() && !tfStock.getText().isEmpty()) {
        btnSubmit.setBackground(colors.m_blue);
        btnSubmit.setBorderColor(colors.m_blue);
        btnSubmit.setForeground(colors.m_fg_1);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setEnabled(true);
      } else {
        btnSubmit.setEnabled(false);
        btnSubmit.setBackground(colors.m_bg_4);
        btnSubmit.setBorderColor(colors.m_bg_4);
        btnSubmit.setForeground(colors.m_grey_1);
        btnSubmit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    }
  }

    MButton sidebarButton, btnSubmit, btnDelete;
    private UploadPanel panel;
    private MTextArea taDescription;
    private MTextField tfTitle;
    private MTextField tfPrice;
    private MTextField tfStock;
    int product_id;
    private MComboBox<String> cbCategory;
  }

  private int get_category_id(String category_name) {
    ResultSet queryResult = DBQueries.select_id_category(category_name);
    int id_result = 0;
    try {
      while (queryResult.next()) {
        id_result = queryResult.getInt("category_id");
      }
      return id_result;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return id_result;
  }

  private SidebarPanel sidebarPanel;
  private JPanel currentUploadPanel, mainPanel;
  private ArrayList<ProductRecord> productRecords;

}
