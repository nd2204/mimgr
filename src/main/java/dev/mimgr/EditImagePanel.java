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

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import dev.mimgr.component.MediaGridView;
import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.DropPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferListener;
import dev.mimgr.utils.ResourceManager;

public class EditImagePanel extends JPanel {

  public MButton getSubmitComponent() {
    return this.btnSubmit;
  }

  public JLabel getLabelComponent() {
    return this.lblAddImage;
  }

  public MTextArea getCaptionComponent() {
    return this.taCaption;
  }

  public MTextField getNameComponent() {
    return this.tfName;
  }

  public MButton getDeleteComponent() {
    return this.btnDelete;
  }

  public void setImageRecord(ImageRecord pr) {
    this.Image = pr;
  }

  public ImageRecord getImageRecord() {
    return this.Image;
  }

  public DropContainerPanel getDropContainerPanel() {
    return this.dropContainerPanel;
  }

  EditImagePanel() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_dim);
    this.setup_form_style();

    this.setPreferredSize(new Dimension(1000, this.getPreferredSize().height));
    this.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

    MScrollPane scrollPane = new MScrollPane();
    scrollPane.getVerticalScrollBar().setUnitIncrement(100);
    this.add(scrollPane);

    scrollPane.add(thisPanel);
    scrollPane.setViewportView(thisPanel);

    thisPanel.setLayout(new GridBagLayout());
    thisPanel.setBackground(colors.m_bg_dim);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    lblAddImage.setFont(nunito_bold_20);
    lblAddImage.setForeground(colors.m_fg_0);
    gbc.insets = new Insets(40, 10, 20, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    thisPanel.add(lblAddImage, gbc);

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    thisPanel.add(new NameAndCaptionPanel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    thisPanel.add(new MediaDropPanel(), gbc);

    gbc.insets = new Insets(0, 10, 40, 5);
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 1;
    thisPanel.add(btnDelete, gbc);

    gbc.insets = new Insets(0, 5, 40, 10);
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    thisPanel.add(btnSubmit, gbc);
  }

  public void setup_form_style() {
    // ========================= Fields =========================
    Consumer<MTextField> setup_common_textfield = (tf) -> {
      tf.setInputForeground(colors.m_fg_0);
      tf.setBackground(colors.m_bg_dim);
      tf.setBorderColor(colors.m_bg_4);
      tf.setFocusBorderColor(colors.m_blue);
      tf.setForeground(colors.m_fg_0);
      tf.setBorderWidth(2);
      tf.setFont(nunito_bold_14);
    };

    tfName = new MTextField(20);
    setup_common_textfield.accept(tfName);

    taCaption = new MTextArea(8, 50);
    taCaption.setPadding(new Insets(20, 20, 20, 20));
    taCaption.setInputForeground(colors.m_fg_0);
    taCaption.setBackground(colors.m_bg_dim);
    taCaption.setBorderColor(colors.m_bg_4);
    taCaption.setFocusBorderColor(colors.m_blue);
    taCaption.setForeground(colors.m_fg_0);
    taCaption.setBorderWidth(2);
    taCaption.setBorderRadius(15);
    taCaption.setFont(nunito_bold_14);
    taCaption.setLineWrap(true);

    // ========================= Buttons =========================
    this.btnSubmit = new MButton("Submit");
    this.btnSubmit.setFont(nunito_bold_14);
    this.btnSubmit.setBackground(colors.m_bg_1);
    this.btnSubmit.setBorderColor(colors.m_bg_1);
    this.btnSubmit.setDefaultForeground(colors.m_bg_4);
    this.btnSubmit.setBorderWidth(2);
    this.btnSubmit.setEnabled(true);

    this.btnDelete = new MButton("Cancel");
    this.btnDelete.setFont(nunito_bold_14);
    this.btnDelete.setBackground(colors.m_bg_dim);
    this.btnDelete.setBorderColor(colors.m_bg_3);
    this.btnDelete.setDefaultForeground(colors.m_grey_0);
    this.btnDelete.setHoverForegroundColor(colors.m_fg_1);
    this.btnDelete.setHoverBorderColor(colors.m_red);
    this.btnDelete.setHoverBackgroundColor(colors.m_red);
    this.btnDelete.setBorderWidth(2);
  }

  private class SelectFromMediaLayout extends MediaGridView implements ActionListener {
    SelectFromMediaLayout() {
      super();
      this.clearButtonsActionListener();
      for (MButton button : this.getButtons()) {
        button.addActionListener(this);
      }
      this.optionBar.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() instanceof MButton button) {
        this.clearSelectedButtons();
        ImageRecord ir = this.getAssociatedImageRecord(button);
        dropContainerPanel.clearData();
        dropContainerPanel.addData(Path.of(ir.m_url).toAbsolutePath().toFile());
        this.setButtonSelected(button, true);
      }
    }

  }

  private class MediaDropPanel extends RoundedPanel implements ActionListener, MTransferListener {
    public MediaDropPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setLayout(new GridBagLayout());
      this.setBackground(colors.m_bg_0);
      this.setBorderRadius(15);

      dropContainerPanel = new DropContainerPanel(colors);
      selectFromMediaLayout = new SelectFromMediaLayout();
      dropContainerPanel.addActionListener(this);

      btnClearImages = dropContainerPanel.getConfirmButton();
      btnClearImages.setHoverBorderColor(colors.m_red);
      btnClearImages.setText("Clear");
      btnClearImages.setHoverBackgroundColor(colors.m_red);

      btnSelectFiles.setFont(nunito_bold_14);
      btnSelectFiles.setBackground(colors.m_bg_dim);
      btnSelectFiles.setBorderColor(colors.m_bg_3);
      btnSelectFiles.setHoverBorderColor(colors.m_accent);
      btnSelectFiles.setClickBackgroundColor(colors.m_bg_1);
      btnSelectFiles.setForeground(colors.m_accent);
      btnSelectFiles.setHorizontalAlignment(SwingConstants.CENTER);
      btnSelectFiles.addActionListener(this);

      btnSelectFromMedia = new MButton("Select From Media");
      btnSelectFromMedia.setFont(nunito_bold_14);
      btnSelectFromMedia.setBackground(colors.m_bg_dim);
      btnSelectFromMedia.setBorderColor(colors.m_bg_3);
      btnSelectFromMedia.setHoverBorderColor(colors.m_accent);
      btnSelectFromMedia.setClickBackgroundColor(colors.m_bg_1);
      btnSelectFromMedia.setForeground(colors.m_accent);
      btnSelectFromMedia.setHorizontalAlignment(SwingConstants.CENTER);
      btnSelectFromMedia.addActionListener(this);

      lblMedia.setFont(nunito_bold_16);
      lblMedia.setForeground(colors.m_fg_0);
      lblMedia.setHorizontalAlignment(SwingConstants.LEFT);
      lblMedia.setVerticalAlignment(SwingConstants.CENTER);

      GridBagConstraints gbc = new GridBagConstraints();

      gbc.insets = new Insets(20, 25, 5, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_START;
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      this.add(lblMedia, gbc);

      gbc.insets = new Insets(20, 5, 5, 20);
      gbc.anchor = GridBagConstraints.FIRST_LINE_END;
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.NONE;
      this.add(btnSelectFromMedia, gbc);

      dropArea = new DropPanel(colors);
      {
        GridBagConstraints gc = new GridBagConstraints();
        dropArea.setLayout(new GridBagLayout());
        dropArea.addTransferListener(this);

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
        dropArea.add(label, gc);

        gc.insets = new Insets(15, 0, 15, 0);
        gc.gridy = 1;
        dropArea.add(label1, gc);

        gc.insets = new Insets(0, 0, 0, 0);
        gc.fill = GridBagConstraints.NONE;
        gc.gridy = 2;
        dropArea.add(btnSelectFiles, gc);
      }

      gbc.insets = new Insets(5, 20, 20, 20);
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weightx = 1.0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridwidth = 2;
      this.add(dropArea, gbc);
      selectFromMediaLayout.setVisible(false);
      selectFromMediaLayout.setMaximumSize(new Dimension(680, Integer.MAX_VALUE));
      selectFromMediaLayout.setMinimumSize(new Dimension(680, 300));
      selectFromMediaLayout.setPreferredSize(new Dimension(680, 330));
      this.add(selectFromMediaLayout, gbc);

      gbc.insets = new Insets(0, 20, 20, 20);
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.weightx = 0.0;
      gbc.weighty = 0.0;
      gbc.gridwidth = 2;
      gbc.fill = GridBagConstraints.NONE;
      JScrollPane sp = dropContainerPanel.getScrollPaneComponent();
      sp.setMaximumSize(new Dimension(660, Integer.MAX_VALUE));
      sp.setMinimumSize(new Dimension(660, sp.getPreferredSize().height));
      sp.setPreferredSize(new Dimension(660, 140));
      this.add(dropContainerPanel, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == this.btnSelectFiles) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Select Files");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        // Show the file chooser dialog and wait for user action
        int userSelection = fileChooser.showOpenDialog(null);

        // Handle the user selection
        if (userSelection == JFileChooser.APPROVE_OPTION) {
          // Get the selected file
          File selectedFile = fileChooser.getSelectedFile();
          if (rm.isValidImageFile(selectedFile)) {
            dropContainerPanel.clearData();
            dropContainerPanel.addData(selectedFile);
          }
        } else {
          System.out.println("No file selected.");
        }
      }

      if (e.getSource() == EditImagePanel.this.dropContainerPanel.getConfirmButton()) {
        // Do something with the data
        for (Object obj : EditImagePanel.this.dropContainerPanel.getAllData()) {
          if (obj instanceof File file) {
            Path newFilePath = rm.moveStagedFileToUploadDir(file);
            System.out.println(rm.getProjectPath().relativize(newFilePath));
          }
        }
        // Clean temp file from download
        rm.cleanTempFiles();
        // Clear the data
        EditImagePanel.this.dropContainerPanel.clearData();
      }

      if (e.getSource() == btnSelectFromMedia) {
        dropArea.setVisible(!dropArea.isVisible());
        selectFromMediaLayout.setVisible(!selectFromMediaLayout.isVisible());
      }
    }

    @Override
    public void onStringImported(String string) {
      if (ResourceManager.isImageUrl(string)) {
        Path fp = rm.downloadTempFile(string);
        dropContainerPanel.clearData();
        dropContainerPanel.addData(fp.toFile());
      }
      return;
    }

    @Override
    public void onImageImported(Image image) {
      return;
    }

    @Override
    public void onFileListImported(List<File> files) {
      dropContainerPanel.clearData();
      dropContainerPanel.addData(files.getFirst());
      return;
    }

    private JLabel lblMedia = new JLabel("Media");
    private MButton btnSelectFiles = new MButton("Select Files");
    private MButton btnSelectFromMedia = new MButton("Select Files");
    private SelectFromMediaLayout selectFromMediaLayout;
    private DropPanel dropArea;
  }

  private class NameAndCaptionPanel extends RoundedPanel {
    public NameAndCaptionPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblName);
      setLabelStyle(lblCaption);
      setLabelStyle(lblCategory);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblName, gc);

      gc.gridx = 0;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      this.add(tfName, gc);

      gc.gridwidth = 2;
      gc.gridx = 0;
      gc.gridy = 2;
      gc.insets = new Insets(5, 25, 5, 20);
      this.add(lblCaption, gc);
      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(taCaption, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private JLabel lblName = new JLabel("Name");
    private JLabel lblCaption = new JLabel("Caption");
    private JLabel lblCategory = new JLabel("Category");
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private ResourceManager rm = ResourceManager.getInstance();
  private JPanel thisPanel = new JPanel();
  private JLabel lblAddImage = new JLabel("Add Image");
  private MTextField tfName;
  private MTextArea taCaption;
  private MButton btnSubmit, btnDelete, btnClearImages;
  private ColorScheme colors;
  private ImageRecord Image;
  private DropContainerPanel dropContainerPanel;
}
