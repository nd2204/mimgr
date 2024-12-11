package dev.mimgr.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers.MultiClickHandler;
import dev.mimgr.theme.ColorTheme;

public class OrderNotificationTableView extends JPanel implements TableModelListener {
  public OrderNotificationTableView() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.notifications = new ArrayList<>();

    // Initialization
    this.table = new MTable();
    this.tv = new TableView();
    this.tableScrollPane = new MScrollPane();
    this.model = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 2;
      }
    };

    this.doubleClickOperation = (row) -> {
      notifications.get(row).action.run();
    };

    this.table.setFillsViewportHeight(false);
    this.table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    this.table.setRowHeight(70);
    this.table.setTableHeader(null);
    this.table.setModel(model);

    tv.add_column(table, "", TableView.setup_image_column());
    tv.add_column(table, "", TableView.setup_default_column());
    tv.add_column(table, "", TableView.setup_action_button_column(50, 50, Integer.MAX_VALUE, SwingConstants.RIGHT));
    tv.load_column(table, model);

    model.addTableModelListener(this);

    this.setLayout(new BorderLayout());
    this.add(table);
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

  public void updateView() {
    model.setRowCount(0);

    for (Notification n : notifications) {
      Icon icon = IconManager.getIcon("dot.png", 16, 16, colors.m_blue);
      List<JButton> buttons = new ArrayList<>();
      MButton button = new MButton();
      button.setBackground(colors.m_bg_1);
      button.setForeground(colors.m_fg_0);
      button.setBorderColor(colors.m_bg_2);
      button.setText(n.buttonText);
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          n.action.run();
        }
      });
      buttons.add(button);

      model.addRow(new Object[] {
        icon,
        n.message,
        buttons,
      });
    }
  }

  public void refresh() {
    updateView();
  }

  public void addNotification(String s, String btn, Runnable b) {
    notifications.add(new Notification(s, btn, b));
    updateView();
  }

  @Override
  public void tableChanged(TableModelEvent v) {
  }

  private class Notification {
    public Runnable action;
    public String buttonText;
    public String message;

    public Notification(String s, String btntxt, Runnable buttonAction) {
      this.buttonText = btntxt;
      this.message = s;
      this.action = buttonAction;
    }
  }

  private ArrayList<Notification> notifications;
  private Consumer<Integer> doubleClickOperation;
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
