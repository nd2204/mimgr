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
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.db.CategoryRecord;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.ResourceManager;

public class FormEditProduct extends JFrame {
  ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public ArrayList<UploadPanel> getUploadPanels() {
    return this.uploadPanels;
  }

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
    uploadPanels = new ArrayList<>();
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(colors.m_bg_dim);
    currentUploadPanel = new JPanel();
    currentUploadPanel.setBackground(colors.m_bg_0);
    sidebarPanel = new SidebarPanel(currentUploadPanel);
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
    MScrollPane sp = new MScrollPane();
    JPanel buttonMenu = new ButtonMenu();
    sp.add(buttonMenu);
    sp.setViewportView(buttonMenu);
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sidebarPanel.addComponent(sp, gc);
    if (productRecords.size() < 2) {
      sidebarPanel.setVisible(false);
    }

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

  private UploadPanel createProductEditPanel(ProductRecord pr) {
    UploadPanel uploadPanel = new UploadPanel();
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
        uploadPanels.add(panel);
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
      this.sidebarButton = sb;
      this.panel = panel;
      this.panel.getDeleteComponent().setText("Delete Product");
      this.panel.getLabelComponent().setVisible(false);
      this.panel.setProductRecord(pr);

      InitializeComponent();
    }

    private void InitializeComponent() {
      this.btnDelete = panel.getDeleteComponent();
      this.btnDelete.addActionListener(this);

      this.btnSubmit = panel.getSubmitComponent();
      this.btnSubmit.setText("Update");
      this.btnSubmit.addActionListener(this);
      this.btnSubmit.setBackground(colors.m_green);
      this.btnSubmit.setBorderColor(colors.m_green);
      this.btnSubmit.setDefaultForeground(colors.m_fg_1);
      this.btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.btnSubmit.setEnabled(true);

      ProductRecord pr = this.panel.getProductRecord();

      this.tfTitle = panel.getTitleComponent();
      this.tfTitle.setText(pr.m_name);
      this.tfTitle.setCaretPosition(0);

      this.tfPrice = panel.getPriceComponent();
      this.tfPrice.setText(String.valueOf(pr.m_price));

      this.taDescription = panel.getDescriptionComponent();
      this.taDescription.setText(pr.m_description);

      this.tfStock = panel.getStockComponent();
      this.tfStock.setText(String.valueOf(pr.m_stock_quantity));

      this.cbCategory = panel.getCategoryComponent();
      this.cbCategory.setSelectedItem(CategoryRecord.selectByKey(pr.m_category_id));

      this.dropContainerPanel = panel.getDropContainerPanel();
      ImageRecord ir = ImageRecord.selectById(pr.m_image_id);
      if (ir != null) {
        this.dropContainerPanel.addData(Paths.get(ir.m_url).toAbsolutePath().toFile());
      }

      TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();

      this.tfTitle.getDocument().addDocumentListener(textFieldListener);
      this.tfPrice.getDocument().addDocumentListener(textFieldListener);
      this.tfStock.getDocument().addDocumentListener(textFieldListener);
      this.taDescription.getDocument().addDocumentListener(textFieldListener);
      JTextField tf = (JTextField) this.cbCategory.getEditor().getEditorComponent();
      tf.getDocument().addDocumentListener(textFieldListener);
    }

    private int processDropData() {
      List<Object> objs = this.dropContainerPanel.getAllData();
      // Process the first image in the drop panel
      int image_id = 0;
      for (Object o : objs) {
        if (o instanceof File file) {
          if (!rm.getUploadPath().resolve(file.getName()).toFile().exists()) {
            Path newFilePath = rm.moveStagedFileToUploadDir(file);
            ImageRecord.insert(
              String.valueOf(rm.getProjectPath().relativize(newFilePath)).replace("\\", "/"),
              file.getName(),
              "",
              SessionManager.getCurrentUser().m_id
            );
          }
          image_id = getImageIdByFileName(file.getName());
          break;
        }
      }
      // Clean temp file from download
      rm.cleanTempFiles();
      return image_id;
    }

    private int getImageIdByFileName(String filename) {
      ImageRecord ir = ImageRecord.selectByName(filename);
      return (ir == null) ? 0 : ir.m_id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnDelete) {
        sidebarPanel.removeMenuButton(sidebarButton);
        int row = ProductRecord.delete(panel.getProductRecord().m_id);
        JOptionPane.showMessageDialog(null, row + " row(s) affected");
      }

      else if (e.getSource() == btnSubmit) {
        String name = tfTitle.getTextString();
        double price = Double.parseDouble(tfPrice.getTextString());
        String description = taDescription.getTextString();
        int stock_quantity = Integer.parseInt(tfStock.getTextString());
        CategoryRecord categoryRecord = (CategoryRecord) cbCategory.getSelectedItem();
        int image_id = processDropData();

        System.out.println(categoryRecord);
        if (categoryRecord.m_id == 0) {
          JOptionPane.showMessageDialog(null, "Not valid category name");
        }
        else {
          int row = ProductRecord.update(name, price, description, stock_quantity, categoryRecord.m_id, image_id, panel.getProductRecord().m_id);
          sidebarPanel.removeMenuButton(sidebarButton);
          JOptionPane.showMessageDialog(null, row + " row(s) affected");
        }
      }

      if (sidebarPanel.getMenuButtonCount() < 1) {
        FormEditProduct.this.dispose();
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
          btnSubmit.setBackground(colors.m_green);
          btnSubmit.setBorderColor(colors.m_green);
          btnSubmit.setDefaultForeground(colors.m_fg_1);
          btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
          btnSubmit.setEnabled(true);
        } else {
          btnSubmit.setBackground(colors.m_bg_1);
          btnSubmit.setBorderColor(colors.m_bg_1);
          btnSubmit.setDefaultForeground(colors.m_bg_3);
          btnSubmit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          btnSubmit.setEnabled(false);
        }
      }
    }

    MButton sidebarButton, btnSubmit, btnDelete;
    private DropContainerPanel dropContainerPanel;
    private UploadPanel panel;
    private MTextArea taDescription;
    private MTextField tfTitle;
    private MTextField tfPrice;
    private MTextField tfStock;
    private MComboBox<CategoryRecord> cbCategory;
  }

  private ResourceManager rm = ResourceManager.getInstance();
  private SidebarPanel sidebarPanel;
  private JPanel currentUploadPanel, mainPanel;
  private ArrayList<ProductRecord> productRecords;
  private ArrayList<UploadPanel> uploadPanels;
}
