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
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.CategoryRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class ProductExportPanel extends JPanel {

  public MComboBox<CategoryRecord> getCategoryComponent() {
    return this.cbCategory;
  }

  public MComboBox<ProductRecord> getProductComponent() {
    return this.cbProduct;
  }

  public MButton getSubmitComponent() {
    return this.btnSubmit;
  }

  public JLabel getLabelComponent() {
    return this.lblExportProduct;
  }

  public MButton getDeleteComponent() {
    return this.btnDelete;
  }

  public String getCategoryName(int category_id) {
    return get_category_name(category_id);
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

  ProductExportPanel() {
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

    lblExportProduct.setFont(nunito_bold_20);
    lblExportProduct.setForeground(colors.m_fg_0);
    gbc.insets = new Insets(40, 10, 20, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    thisPanel.add(lblExportProduct, gbc);

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    thisPanel.add(new exportOptionPanel(), gbc);

    gbc.insets = new Insets(0, 10, 40, 5);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    thisPanel.add(btnDelete, gbc);

    gbc.insets = new Insets(0, 5, 40, 10);
    gbc.gridx = 1;
    gbc.gridy = 2;
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

    cbCategory = new MComboBox<>(colors);
    CategoryTree.populateComboBox(cbCategory);
    cbCategory.getEditor().getEditorComponent().setFont(nunito_bold_14);

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
    this.btnSubmit = new MButton("Generate CSV");
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

  private class exportOptionPanel extends RoundedPanel {
    public exportOptionPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblTitle);
      setLabelStyle(lblProduct);
      setLabelStyle(lblCategory);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblCategory, gc);

      gc.gridx = 0;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      gc.ipadx = 30;
      gc.ipady = 30;
      this.add(cbCategory, gc);

      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(20, 25, 5, 20);
      gc.ipadx = 0;
      gc.ipady = 0;
      this.add(lblProduct, gc);

      gc.gridx = 0;
      gc.gridy = 4;
      gc.insets = new Insets(0, 20, 20, 20);
      gc.ipadx = 30;
      gc.ipady = 30;
      this.add(cbProduct, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private JLabel lblTitle = new JLabel("Title");
    private JLabel lblProduct = new JLabel("Product");
    private JLabel lblCategory = new JLabel("Category");
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private JPanel thisPanel = new JPanel();
  private JLabel lblExportProduct = new JLabel("Export products to a CSV file");
  private MComboBox<CategoryRecord> cbCategory;
  private MComboBox<ProductRecord> cbProduct;
  private MButton btnSubmit, btnDelete;
  private ColorScheme colors;
}
