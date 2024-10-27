package dev.mimgr;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormProduct extends JPanel {
  public FormProduct(ColorScheme colors) {
    this.colors = colors;
    Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
    Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
    Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
    Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

    // =======================================================
    // Setup Layout
    // =======================================================

    this.setLayout(new GridBagLayout());

    int padding = 25;

    // Top
    c.insets = new Insets(25, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(topLabel, c);
    c.gridx = 1;
    c.gridy = 0;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.insets = new Insets(20, 5, 20, 5);
    this.add(this.importButton, c);
    c.weightx = 0.0;
    c.gridx = 2;
    this.add(this.exportButton, c);
    c.gridx = 3;
    c.insets = new Insets(20, 5, 20, padding);
    this.add(addProductButton, c);

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
    c.gridy = 1;
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

    this.importButton.setBackground(colors.m_bg_2);
    this.importButton.setBorderColor(colors.m_bg_2);
    this.importButton.setForeground(colors.m_fg_0);
    this.importButton.setFont(nunito_extrabold_14);

    this.exportButton.setBackground(colors.m_bg_2);
    this.exportButton.setBorderColor(colors.m_bg_2);
    this.exportButton.setForeground(colors.m_fg_0);
    this.exportButton.setFont(nunito_extrabold_14);

    this.addProductButton.setBackground(colors.m_green);
    this.addProductButton.setBorderColor(colors.m_green);
    this.addProductButton.setForeground(colors.m_fg_1);
    this.addProductButton.setFont(nunito_extrabold_14);
    this.addProductButton.setIcon(IconManager.getIcon("add.png", 16, 16, colors.m_fg_1));
    this.addProductButton.setText(" " + this.addProductButton.getText());

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
    model.addColumn("Last Name");
    model.addColumn("Sport");
    model.addColumn("# of Years");
    model.addColumn("Vegetarian");
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

    columnModel.getColumn(0).setCellRenderer(new CustomCheckBoxRenderer());
    columnModel.getColumn(0).setCellEditor(new DefaultCellEditor(checkBoxModel));
  }

  // Custom renderer that uses CustomCheckBox
  private class CustomCheckBoxRenderer extends MCheckBox implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      setHorizontalAlignment(SwingConstants.CENTER);
      setCheckColor(colors.m_green);
      setBoxColor(colors.m_bg_4);
      setBackground(colors.m_bg_0);
      setSelected((Boolean) value);
      return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      // Draw the border using the specified color
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setColor(colors.m_bg_4);
      g2.drawLine(0, getHeight(), getWidth(), getHeight()); // Bottom border

      g2.dispose();
    }
  }

  private MCheckBox checkBoxModel = new MCheckBox();
  private ColorScheme colors;
  private RoundedPanel contentContainer = new RoundedPanel();
  private JLabel topLabel = new JLabel("Products");
  private GridBagConstraints c = new GridBagConstraints();
  private MTextField filterTextField = new MTextField(30);
  private MTable table;
  private JScrollPane scrollPane;
  private MButton importButton = new MButton("Import");
  private MButton exportButton = new MButton("Export");
  private MButton addProductButton = new MButton("Add product");
}
