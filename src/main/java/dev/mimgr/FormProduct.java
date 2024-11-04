package dev.mimgr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MCheckBox;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBQueries;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.builtin.ColorScheme;

/**
 *
 * @author dn200
 */
public class FormProduct extends JPanel implements ActionListener, DocumentListener, TableModelListener {

  public FormProduct(ColorScheme colors) {
    this.colors = colors;
    Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
    Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
    // Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
    Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

    // =======================================================
    // Setup Layout
    // =======================================================
    this.setLayout(new GridBagLayout());

    int padding = 25;

    // Top
    c.insets = new Insets(25, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(topLabel, c);
    c.gridx = 1;
    c.gridy = 0;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.insets = new Insets(20, 5, 20, 5);
    this.add(this.importButton, c);
    c.weightx = 0.0;
    c.gridx = 2;
    this.add(this.exportButton, c);
    c.gridx = 3;
    c.insets = new Insets(20, 5, 20, padding);
    this.add(addProductButton, c);

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

      this.bulkAction = new MComboBox<>(items, colors);
      this.bulkAction.setBackground(colors.m_bg_0);
      this.bulkAction.setForeground(colors.m_grey_0);

      cc.gridx = 0;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.ipadx = 50;
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.BOTH;
      cc.insets = new Insets(15, 15, 0, 5);
      contentContainer.add(bulkAction, cc);

      cc.gridx = 1;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.insets = new Insets(15, 0, 0, 15);
      cc.fill = GridBagConstraints.VERTICAL;
      contentContainer.add(applyBulkAction, cc);

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
      setup_table();
      contentContainer.add(scrollPane, cc);
    }

    c.insets = new Insets(0, padding, 20, padding);
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = 4;
    this.add(contentContainer, c);

    // Post Content
    // =======================================================
    // Setup Appearance
    // =======================================================
    this.setBackground(colors.m_bg_dim);

    this.topLabel.setFont(nunito_bold_20);
    this.topLabel.setForeground(colors.m_fg_0);

    this.importButton.setBackground(colors.m_bg_2);
    this.importButton.setBorderColor(colors.m_bg_2);
    this.importButton.setForeground(colors.m_fg_0);
    this.importButton.setHoverBorderColor(colors.m_bg_1);
    this.importButton.setHoverBackgroundColor(colors.m_bg_1);
    this.importButton.setClickBackgroundColor(colors.m_bg_dim);
    this.importButton.setFont(nunito_extrabold_14);

    this.exportButton.setBackground(colors.m_bg_2);
    this.exportButton.setBorderColor(colors.m_bg_2);
    this.exportButton.setHoverBorderColor(colors.m_bg_1);
    this.exportButton.setHoverBackgroundColor(colors.m_bg_1);
    this.exportButton.setClickBackgroundColor(colors.m_bg_dim);
    this.exportButton.setForeground(colors.m_fg_0);
    this.exportButton.setFont(nunito_extrabold_14);

    this.addProductButton.setBackground(colors.m_bg_2);
    this.addProductButton.setBorderColor(colors.m_bg_2);
    this.addProductButton.setHoverBorderColor(colors.m_green);
    this.addProductButton.setHoverBackgroundColor(colors.m_bg_1);
    this.addProductButton.setClickBackgroundColor(colors.m_bg_dim);
    this.addProductButton.setForeground(colors.m_green);
    this.addProductButton.setFont(nunito_extrabold_14);
    this.addProductButton.setIcon(IconManager.getIcon("add.png", 16, 16, colors.m_green));
    this.addProductButton.setText(" " + this.addProductButton.getText());
    this.addProductButton.addActionListener(this);
    ;

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

    this.scrollPane.setBackground(colors.m_bg_0);
    this.scrollPane.setBorder(BorderFactory.createEmptyBorder());

    this.checkBoxModel.setCheckColor(colors.m_green);
    this.checkBoxModel.setBoxColor(colors.m_bg_4);
    this.checkBoxModel.setBackground(colors.m_bg_0);

    this.applyBulkAction.setForeground(colors.m_grey_2);
    this.applyBulkAction.setBackground(colors.m_bg_0);
    this.applyBulkAction.setBorderColor(colors.m_bg_5);
    this.applyBulkAction.setHoverBackgroundColor(colors.m_bg_1);
    this.applyBulkAction.setClickBackgroundColor(colors.m_bg_dim);
    this.applyBulkAction.setHoverForegroundColor(colors.m_blue);
    this.applyBulkAction.setHoverBorderColor(colors.m_blue);
    this.applyBulkAction.setBorderRadius(0);
  }

  private void setup_table() {
    table = new MTable(colors);
    model = new DefaultTableModel() {
      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 0;
      }
    };

    table.setModel(model);

    tv.add_column(table, "", TableView.setup_checkbox_column(colors));
    tv.add_column(table, "Name", TableView.setup_default_column());
    tv.add_column(table, "Price", TableView.setup_default_column());
    tv.add_column(table, "Stock", TableView.setup_default_column());
    tv.add_column(table, "Description", TableView.setup_default_column());
    tv.load_column(table, model);
    model.addTableModelListener(this);
    // get_all_intruments(model);

    scrollPane = new JScrollPane(table);
    table.setup_scrollbar(scrollPane);
    table.setFillsViewportHeight(true);

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Check if it's a double-click
        if (e.getClickCount() == 2) {
          // Get the selected row
          int row = table.getSelectedRow();
          if (row != -1) {
            // Retrieve data for the selected row
            ProductRecord pr = productList.get(row);
            FormEditProduct frame = new FormEditProduct(pr);
            frame.setVisible(true);

            // // Display the data in a dialog
            // JOptionPane.showMessageDialog(null,
            //     "Details:\nName: " + pr.m_name + "\nQuantity: " + pr.m_stock_quantity + "\nPrice: " + pr.m_price + "\nDescription: " + pr.m_description,
            //     "Intrument Details", JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
    });
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.addProductButton) {
      JFrame jFrame = new FormAddProduct(colors);
      jFrame.setVisible(true);
    }

    if (e.getSource() == this.applyBulkAction) {
    }
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    get_intruments(model, this.filterTextField.getText());
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    get_intruments(model, this.filterTextField.getText());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    get_intruments(model, this.filterTextField.getText());
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    int row = e.getFirstRow();
    int column = e.getColumn();
    switch (e.getType()) {
      case TableModelEvent.UPDATE:
        Object newValue = model.getValueAt(row, column);
        if (column == 0) {
          if (newValue instanceof Boolean) {
            if ((Boolean) newValue) {
              selectedProducts.put(row, productList.get(row));
            } else {
              selectedProducts.remove(row);
            }
          }
        }
        break;
    }
  }

  private void get_all_intruments(DefaultTableModel model) {
    updateTable(DBQueries.select_all_intruments(), model);
  }

  private void updateTable(ResultSet queryResult, DefaultTableModel model) {
    model.setRowCount(0);
    if (!productList.isEmpty()) {
      productList = new ArrayList<>();
    }
    try {
      while (queryResult.next()) {
        ProductRecord pr = new ProductRecord(queryResult);
        productList.add(pr);
        model.addRow(new Object[] {
            Boolean.FALSE,
            pr.m_name,
            pr.m_price,
            pr.m_stock_quantity,
            pr.m_description
        });
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void get_intruments(DefaultTableModel model, String name) {
    if (name.equals(this.filterTextField.getPlaceholder())) {
      return;
    }
    if (!name.isBlank()) {
      updateTable(DBQueries.select_intruments(name), model);
    } else {
      get_all_intruments(model);
    }
  }

  private TableView tv = new TableView();
  private HashMap<Integer, ProductRecord> selectedProducts = new HashMap<>();
  private ArrayList<ProductRecord> productList = new ArrayList<>();
  private MCheckBox checkBoxModel = new MCheckBox();
  private ColorScheme colors;
  private RoundedPanel contentContainer = new RoundedPanel();
  private JLabel topLabel = new JLabel("Products");
  private GridBagConstraints c = new GridBagConstraints();
  private MTextField filterTextField = new MTextField(30);
  private MTable table;
  private JScrollPane scrollPane;
  private MButton importButton = new MButton("Import");
  private MButton exportButton = new MButton("Export");
  private MButton addProductButton = new MButton("Add product");
  private DefaultTableModel model;
  private MComboBox<String> bulkAction;
  private MButton applyBulkAction = new MButton("Apply");
}
