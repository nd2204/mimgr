package dev.mimgr.component;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.FormEditProduct;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dev.mimgr.IconManager;
import dev.mimgr.PanelManager;
import dev.mimgr.TableView;
import dev.mimgr.UploadPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers.MultiClickHandler;
import dev.mimgr.theme.ColorTheme;

public class ProductTableView extends JPanel implements TableModelListener {
  public ProductTableView() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();

    // Initialization
    this.table = new MTable();
    this.tv = new TableView();
    this.tableScrollPane = new MScrollPane();
    this.model = new DefaultTableModel() {
      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 0 || column == 6;
      }
    };

    this.editOperation = (list) -> {
      FormEditProduct frame = new FormEditProduct(list);
      List<UploadPanel> uploadPanels = frame.getUploadPanels();
      frame.setVisible(true);
      for (UploadPanel panel : uploadPanels) {
        setButtonRefreshOnClick(panel.getDeleteComponent());
        setButtonRefreshOnClick(panel.getSubmitComponent());
      }
    };

    this.deleteOperation = (list) -> {
      if (!list.isEmpty()) {
        int selectedImagesCount = list.size();
        int response = JOptionPane.showConfirmDialog(
          this,
          "Delete " + selectedImagesCount + " items?",
          "Confirm Delete",
          JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
          deleteProducts(list);
          PanelManager.createPopup(new NotificationPopup("Deleted " + selectedImagesCount + " product(s)", NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
        }
      } else {
        JOptionPane.showMessageDialog(null, "Nothing to delete");
      }
    };

    this.doubleClickOperation = (row) -> {
      table.setValueAt(!(Boolean) table.getValueAt(row, 0), row, 0);
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
    tv.add_column(table, "Action", TableView.setup_action_button_column());
    tv.load_column(table, model);

    model.addTableModelListener(this);
    updateView(() -> ProductRecord.selectAll());

    this.setLayout(new BorderLayout());
    this.add(tableScrollPane);
    tableScrollPane.setViewportView(table);
    this.setVisible(true);

    table.addMouseListener(new MouseAdapter() {
      private MultiClickHandler multiClickHandler = new MultiClickHandler(2, 200);
      @Override
      public void mousePressed(MouseEvent e) {
        // Check if it's a double-click
        if (multiClickHandler.isValidClickCount()) {
          int row = table.getSelectedRow();
          if (row != -1) {
            doubleClickOperation.accept(row);
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

        List<JButton> buttons = new ArrayList<>();
        buttons.add(TableView.createEditActionButton(
        (e) -> {editOperation.accept(List.of(pr));}
        ));
        buttons.add(TableView.createDeleteActionButton(
        (e) -> {deleteOperation.accept(List.of(pr));}
        ));

        productList.add(pr);
        model.addRow(new Object[] {
          Boolean.FALSE,
          icon,
          pr.m_name,
          "€ " + pr.m_price,
          pr.m_stock_quantity,
          pr.m_description,
          buttons,
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteSelectedProducts() {
    deleteProducts(selectedProducts.values());
  }

  private void deleteProducts(Iterable<ProductRecord> products) {
    for (ProductRecord pr : products) {
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

  public void setDoubleClickOperation() {

  }

  private Consumer<Integer> doubleClickOperation;
  private Consumer<Iterable<ProductRecord>> editOperation;
  private Consumer<List<ProductRecord>> deleteOperation;
  private Supplier<ResultSet> currentQueryInvoker;
  private Supplier<ResultSet> defaultQueryInvoker;
  private HashMap<Integer, ProductRecord> selectedProducts = new HashMap<>();
  private ArrayList<ProductRecord> productList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
