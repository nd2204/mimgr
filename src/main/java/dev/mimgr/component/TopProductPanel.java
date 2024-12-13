package dev.mimgr.component;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.FontManager;
import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class TopProductPanel extends RoundedPanel {
  public TopProductPanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new GridBagLayout());
    this.setBackground(colors.m_bg_0);
    this.setBorderColor(colors.m_bg_1);
    this.setBorderWidth(1);
    this.setBorderRadius(15);

    this.table = new MTable();
    this.tv = new TableView();
    this.tableScrollPane = new MScrollPane();
    this.model = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    this.table.setFillsViewportHeight(true);
    this.table.setRowHeight(80);
    this.table.setAutoscrolls(true);
    this.table.setup_scrollbar(tableScrollPane);
    this.table.setTableHeader(null);
    this.table.setModel(model);

    this.tableScrollPane.add(table);
    this.tableScrollPane.setBackground(colors.m_bg_0);
    this.tableScrollPane.setOpaque(true);
    this.tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

    tv.add_column(table, "Image", TableView.setup_image_column());
    tv.add_column(table, "Name", TableView.setup_custom_column(150, 150, Integer.MAX_VALUE));
    tv.add_column(table, "Total Sales", TableView.setup_custom_column(80, 80, 120));
    tv.load_column(table, model);

    this.setLayout(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 0;
    gc.gridy = 0;
    gc.weightx = 1.0;
    gc.fill = GridBagConstraints.HORIZONTAL;
    gc.insets = new Insets(15, 15, 5, 15);

    JLabel lblTopProducts = new JLabel("Top Products");
    lblTopProducts.setFont(nunito_bold_18);
    lblTopProducts.setForeground(colors.m_fg_0);
    this.add(lblTopProducts, gc);

    gc.gridy++;

    gc.fill = GridBagConstraints.BOTH;
    gc.gridwidth = GridBagConstraints.REMAINDER;
    gc.weightx = gc.weighty = 1.0;
    gc.insets = new Insets(5, 5, 5, 5);

    this.add(tableScrollPane, gc);

    tableScrollPane.setViewportView(table);
    this.setVisible(true);

    defaultQueryInvoker = () -> ProductRecord.selectTopProductByQuantitySold();

    updateView();
  }

  public void updateView() {
    model.setRowCount(0);
    productList = new ArrayList<>();
    ResultSet queryResult = defaultQueryInvoker.get();
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
          icon,
          pr.m_name,
          "€ " + pr.m_price * queryResult.getInt("total_quantity_sold"),
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void refresh() {
    updateView();
  }

  private Font nunito_bold_18 = FontManager.getFont("NunitoBold", 18f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 20f);
  private Font nunito_bold_22 = FontManager.getFont("NunitoBold", 22f);
  private Font nunito_bold_26 = FontManager.getFont("NunitoBold", 26f);

  private Supplier<ResultSet> defaultQueryInvoker;
  private ArrayList<ProductRecord> productList = new ArrayList<>();

  private MTable            table;
  private TableView         tv;
  private DefaultTableModel model;
  private JScrollPane       tableScrollPane;
  private ColorScheme       colors;
}
