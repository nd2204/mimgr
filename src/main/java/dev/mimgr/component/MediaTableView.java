package dev.mimgr.component;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.EditImagePanel;
import dev.mimgr.FormEditImage;
import dev.mimgr.IconManager;
import dev.mimgr.PanelManager;
import dev.mimgr.TableView;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTable;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaTableView extends JPanel implements TableModelListener, IMediaView {
  public MediaTableView() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();

    // Initialization
    this.table = new MTable();
    this.tv = new TableView();
    this.tableScrollPane = new MScrollPane();
    this.model = new DefaultTableModel() {
      @Override
      public Class<?> getColumnClass(int columnIndex) {
        return (columnIndex == 0 || columnIndex == 7) ? Boolean.class : String.class;
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 0 || column == 7;
      }
    };

    this.editOperation = (list) -> {
      FormEditImage frame = new FormEditImage(list);
      List<EditImagePanel> EditImagePanels = frame.getEditImagePanels();
      frame.setVisible(true);
      for (EditImagePanel panel : EditImagePanels) {
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
          deleteImages(list);
          PanelManager.createPopup(new NotificationPopup("Deleted " + selectedImagesCount + " image(s)", NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
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

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Check if it's a double-click
        if (e.getClickCount() == 2) {
          int row = table.getSelectedRow();
          if (row != -1) {
            doubleClickOperation.accept(row);
          }
        }
      }
    });

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
    this.tv.add_column(table, "Action", TableView.setup_action_button_column());
    this.tv.load_column(table, model);

    model.addTableModelListener(this);
    defaultQueryInvoker = () -> ImageRecord.selectAllNewest();
    updateView(defaultQueryInvoker);

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

        List<JButton> buttons = new ArrayList<>();
        buttons.add(TableView.createEditActionButton(
          (e) -> {editOperation.accept(List.of(ir));}
        ));
        buttons.add(TableView.createDeleteActionButton(
          (e) -> {deleteOperation.accept(List.of(ir));}
        ));

        model.addRow(new Object[] {
          Boolean.FALSE,
          icon,
          ir.m_name,
          ir.m_url.substring(ir.m_url.lastIndexOf("/") + 1),
          ir.m_created_at,
          ImageRecord.getImageAuthor(ir),
          ir.m_caption,
          buttons
        });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteSelectedImages() {
    deleteImages(selectedImages.values());
  }

  private void deleteImages(Iterable<ImageRecord> images) {
    for (ImageRecord ir : images) {
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
    updateTable(this.defaultQueryInvoker);
  }

  @Override
  public void updateView(Supplier<ResultSet> queryInvoker) {
    updateTable(queryInvoker);
  }

  @Override
  public void setButtonRefreshOnClick(MButton btn) {
    btn.addActionListener((actionEvent) -> {
      new Thread(() -> {
        SwingUtilities.invokeLater(() -> {
          refresh();
        });
      }).start();
    });
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

  public void setDefaultQueryInvoker(Supplier<ResultSet> invoker) {
    this.defaultQueryInvoker = invoker;
    refresh();
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
      default:
        selectedImages.clear();
        break;
    }
  }

  private Consumer<Integer> doubleClickOperation;
  private Consumer<Iterable<ImageRecord>> editOperation;
  private Consumer<List<ImageRecord>> deleteOperation;
  private Supplier<ResultSet> currentQueryInvoker;
  private Supplier<ResultSet> defaultQueryInvoker;
  private HashMap<Integer, ImageRecord> selectedImages = new HashMap<>();
  private ArrayList<ImageRecord> imageList = new ArrayList<>();
  private JScrollPane       tableScrollPane;
  private MTable            table;
  public DefaultTableModel model;
  private ColorScheme       colors;
  private TableView         tv;
}
