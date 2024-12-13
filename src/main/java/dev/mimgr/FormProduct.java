package dev.mimgr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.component.NotificationPopup;
import dev.mimgr.component.ProductTableView;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers;

/**
 *
 * @author dn200
 */
public class FormProduct extends JPanel implements ActionListener, DocumentListener {

  public FormProduct() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    InitializeComponent();

    this.setLayout(new GridBagLayout());
    int padding = 25;
    // Top
    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;

    c.insets = new Insets(20, 25, 20, 15);
    this.add(Helpers.createHomeButton(), c);
    c.gridx++;

    c.insets = new Insets(26, 0, 25, padding);
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(topLabel, c);
    c.gridx++;

    c.weightx = 0.0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.insets = new Insets(20, 5, 20, 5);
    this.add(this.btnExport, c);
    c.gridx++;

    c.insets = new Insets(20, 5, 20, padding);
    this.add(btnAddProduct, c);
    // Content
    {
      GridBagConstraints cc = new GridBagConstraints();
      contentContainer.setLayout(new GridBagLayout());
      contentContainer.setBackground(colors.m_bg_0);

      String[] items = {
          "Bulk actions",
          "Delete Permanently",
          "Edit"
      };

      this.cbBulkAction = new MComboBox<>(items, colors);
      this.cbBulkAction.setBackground(colors.m_bg_0);
      this.cbBulkAction.setForeground(colors.m_grey_0);

      cc.gridx = 0;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.ipadx = 50;
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.BOTH;
      cc.insets = new Insets(15, 15, 0, 5);
      contentContainer.add(cbBulkAction, cc);

      cc.gridx = 1;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.insets = new Insets(15, 0, 0, 15);
      cc.fill = GridBagConstraints.VERTICAL;
      contentContainer.add(btnApplyBulkAction, cc);

      cc.gridx = 2;
      cc.gridy = 0;
      cc.weightx = 1.0;
      cc.fill = GridBagConstraints.VERTICAL;
      cc.anchor = GridBagConstraints.FIRST_LINE_END;
      contentContainer.add(filterTextField, cc);

      cc.gridx = 0;
      cc.gridy = 1;
      cc.weightx = 1.0;
      cc.weighty = 1.0;
      cc.gridwidth = 4;
      cc.insets = new Insets(15, 0, 15, 0);
      cc.fill = GridBagConstraints.BOTH;
      contentContainer.add(productTableView, cc);
    }

    c.gridx = 0;
    c.gridy = 1;
    c.insets = new Insets(0, padding, 20, padding);
    c.fill = GridBagConstraints.BOTH;

    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    this.add(contentContainer, c);
  }

  private void InitializeComponent() {
    Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
    Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
    // Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
    Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

    this.productTableView = new ProductTableView();
    this.setBackground(colors.m_bg_dim);

    this.topLabel.setFont(nunito_bold_20);
    this.topLabel.setForeground(colors.m_fg_0);

    this.btnImport.setBackground(colors.m_bg_2);
    this.btnImport.setBorderColor(colors.m_bg_2);
    this.btnImport.setForeground(colors.m_fg_0);
    this.btnImport.setHoverBorderColor(colors.m_bg_1);
    this.btnImport.setHoverBackgroundColor(colors.m_bg_1);
    this.btnImport.setClickBackgroundColor(colors.m_bg_dim);
    this.btnImport.setFont(nunito_extrabold_14);

    this.btnExport.setBackground(colors.m_bg_2);
    this.btnExport.setBorderColor(colors.m_bg_2);
    this.btnExport.setHoverBorderColor(colors.m_bg_1);
    this.btnExport.setHoverBackgroundColor(colors.m_bg_1);
    this.btnExport.setClickBackgroundColor(colors.m_bg_dim);
    this.btnExport.setForeground(colors.m_fg_0);
    this.btnExport.setFont(nunito_extrabold_14);
    this.btnExport.addActionListener(this);

    this.btnAddProduct.setBackground(colors.m_bg_2);
    this.btnAddProduct.setBorderColor(colors.m_bg_2);
    this.btnAddProduct.setHoverBorderColor(colors.m_green);
    this.btnAddProduct.setHoverBackgroundColor(colors.m_bg_1);
    this.btnAddProduct.setClickBackgroundColor(colors.m_bg_dim);
    this.btnAddProduct.setForeground(colors.m_green);
    this.btnAddProduct.setFont(nunito_extrabold_14);
    this.btnAddProduct.setIcon(IconManager.getIcon("add.png", 16, 16, colors.m_green));
    this.btnAddProduct.setText(" " + this.btnAddProduct.getText());
    this.btnAddProduct.addActionListener(this);

    this.filterTextField.setIcon(IconManager.getIcon("search.png", 20, 20, colors.m_grey_0), MTextField.ICON_PREFIX);
    this.filterTextField.setBackground(colors.m_bg_dim);
    this.filterTextField.setForeground(colors.m_fg_0);
    this.filterTextField.setPlaceholder("Filter products");
    this.filterTextField.setPlaceholderForeground(colors.m_grey_0);
    this.filterTextField.setBorderWidth(1);
    this.filterTextField.setBorderColor(colors.m_bg_5);
    this.filterTextField.setInputForeground(colors.m_fg_0);
    this.filterTextField.setFont(nunito_bold_14);
    this.filterTextField.getDocument().addDocumentListener(this);

    this.btnApplyBulkAction.setForeground(colors.m_grey_2);
    this.btnApplyBulkAction.setBackground(colors.m_bg_0);
    this.btnApplyBulkAction.setBorderColor(colors.m_bg_5);
    this.btnApplyBulkAction.setHoverBackgroundColor(colors.m_bg_1);
    this.btnApplyBulkAction.setClickBackgroundColor(colors.m_bg_dim);
    this.btnApplyBulkAction.setHoverForegroundColor(colors.m_blue);
    this.btnApplyBulkAction.setHoverBorderColor(colors.m_blue);
    this.btnApplyBulkAction.setBorderRadius(0);
    this.btnApplyBulkAction.addActionListener(this);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.btnAddProduct) {
      FormAddProduct jFrameAddProduct = new FormAddProduct();
      jFrameAddProduct.setVisible(true);
      productTableView.setButtonRefreshOnClick(
        jFrameAddProduct.getAddProductSubmitButton()
      );
    }

    if (e.getSource() == this.btnExport) {
      FormExport jFrameExport = new FormExport();
      jFrameExport.setVisible(true);
    }

    if (e.getSource() == this.btnApplyBulkAction) {
      System.out.println(((String) this.cbBulkAction.getSelectedItem()));
      if (((String) this.cbBulkAction.getSelectedItem()).equals("Edit")) {

        FormEditProduct frame = new FormEditProduct(productTableView.getSelectedProducts());
        frame.setVisible(true);
        List<UploadPanel> uploadPanel = frame.getUploadPanels();
        for (UploadPanel panel : uploadPanel) {
          productTableView.setButtonRefreshOnClick(panel.getDeleteComponent());
          productTableView.setButtonRefreshOnClick(panel.getSubmitComponent());
        }
      }

      if (((String) this.cbBulkAction.getSelectedItem()).equals("Delete Permanently")) {
        int selectedProductsCount = productTableView.getSelectedProducts().size();
        int response = JOptionPane.showConfirmDialog(
            this,
            "Delete " + selectedProductsCount + " items?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
          productTableView.deleteSelectedProducts();
          PanelManager.createPopup(new NotificationPopup("Deleted " + selectedProductsCount + " product(s)", NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
        }
      }
    }
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    get_intruments(this.filterTextField.getText());
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    get_intruments(this.filterTextField.getText());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    get_intruments(this.filterTextField.getText());
  }


  private void get_intruments(String name) {
    if (name.equals(this.filterTextField.getPlaceholder())) {
      return;
    }
    if (!name.isBlank()) {
      productTableView.updateView(() -> ProductRecord.selectLikeName(name));
    } else {
      productTableView.reset();
    }
  }

  private ColorScheme colors;
  private ProductTableView productTableView;

  private RoundedPanel contentContainer = new RoundedPanel();
  private JLabel topLabel = new JLabel("Products");

  private MTextField filterTextField = new MTextField(30);
  private MButton btnImport = new MButton("Import");
  private MButton btnExport = new MButton("Export");
  private MButton btnAddProduct = new MButton("Add product");
  private MButton btnApplyBulkAction = new MButton("Apply");
  private MComboBox<String> cbBulkAction;
}
