package dev.mimgr.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.DBConnection;
import dev.mimgr.db.DBQueries;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaTableView extends JPanel implements TableModelListener {
  public MediaTableView(ColorScheme colors) {
    this.colors = colors;
    this.emptyImageIcon = IconManager.getIcon("image.png", colors.m_grey_0);
    this.tv = new TableView();
    this.table = new MTable(colors);
    this.table.setFillsViewportHeight(true);
    this.table.setRowHeight(this.emptyImageIcon.getIconHeight() + 40);
    this.table.setAutoscrolls(true);
    // table.setAutoResizeMode(MTable.AUTO_RESIZE_OFF);

    this.tableScrollPane = new MScrollPane(colors);
    this.tableScrollPane.setBackground(colors.m_bg_0);
    this.tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

    tableScrollPane = new JScrollPane(table);
    table.setup_scrollbar(tableScrollPane);

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
    tv.add_column(table, "", TableView.setup_image_column(colors));
    tv.add_column(table, "Image name", TableView.setup_default_column());
    tv.add_column(table, "Filename", TableView.setup_default_column());
    tv.add_column(table, "Date", TableView.setup_default_column());
    tv.add_column(table, "Author", TableView.setup_default_column());
    tv.add_column(table, "Caption", TableView.setup_default_column());
    tv.load_column(table, model);

    model.addTableModelListener(this);
    getAllImages();
  }

  public void getAllImages() {
    updateTable(DBQueries.select_all_images(), this.model);
  }

  public void updateTable(ResultSet queryResult, DefaultTableModel model) {
    model.setRowCount(0);
    if (!imageList.isEmpty()) {
      imageList = new ArrayList<>();
    }
    try {
      while (queryResult.next()) {
        ImageRecord ir = new ImageRecord(queryResult);
        imageList.add(ir);
        model.addRow(new Object[] {
            Boolean.FALSE,
            this.emptyImageIcon = IconManager.loadIcon(Paths.get(ir.m_url).toAbsolutePath().toFile()),
            ir.m_name,
            ir.m_url.substring(ir.m_url.lastIndexOf("/") + 1),
            ir.m_created_at,
            ImageRecord.getImageAuthor(DBConnection.get_instance().get_connection(), ir),
            ir.m_caption
        });
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void delete_image(DefaultTableModel model, Iterable<ImageRecord> prList) {
    for (ImageRecord i : prList) {
      DBQueries.delete_image(i.m_id);
      try {
        Files.deleteIfExists(Path.of(i.m_url));
      } catch (IOException e) {}
    }
    if (!imageList.isEmpty()) {
      imageList = new ArrayList<>();
    }
    getAllImages();
  }

  private ArrayList<ImageRecord> imageList = new ArrayList<>();
  private Icon              emptyImageIcon;
  private JScrollPane       tableScrollPane;
  private MTable            table;
  private DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
