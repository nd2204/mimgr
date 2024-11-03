package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.DropPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBQueries;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferListener;
import dev.mimgr.utils.ResourceManager;

public class UploadPanel extends JPanel implements ActionListener, DocumentListener {

  UploadPanel(ColorScheme colors) {
    this.colors = colors;
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_dim);
    this.setup_form_style();

    this.setPreferredSize(new Dimension(1000, this.getPreferredSize().height));
    this.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

    MScrollPane scrollPane = new MScrollPane(colors);
    this.add(scrollPane);

    scrollPane.add(thisPanel);
    scrollPane.setViewportView(thisPanel);

    thisPanel.setLayout(new GridBagLayout());
    thisPanel.setBackground(colors.m_bg_dim);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    lblAddProduct.setFont(nunito_bold_20);
    lblAddProduct.setForeground(colors.m_fg_0);
    gbc.gridx = 0;
    gbc.gridy = 0;
    thisPanel.add(lblAddProduct, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    thisPanel.add(new titleAndDescriptionPanel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    thisPanel.add(new MediaDropPanel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    thisPanel.add(new PricingPanel(), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    thisPanel.add(new InventoryPanel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    thisPanel.add(uploadButton, gbc);
  }

  public void setup_form_style() {

    // ========================= Fields =========================
    Consumer<MTextField> setup_common_textfield = (tf) -> {
      tf.setInputForeground(colors.m_fg_0);
      tf.setBackground(colors.m_bg_1);
      tf.setBorderColor(colors.m_bg_4);
      tf.setFocusBorderColor(colors.m_blue);
      tf.setBorderWidth(2);
      tf.setFont(font_bold);
      tf.getDocument().addDocumentListener(UploadPanel.this);
    };

    tfTitle = new MTextField(30);
    setup_common_textfield.accept(tfTitle);
    tfPrice = new MTextField(30);
    setup_common_textfield.accept(tfPrice);
    tfStock = new MTextField(30);
    setup_common_textfield.accept(tfStock);

    taDescription = new MTextArea(8, 80);
    taDescription.setInputForeground(colors.m_fg_0);
    taDescription.setBackground(colors.m_bg_1);
    taDescription.setBorderColor(colors.m_bg_4);
    taDescription.setFocusBorderColor(colors.m_blue);
    taDescription.setBorderWidth(2);
    taDescription.setBorderRadius(15);
    taDescription.setFont(font_bold);
    taDescription.setLineWrap(true);
    taDescription.getDocument().addDocumentListener(this);

    String[] options = get_category_names();
    cbCategory = new MComboBox<>(options, colors);

    // ========================= Buttons =========================
    this.uploadButton = new MButton("Submit");
    this.uploadButton.setFont(font_bold);
    this.uploadButton.setBackground(colors.m_bg_dim);
    this.uploadButton.setBorderColor(colors.m_bg_3);
    this.uploadButton.setForeground(colors.m_bg_3);
    this.uploadButton.setBorderWidth(2);
    this.uploadButton.setEnabled(false);
    this.uploadButton.addActionListener(this);
  }

  private void checkFields() {
    if (!tfTitle.getText().isEmpty() && !tfPrice.getText().isEmpty() && !tfStock.getText().isEmpty() && !taDescription.getText().isEmpty()) {
      this.uploadButton.setBackground(colors.m_blue);
      this.uploadButton.setBorderColor(colors.m_blue);
      this.uploadButton.setForeground(colors.m_fg_1);
      this.uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.uploadButton.setEnabled(true);
    } else {
      this.uploadButton.setEnabled(false);
      this.uploadButton.setBackground(colors.m_bg_4);
      this.uploadButton.setBorderColor(colors.m_bg_4);
      this.uploadButton.setForeground(colors.m_grey_1);
      this.uploadButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
  }

  private String[] get_category_names() {
    ResultSet queryResult = DBQueries.select_all_categories();
    List<String> nameList = new ArrayList<>();
    try {
      while (queryResult.next()) {
        nameList.add(queryResult.getString("category_name"));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String[] namesArray = nameList.toArray(new String[0]);
    return namesArray;
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

  private class MediaDropPanel extends RoundedPanel implements ActionListener, MTransferListener {
    public MediaDropPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setLayout(new GridBagLayout());
      this.setBackground(colors.m_bg_0);
      this.setBorderRadius(15);

      droppedItemsPanel = new DropContainerPanel(colors);
      droppedItemsPanel.addActionListener(this);

      selectFilesButton.setBackground(colors.m_bg_dim);
      selectFilesButton.setBorderColor(colors.m_bg_3);
      selectFilesButton.setHoverBorderColor(colors.m_accent);
      selectFilesButton.setClickBackgroundColor(colors.m_bg_1);
      selectFilesButton.setForeground(colors.m_accent);
      selectFilesButton.setHorizontalAlignment(SwingConstants.CENTER);
      selectFilesButton.addActionListener(this);

      lblMedia.setFont(nunito_bold_16);
      lblMedia.setForeground(colors.m_fg_0);

      GridBagConstraints gbc = new GridBagConstraints();

      gbc.insets = new Insets(20, 25, 5, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 0;
      this.add(lblMedia, gbc);

      DropPanel dropArea = new DropPanel(colors);
    {
        GridBagConstraints gc = new GridBagConstraints();
        dropArea.setLayout(new GridBagLayout());
        dropArea.addTransferListener(this);

        JLabel label = new JLabel("Drop files to upload");
        JLabel label1 = new JLabel("or");

        label.setFont(nunito_bold_20);
        label.setForeground(colors.m_grey_0);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setFont(nunito_bold_16);
        label1.setForeground(colors.m_grey_0);
        label1.setHorizontalAlignment(SwingConstants.CENTER);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.CENTER;
        dropArea.add(label, gc);

        gc.insets = new Insets(15, 0, 15, 0);
        gc.gridy = 1;
        dropArea.add(label1, gc);

        gc.insets = new Insets(0, 0, 0, 0);
        gc.fill = GridBagConstraints.NONE;
        gc.gridy = 2;
        dropArea.add(selectFilesButton, gc);
      }

      gbc.insets = new Insets(5, 20, 20, 20);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weightx = 1.0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      this.add(dropArea, gbc);

      gbc.insets = new Insets(0, 20, 20, 20);
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.weightx = 0.0;
      gbc.weighty = 0.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.NONE;
      JScrollPane sp = droppedItemsPanel.getScrollPaneComponent();
      sp.setMaximumSize(new Dimension(660, Integer.MAX_VALUE));
      sp.setMinimumSize(new Dimension(660, sp.getPreferredSize().height));
      sp.setPreferredSize(new Dimension(660, 140));
      this.add(droppedItemsPanel, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == this.selectFilesButton) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Select Files");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        // Show the file chooser dialog and wait for user action
        int userSelection = fileChooser.showOpenDialog(null);

        // Handle the user selection
        if (userSelection == JFileChooser.APPROVE_OPTION) {
          // Get the selected file
          File[] selectedFile = fileChooser.getSelectedFiles();
          for (File file : selectedFile) {
            if (rm.isValidImageFile(file)) {
              droppedItemsPanel.addData(file);
            }
          }
        } else {
          System.out.println("No file selected.");
        }
      }

      if (e.getSource() == this.droppedItemsPanel.getConfirmButton()) {
        // Do something with the data
        for (Object obj : this.droppedItemsPanel.getAllData()) {
          if (obj instanceof File file) {
            Path newFilePath = rm.moveStagedFileToUploadDir(file);
            System.out.println(rm.getProjectPath().relativize(newFilePath));
          }
        }
        // Clean temp file from download
        rm.cleanTempFiles();
        // Clear the data
        this.droppedItemsPanel.clearData();
      }
    }

    @Override
    public void onStringImported(String string) {
      if (ResourceManager.isImageUrl(string)) {
        // System.out.println(rm.getUploadPath());
        Path fp = rm.downloadTempFile(string);
        droppedItemsPanel.addData(fp.toFile());
      }
      return;
    }

    @Override
    public void onImageImported(Image image) {
      return;
    }

    @Override
    public void onFileListImported(List<File> files) {
      for (File file : files) {
        droppedItemsPanel.addData(file);
      }
      return;
    }

    private JLabel lblMedia = new JLabel("Media");
    private MButton selectFilesButton = new MButton("Select Files");
    private DropContainerPanel droppedItemsPanel;
  }

  private class PricingPanel extends RoundedPanel {
    public PricingPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());

      this.lblInventory.setFont(nunito_bold_16);
      this.lblInventory.setForeground(colors.m_fg_0);
      this.lblStock.setFont(nunito_bold_14);
      this.lblStock.setForeground(colors.m_grey_1);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 1.0;

      gbc.insets = new Insets(20, 25, 20, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 0;
      this.add(lblInventory, gbc);

      gbc.insets = new Insets(0, 25, 5, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 1;
      this.add(lblStock, gbc);

      gbc.insets = new Insets(0, 25, 20, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 2;
      this.add(tfStock, gbc);
    }

    private JLabel lblInventory = new JLabel("Inventory");
    private JLabel lblStock = new JLabel("Stock Quantity");
  }

  private class InventoryPanel extends RoundedPanel {
    public InventoryPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());

      this.lblPricing.setFont(nunito_bold_16);
      this.lblPricing.setForeground(colors.m_fg_0);
      this.lblPrice.setFont(nunito_bold_14);
      this.lblPrice.setForeground(colors.m_grey_1);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 1.0;

      gbc.insets = new Insets(20, 25, 20, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 0;
      this.add(lblPricing, gbc);

      gbc.insets = new Insets(0, 25, 5, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 1;
      this.add(lblPrice, gbc);

      gbc.insets = new Insets(0, 25, 20, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 2;
      this.add(tfPrice, gbc);
    }

    private JLabel lblPricing = new JLabel("Pricing");
    private JLabel lblPrice = new JLabel("Price");
  }

  private class titleAndDescriptionPanel extends RoundedPanel {
    public titleAndDescriptionPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblTitle);
      setLabelStyle(lblDescription);
      setLabelStyle(lblCategory);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblTitle, gc);

      gc.gridx = 0;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      this.add(tfTitle, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblCategory, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      gc.weighty = 1.0;
      gc.fill = GridBagConstraints.BOTH;
      this.add(cbCategory, gc);

      gc.gridwidth = 2;
      gc.gridx = 0;
      gc.gridy = 2;
      gc.insets = new Insets(5, 25, 5, 20);
      this.add(lblDescription, gc);
      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(taDescription, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private JLabel lblTitle = new JLabel("Title");
    private JLabel lblDescription = new JLabel("Description");
    private JLabel lblCategory = new JLabel("Category");
  }

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


  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == uploadButton) {
      String name = tfTitle.getTextString();
      double price = Double.parseDouble(tfPrice.getTextString());
      String description = taDescription.getTextString();
      int stock_quantity = Integer.parseInt(tfStock.getTextString());
      int category_id = get_category_id(txt.getText());
      System.out.println(category_id);
      if (category_id == 0) {
        JOptionPane.showMessageDialog(null, "Not valid category name");
      }
      else {
        JOptionPane.showMessageDialog(null, "Success");
        DBQueries.insert_product(name, price, description, stock_quantity, category_id);
      }
    }
    return;
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);
  private Font font_bold = FontManager.getFont("RobotoMonoBold", 14f);

  private ResourceManager rm = ResourceManager.getInstance();
  private JPanel thisPanel = new JPanel();
  private JLabel lblAddProduct = new JLabel("Add Product");
  private MTextField tfTitle, tfPrice, tfStock;
  private JTextField txt;
  private MComboBox<String> cbCategory;
  private MTextArea taDescription;
  private MButton uploadButton;
  private ColorScheme colors;
}
