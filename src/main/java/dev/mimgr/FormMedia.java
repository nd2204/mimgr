package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.DropPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.DBQueries;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferListener;
import dev.mimgr.utils.ResourceManager;

/**
 *
 * @author dn200
 */
public class FormMedia extends JPanel implements ActionListener, MTransferListener, TableModelListener {
  public FormMedia(ColorScheme colors) {
    this.colors = colors;
    // =======================================================
    // Setup Layout
    // =======================================================

    this.setLayout(new GridBagLayout());

    int padding = 25;

    // Top
    c.insets = new Insets(25, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(topLabel, c);

    c.gridx = 1;
    c.gridy = 0;
    c.weightx = 1.0;
    c.insets = new Insets(20, 5, 20, 5);
    this.add(this.addMediaButton, c);

    dropPanel = new JPanel();
    dropPanel.setOpaque(false);
    dropPanel.setVisible(false);
    dropPanel.setLayout(new GridBagLayout());
    droppedItemsPanel = new DropContainerPanel(this.colors);
    droppedItemsPanel.addActionListener(this);
    {
      MediaDropPanel dropArea = new MediaDropPanel();
      dropArea.addTransferListener(this);
      GridBagConstraints gc = new GridBagConstraints();
      gc.gridx = 0;
      gc.gridy = 0;
      gc.weightx = 1.0;
      gc.weighty = 1.0;
      gc.fill = GridBagConstraints.BOTH;
      dropPanel.add(dropArea, gc);

      gc.gridx = 0;
      gc.gridy = 1;
      gc.fill = GridBagConstraints.BOTH;
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.insets = new Insets(0, 0, 0, 0);
      dropPanel.add(droppedItemsPanel, gc);
    }

    c.insets = new Insets(0, 25, 15, 25);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1.0;
    c.ipady = 245;
    c.weighty = 0.0;
    c.gridwidth = 2;
    c.fill = GridBagConstraints.BOTH;
    this.add(dropPanel, c);

    // Content
  {
      GridBagConstraints cc = new GridBagConstraints();
      contentContainer.setLayout(new GridBagLayout());
      contentContainer.setBackground(colors.m_bg_0);

      String[] cbBulkAction = { 
        "Bulk actions",
        "Delete Permanently"
      };

      this.bulkAction = new MComboBox<>(cbBulkAction, colors);
      this.bulkAction.setBackground(colors.m_bg_0);
      this.bulkAction.setForeground(colors.m_grey_0);

      cc.gridx = 0;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.ipadx = 50;
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.BOTH;
      cc.insets = new Insets(15, 15, 0, 5);
      contentContainer.add(bulkAction, cc);

      cc.gridx = 1;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.insets = new Insets(15, 0, 0, 15);
      cc.fill = GridBagConstraints.VERTICAL;
      contentContainer.add(applyBulkAction, cc);

      cc.gridx = 2;
      cc.gridy = 0;
      cc.weightx = 1.0;
      cc.fill = GridBagConstraints.VERTICAL;
      cc.anchor = GridBagConstraints.FIRST_LINE_END;
      contentContainer.add(filterTextField, cc);

      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.gridx = 0;
      cc.gridy = 1;
      cc.weightx = 1.0;
      cc.weighty = 1.0;
      cc.gridwidth = 4;
      cc.insets = new Insets(15, 0, 15, 0);
      cc.fill = GridBagConstraints.BOTH;
      setup_table();
      contentContainer.add(tableScrollPane, cc);
    }

    c.insets = new Insets(0, padding, 20, padding);
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = 4;
    this.add(contentContainer, c);

    // Post Content

    // =======================================================
    // Setup Appearance
    // =======================================================
    this.setBackground(colors.m_bg_dim);

    this.topLabel.setFont(nunito_bold_20);
    this.topLabel.setForeground(colors.m_fg_0);

    this.addMediaButton.setBackground(colors.m_bg_0);
    this.addMediaButton.setBorderColor(colors.m_bg_4);
    this.addMediaButton.setHoverBackgroundColor(colors.m_bg_2);
    this.addMediaButton.setHoverBorderColor(colors.m_bg_3);
    this.addMediaButton.setClickBackgroundColor(colors.m_bg_1);
    this.addMediaButton.setFont(nunito_extrabold_14);
    this.addMediaButton.setIcon(IconManager.getIcon("upload.png", 16, 16, colors.m_grey_0));
    this.addMediaButton.setForeground(colors.m_grey_0);
    this.addMediaButton.setText(" " + this.addMediaButton.getText());
    this.addMediaButton.addActionListener(this);

    this.filterTextField.setIcon(IconManager.getIcon("search.png", 20, 20, colors.m_grey_0), MTextField.ICON_PREFIX);
    this.filterTextField.setBackground(colors.m_bg_dim);
    this.filterTextField.setForeground(colors.m_fg_0);
    this.filterTextField.setPlaceholder("Filter Images");
    this.filterTextField.setPlaceholderForeground(colors.m_grey_0);
    this.filterTextField.setBorderWidth(1);
    this.filterTextField.setBorderColor(colors.m_bg_5);
    this.filterTextField.setInputForeground(colors.m_fg_0);
    this.filterTextField.setFont(nunito_bold_14);

    this.tableScrollPane.setBackground(colors.m_bg_0);
    this.tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

    this.selectFilesButton.setBackground(colors.m_bg_dim);
    this.selectFilesButton.setBorderColor(colors.m_bg_3);
    this.selectFilesButton.setHoverBorderColor(colors.m_accent);
    this.selectFilesButton.setClickBackgroundColor(colors.m_bg_1);
    this.selectFilesButton.setForeground(colors.m_accent);
    this.selectFilesButton.setHorizontalAlignment(SwingConstants.CENTER);
    this.selectFilesButton.addActionListener(this);

    this.applyBulkAction.setForeground(colors.m_grey_2);
    this.applyBulkAction.setBackground(colors.m_bg_0);
    this.applyBulkAction.setBorderColor(colors.m_bg_5);
    this.applyBulkAction.setHoverBackgroundColor(colors.m_bg_1);
    this.applyBulkAction.setClickBackgroundColor(colors.m_bg_dim);
    this.applyBulkAction.setHoverForegroundColor(colors.m_blue);
    this.applyBulkAction.setHoverBorderColor(colors.m_blue);
    this.applyBulkAction.setBorderRadius(0);
    this.applyBulkAction.addActionListener(this);;
  }

  private void setup_table() {
    this.emptyImageIcon = IconManager.getIcon("image.png", colors.m_grey_0);
    table = new MTable(colors);
    table.setFillsViewportHeight(true);
    table.setRowHeight(emptyImageIcon.getIconHeight() + 40);
    table.setAutoscrolls(true);
    // table.setAutoResizeMode(MTable.AUTO_RESIZE_OFF);

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
    get_all_images(model);
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.addMediaButton) {
      if (dropPanel.isVisible()) {
        dropPanel.setVisible(false);
      } else {
        dropPanel.setVisible(true);
      }
    }

    if (e.getSource() == this.selectFilesButton) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
      fileChooser.setDialogTitle("Select Files");
      fileChooser.setMultiSelectionEnabled(true);
      fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

      // Show the file chooser dialog and wait for user action
      int userSelection = fileChooser.showOpenDialog(null);

      // Handle the user selection
      if (userSelection == JFileChooser.APPROVE_OPTION) {
        // Get the selected file
        File[] selectedFile = fileChooser.getSelectedFiles();
        for (File file : selectedFile) {
          if (rm.isValidImageFile(file)) {
            droppedItemsPanel.addData(file);
          }
        }
      } else {
        System.out.println("No file selected.");
      }
    }

    if (e.getSource() == this.droppedItemsPanel.getConfirmButton()) {
      // Do something with the data
      for (Object obj : this.droppedItemsPanel.getAllData()) {
        if (obj instanceof File file) {
          Path newFilePath = rm.moveStagedFileToUploadDir(file);
          System.out.println(rm.getProjectPath().relativize(newFilePath));
          DBQueries.insert_image(String.valueOf(rm.getProjectPath().relativize(newFilePath)).replace("\\", "/"), file.getName(), "");
          get_all_images(model);
          dropPanel.setVisible(false);
        }
      }
      // Clean temp file from download
      rm.cleanTempFiles();
      // Clear the data
      this.droppedItemsPanel.clearData();
    }

    if (e.getSource() == this.applyBulkAction) {
      System.out.println(((String) this.bulkAction.getSelectedItem()));
      // if (((String) this.bulkAction.getSelectedItem()).equals("Edit")) {

      //   FormEditProduct frame = new FormEditProduct(selectedImages.values());
      //   frame.setVisible(true);
      // }
      if (((String) this.bulkAction.getSelectedItem()).equals("Delete Permanently")) {
        if (!selectedImages.isEmpty()) {
          delete_image(model, selectedImages.values());
          selectedImages.clear();
        }
      }
    }
    return;
  }

  @Override
  public void onStringImported(String string) {
    if (ResourceManager.isImageUrl(string)) {
      Path fp = rm.downloadTempFile(string);
      droppedItemsPanel.addData(fp.toFile());
    }
    return;
  }

  @Override
  public void onImageImported(Image image) {
    System.out.println("Dropped Image data");
    System.out.println(image);
    return;
  }

  @Override
  public void onFileListImported(List<File> files) {
    System.out.println("Dropped File list data");
    for (File file : files) {
      System.out.println("valid: " + rm.isValidImageFile(file) + " " + file);
      droppedItemsPanel.addData(file);
    }
    return;
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

  private void delete_image(DefaultTableModel model, Iterable<ImageRecord> prList) {
    for (ImageRecord i : prList) {
      DBQueries.delete_image(i.m_id);
    }
    model.setRowCount(0);
    if (!imageList.isEmpty()) {
      imageList = new ArrayList<>();
    }
    get_all_images(model);
  }

  private class MediaDropPanel extends DropPanel {
    public MediaDropPanel() {
      super(colors);
      this.setLayout(new GridBagLayout());
      this.setPreferredSize(new Dimension(100, 300));
      this.setBackground(colors.m_bg_0);

      GridBagConstraints gc = new GridBagConstraints();

      JLabel label = new JLabel("Drop files to upload");
      JLabel label1 = new JLabel("or");

      label.setFont(nunito_bold_20);
      label.setForeground(colors.m_grey_0);
      label.setHorizontalAlignment(SwingConstants.CENTER);
      label1.setFont(nunito_bold_16);
      label1.setForeground(colors.m_grey_0);
      label1.setHorizontalAlignment(SwingConstants.CENTER);

      gc.gridx = 0;
      gc.gridy = 0;
      gc.fill = GridBagConstraints.BOTH;
      gc.anchor = GridBagConstraints.CENTER;
      this.add(label, gc);

      gc.insets = new Insets(15, 0, 15, 0);
      gc.gridy = 1;
      this.add(label1, gc);

      gc.insets = new Insets(0, 0, 0, 0);
      gc.fill = GridBagConstraints.NONE;
      gc.gridy = 2;
      this.add(selectFilesButton, gc);
    }
  }

  private void get_all_images(DefaultTableModel model) {
    updateTable(DBQueries.select_all_images(), model);
  }

  private void updateTable(ResultSet queryResult, DefaultTableModel model) {
    model.setRowCount(0);
    if (!imageList.isEmpty()) {
      imageList = new ArrayList<>();
    }
    try {
      while (queryResult.next()) {
        ImageRecord pr = new ImageRecord(queryResult);
        imageList.add(pr);
        model.addRow(new Object[] {
            Boolean.FALSE,
            this.emptyImageIcon = IconManager.loadIcon(Paths.get(pr.m_url).toAbsolutePath().toFile()),
            pr.m_name,
            pr.m_url.substring(pr.m_url.lastIndexOf("/") + 1),
            pr.m_created_at,
            pr.m_author,
            pr.m_caption
        });
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private TableView tv = new TableView();
  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);


  private ColorScheme colors;
  private RoundedPanel contentContainer = new RoundedPanel();
  private GridBagConstraints c = new GridBagConstraints();
  private MTextField filterTextField = new MTextField(30);
  private MTable table;
  private JScrollPane tableScrollPane;
  private JLabel topLabel = new JLabel("Media Library");
  private MButton addMediaButton = new MButton("Add New Media File");
  private MButton selectFilesButton = new MButton("Select Files");
  private JPanel dropPanel = null;
  private Icon emptyImageIcon;
  private MComboBox<String> bulkAction;
  private MButton applyBulkAction = new MButton("Apply");
  private DropContainerPanel droppedItemsPanel;
  private ResourceManager rm = ResourceManager.getInstance();
  DefaultTableModel model;
  private ArrayList<ImageRecord> imageList = new ArrayList<>();
  private HashMap<Integer, ImageRecord> selectedImages = new HashMap<>();
}
