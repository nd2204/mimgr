package dev.mimgr.component;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.FormEditProduct;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.UploadPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.builtin.ColorScheme;

public class ProductTableView extends JPanel implements TableModelListener {
  public ProductTableView(ColorScheme colors) {
    this.colors = colors;

    // Initialization
    this.table = new MTable(colors);
    this.tv = new TableView();
    this.tableScrollPane = new MScrollPane(this.colors);
    this.model = new DefaultTableModel() {
      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 0;
      }
    };

    this.table.setFillsViewportHeight(true);
    this.table.setRowHeight(100);
    this.table.setAutoscrolls(true);
    this.table.setup_scrollbar(tableScrollPane);
    this.table.setModel(model);

    this.tableScrollPane.add(table);
    this.tableScrollPane.setBackground(colors.m_bg_0);
    this.tableScrollPane.setOpaque(true);
    this.tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

    tv.add_column(table, "", TableView.setup_checkbox_column(colors));
    tv.add_column(table, "", TableView.setup_image_column(colors));
    tv.add_column(table, "Name", TableView.setup_custom_column(150, 150, Integer.MAX_VALUE));
    tv.add_column(table, "Price", TableView.setup_custom_column(80, 80, 120));
    tv.add_column(table, "Stock", TableView.setup_custom_column(50, 50, 100));
    tv.add_column(table, "Description", TableView.setup_default_column());
    tv.load_column(table, model);

    model.addTableModelListener(this);
    updateView(() -> ProductRecord.selectAll());

    this.setLayout(new BorderLayout());
    this.add(tableScrollPane);
    tableScrollPane.setViewportView(table);
    this.setVisible(true);

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Check if it's a double-click
        if (e.getClickCount() == 2) {
          int row = table.getSelectedRow();
          if (row != -1) {
            FormEditProduct frame = new FormEditProduct(productList.get(row));
            List<UploadPanel> uploadPanels = frame.getUploadPanels();
            frame.setVisible(true);
            for (UploadPanel panel : uploadPanels) {
              setButtonRefreshOnClick(panel.getDeleteComponent());
              setButtonRefreshOnClick(panel.getSubmitComponent());
            }
          }
        }
      }
    });
  }

  public void updateView(Supplier<ResultSet> queryInvoker) {
    model.setRowCount(0);
    selectedProducts.clear();
    productList = new ArrayList<>();
    ResultSet queryResult = queryInvoker.get();
    currentQueryInvoker = queryInvoker;
    try {
      while (queryResult.next()) {
        ProductRecord pr = new ProductRecord(queryResult);
        ImageRecord ir = ImageRecord.selectById(pr.m_image_id);

        Icon icon = IconManager.getIcon("image.png", colors.m_grey_0);
        if (ir != null) {
          Icon testIcon = IconManager.loadIcon(Paths.get(ir.m_url).toAbsolutePath().toFile());
          if (testIcon != null) {
            icon = testIcon;
          }
        }
        productList.add(pr);
        model.addRow(new Object[] {
            Boolean.FALSE,
            icon,
            pr.m_name,
            "€ " + pr.m_price,
            pr.m_stock_quantity,
            pr.m_description
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteSelectedProducts() {
    for (ProductRecord pr : selectedProducts.values()) {
      deleteProduct(pr);
    }
    selectedProducts.clear();
    refresh();
  }

  public List<ProductRecord> getSelectedProducts() {
    return new ArrayList<>(this.selectedProducts.values());
  }

  public void refresh() {
    updateView(currentQueryInvoker);
  }

  public void reset() {
    updateView(() -> ProductRecord.selectAll());
  }

  private void deleteProduct(ProductRecord pr) {
    ProductRecord.delete(pr);
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

  public void setButtonRefreshOnClick(MButton btn) {
    btn.addActionListener((actionEvent) -> {
      new Thread(() -> {
        SwingUtilities.invokeLater(() -> {
          refresh();
        });
      }).start();
    });
  }

  private Supplier<ResultSet> currentQueryInvoker;
  private HashMap<Integer, ProductRecord> selectedProducts = new HashMap<>();
  private ArrayList<ProductRecord> productList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
