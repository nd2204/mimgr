package dev.mimgr;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.component.ProductSelectionView;
import dev.mimgr.component.ProductTableView;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.OrderRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class OrderItemPanel extends JPanel {

  public void setOrderRecord(OrderRecord pr) {
    this.orderRecord = pr;
  }

  public OrderRecord getOrderRecord() {
    return this.orderRecord;
  }

  OrderItemPanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    // ========================= Fields =========================
    Consumer<MTextField> setup_common_textfield = (tf) -> {
      tf.setInputForeground(colors.m_fg_0);
      tf.setBackground(colors.m_bg_dim);
      tf.setBorderColor(colors.m_bg_4);
      tf.setFocusBorderColor(colors.m_blue);
      tf.setForeground(colors.m_fg_0);
      tf.setPlaceholderForeground(colors.m_bg_2);
      tf.setBorderWidth(2);
      tf.setFont(nunito_bold_14);
      tf.setCaretColor(colors.m_grey_0);
      tf.setFont(nunito_bold_14);
    };

    this.taNotes = new MTextArea(1, 40);
    this.taNotes.setPadding(new Insets(10, 10, 10, 10));
    this.taNotes.setInputForeground(colors.m_fg_0);
    this.taNotes.setBackground(colors.m_bg_dim);
    this.taNotes.setBorderColor(colors.m_bg_4);
    this.taNotes.setFocusBorderColor(colors.m_blue);
    this.taNotes.setForeground(colors.m_fg_0);
    this.taNotes.setBorderWidth(2);
    this.taNotes.setBorderRadius(15);
    this.taNotes.setFont(nunito_bold_14);
    this.taNotes.setLineWrap(true);

    tfSearchProduct = new MTextField(30);
    setup_common_textfield.accept(tfSearchProduct);
    tfSearchProduct.setPlaceholder("Search Products");
    tfSearchProduct.setIcon(IconManager.getIcon("search.png", 16, 16, colors.m_bg_2), MTextField.ICON_PREFIX);

    // ========================= Buttons =========================
    this.btnBrowseProduct = new MButton("Browse Product");
    this.btnBrowseProduct.setFont(nunito_bold_14);
    this.btnBrowseProduct.setBackground(colors.m_bg_1);
    this.btnBrowseProduct.setBorderColor(colors.m_bg_3);
    this.btnBrowseProduct.setDefaultForeground(colors.m_grey_0);
    this.btnBrowseProduct.setHoverForegroundColor(colors.m_fg_0);
    this.btnBrowseProduct.setHoverBorderColor(colors.m_bg_3);
    this.btnBrowseProduct.setHoverBackgroundColor(colors.m_bg_2);
    this.btnBrowseProduct.setBorderWidth(2);

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

    // ==================== Layout ========================

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_dim);

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

    // -----------
    gbc.gridx = 0;
    gbc.gridy = 0;
    // -----------

    lblPanelTitle.setFont(nunito_bold_20);
    lblPanelTitle.setForeground(colors.m_fg_0);
    gbc.insets = new Insets(40, 10, 20, 10);
    thisPanel.add(lblPanelTitle, gbc);

    // -----------
    gbc.gridx = 0;
    gbc.gridy++;
    // -----------

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridwidth = 2;
    thisPanel.add(new titleAndDescriptionPanel(), gbc);

    // -----------
    gbc.gridx = 0;
    gbc.gridy++;
    // -----------

    // -----------
    gbc.gridx = 0;
    gbc.gridy++;
    // -----------

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridwidth = 2;
    thisPanel.add(new PaymentPanel(), gbc);

    // -----------
    gbc.gridx = 0;
    gbc.gridy++;
    // -----------

    gbc.insets = new Insets(0, 10, 40, 5);
    gbc.gridwidth = 1;
    thisPanel.add(btnDelete, gbc);
    gbc.gridx++;

    gbc.insets = new Insets(0, 5, 40, 10);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    thisPanel.add(btnSubmit, gbc);
  }

  private class PaymentPanel extends RoundedPanel {
    public PaymentPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblNote);
      lblPayment.setFont(nunito_bold_16);
      lblPayment.setForeground(colors.m_fg_0);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      // ----------
      gc.gridx = 0;
      gc.gridy = 0;
      // ----------

      gc.insets = new Insets(20, 20, 20, 20);
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      this.add(lblPayment, gc);

      // ----------
      gc.gridx = 0;
      gc.gridy++;
      // ----------

      gc.insets = new Insets(0, 20, 10, 20);
      gc.gridwidth = 1;
      this.add(lblNote, gc);
      gc.gridx++;

      // ----------
      gc.gridx = 0;
      gc.gridy++;
      // ----------

      gc.insets = new Insets(0, 20, 10, 20);
      gc.gridwidth = 1;
      this.add(taNotes, gc);
      gc.gridx++;
    }

  }

  private class titleAndDescriptionPanel extends RoundedPanel {
    public titleAndDescriptionPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      lblOrderTitle.setFont(nunito_bold_16);
      lblOrderTitle.setForeground(colors.m_fg_0);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      // ----------
      gc.gridx = 0;
      gc.gridy = 0;
      // ----------

      gc.insets = new Insets(20, 20, 20, 20);
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      this.add(lblOrderTitle, gc);

      // ----------
      gc.gridx = 0;
      gc.gridy++;
      // ----------

      gc.fill = GridBagConstraints.VERTICAL;
      gc.insets = new Insets(0, 20, 20, 10);
      this.add(tfSearchProduct, gc);
      gc.gridx++;

      gc.insets = new Insets(0, 0, 20, 20);
      this.add(btnBrowseProduct, gc);

      // ----------
      gc.gridx = 0;
      gc.gridy++;
      // ----------

      gc.insets = new Insets(0, 1, 20, 1);
      productSelectionView = new ProductSelectionView();
      gc.fill = GridBagConstraints.BOTH; 
      gc.gridwidth = 3;
      this.add(productSelectionView, gc);
    }
  }

  private void setLabelStyle(JLabel lbl) {
    lbl.setForeground(colors.m_grey_1);
    lbl.setFont(nunito_bold_14);
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private JLabel lblOrderTitle = new JLabel("Order Detail");
  public MTextField tfSearchProduct;
  public MButton btnBrowseProduct;

  private JLabel lblPayment = new JLabel("Payment");
  private JLabel lblNote = new JLabel("Description");
  public MTextArea taNotes;

  public JLabel lblPanelTitle = new JLabel("Create Order");
  public JPanel thisPanel = new JPanel();
  public MButton btnSubmit, btnDelete;

  public ProductSelectionView productSelectionView;

  private ColorScheme colors;
  private OrderRecord orderRecord;
}
