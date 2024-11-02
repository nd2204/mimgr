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
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.DropPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MCheckBoxCellEditor;
import dev.mimgr.custom.MCheckBoxCellRenderer;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MImageCellRenderer;
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferListener;
import dev.mimgr.utils.ResourceManager;

/**
 *
 * @author dn200
 */
public class FormMedia extends JPanel implements ActionListener, MTransferListener {
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

      String[] items = { 
        "Bulk actions",
        "Delete Permanently"
      };

      this.bulkAction = new MComboBox<>(items, colors);
      this.bulkAction.setBackground(colors.m_bg_0);
      this.bulkAction.setForeground(colors.m_grey_0);

      cc.gridx = 0;
      cc.gridy = 0;
      cc.weightx = 0.0;
      cc.ipadx = 40;
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
  }

  private void setup_table() {
    table = new MTable(colors);
    table.setFillsViewportHeight(true);
    table.setRowHeight(emptyImageIcon.getIconHeight() + 40);
    table.setAutoscrolls(true);
    this.emptyImageIcon = IconManager.getIcon("image.png", colors.m_grey_0);
    // table.setAutoResizeMode(MTable.AUTO_RESIZE_OFF);

    tableScrollPane = new JScrollPane(table);
    table.setup_scrollbar(tableScrollPane);

    DefaultTableModel model = new DefaultTableModel() {
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

    model.addColumn("");
    model.addColumn("");
    model.addColumn("Image name");
    model.addColumn("Filename");
    model.addColumn("Date");
    model.addColumn("Author");
    model.addColumn("Caption");

    for (int i = 0; i < 20; ++i) {
      model.addRow(new Object[]{Boolean.FALSE, emptyImageIcon, "Smith", "Snowboarding", 3});
    }

    TableColumnModel tcm = table.getColumnModel();
    // Setup checkbox column
    TableColumn column = tcm.getColumn(0);
    MCheckBoxCellRenderer checkBoxRenderer = new MCheckBoxCellRenderer(colors);
    checkBoxRenderer.setCheckColor(colors.m_green);
    checkBoxRenderer.setBoxColor(colors.m_bg_3);
    checkBoxRenderer.setBackground(colors.m_bg_0);
    column.setCellRenderer(checkBoxRenderer);
    column.setPreferredWidth(30);
    column.setMinWidth(30);

    MCheckBoxCellEditor.CustomCheckBox editorCheckbox = new MCheckBoxCellEditor.CustomCheckBox(checkBoxRenderer, colors);
    MCheckBoxCellEditor checkBoxCellEditor = new MCheckBoxCellEditor(editorCheckbox, colors);
    column.setCellEditor(checkBoxCellEditor);

    // Setup image column
    column = tcm.getColumn(1);
    column.setMinWidth(80);
    column.setPreferredWidth(80);
    column.setCellRenderer(new MImageCellRenderer(colors));

    for(int i = 2; i < table.getColumnCount() - 1; ++i) {
      column = tcm.getColumn(i);
      column.setPreferredWidth(150);
    }
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
          if (isValidImageFile(file)) {
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
          Path newFilePath = moveStagedFileToUploadDir(file);
          System.out.println(rm.getProjectPath().relativize(newFilePath));
        }
      }
      // Clean temp file from download
      rm.cleanTempFiles();
      // Clear the data
      this.droppedItemsPanel.clearData();
    }
    return;
  }

  public static boolean isImageUrl(String urlString) {
    try {
      URI uri = new URI(urlString);
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
      .uri(uri)
      .method("HEAD", HttpRequest.BodyPublishers.noBody())
      .build();
      HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
      String contentType = response.headers().firstValue("Content-Type").orElse("");

      return contentType != null && contentType.startsWith("image/");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public void onStringImported(String string) {
    System.out.println("Dropped String data");
    System.out.println(string);
    System.out.println("is image url: " + isImageUrl(string));
    if (isImageUrl(string)) {
      // System.out.println(rm.getUploadPath());
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
      System.out.println("valid: " + isValidImageFile(file) + " " + file);
      droppedItemsPanel.addData(file);
    }
    return;
  }

  private boolean isValidImageFile(File file) {
    String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
    String fileName = file.getName().toLowerCase();

    // Check file extension
    for (String extension : validExtensions) {
      if (fileName.endsWith(extension)) {
        return true; // Valid extension found
      }
    }

    // Optionally: Check MIME type if extension check fails
    try {
      // Check if the file can be read as an image
      return ImageIO.read(file) != null;
    } catch (Exception e) {
      return false; // If an exception occurs, treat as invalid
    }
  }

  private Path moveStagedFileToUploadDir(File file) {
    Path destinationPath = null;
    try {
      destinationPath = ResourceManager.getUniquePath(rm.getUploadPath().resolve(file.getName()));
      Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
      System.out.println("Copied to: " + destinationPath);
    } catch (IOException e) {
      System.out.println("Error occurred while copying file.");
      e.printStackTrace();
    }
    return destinationPath;
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
}
