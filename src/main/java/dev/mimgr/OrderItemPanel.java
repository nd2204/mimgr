package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dev.mimgr.component.OrderItemTableView;
import dev.mimgr.component.ProductSelectionView;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.OrderItemRecord;
import dev.mimgr.db.OrderRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class OrderItemPanel extends JPanel implements ActionListener {

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

    this.taNotes = new MTextArea();
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
    tfSearchProduct.getDocument().addDocumentListener(new TextFieldDocumentListener());

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
    this.btnBrowseProduct.addActionListener(this);

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

    this.orderItemTableView = new OrderItemTableView();

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

      initProductSelectionViewIfNull();
      gc.insets = new Insets(0, 1, 20, 1);
      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.gridwidth = 3;
      this.add(productSelectionView, gc);
    }
  }

  private class InvoicePanel extends JPanel {
    public InvoicePanel() {
      this.setBackground(null);
      this.setOpaque(false);
      this.setLayout(new GridBagLayout());
      this.setMinimumSize(new Dimension(200, this.getPreferredSize().height));

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.insets = new Insets(0, 0, 20, 0);

      setRow(c, lblSubtotal, lblSubtotalValue);
      setRow(c, lblTaxes, lblTaxesValue);
      setRow(c, lblTotal, lblTotalValue);

      lblSubtotal.setForeground(colors.m_grey_0);
      lblTaxes.setForeground(colors.m_blue);
      lblTaxesValue.setForeground(colors.m_blue);
      lblTotalValue.setFont(FontManager.getFont("NunitoExtraBold", 16.0f));
      lblTotal.setFont(FontManager.getFont("NunitoExtraBold", 16.0f));
      lblTotal.setForeground(colors.m_fg_0);
      lblTotalValue.setForeground(colors.m_fg_0);
    }

    private void setRow(GridBagConstraints c, JLabel lblLeft, JLabel lblRight) {
      lblLeft.setFont(nunito_bold_16);
      lblRight.setFont(nunito_bold_16);
      lblLeft.setHorizontalAlignment(SwingConstants.LEFT);
      lblRight.setHorizontalAlignment(SwingConstants.RIGHT);
      lblLeft.setForeground(colors.m_grey_0);
      lblRight.setForeground(colors.m_grey_0);

      c.gridx = 0;
      c.ipadx = 50;
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      this.add(lblLeft, c);
      c.gridx++;

      c.weightx = 1.0;
      c.fill = GridBagConstraints.BOTH;
      this.add(Box.createHorizontalGlue(), c);
      c.gridx++;

      c.ipadx = 0;
      c.gridx = 2;
      c.weightx = 0.0;
      c.anchor = GridBagConstraints.FIRST_LINE_END;
      this.add(lblRight, c);

      c.gridy++;
    }

    public void setSubtotalValue(double val) {
      lblSubtotalValue.setText(String.format("€%.2f",val));
      setTotalValue(calculateTotal());
    }

    public double getSubtotalValue() {
      return Double.parseDouble(lblSubtotalValue.getText().replace("€", ""));
    }

    public void setTaxesValue(double val) {
      lblTaxesValue.setText(String.format("€%.2f",val));
      setTotalValue(calculateTotal());
    }

    public double getTaxesValue() {
      return Double.parseDouble(lblTaxesValue.getText().replace("€", ""));
    }

    public void setTotalValue(double val) {
      lblTotalValue.setText(String.format("€%.2f",val));
    }

    public double getTotalValue() {
      return Double.parseDouble(lblTotalValue.getText().replace("€", ""));
    }

    public double calculateTotal() {
      return getSubtotalValue() + getTaxesValue();
    }

    private JLabel lblSubtotal = new JLabel("Subtotal");
    private JLabel lblSubtotalValue = new JLabel("€0.00");

    private JLabel lblTaxes = new JLabel("Taxes");
    private JLabel lblTaxesValue = new JLabel("€0.00");

    private JLabel lblTotal = new JLabel("Total");
    private JLabel lblTotalValue = new JLabel("€0.00");
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

      // ----------
      gc.gridx = 0;
      gc.gridy = 0;
      gc.gridwidth = GridBagConstraints.REMAINDER;
      gc.weightx = 1.0;
      gc.fill = GridBagConstraints.HORIZONTAL;
      // ----------

      gc.insets = new Insets(20, 20, 10, 20);
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      this.add(lblPayment, gc);
      gc.gridy++;

      gc.insets = new Insets(0, 1, 30, 1);
      orderItemTableView.setVisible(false);
      this.add(orderItemTableView, gc);
      gc.gridy++;

      // ----------
      gc.gridx = 0;
      gc.gridy = 2;
      gc.gridwidth = 1;
      gc.weightx = 1.0;
      gc.insets = new Insets(0, 20, 10, 20);
      // ----------

      this.add(lblNote, gc);
      gc.gridy++;

      this.add(taNotes, gc);

      // ----------
      gc.gridy = 2;
      gc.gridx++;
      // ----------

      gc.insets = new Insets(0, 0, 20, 20);
      invoicePanel = new InvoicePanel();
      gc.fill = GridBagConstraints.BOTH;
      gc.weighty = 1.0;
      gc.gridheight = GridBagConstraints.REMAINDER;

      JSeparator sep = new JSeparator(JSeparator.VERTICAL);
      sep.setForeground(colors.m_bg_5);
      sep.setBackground(null);
      this.add(sep, gc);
      gc.gridx++;

      this.add(invoicePanel, gc);
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
      String name = tfSearchProduct.getText();
      if (name.equals(tfSearchProduct.getPlaceholder())) {
        return;
      }
      if (!name.isBlank()) {
        productSelectionView.setVisible(true);
        productSelectionView.updateView(() -> ProductRecord.selectLikeName(name));
      } else {
        productSelectionView.setVisible(false);
      }
    }
  }

  private void initProductSelectionViewIfNull() {
    if (productSelectionView == null || !productSelectionView.isValid()) {
      productSelectionView = new ProductSelectionView(orderItemTableView);
      orderItemTableView.addTableModelListener(new TableModelListener() {
        private List<Entry<ProductRecord, Integer>> itemList;
        private double taxRate = 0.05; // Apply fixed 5% tax
        @Override
        public void tableChanged(TableModelEvent ev) {
          if (orderItemTableView.isVisible()) {
            btnSubmit.setBackground(colors.m_blue);
            btnSubmit.setBorderColor(colors.m_bg_1);
            btnSubmit.setDefaultForeground(colors.m_fg_1);
            btnSubmit.setEnabled(true);
          } else {
            btnSubmit.setBackground(colors.m_bg_1);
            btnSubmit.setBorderColor(colors.m_bg_1);
            btnSubmit.setDefaultForeground(colors.m_bg_4);
            btnSubmit.setEnabled(false);
          }

          int row = ev.getFirstRow();
          int column = ev.getColumn();
          switch (ev.getType()) {
            case TableModelEvent.UPDATE: {
              Object newValue = orderItemTableView.model.getValueAt(row, column);
              if (column == 3) {
                if (newValue instanceof String s) {
                  int val = Integer.parseInt(s);
                  orderItemTableView.setProduct(
                    orderItemTableView.productList.get(row),
                  (val < 1) ? 1 : ((val > 99) ? 99 : val)
                  );
                }
              }
              break;
            }
          }

          itemList = orderItemTableView.getItems();
          double subtotal = 0;
          for (Entry<ProductRecord, Integer> e : itemList) {
            ProductRecord pr = e.getKey();
            int quantity = e.getValue();
            subtotal += pr.m_price * quantity;
          }
          invoicePanel.setSubtotalValue(subtotal);
          invoicePanel.setTaxesValue(subtotal * taxRate);
        }
      });
      productSelectionView.setVisible(false);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnBrowseProduct) {
      if (tfSearchProduct.getTextString().isEmpty()) {
        productSelectionView.setVisible(!productSelectionView.isVisible());
      }
    }
  }

  public int commit_selected_order_items() {
    // Create new order
    OrderRecord or = new OrderRecord(
      0,
      Instant.now(),
      invoicePanel.getTotalValue(),
      OrderRecord.paymentStatuses[OrderRecord.PAYMENT_STATUS_PAID],
      OrderRecord.orderStatuses[OrderRecord.ORDER_STATUS_CLOSED]
    );
    OrderRecord.insert(or);

    List<OrderItemRecord> orderItemRecords = new ArrayList<>();
    orderItemTableView.getItems().forEach((e) -> {
      int quantity = e.getValue(); 
      ProductRecord pr = e.getKey();
      OrderItemRecord oir = new OrderItemRecord(
        0,
        OrderRecord.selectByFields(or).m_id,
        pr.m_id,
        quantity,
        pr.m_price,
        pr.m_price * quantity
      );
      orderItemRecords.add(oir);
    });
    return OrderItemRecord.insert(orderItemRecords);
  }

  private void setLabelStyle(JLabel lbl) {
    lbl.setForeground(colors.m_grey_1);
    lbl.setFont(nunito_bold_14);
  }

  // Declare form components
  private JLabel lblOrderTitle = new JLabel("Order Detail");
  public MTextField tfSearchProduct;
  public MButton btnBrowseProduct;

  private JLabel lblPayment = new JLabel("Payment");
  private JLabel lblNote = new JLabel("Note");
  public MTextArea taNotes;

  public JLabel lblPanelTitle = new JLabel("Create Order");
  public JPanel thisPanel = new JPanel();
  public MButton btnSubmit, btnDelete;

  public ProductSelectionView productSelectionView;
  private OrderItemTableView orderItemTableView;
  private InvoicePanel invoicePanel;

  private ColorScheme colors;
  private OrderRecord orderRecord;

  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);
}
