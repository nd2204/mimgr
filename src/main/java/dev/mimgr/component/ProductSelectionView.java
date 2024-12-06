package dev.mimgr.component;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers.MultiClickHandler;
import dev.mimgr.theme.ColorTheme;

public class ProductSelectionView extends JPanel implements TableModelListener {
  public ProductSelectionView() {
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

    tv.add_column(table, "", TableView.setup_image_column(colors));
    tv.add_column(table, "Name", TableView.setup_custom_column(150, 150, Integer.MAX_VALUE));
    tv.add_column(table, "Price", TableView.setup_custom_column(80, 80, 120));
    tv.add_column(table, "Stock", TableView.setup_custom_column(50, 50, 100));
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
        buttons.add(TableView.createAddActionButton(
          (e) -> {System.out.println("Test");}
        ));

        productList.add(pr);
        model.addRow(new Object[] {
          icon,
          pr.m_name,
          "€ " + pr.m_price,
          pr.m_stock_quantity,
          buttons,
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void refresh() {
    updateView(currentQueryInvoker);
  }

  public void reset() {
    updateView(() -> ProductRecord.selectAll());
  }

  @Override
  public void tableChanged(TableModelEvent e) {
  }

  public void setDoubleClickOperation() {

  }

  private Consumer<Integer> doubleClickOperation;
  private Supplier<ResultSet> currentQueryInvoker;
  private ArrayList<ProductRecord> productList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
