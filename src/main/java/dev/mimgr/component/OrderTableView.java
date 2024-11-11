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
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;

import dev.mimgr.db.OrderRecord;
import dev.mimgr.db.DBQueries;
import dev.mimgr.TableView;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.theme.builtin.ColorScheme;

public class OrderTableView extends JPanel implements TableModelListener {
  public OrderTableView(ColorScheme colors) {
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
    this.table.setRowHeight(40);
    this.table.setAutoscrolls(true);
    this.table.setup_scrollbar(tableScrollPane);
    this.table.setModel(model);

    this.tableScrollPane.add(table);
    this.tableScrollPane.setBackground(colors.m_bg_0);
    this.tableScrollPane.setOpaque(true);
    this.tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

    tv.add_column(table, "", TableView.setup_checkbox_column(colors));
    tv.add_column(table, "Order", TableView.setup_custom_column(60, 60, 80));
    tv.add_column(table, "Date", TableView.setup_custom_column(180, 180, 180));
    tv.add_column(table, "Total", TableView.setup_custom_column(80, 80, 120));
    tv.add_column(table, "Order status", TableView.setup_status_column(colors));
    tv.add_column(table, "Payment status", TableView.setup_status_column(colors));
    tv.add_column(table, "Items", TableView.setup_default_column());
    tv.load_column(table, model);

    model.addTableModelListener(this);
    updateView(() -> OrderRecord.selectAll());

    this.setLayout(new BorderLayout());
    this.add(tableScrollPane);
    tableScrollPane.setViewportView(table);
    this.setVisible(true);

//     table.addMouseListener(new MouseAdapter() {
//       @Override
//       public void mouseClicked(MouseEvent e) {
//         // Check if it's a double-click
//         if (e.getClickCount() == 2) {
//           int row = table.getSelectedRow();
//           if (row != -1) {
//             FormEditProduct frame = new FormEditProduct(orderList.get(row));
//             List<UploadPanel> uploadPanels = frame.getUploadPanels();
//             frame.setVisible(true);
//             for (UploadPanel panel : uploadPanels) {
//               setButtonRefreshOnClick(panel.getDeleteComponent());
//               setButtonRefreshOnClick(panel.getSubmitComponent());
//             }
//           }
//         }
//       }
//     });
  }

  public void updateView(Supplier<ResultSet> queryInvoker) {
    model.setRowCount(0);
    selectedOrders.clear();
    orderList = new ArrayList<>();
    ResultSet queryResult = queryInvoker.get();
    currentQueryInvoker = queryInvoker;

    try {
      OrderRecord or;
      while (queryResult.next()) {
        or = new OrderRecord(queryResult);
        orderList.add(or);
        ZonedDateTime zonedDateTime = or.m_date.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        SimpleEntry<Integer, Double> pair = getOrderItemCountAndTotal(or.m_id);
        model.addRow(new Object[] {
          Boolean.FALSE,
          "#" + or.m_id,
          zonedDateTime.format(formatter),
          "€ " + pair.getValue(),
          new SimpleEntry<String, Color>(or.m_order_status, getStatusColor(or.m_order_status)),
          new SimpleEntry<String, Color>(or.m_payment_status, getStatusColor(or.m_payment_status)),
          pair.getKey()
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteSelected() {
    for (OrderRecord pr : selectedOrders.values()) {
      deleteOrder(pr);
    }
    selectedOrders.clear();
    refresh();
  }

  public List<OrderRecord> getSelected() {
    return new ArrayList<>(this.selectedOrders.values());
  }

  public void refresh() {
    updateView(currentQueryInvoker);
  }

  public void reset() {
    updateView(() -> OrderRecord.selectAll());
  }

  private void deleteOrder(OrderRecord pr) {
    OrderRecord.delete(pr);
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

  private Supplier<ResultSet> currentQueryInvoker;
  private HashMap<Integer, OrderRecord> selectedOrders = new HashMap<>();
  private ArrayList<OrderRecord> orderList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
