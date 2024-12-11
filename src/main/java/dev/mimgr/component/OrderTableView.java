package dev.mimgr.component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;

import dev.mimgr.db.OrderRecord;
import dev.mimgr.db.DBQueries;
import dev.mimgr.PanelManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.utils.Helpers;

public class OrderTableView extends JPanel implements TableModelListener {
  public OrderTableView() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.defaultQueryInvoker = () -> OrderRecord.selectAllNewest();

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
        return column == 0 || column == 7;
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
          deleteOrders(list);
          PanelManager.createPopup(new NotificationPopup("Deleted " + selectedImagesCount + " order(s)", NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
        }
      } else {
        PanelManager.createPopup(new NotificationPopup("Nothing to delete", NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
      }
    };


    this.table.setFillsViewportHeight(true);
    this.table.setRowHeight(40);
    this.table.setAutoscrolls(true);
    this.table.setup_scrollbar(tableScrollPane);
    this.table.setModel(model);

    this.tableScrollPane.add(table);
    this.tableScrollPane.setBackground(colors.m_bg_0);
    this.tableScrollPane.setOpaque(true);
    this.tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

    tv.add_column(table, "", TableView.setup_checkbox_column());
    tv.add_column(table, "Order", TableView.setup_custom_column(60, 60, 80));
    tv.add_column(table, "Date", TableView.setup_custom_column(180, 180, 200));
    tv.add_column(table, "Total", TableView.setup_custom_column(100, 100, 200));
    tv.add_column(table, "Order status", TableView.setup_status_column());
    tv.add_column(table, "Payment status", TableView.setup_status_column());
    tv.add_column(table, "Items", TableView.setup_default_column());
    tv.add_column(table, "Actions", TableView.setup_action_button_column());
    tv.load_column(table, model);

    model.addTableModelListener(this);
    updateView(defaultQueryInvoker);

    this.setLayout(new BorderLayout());
    this.add(tableScrollPane);
    tableScrollPane.setViewportView(table);
    this.setVisible(true);
  }

  public void updateView(Supplier<ResultSet> queryInvoker) {
    model.setRowCount(0);
    selectedOrders.clear();
    orderList = new ArrayList<>();
    ResultSet queryResult = queryInvoker.get();
    currentQueryInvoker = queryInvoker;

    try {
      while (queryResult.next()) {
        OrderRecord or = new OrderRecord(queryResult);
        orderList.add(or);
        SimpleEntry<Integer, Double> pair = getOrderItemCountAndTotal(or.m_id);
        List<JButton> buttons = new ArrayList<>();
        buttons.add(TableView.createDeleteActionButton(
          (e) -> {deleteOperation.accept(List.of(or));}
        ));
        model.addRow(new Object[] {
          Boolean.FALSE,
          "#" + or.m_id,
          Helpers.formatRelativeDatetime(or.m_date),
          Helpers.CURRENCY_SYMBOL + " " + pair.getValue(),
          new SimpleEntry<String, Color>(or.m_order_status, getStatusColor(or.m_order_status)),
          new SimpleEntry<String, Color>(or.m_payment_status, getStatusColor(or.m_payment_status)),
          getItemPlural(pair.getKey()),
          buttons
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private String getItemPlural(int value) {
    String itemStr = value + " item";
    return (value > 1) ? itemStr + "s" : itemStr;
  }

  public void deleteSelected() {
    deleteOrders(new ArrayList<>(selectedOrders.values()));
    selectedOrders.clear();
  }

  private void deleteOrders(Iterable<OrderRecord> orders) {
    OrderRecord.delete(orders);
    refresh();
  }

  public List<OrderRecord> getSelected() {
    return new ArrayList<>(this.selectedOrders.values());
  }

  public synchronized void refresh() {
    updateView(currentQueryInvoker);
  }

  public void reset() {
    updateView(defaultQueryInvoker);
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
            selectedOrders.put(row, orderList.get(row));
          } else {
            selectedOrders.remove(row);
          }
        }
      }
      break;
    }
  }

  public SimpleEntry<Integer, Double> getOrderItemCountAndTotal(int orderId) {
    ResultSet rs = DBQueries.select("""
      SELECT
        COUNT(order_item_id) AS total_item,
        SUM(total_price) AS total_price
      FROM order_items
      WHERE order_id = ?
      GROUP BY order_id;
      """, orderId);
    try {
      if (rs.next()) {
        return new SimpleEntry<>(rs.getInt("total_item"), rs.getDouble("total_price"));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return new SimpleEntry<>(0, 0.0);
  }

  public void setDefaultQueryInvoker(Supplier<ResultSet> invoker) {
    this.defaultQueryInvoker = invoker;
  }

  public Color getStatusColor(String status) {
    Map<String, Color> colorDict = new HashMap<>();
    colorDict.put("Open",     colors.m_green);

    colorDict.put("Archived", colors.m_yellow);
    colorDict.put("Pending",  colors.m_yellow);

    colorDict.put("Paid",     colors.m_grey_0);
    colorDict.put("Closed",   colors.m_grey_0);

    colorDict.put("Unpaid",   colors.m_red);
    return colorDict.get(status);
  }

  // public void setButtonRefreshOnClick(MButton btn) {
  //   btn.addActionListener((actionEvent) -> {
  //     new Thread(() -> {
  //       SwingUtilities.invokeLater(() -> {
  //         refresh();
  //       });
  //     }).start();
  //   });
  // }

  private Consumer<List<OrderRecord>> deleteOperation;
  private Supplier<ResultSet> currentQueryInvoker;
  private Supplier<ResultSet> defaultQueryInvoker;
  private HashMap<Integer, OrderRecord> selectedOrders = new HashMap<>();
  private ArrayList<OrderRecord> orderList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
