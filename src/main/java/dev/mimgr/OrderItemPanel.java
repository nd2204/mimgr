package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class OrderItemPanel extends JPanel {

  public MComboBox<ProductRecord> getProductComponent() {
    return this.cbProduct;
  }

  public MButton getSubmitComponent() {
    return this.btnSubmit;
  }

  public JLabel getLabelComponent() {
    return this.lblAddProduct;
  }

  public MTextArea getDescriptionComponent() {
    return this.taDescription;
  }

  public MTextField getTitleComponent() {
    return this.tfTitle;
  }

  public MTextField getPriceComponent() {
    return this.tfPrice;
  }

  public MTextField getStockComponent() {
    return this.tfStock;
  }

  public MButton getDeleteComponent() {
    return this.btnDelete;
  }

  public String getCategoryName(int category_id) {
    return get_category_name(category_id);
  }

  public void setProductRecord(ProductRecord pr) {
    this.product = pr;
  }

  public ProductRecord getProductRecord() {
    return this.product;
  }

  private String get_category_name(int category_id) {
    ResultSet queryResult = ProductRecord.selectByKey(category_id);
    String name_result = "";
    try {
      while (queryResult.next()) {
        name_result = queryResult.getString("category_name");
      }
      return name_result;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return name_result;
  }

  OrderItemPanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_dim);
    this.setup_form_style();

    this.setPreferredSize(new Dimension(1000, this.getPreferredSize().height));
    this.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

    MScrollPane scrollPane = new MScrollPane();
    scrollPane.getVerticalScrollBar().setUnitIncrement(100);
    this.add(scrollPane);

    scrollPane.add(thisPanel);
    scrollPane.setViewportView(thisPanel);

    thisPanel.setLayout(new GridBagLayout());
    thisPanel.setBackground(colors.m_bg_dim);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    lblAddProduct.setFont(nunito_bold_20);
    lblAddProduct.setForeground(colors.m_fg_0);
    gbc.insets = new Insets(40, 10, 20, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    thisPanel.add(lblAddProduct, gbc);

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    thisPanel.add(new titleAndDescriptionPanel(), gbc);

    gbc.insets = new Insets(0, 10, 10, 5);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    thisPanel.add(new PricingPanel(), gbc);

    gbc.insets = new Insets(0, 5, 10, 10);
    gbc.gridx = 1;
    gbc.gridy = 2;
    thisPanel.add(new InventoryPanel(), gbc);

    gbc.insets = new Insets(0, 10, 40, 5);
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    thisPanel.add(btnDelete, gbc);

    gbc.insets = new Insets(0, 5, 40, 10);
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    thisPanel.add(btnSubmit, gbc);
  }

  public void setup_form_style() {
    // ========================= Fields =========================
    Consumer<MTextField> setup_common_textfield = (tf) -> {
      tf.setInputForeground(colors.m_fg_0);
      tf.setBackground(colors.m_bg_dim);
      tf.setBorderColor(colors.m_bg_4);
      tf.setFocusBorderColor(colors.m_blue);
      tf.setForeground(colors.m_fg_0);
      tf.setBorderWidth(2);
      tf.setFont(nunito_bold_14);
    };

    tfTitle = new MTextField(20);
    setup_common_textfield.accept(tfTitle);
    tfPrice = new MTextField(20);
    setup_common_textfield.accept(tfPrice);
    tfStock = new MTextField(20);
    setup_common_textfield.accept(tfStock);

    taDescription = new MTextArea(8, 50);
    taDescription.setPadding(new Insets(20, 20, 20, 20));
    taDescription.setInputForeground(colors.m_fg_0);
    taDescription.setBackground(colors.m_bg_dim);
    taDescription.setBorderColor(colors.m_bg_4);
    taDescription.setFocusBorderColor(colors.m_blue);
    taDescription.setForeground(colors.m_fg_0);
    taDescription.setBorderWidth(2);
    taDescription.setBorderRadius(15);
    taDescription.setFont(nunito_bold_14);
    taDescription.setLineWrap(true);


    cbProduct = new MComboBox<>(colors);
    try (ResultSet rs = ProductRecord.selectAll()) {
      while (rs.next()) {
        ProductRecord pr = new ProductRecord(rs);
        cbProduct.addItem(pr);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    cbProduct.getEditor().getEditorComponent().setFont(nunito_bold_14);

    // ========================= Buttons =========================
    this.btnSubmit = new MButton("Submit");
    this.btnSubmit.setFont(nunito_bold_14);
    this.btnSubmit.setBackground(colors.m_bg_1);
    this.btnSubmit.setBorderColor(colors.m_bg_1);
    this.btnSubmit.setDefaultForeground(colors.m_bg_4);
    this.btnSubmit.setBorderWidth(2);
    this.btnSubmit.setEnabled(true);

    this.btnDelete = new MButton("Cancel");
    this.btnDelete.setFont(nunito_bold_14);
    this.btnDelete.setBackground(colors.m_bg_dim);
    this.btnDelete.setBorderColor(colors.m_bg_3);
    this.btnDelete.setDefaultForeground(colors.m_grey_0);
    this.btnDelete.setHoverForegroundColor(colors.m_fg_1);
    this.btnDelete.setHoverBorderColor(colors.m_red);
    this.btnDelete.setHoverBackgroundColor(colors.m_red);
    this.btnDelete.setBorderWidth(2);
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
      setLabelStyle(lblProduct);
      this.lblOrderItemNumber.setFont(nunito_bold_16);
      this.lblOrderItemNumber.setForeground(colors.m_fg_0);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      gc.insets = new Insets(20, 25, 0, 20);
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 0;
      this.add(lblOrderItemNumber, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 1;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblProduct, gc);

      gc.gridx = 0;
      gc.gridy = 2;
      gc.insets = new Insets(0, 20, 10, 20);
      this.add(cbProduct, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 1;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblTitle, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 2;
      gc.insets = new Insets(0, 20, 10, 20);
      gc.weighty = 1.0;
      gc.fill = GridBagConstraints.BOTH;
      this.add(tfTitle, gc);

      gc.gridwidth = 2;
      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(5, 25, 5, 20);
      this.add(lblDescription, gc);
      gc.gridx = 0;
      gc.gridy = 4;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(taDescription, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private JLabel lblOrderItemNumber = new JLabel("Order Item 1");
    private JLabel lblTitle = new JLabel("Title");
    private JLabel lblDescription = new JLabel("Description");
    private JLabel lblProduct = new JLabel("Product");
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private JPanel thisPanel = new JPanel();
  private JLabel lblAddProduct = new JLabel("Create Order");
  private MTextField tfTitle, tfPrice, tfStock;
  private MComboBox<ProductRecord> cbProduct;
  private MTextArea taDescription;
  private MButton btnSubmit, btnDelete;
  private ColorScheme colors;
  private ProductRecord product;
}
