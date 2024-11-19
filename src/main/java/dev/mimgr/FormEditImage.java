package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.ResourceManager;

public class FormEditImage extends JFrame {
  ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();

  private double m_aspect_ratio;
  private int    m_width;
  private int    m_height;

  public ArrayList<EditImagePanel> getEditImagePanels() {
    return this.EditImagePanels;
  }

  public FormEditImage(ImageRecord ir) {
    ImageRecords = new ArrayList<>();
    ImageRecords.add(ir);
    Init();
  }

  public FormEditImage(Iterable<ImageRecord> irList) {
    ImageRecords = new ArrayList<>();
    for (ImageRecord ir : irList) {
      ImageRecords.add(ir);
    }
    Init();
  }

  public FormEditImage(ImageRecord[] irList) {
    ImageRecords = new ArrayList<>(Arrays.asList(irList));
    Init();
  }

  private void Init() {
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);
    EditImagePanels = new ArrayList<>();
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(colors.m_bg_dim);
    currentEditImagePanel = new JPanel();
    currentEditImagePanel.setBackground(colors.m_bg_0);
    sidebarPanel = new SidebarPanel(currentEditImagePanel);
    sidebarPanel.setBackground(colors.m_bg_0);
    sidebarPanel.setPreferredSize(new Dimension(300, this.getHeight()));
    sidebarPanel.setupButtonStyle = (button) -> {
      button.setPadding(new Insets(10, 0, 10, 10));
      button.setFont(FontManager.getFont("NunitoBold", 16f));
      button.setForeground(colors.m_fg_0);
      button.setBackground(colors.m_bg_1);
      button.setBorderColor(null);
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setHoverBackgroundColor(colors.m_bg_2);
      button.setPreferredSize(new Dimension(220, button.getPreferredSize().height));
    };
    sidebarPanel.setupSelectedButtonStyle = (button) -> {
      button.setFont(FontManager.getFont("NunitoBold", 16f));
      button.setForeground(colors.m_fg_0);
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setBorderColor(null);
      button.setBackground(colors.m_bg_dim);
    };
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = gc.gridy = 0;
    gc.weightx = gc.weighty = 1.0;
    gc.insets = new Insets(20, 10, 20, 10);
    gc.fill = GridBagConstraints.BOTH;
    MScrollPane sp = new MScrollPane();
    JPanel buttonMenu = new ButtonMenu();
    sp.add(buttonMenu);
    sp.setViewportView(buttonMenu);
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sidebarPanel.addComponent(sp, gc);
    if (ImageRecords.size() < 2) {
      sidebarPanel.setVisible(false);
    }

    FontManager.loadFont("Roboto", "Roboto-Regular.ttf");
    FontManager.loadFont("RobotoBold", "Roboto-Bold.ttf");
    FontManager.loadFont("RobotoMonoBold", "RobotoMono-Bold.ttf");
    FontManager.loadFont("NunitoSemiBold", "Nunito-SemiBold.ttf");
    FontManager.loadFont("Nunito", "Nunito-Regular.ttf");
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
    FontManager.loadFont("NunitoExtraBold", "Nunito-ExtraBold.ttf");

    // Main window
    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setName("Upload");
    this.setSize(1154, m_height);
    this.setLocationRelativeTo(null);
    this.add(mainPanel);
    this.setTitle("Edit Image");
    mainPanel.add(currentEditImagePanel, BorderLayout.CENTER);
    mainPanel.add(sidebarPanel, BorderLayout.WEST);
    mainPanel.add(new TopPanel(), BorderLayout.NORTH);
    mainPanel.setVisible(true);

    this.setVisible(true);
    this.requestFocus();
  }

  private EditImagePanel createImageEditPanel(ImageRecord ir) {
    EditImagePanel EditImagePanel = new EditImagePanel();
    return EditImagePanel;
  }

  private class TopPanel extends JPanel {
    public TopPanel() {
      this.setLayout(new FlowLayout(FlowLayout.LEFT));
      this.setBackground(colors.m_bg_dim);
      JLabel lblEditImage = new JLabel("Edit Image");
      lblEditImage.setFont(FontManager.getFont("NunitoExtraBold", 20f));
      lblEditImage.setAlignmentX(JLabel.LEFT_ALIGNMENT);
      lblEditImage.setAlignmentY(JLabel.CENTER_ALIGNMENT);
      lblEditImage.setBorder(new EmptyBorder(20, 20, 20, 20));
      lblEditImage.setForeground(colors.m_fg_0);
      this.add(lblEditImage);
    }
  }

  private class ButtonMenu extends JPanel {
    public ButtonMenu() {
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      // this.setPreferredSize(new Dimension(280, this.getPreferredSize().height));
      final int padding_horizontal = 0;
      final int padding_vertical = 5;

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.anchor = GridBagConstraints.FIRST_LINE_START;
      c.fill = GridBagConstraints.HORIZONTAL;

      EditImagePanel panel;
      MButton firstMenuBtn = null;
      for (ImageRecord ir : ImageRecords) {
        panel = createImageEditPanel(ir);
        MButton button = sidebarPanel.addDetachedButton(ir.m_name, null, panel);
        if (ir == ImageRecords.getFirst()) {
          c.insets = new Insets(0, padding_horizontal, padding_vertical, padding_horizontal);
          firstMenuBtn = button;
        }
        else if (ir == ImageRecords.getLast()) {
          c.insets = new Insets(padding_vertical, padding_horizontal, 0, padding_horizontal);
        } else {
          c.insets = new Insets(padding_vertical, padding_horizontal, padding_vertical, padding_horizontal);
        }
        EditImagePanels.add(panel);
        new EditButtonListener(button, panel, ir);
        this.add(button , c);
        c.gridy++;
      }

      sidebarPanel.setCurrentMenu(firstMenuBtn);
      // Dummy component to anchor buttons to the top and prevent infinite panel growth
      c.weighty = 1.0;
      this.add(Box.createVerticalGlue(), c);
    }
  }

  private class EditButtonListener implements ActionListener {
    public EditButtonListener(MButton sb, EditImagePanel panel, ImageRecord ir) {
      this.sidebarButton = sb;
      this.panel = panel;
      this.panel.getDeleteComponent().setText("Delete Image");
      this.panel.getLabelComponent().setVisible(false);
      this.panel.setImageRecord(ir);

      InitializeComponent();
    }

    private void InitializeComponent() {
      this.btnDelete = panel.getDeleteComponent();
      this.btnDelete.addActionListener(this);

      this.btnSubmit = panel.getSubmitComponent();
      this.btnSubmit.setText("Update");
      this.btnSubmit.addActionListener(this);
      this.btnSubmit.setBackground(colors.m_green);
      this.btnSubmit.setBorderColor(colors.m_green);
      this.btnSubmit.setDefaultForeground(colors.m_fg_1);
      this.btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.btnSubmit.setEnabled(true);

      ImageRecord ir = this.panel.getImageRecord();

      this.tfName = panel.getNameComponent();
      this.tfName.setText(ir.m_name);
      this.tfName.setCaretPosition(0);

      this.taCaption = panel.getCaptionComponent();
      this.taCaption.setText(ir.m_caption);

      this.dropContainerPanel = panel.getDropContainerPanel();
      if (ir != null) {
        this.dropContainerPanel.addData(Paths.get(ir.m_url).toAbsolutePath().toFile());
      }

      TextFieldDocumentListener textFieldListener = new TextFieldDocumentListener();

      this.tfName.getDocument().addDocumentListener(textFieldListener);
      this.taCaption.getDocument().addDocumentListener(textFieldListener);
    }

    private String processDropData() {
      List<Object> objs = this.dropContainerPanel.getAllData();
      // Process the first image in the drop panel
      String image_url = "";
      for (Object o : objs) {
        if (o instanceof File file) {
          if (!rm.getUploadPath().resolve(file.getName()).toFile().exists()) {
            Path newFilePath = rm.moveStagedFileToUploadDir(file);
            image_url = String.valueOf(rm.getProjectPath().relativize(newFilePath)).replace("\\", "/");
          }
          break;
        }
      }
      // Clean temp file from download
      rm.cleanTempFiles();
      return image_url;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnDelete) {
        sidebarPanel.removeMenuButton(sidebarButton);
        int row = ImageRecord.delete(panel.getImageRecord().m_id);
        JOptionPane.showMessageDialog(null, row + " row(s) affected");
      }

      else if (e.getSource() == btnSubmit) {
        String name = tfName.getTextString();
        String Caption = taCaption.getTextString();
        String image_url = processDropData();
        int row = ImageRecord.update(name, Caption, image_url,SessionManager.getCurrentUser().m_id, panel.getImageRecord().m_id);
        sidebarPanel.removeMenuButton(sidebarButton);
        JOptionPane.showMessageDialog(null, row + " row(s) affected");
      }

      if (sidebarPanel.getMenuButtonCount() < 1) {
        FormEditImage.this.dispose();
      }
    }

    private class TextFieldDocumentListener implements DocumentListener {
      @Override
      public void insertUpdate(DocumentEvent e) {
        checkFields();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        checkFields();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        checkFields();
      }

      private void checkFields() {
        if (!tfName.getText().isEmpty()) {
          btnSubmit.setBackground(colors.m_green);
          btnSubmit.setBorderColor(colors.m_green);
          btnSubmit.setDefaultForeground(colors.m_fg_1);
          btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
          btnSubmit.setEnabled(true);
        } else {
          btnSubmit.setBackground(colors.m_bg_1);
          btnSubmit.setBorderColor(colors.m_bg_1);
          btnSubmit.setDefaultForeground(colors.m_bg_3);
          btnSubmit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          btnSubmit.setEnabled(false);
        }
      }
    }

    MButton sidebarButton, btnSubmit, btnDelete;
    private DropContainerPanel dropContainerPanel;
    private EditImagePanel panel;
    private MTextArea taCaption;
    private MTextField tfName;
  }

  private ResourceManager rm = ResourceManager.getInstance();
  private SidebarPanel sidebarPanel;
  private JPanel currentEditImagePanel, mainPanel;
  private ArrayList<ImageRecord> ImageRecords;
  private ArrayList<EditImagePanel> EditImagePanels;
}
