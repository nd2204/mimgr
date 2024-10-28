package dev.mimgr;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MCheckBox;
import dev.mimgr.custom.MCheckBoxCellRenderer;
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferHandler;
import dev.mimgr.utils.MTransferListener;

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

    dropPanel = new DropPanel();
    dropPanel.setVisible(false);
    MTransferHandler transferHandler = new MTransferHandler();
    transferHandler.addTransferListener(this);
    dropPanel.setTransferHandler(transferHandler);

    c.insets = new Insets(0, 25, 15, 25);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = 2;
    c.fill = GridBagConstraints.BOTH;
    this.add(dropPanel, c);

    // Content
  {
      GridBagConstraints cc = new GridBagConstraints();
      contentContainer.setLayout(new GridBagLayout());
      contentContainer.setBackground(colors.m_bg_0);

      cc.gridx = 0;
      cc.gridy = 0;
      cc.weightx = 1.0;
      cc.anchor = GridBagConstraints.FIRST_LINE_START;
      cc.fill = GridBagConstraints.HORIZONTAL;
      cc.insets = new Insets(15, 15, 15, 15);
      contentContainer.add(filterTextField, cc);

      cc.gridx = 0;
      cc.gridy = 1;
      cc.weightx = 1.0;
      cc.weighty = 1.0;
      cc.insets = new Insets(15, 0, 15, 0);
      cc.fill = GridBagConstraints.BOTH;
      setup_table();
      contentContainer.add(scrollPane, cc);
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
    this.addMediaButton.setHoverBackgroundColor(colors.m_bg_dim);
    this.addMediaButton.setBorderColor(colors.m_bg_4);
    this.addMediaButton.setHoverBorderColor(colors.m_bg_2);
    this.addMediaButton.setFont(nunito_extrabold_14);
    this.addMediaButton.setIcon(IconManager.getIcon("upload.png", 16, 16, colors.m_grey_0));
    this.addMediaButton.setForeground(colors.m_grey_0);
    this.addMediaButton.setText(" " + this.addMediaButton.getText());
    this.addMediaButton.addActionListener(this);

    this.filterTextField.setIcon(IconManager.getIcon("search.png", 20, 20, colors.m_grey_0), MTextField.ICON_PREFIX);
    this.filterTextField.setBackground(colors.m_bg_dim);
    this.filterTextField.setForeground(colors.m_fg_0);
    this.filterTextField.setPlaceholder("Filter products");
    this.filterTextField.setPlaceholderForeground(colors.m_grey_0);
    this.filterTextField.setBorderWidth(1);
    this.filterTextField.setBorderColor(colors.m_bg_5);
    this.filterTextField.setInputForeground(colors.m_fg_0);
    this.filterTextField.setFont(nunito_bold_14);

    this.scrollPane.setBackground(colors.m_bg_0);
    this.scrollPane.setBorder(BorderFactory.createEmptyBorder());

    this.checkBoxModel.setCheckColor(colors.m_green);
    this.checkBoxModel.setBoxColor(colors.m_bg_4);
    this.checkBoxModel.setBackground(colors.m_bg_0);

    this.selectFilesButton.setBackground(colors.m_bg_0);
    this.selectFilesButton.setBorderColor(colors.m_accent);
    this.selectFilesButton.setForeground(colors.m_accent);
    this.selectFilesButton.addActionListener(this);
    this.selectFilesButton.setHorizontalAlignment(SwingConstants.CENTER);
  }

  private void setup_table() {
    table = new MTable(colors);
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
    model.addColumn("");
    model.addColumn("File");
    model.addColumn("Author");
    model.addColumn("Caption");
    model.addColumn("Date");
    model.addRow(new Object[]{Boolean.FALSE, "Kathy", "Smith", "Snowboarding", 3});
    model.addRow(new Object[]{Boolean.FALSE, "John", "Doe", "Rowing", 3});

    table.setModel(model);
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(50);
    columnModel.getColumn(0).setMinWidth(50);
    columnModel.getColumn(0).setMaxWidth(50);
    scrollPane = new JScrollPane(table);
    // table.setup_scrollbar(scrollPane);
    table.setFillsViewportHeight(true);

    columnModel.getColumn(0).setCellRenderer(new MCheckBoxCellRenderer(colors));
    columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(checkBoxModel));
  }

  private class DropPanel extends JPanel {
    DropPanel() {
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

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D) g.create();

      // Set dashed stroke
      float[] dashPattern = {10, 10}; // Dash length, gap length
      g2d.setStroke(new BasicStroke(
        2,                     // Line width
        BasicStroke.CAP_BUTT,  // End cap
        BasicStroke.JOIN_BEVEL, // Line join style
        0,                     // Miter limit
        dashPattern,           // Dash pattern
        0                      // Dash phase
      ));

      g2d.setColor(colors.m_grey_0); // Set border color
      g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10); // Draw border with padding
      g2d.dispose();
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
    return;
  }

  @Override
  public void onStringImported(String string) {
    System.out.println("Dropped String data");
    System.out.println(string);
    return;
  }

  @Override
  public void onImageImported(Image image) {
    System.out.println("Dropped Image data");
    System.out.println(image);
    return;
  }

  @Override
  public void onFileListImported(List<?> files) {
    System.out.println("Dropped File list data");
    System.out.println(files);
    return;
  }

  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private MCheckBox checkBoxModel = new MCheckBox();
  private ColorScheme colors;
  private RoundedPanel contentContainer = new RoundedPanel();
  private GridBagConstraints c = new GridBagConstraints();
  private MTextField filterTextField = new MTextField(30);
  private MTable table;
  private JScrollPane scrollPane;
  private JLabel topLabel = new JLabel("Media Library");
  private MButton addMediaButton = new MButton("Add New Media File");
  private MButton selectFilesButton = new MButton("Select Files");
  private DropPanel dropPanel = null;
}
