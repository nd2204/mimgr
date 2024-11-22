package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.component.FilterOptionPanel;
import dev.mimgr.component.OrderTableView;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.OrderRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormOrder extends JPanel implements ActionListener, DocumentListener {
  public FormOrder() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(this.colors.m_bg_dim);

    InitializeComponent();

    this.setLayout(new GridBagLayout());
    int padding = 25;

    GridBagConstraints c = new GridBagConstraints();
    // ----------------------------------------------
    c.gridx = 0;
    c.gridy = 0;
    // Row 0 ----------------------------------------

    c.insets = new Insets(25, padding, 25, padding);
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(topLabel, c);
    c.gridx++;

    c.gridy = 0;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.insets = new Insets(20, 5, 20, 5);
    this.add(this.btnExport, c);
    c.gridx++;

    c.gridx = 2;
    c.insets = new Insets(20, 5, 20, padding);
    c.weightx = 0.0;
    this.add(btnCreateOrder, c);

    // ----------------------------------------------
    c.gridx = 0;
    c.gridy = 1;
    // Row 1 ----------------------------------------

    c.insets = new Insets(0, padding, 20, padding);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = 4;
    setupContentContainer();
    this.add(contentContainer, c);
  }

  private void setupContentContainer() {
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

    // ----------------------------------------------
    cc.gridx = 0;
    cc.gridy = 0;
    // Row 0 ----------------------------------------

    cc.weightx = 1.0;
    cc.anchor = GridBagConstraints.FIRST_LINE_START;
    cc.insets = new Insets(5, 5, 0, 5);
    contentContainer.add(filterOptionPanel, cc);

    // End Row 0 ------------------------------------
    cc.gridx = 0;
    cc.gridy = 1;
    // Row 1 ----------------------------------------

    cc.weightx = 1.0;
    cc.insets = new Insets(5, 15, 0, 5);
    cc.fill = GridBagConstraints.BOTH;
    contentContainer.add(filterTextField, cc);
    cc.gridx++;

    cc.weightx = 0.0;
    cc.ipadx = 50;
    cc.insets = new Insets(5, 15, 0, 5);
    cc.fill = GridBagConstraints.VERTICAL;
    cc.anchor = GridBagConstraints.FIRST_LINE_END;
    contentContainer.add(cbBulkAction, cc);
    cc.gridx++;

    cc.ipadx = 0;
    cc.insets = new Insets(5, 0, 0, 15);
    cc.fill = GridBagConstraints.VERTICAL;
    contentContainer.add(btnApplyBulkAction, cc);

    // End Row 1 ------------------------------------
    cc.gridx = 0;
    cc.gridy = 2;
    // Row 2 ----------------------------------------

    cc.weightx = 1.0;
    cc.weighty = 1.0;
    cc.gridwidth = 4;
    cc.insets = new Insets(15, 0, 15, 0);
    cc.fill = GridBagConstraints.BOTH;
    contentContainer.add(orderTableView, cc);
  }

  private void InitializeComponent() {
    Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
    Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
    // Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
    Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

    this.orderTableView = new OrderTableView();
    this.setBackground(colors.m_bg_dim);

    this.topLabel.setFont(nunito_bold_20);
    this.topLabel.setForeground(colors.m_fg_0);

    this.btnExport = new MButton("Export");
    this.btnExport.setBackground(colors.m_bg_2);
    this.btnExport.setBorderColor(colors.m_bg_2);
    this.btnExport.setHoverBorderColor(colors.m_bg_1);
    this.btnExport.setHoverBackgroundColor(colors.m_bg_1);
    this.btnExport.setClickBackgroundColor(colors.m_bg_dim);
    this.btnExport.setForeground(colors.m_fg_0);
    this.btnExport.setFont(nunito_extrabold_14);

    Function<String, Consumer<MButton>> buttonSetupGenerator = (text) -> {
      return (button) -> {
        button.setText(text);
        button.setBackground(colors.m_bg_2);
        button.setBorderColor(colors.m_bg_3);
        button.setForeground(colors.m_grey_2);
        button.setHoverBackgroundColor(colors.m_bg_3);
        button.setHoverBorderColor(colors.m_bg_3);
        button.setClickBackgroundColor(colors.m_bg_1);
        button.setClickBorderColor(colors.m_bg_1);
        button.setPreferredSize(new Dimension(100, button.getPreferredSize().height));
      };
    };

    this.filterOptionPanel = new FilterOptionPanel();
    this.filterOptionPanel.addFilterOption("All", buttonSetupGenerator.apply("All"), (e) -> {
      orderTableView.updateView(() -> OrderRecord.selectAllNewest());
    });
    this.filterOptionPanel.addFilterOption("Open", buttonSetupGenerator.apply("Open"), (e) -> {
      orderTableView.updateView(() -> OrderRecord.selectByField(OrderRecord.FIELD_ORDER_STATUS, "Open"));
    });
    this.filterOptionPanel.addFilterOption("Closed", buttonSetupGenerator.apply("Closed"), (e) -> {
      orderTableView.updateView(() -> OrderRecord.selectByField(OrderRecord.FIELD_ORDER_STATUS, "Closed"));
    });
    this.filterOptionPanel.addFilterOption("Paid", buttonSetupGenerator.apply("Paid"), (e) -> {
      orderTableView.updateView(() -> OrderRecord.selectByField(OrderRecord.FIELD_PAYMENT_STATUS, "Paid"));
    });
    this.filterOptionPanel.addFilterOption("Unpaid", buttonSetupGenerator.apply("Unpaid"), (e) -> {
      orderTableView.updateView(() -> OrderRecord.selectByField(OrderRecord.FIELD_PAYMENT_STATUS, "Unpaid"));
    });

    this.btnCreateOrder = new MButton("Create Order");
    this.btnCreateOrder.setBackground(colors.m_bg_2);
    this.btnCreateOrder.setBorderColor(colors.m_bg_2);
    this.btnCreateOrder.setHoverBorderColor(colors.m_green);
    this.btnCreateOrder.setHoverBackgroundColor(colors.m_bg_1);
    this.btnCreateOrder.setClickBackgroundColor(colors.m_bg_dim);
    this.btnCreateOrder.setForeground(colors.m_green);
    this.btnCreateOrder.setFont(nunito_extrabold_14);
    this.btnCreateOrder.setIcon(IconManager.getIcon("add.png", 16, 16, colors.m_green));
    this.btnCreateOrder.setText(" " + this.btnCreateOrder.getText());
    this.btnCreateOrder.addActionListener(this);

    this.filterTextField = new MTextField(30);
    this.filterTextField.setIcon(IconManager.getIcon("search.png", 20, 20, colors.m_grey_0), MTextField.ICON_PREFIX);
    this.filterTextField.setBackground(colors.m_bg_dim);
    this.filterTextField.setForeground(colors.m_fg_0);
    this.filterTextField.setPlaceholder("Filter Orders");
    this.filterTextField.setPlaceholderForeground(colors.m_grey_0);
    this.filterTextField.setBorderWidth(1);
    this.filterTextField.setBorderColor(colors.m_bg_5);
    this.filterTextField.setInputForeground(colors.m_fg_0);
    this.filterTextField.setFont(nunito_bold_14);
    this.filterTextField.getDocument().addDocumentListener(this);

    this.btnApplyBulkAction = new MButton("Apply");
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
    if (e.getSource() == this.btnCreateOrder) {
      FormCreateOrder jFrameAddOrder = new FormCreateOrder();
      jFrameAddOrder.setVisible(true);
      // OrderTableView.setButtonRefreshOnClick(
      //   jFrameAddOrder.getAddOrderSubmitButton()
      // );
    }

    if (e.getSource() == this.btnApplyBulkAction) {
      if (((String) this.cbBulkAction.getSelectedItem()).equals("Edit")) {

      }

      if (((String) this.cbBulkAction.getSelectedItem()).equals("Delete Permanently")) {
        int response = JOptionPane.showConfirmDialog(
          this,
          "Delete " + orderTableView.getSelected().size() + " items?",
          "Confirm Delete",
          JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
          orderTableView.deleteSelected();
        }
      }
    }
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    filter(this.filterTextField.getText());
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    filter(this.filterTextField.getText());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    filter(this.filterTextField.getText());
  }


  private void filter(String name) {
    if (name.equals(this.filterTextField.getPlaceholder())) {
      return;
    }
    if (!name.isBlank()) {
      orderTableView.updateView(() -> OrderRecord.selectLike(name));
    } else {
      orderTableView.reset();
    }
  }

  private ColorScheme colors;
  private OrderTableView orderTableView;

  private RoundedPanel contentContainer = new RoundedPanel();
  private JLabel topLabel = new JLabel("Orders");

  private FilterOptionPanel filterOptionPanel;
  private MTextField filterTextField;
  private MButton btnExport;
  private MButton btnCreateOrder;
  private MButton btnApplyBulkAction;
  private MComboBox<String> cbBulkAction;
}
