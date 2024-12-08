package dev.mimgr.component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers.MultiClickHandler;
import dev.mimgr.theme.ColorTheme;


public class OrderItemTableView extends JPanel {
  public OrderItemTableView() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();

    // Initialization
    this.table = new MTable();
    this.tv = new TableView();
    this.productMap = new HashMap<>();
    this.model = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 3 || column == 4;
      }
    };

    this.doubleClickOperation = (row) -> {
    };

    this.table.setFillsViewportHeight(true);
    this.table.setRowHeight(100);
    this.table.setAutoscrolls(true);
    this.table.setModel(model);

    tv.add_column(table, "", TableView.setup_image_column());
    tv.add_column(table, "Name", TableView.setup_custom_column(150, 150, Integer.MAX_VALUE));
    tv.add_column(table, "Price", TableView.setup_custom_column(80, 80, 120));
    tv.add_column(table, "Quantity", TableView.setup_number_input_column());
    tv.add_column(table, "Action", TableView.setup_action_button_column());
    tv.load_column(table, model);

    model.addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent ev) {
      }
    });

    updateView();

    this.setLayout(new BorderLayout());
    JTableHeader header = table.getTableHeader();
    this.add(header, BorderLayout.NORTH);
    this.add(table, BorderLayout.SOUTH);

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

  public void updateView() {
    model.setRowCount(0);
    productList.clear();
    for (Entry<ProductRecord, Integer> entry : productMap.entrySet()) {
      ProductRecord pr = entry.getKey();
      productList.add(pr);
      int quantity = entry.getValue();
      ImageRecord ir = ImageRecord.selectById(pr.m_image_id);
      Icon icon = IconManager.getIcon("image.png", colors.m_grey_0);
      if (ir != null) {
        Icon testIcon = IconManager.loadIcon(Paths.get(ir.m_url).toAbsolutePath().toFile());
        if (testIcon != null) {
          icon = testIcon;
        }
      }

      List<JButton> buttons = new ArrayList<>();
      buttons.add(TableView.createDeleteActionButton(
      (e) -> { this.removeProduct(pr); }
      ));

      model.addRow(new Object[] {
        icon,
        pr.m_name,
        "€ " + pr.m_price,
        String.valueOf(quantity),
        buttons,
      });
    }
  }

  public void setDoubleClickOperation(Consumer<Integer> op) {
    doubleClickOperation = op;
  }

  public void setProduct(ProductRecord pr, int quantity) {
    if (!this.isVisible()) this.setVisible(true);
    productMap.put(pr, quantity);
    updateView();
  }

  public void addProduct(ProductRecord pr, int quantity) {
    int current_quantity = productMap.containsKey(pr) ? productMap.get(pr) : 0;
    setProduct(pr, current_quantity + quantity);
  }

  public void removeProduct(ProductRecord pr) {
    productMap.remove(pr);
    if (productMap.isEmpty()) this.setVisible(false);
    updateView();
  }

  public void addTableModelListener(TableModelListener listener) {
    model.addTableModelListener(listener);
  }

  public List<Entry<ProductRecord, Integer>> getItems() {
    return new ArrayList<>(productMap.entrySet());
  }

  private Consumer<Integer> doubleClickOperation;
  public List<ProductRecord> productList = new ArrayList<>();
  private HashMap<ProductRecord, Integer> productMap = new HashMap<>();
  private MTable            table;
  public DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
