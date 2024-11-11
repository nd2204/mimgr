package dev.mimgr.component;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.IconManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaTableView extends JPanel implements TableModelListener, IMediaView {
  public MediaTableView(ColorScheme colors) {
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

    this.tv.add_column(table, "", TableView.setup_checkbox_column(colors));
    this.tv.add_column(table, "", TableView.setup_image_column(colors));
    this.tv.add_column(table, "Image name", TableView.setup_default_column());
    this.tv.add_column(table, "Filename", TableView.setup_default_column());
    this.tv.add_column(table, "Date", TableView.setup_default_column());
    this.tv.add_column(table, "Author", TableView.setup_default_column());
    this.tv.add_column(table, "Caption", TableView.setup_default_column());
    this.tv.load_column(table, model);

    model.addTableModelListener(this);
    updateView(() -> ImageRecord.selectAll());

    this.setLayout(new BorderLayout());
    this.add(tableScrollPane);
    tableScrollPane.setViewportView(table);
    this.setVisible(true);
  }

  public void updateTable(Supplier<ResultSet> queryInvoker) {
    model.setRowCount(0);
    imageList = new ArrayList<>();
    ResultSet queryResult = queryInvoker.get();
    currentQueryInvoker = queryInvoker;
    try {
      while (queryResult.next()) {
        ImageRecord ir = new ImageRecord(queryResult);
        imageList.add(ir);
        Icon icon = IconManager.loadIcon(Paths.get(ir.m_url).toAbsolutePath().toFile());
        if (icon == null) {
          icon = IconManager.getIcon("image.png", colors.m_grey_0);
        }
        model.addRow(new Object[] {
            Boolean.FALSE,
            icon,
            ir.m_name,
            ir.m_url.substring(ir.m_url.lastIndexOf("/") + 1),
            ir.m_created_at,
            ImageRecord.getImageAuthor(ir),
            ir.m_caption
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteSelectedImages() {
    for (ImageRecord ir : selectedImages.values()) {
      deleteImage(ir);
    }
    selectedImages.clear();
    refresh();
  }

  @Override
  public List<ImageRecord> getSelectedImages() {
    return new ArrayList<>(this.selectedImages.values());
  }

  @Override
  public void refresh() {
    updateTable(currentQueryInvoker);
  }

  @Override
  public void reset() {
    updateTable(() -> ImageRecord.selectAll());
  }

  @Override
  public void updateView(Supplier<ResultSet> queryInvoker) {
    updateTable(queryInvoker);
  }

  @Override
  public Supplier<ResultSet> getCurrentQueryInvoker() {
    return this.currentQueryInvoker;
  }

  public Set<Integer> getSelectedImagesId() {
    return this.selectedImages.keySet();
  }

  private void deleteImage(ImageRecord ir) {
    ImageRecord.delete(ir);
    try {
      Files.deleteIfExists(Path.of(ir.m_url));
    } catch (IOException e) {}
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
              selectedImages.put(row, imageList.get(row));
            } else {
              selectedImages.remove(row);
            }
          }
        }
        break;
    }
  }

  private Supplier<ResultSet> currentQueryInvoker;
  private HashMap<Integer, ImageRecord> selectedImages = new HashMap<>();
  private ArrayList<ImageRecord> imageList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  public DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
