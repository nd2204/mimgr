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
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.component.FilterOptionPanel;
import dev.mimgr.component.IMediaView;
import dev.mimgr.component.MediaViewSwitcher;
import dev.mimgr.component.NotificationPopup;
import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.DropPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferListener;
import dev.mimgr.utils.ResourceManager;

/**
 *
 * @author dn200
 */
public class FormMedia extends JPanel implements DocumentListener {
  public FormMedia() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    InitializeComponent();
    // =======================================================
    // Setup Layout
    // =======================================================
    this.setLayout(new GridBagLayout());

    int padding = 25;

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;

    // Top
    c.insets = new Insets(25, padding, 25, padding);
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(topLabel, c);
    c.gridx++;

    c.weightx = 1.0;
    c.insets = new Insets(20, 5, 20, 5);
    this.add(this.addMediaButton, c);
    c.gridx++;

    c.gridx = 0;
    c.gridy++;

    dropPanel = new JPanel();
    dropPanel.setOpaque(false);
    dropPanel.setVisible(false);
    dropPanel.setLayout(new GridBagLayout());
    {
      MediaDropPanel dropArea = new MediaDropPanel();
      dropArea.addTransferListener(new MediaTransferListener());
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
          "Edit",
          "Delete Permanently"
      };

      this.bulkAction = new MComboBox<>(cbBulkAction, colors);
      this.bulkAction.setBackground(colors.m_bg_0);
      this.bulkAction.setForeground(colors.m_grey_0);

      // ----------------------------------------------
      cc.gridx = 0;
      cc.gridy = 0;
      // Row 0 ----------------------------------------

      cc.weightx = 1.0;
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.BOTH;
      cc.gridwidth = 4;
      cc.insets = new Insets(5, 5, 0, 5);
      contentContainer.add(filterOptionPanel, cc);

      // End Row 0 ------------------------------------
      cc.gridx = 0;
      cc.gridy = 1;
      cc.gridwidth = 1;
      // Row 1 ----------------------------------------

      cc.ipadx = 50;
      cc.weightx = 0.0;
      cc.insets = new Insets(5, 15, 0, 5);
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.VERTICAL;
      contentContainer.add(layoutSelectorComponent, cc);
      cc.gridx++;

      cc.ipadx = 0;
      cc.weightx = 1.0;
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.BOTH;
      cc.insets = new Insets(5, 5, 0, 0);
      contentContainer.add(filterTextField, cc);
      cc.gridx++;

      cc.weightx = 0.0;
      cc.ipadx = 50;
      cc.insets = new Insets(5, 15, 0, 5);
      cc.fill = GridBagConstraints.VERTICAL;
      cc.anchor = GridBagConstraints.FIRST_LINE_END;
      contentContainer.add(bulkAction, cc);
      cc.gridx++;

      cc.ipadx = 0;
      cc.insets = new Insets(5, 0, 0, 15);
      cc.fill = GridBagConstraints.VERTICAL;
      contentContainer.add(applyBulkAction, cc);

      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.gridx = 0;
      cc.gridy = 3;
      cc.weightx = 1.0;
      cc.weighty = 1.0;
      cc.gridwidth = GridBagConstraints.REMAINDER;
      cc.insets = new Insets(15, 0, 15, 0);
      cc.fill = GridBagConstraints.BOTH;
      contentContainer.add(mediaViewSwitcher.getPanel(), cc);
    }

    c.insets = new Insets(0, padding, 20, padding);
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    this.add(contentContainer, c);
  }

  private void InitializeComponent() {
    // Post Content

    // =======================================================
    // Setup Appearance
    // =======================================================
    this.setBackground(colors.m_bg_dim);
    this.topLabel = new JLabel("Media Library");
    this.topLabel.setFont(nunito_bold_20);
    this.topLabel.setForeground(colors.m_fg_0);

    this.rm = ResourceManager.getInstance();

    Function<String, Consumer<MButton>> buttonSetupGenerator = (text) -> {
      return (button) -> {
        button.setText(text);
        button.setBackground(colors.m_bg_2);
        button.setBorderColor(colors.m_bg_3);
        button.setForeground(colors.m_grey_2);
        button.setHoverBackgroundColor(colors.m_bg_3);
        button.setHoverBorderColor(colors.m_bg_3);
        button.setClickBackgroundColor(colors.m_bg_1);
        button.setClickBorderColor(colors.m_bg_1);
        button.setPreferredSize(new Dimension(100, button.getPreferredSize().height));
      };
    };

    this.filterOptionPanel = new FilterOptionPanel();
    this.filterOptionPanel.addFilterOption("All", buttonSetupGenerator.apply("All"), (e) -> {
      IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
      currentView.updateView(() -> ImageRecord.selectAllNewest());
    });
    this.filterOptionPanel.addFilterOption("Oldest", buttonSetupGenerator.apply("Oldest"), (e) -> {
      IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
      currentView.updateView(() -> ImageRecord.selectAllOldest());
    });
    this.filterOptionPanel.addFilterOption("Newest", buttonSetupGenerator.apply("Newest"), (e) -> {
      IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
      currentView.updateView(() -> ImageRecord.selectAllNewest());
    });
    this.filterOptionPanel.addFilterOption("Mine", buttonSetupGenerator.apply("Mine"), (e) -> {
      IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
      currentView.updateView(
        () -> ImageRecord.selectByField(ImageRecord.FIELD_AUTHOR, String.valueOf(SessionManager.getCurrentUser().m_id))
      );
    });

    this.mediaViewSwitcher = new MediaViewSwitcher();

    this.droppedItemsPanel = new DropContainerPanel(this.colors);
    this.droppedItemsPanel.addActionListener(mediaButtonActionListener);

    this.addMediaButton = new MButton("Add New Media File");
    this.addMediaButton.setBackground(colors.m_bg_0);
    this.addMediaButton.setBorderColor(colors.m_bg_4);
    this.addMediaButton.setHoverBackgroundColor(colors.m_bg_2);
    this.addMediaButton.setHoverBorderColor(colors.m_bg_3);
    this.addMediaButton.setClickBackgroundColor(colors.m_bg_1);
    this.addMediaButton.setFont(nunito_extrabold_14);
    this.addMediaButton.setIcon(IconManager.getIcon("upload.png", 16, 16, colors.m_grey_0));
    this.addMediaButton.setForeground(colors.m_grey_0);
    this.addMediaButton.setText(" " + this.addMediaButton.getText());
    this.addMediaButton.addActionListener(mediaButtonActionListener);

    this.filterTextField = new MTextField(30);
    this.filterTextField.setIcon(IconManager.getIcon("search.png", 20, 20, colors.m_grey_0), MTextField.ICON_PREFIX);
    this.filterTextField.setBackground(colors.m_bg_dim);
    this.filterTextField.setForeground(colors.m_fg_0);
    this.filterTextField.setPlaceholder("Filter Images");
    this.filterTextField.setPlaceholderForeground(colors.m_grey_0);
    this.filterTextField.setBorderWidth(1);
    this.filterTextField.setBorderColor(colors.m_bg_5);
    this.filterTextField.setInputForeground(colors.m_fg_0);
    this.filterTextField.setFont(nunito_bold_14);
    this.filterTextField.getDocument().addDocumentListener(this);

    this.selectFilesButton = new MButton("Select Files");
    this.selectFilesButton.setBackground(colors.m_bg_dim);
    this.selectFilesButton.setBorderColor(colors.m_bg_3);
    this.selectFilesButton.setHoverBorderColor(colors.m_accent);
    this.selectFilesButton.setClickBackgroundColor(colors.m_bg_1);
    this.selectFilesButton.setForeground(colors.m_accent);
    this.selectFilesButton.setHorizontalAlignment(SwingConstants.CENTER);
    this.selectFilesButton.addActionListener(mediaButtonActionListener);

    this.applyBulkAction = new MButton("Apply");
    this.applyBulkAction.setForeground(colors.m_grey_2);
    this.applyBulkAction.setBackground(colors.m_bg_0);
    this.applyBulkAction.setBorderColor(colors.m_bg_5);
    this.applyBulkAction.setHoverBackgroundColor(colors.m_bg_1);
    this.applyBulkAction.setClickBackgroundColor(colors.m_bg_dim);
    this.applyBulkAction.setHoverForegroundColor(colors.m_blue);
    this.applyBulkAction.setHoverBorderColor(colors.m_blue);
    this.applyBulkAction.setBorderRadius(0);
    this.applyBulkAction.addActionListener(mediaButtonActionListener);

    this.layoutSelectorComponent = new LayoutSelectorComponent();
  }


  private class MediaButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == FormMedia.this.addMediaButton) {
        dropPanel.setVisible(!dropPanel.isVisible());
      }

      if (e.getSource() == FormMedia.this.selectFilesButton) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Select Files");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

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

      if (e.getSource() == btnTableView) {
        IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
        mediaViewSwitcher.setCurrentView(MediaViewSwitcher.VIEW_TABLE, currentView.getCurrentQueryInvoker());
        btnGridView.setIcon(FormMedia.this.gridIcon);
        btnTableView.setIcon(IconManager.changeIconColor(FormMedia.this.tableIcon, colors.m_grey_2));
      }

      if (e.getSource() == btnGridView) {
        IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
        mediaViewSwitcher.setCurrentView(MediaViewSwitcher.VIEW_GRID, currentView.getCurrentQueryInvoker());
        btnGridView.setIcon(IconManager.changeIconColor(FormMedia.this.gridIcon, colors.m_grey_2));
        btnTableView.setIcon(FormMedia.this.tableIcon);
      }

      if (e.getSource() == FormMedia.this.droppedItemsPanel.getConfirmButton()) {
        // Do something with the data
        IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
        List<Object> objs = FormMedia.this.droppedItemsPanel.getAllData();
        int count = 0;
        for (Object obj : objs) {
          if (obj instanceof File file) {
            Path newFilePath = rm.moveStagedFileToUploadDir(file);
            System.out.println(rm.getProjectPath().relativize(newFilePath));
            ImageRecord.insert(
                String.valueOf(rm.getProjectPath().relativize(newFilePath)).replace("\\", "/"),
                file.getName(),
                "",
                SessionManager.getCurrentUser().m_id);
            count++;
          }
        }
        dropPanel.setVisible(false);
        currentView.refresh();
        PanelManager.createPopup(new NotificationPopup(
          "Added " + count + " image(s)",
          NotificationPopup.NOTIFY_LEVEL_INFO,
          5000
        ));
        // Clean temp file from download
        rm.cleanTempFiles();
        // Clear the data
        FormMedia.this.droppedItemsPanel.clearData();
      }

      if (e.getSource() == FormMedia.this.applyBulkAction) {
        System.out.println(((String) FormMedia.this.bulkAction.getSelectedItem()));
        if (((String) FormMedia.this.bulkAction.getSelectedItem()).equals("Edit")) {
          // TODO FormEditMedia
          IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
          List<ImageRecord> selectedImage = currentView.getSelectedImages();
          FormEditImage frame = new FormEditImage(selectedImage);
          frame.setVisible(true);
          List<EditImagePanel> EditImagePanel = frame.getEditImagePanels();
          for (EditImagePanel panel : EditImagePanel) {
            currentView.setButtonRefreshOnClick(panel.getDeleteComponent());
            currentView.setButtonRefreshOnClick(panel.getSubmitComponent());
          }
        }
        if (((String) FormMedia.this.bulkAction.getSelectedItem()).equals("Delete Permanently")) {
          IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
          List<ImageRecord> selectedImage = currentView.getSelectedImages();
          if (!selectedImage.isEmpty()) {
            int selectedImagesCount = selectedImage.size();
            int response = JOptionPane.showConfirmDialog(
                FormMedia.this,
                "Delete " + selectedImagesCount + " items?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
              currentView.deleteSelectedImages();
              PanelManager.createPopup(new NotificationPopup("Deleted " + selectedImagesCount + " image(s)", NotificationPopup.NOTIFY_LEVEL_INFO, 5000));
            }
          } else {
            JOptionPane.showMessageDialog(null, "Nothing to delete");
          }
        }
      }
      return;
    }
  }

  private class MediaTransferListener implements MTransferListener {
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
      System.out.println(image);
      Path p = rm.saveImageToTempFile(image);
      if (p != null) {
        droppedItemsPanel.addData(p.toFile());
      }
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

  private class LayoutSelectorComponent extends RoundedPanel {
    public LayoutSelectorComponent() {
      super();
      this.setBackground(colors.m_bg_dim);
      this.setBorderColor(colors.m_bg_5);
      this.setPreferredSize(new Dimension(50, this.getPreferredSize().height));
      this.setMaximumSize(new Dimension(50, this.getPreferredSize().height));
      this.setMinimumSize(new Dimension(50, this.getPreferredSize().height));

      Consumer<MButton> setupButton = btn -> {
        btn.setBackground(colors.m_bg_dim);
        btn.setBackground(null);
        btn.setBorderWidth(0);
        btn.setBorderColor(null);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(20, 20));
        btn.addActionListener(mediaButtonActionListener);
      };

      FormMedia.this.gridIcon = IconManager.getIcon("grid.png", 16, 16, colors.m_bg_5);
      FormMedia.this.btnGridView = new MButton(gridIcon);
      setupButton.accept(FormMedia.this.btnGridView);

      FormMedia.this.tableIcon = IconManager.getIcon("table.png", 16, 16, colors.m_bg_5);
      FormMedia.this.btnTableView = new MButton(IconManager.changeIconColor(FormMedia.this.tableIcon, colors.m_grey_2));
      setupButton.accept(FormMedia.this.btnTableView);

      this.setLayout(new GridBagLayout());

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      c.weighty = 1.0;
      c.insets = new Insets(1, 1, 1, 0);

      this.add(btnTableView, c);

      JSeparator sep = new JSeparator(JSeparator.VERTICAL);
      sep.setForeground(colors.m_bg_5);
      sep.setBackground(null);
      c.gridx = 1;
      c.fill = GridBagConstraints.VERTICAL;
      c.insets = new Insets(1, 0, 1, 0);
      c.weightx = 0.0;
      c.weighty = 1.0;
      c.anchor = GridBagConstraints.CENTER;
      this.add(sep, c);

      c.insets = new Insets(1, 0, 1, 1);
      c.gridx = 2;
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      c.weighty = 1.0;
      this.add(btnGridView, c);
    }
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    get_images(this.filterTextField.getText());
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    get_images(this.filterTextField.getText());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    get_images(this.filterTextField.getText());
  }

  private void get_images(String name) {
    if (name.equals(this.filterTextField.getPlaceholder())) {
      return;
    }
    if (!name.isBlank()) {
      IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
      currentView.updateView(() -> ImageRecord.selectLikeName(name));
    } else {
      IMediaView currentView = mediaViewSwitcher.getCurrentMediaInterface();
      currentView.reset();
    }
  }

  private Icon gridIcon;
  private Icon tableIcon;

  private ColorScheme colors;
  private ResourceManager rm;
  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  // Controller Component
  private MTextField filterTextField;
  private MComboBox<String> bulkAction;
  private MButton addMediaButton;
  private MButton selectFilesButton;
  private MButton applyBulkAction;
  private MButton btnGridView;
  private MButton btnTableView;
  private MediaViewSwitcher mediaViewSwitcher;
  private MediaButtonActionListener mediaButtonActionListener = new MediaButtonActionListener();

  // View Component
  // private MediaTableView mediaTableView;
  private JLabel topLabel;
  private JPanel dropPanel = null;
  private RoundedPanel contentContainer = new RoundedPanel();
  private DropContainerPanel droppedItemsPanel;
  private LayoutSelectorComponent layoutSelectorComponent;
  private FilterOptionPanel filterOptionPanel;
}
