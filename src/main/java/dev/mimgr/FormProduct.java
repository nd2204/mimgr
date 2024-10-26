package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MTable;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormProduct extends JPanel {
  public FormProduct(ColorScheme colors) {
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
  }

  private void setup_table() {
    Object[][] data = {
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"Kathy", "Smith", "Snowboarding", 5, false},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"John", "Doe", "Rowing", 3, true},
      {"Sue", "Black", "Knitting", 2, false},
      {"Jane", "White", "Speed reading", 20, true},
      {"Jane", "White", "Speed reading", 20, true},
      {"Joe", "Brown", "Pool", 10, true}
    };
    String[] column = {"First Name",
      "Last Name",
      "Sport",
      "# of Years",
      "Vegetarian"};
    table = new MTable(data, column);
    scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);
  }

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
