package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollBar;
import dev.mimgr.custom.MScrollBarUI;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.CategoryRecord;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class ProductExportPanel extends JPanel {
  public optionsSelectedPanel getOptionsCategorySelectedPanel() {
    return this.OptionsCategorySelectedPanel;
  }

  public optionsSelectedPanel getOptionsProductSelectedPanel() {
    return this.OptionsProductSelectedPanel;
  }

  public MComboBox<CategoryRecord> getCategoryComponent() {
    return this.cbCategory;
  }

  public MComboBox<String> getProductComponent() {
    return this.cbProduct;
  }

  public MButton getSubmitComponent() {
    return this.btnSubmit;
  }

  public JLabel getLabelComponent() {
    return this.lblExportProduct;
  }

  public MButton getDeleteComponent() {
    return this.btnDelete;
  }

  public String getCategoryName(int category_id) {
    return get_category_name(category_id);
  }

  private String get_category_name(int category_id) {
    ResultSet queryResult = ProductRecord.selectByKey(category_id);
    String name_result = "";
    try {
      while (queryResult.next()) {
        name_result = queryResult.getString("category_name");
      }
      return name_result;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return name_result;
  }

  ProductExportPanel() {
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

    lblExportProduct.setFont(nunito_bold_20);
    lblExportProduct.setForeground(colors.m_fg_0);
    gbc.insets = new Insets(40, 10, 20, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    thisPanel.add(lblExportProduct, gbc);

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    thisPanel.add(new exportOptionPanel(), gbc);

    gbc.insets = new Insets(0, 10, 40, 5);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    thisPanel.add(btnDelete, gbc);

    gbc.insets = new Insets(0, 5, 40, 10);
    gbc.gridx = 1;
    gbc.gridy = 2;
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

    cbCategory = new MComboBox<>(colors);
    CategoryTree.populateComboBox(cbCategory);
    cbCategory.getEditor().getEditorComponent().setFont(nunito_bold_14);

    cbProduct = new MComboBox<>(colors);
    try (ResultSet rs = ProductRecord.selectColumnsName()) {
      while (rs.next()) {
        cbProduct.addItem(rs.getString("COLUMN_NAME"));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    cbProduct.getEditor().getEditorComponent().setFont(nunito_bold_14);

    // ========================= Buttons =========================
    this.btnSubmit = new MButton("Generate CSV");
    this.btnSubmit.setFont(nunito_bold_14);
    this.btnSubmit.setDefaultForeground(colors.m_bg_4);
    this.btnSubmit.setBorderWidth(2);
    this.btnSubmit.setEnabled(true);
    this.btnSubmit.setBackground(colors.m_blue);
    this.btnSubmit.setBorderColor(colors.m_blue);
    this.btnSubmit.setForeground(colors.m_fg_1);
    this.btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

    this.btnDelete = new MButton("Cancel");
    this.btnDelete.setFont(nunito_bold_14);
    this.btnDelete.setBackground(colors.m_bg_dim);
    this.btnDelete.setBorderColor(colors.m_bg_3);
    this.btnDelete.setDefaultForeground(colors.m_grey_0);
    this.btnDelete.setHoverForegroundColor(colors.m_fg_1);
    this.btnDelete.setHoverBorderColor(colors.m_red);
    this.btnDelete.setHoverBackgroundColor(colors.m_red);
    this.btnDelete.setBorderWidth(2);

    this.OptionsCategorySelectedPanel = new optionsSelectedPanel(this.colors);
    this.OptionsProductSelectedPanel = new optionsSelectedPanel(this.colors);
  }

  private class exportOptionPanel extends RoundedPanel {
    public exportOptionPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblTitle);
      setLabelStyle(lblProduct);
      setLabelStyle(lblCategory);
      setTextAreaStyle(taCategory);
      setTextAreaStyle(taProduct);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblCategory, gc);

      gc.gridx = 0;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      gc.ipadx = 30;
      gc.ipady = 30;
      this.add(cbCategory, gc);

      gc.gridx = 0;
      gc.gridy = 2;
      gc.fill = GridBagConstraints.BOTH;
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.insets = new Insets(0, 0, 0, 0);
      JScrollPane sp = OptionsCategorySelectedPanel.getScrollPaneComponent();
      sp.setMaximumSize(new Dimension(660, Integer.MAX_VALUE));
      sp.setMinimumSize(new Dimension(660, sp.getPreferredSize().height));
      sp.setPreferredSize(new Dimension(660, 80));
      this.add(OptionsCategorySelectedPanel, gc);

      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(20, 25, 5, 20);
      gc.ipadx = 0;
      gc.ipady = 0;
      this.add(lblProduct, gc);

      gc.gridx = 0;
      gc.gridy = 4;
      gc.insets = new Insets(0, 20, 20, 20);
      gc.ipadx = 30;
      gc.ipady = 30;
      this.add(cbProduct, gc);

      gc.gridx = 0;
      gc.gridy = 5;
      gc.fill = GridBagConstraints.BOTH;
      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.insets = new Insets(0, 0, 0, 0);
      JScrollPane sp2 = OptionsProductSelectedPanel.getScrollPaneComponent();
      sp2.setMaximumSize(new Dimension(660, Integer.MAX_VALUE));
      sp2.setMinimumSize(new Dimension(660, sp.getPreferredSize().height));
      sp2.setPreferredSize(new Dimension(660, 80));
      this.add(OptionsProductSelectedPanel, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private void setTextAreaStyle(MTextArea ta) {
      ta.setPadding(new Insets(20, 20, 20, 20));
      ta.setInputForeground(colors.m_fg_0);
      ta.setBackground(colors.m_bg_dim);
      ta.setBorderColor(colors.m_bg_4);
      ta.setFocusBorderColor(colors.m_blue);
      ta.setForeground(colors.m_fg_0);
      ta.setBorderWidth(2);
      ta.setBorderRadius(15);
      ta.setFont(nunito_bold_14);
      ta.setLineWrap(true);
    }

    private JLabel lblTitle = new JLabel("Title");
    private JLabel lblProduct = new JLabel("Product");
    private JLabel lblCategory = new JLabel("Category");
    private MTextArea taCategory = new MTextArea(8, 50);
    private MTextArea taProduct = new MTextArea(8, 50);

  }

  public class optionsSelectedPanel extends JPanel implements ActionListener {
    public optionsSelectedPanel(ColorScheme colors) {
      thisPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
      thisPanel.setBackground(colors.m_bg_0);
      thisPanel.setSize(0, 140);
      thisPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
  
      scrollPane = new JScrollPane(
        thisPanel,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
      );
  
      MScrollBar hsb = new MScrollBar();
      hsb.setOrientation(MScrollBarUI.HORIZONTAL);
      scrollPane.setHorizontalScrollBar(hsb);
  
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      scrollPane.setOpaque(false);
  
      this.colors = colors;
      this.setLayout(new BorderLayout());
      this.setBackground(colors.m_bg_0);
      this.setVisible(true);
      this.add(scrollPane, BorderLayout.CENTER);
    }
  
    public void addData(Object data) {
      MButton button = null;
      if (data instanceof File file) {
        button = createButton(IconManager.loadIcon(file));
      }
      else if (data instanceof String s) {
        if (!s.isBlank() && !stagedData.containsValue(data)) {
          button = createButton(null);
          button.setText(s);
        }
      }
      else if (data instanceof ImageIcon icon) {
        button = createButton(icon);
      }
      else if (data instanceof CategoryRecord cr) {
        if (!stagedData.containsValue(data)) {
          button = createButton(null);
          button.setText(cr.m_name);
        }
      }
  
      if (button != null) {
        setup_button(button);
        stagedData.put(button, data);
        thisPanel.add(button);
      }
  
      revalidate();
      repaint();
    }
  
    public void clearData() {
      for (MButton button : this.stagedData.keySet()) {
        thisPanel.remove(button);
      }
      this.stagedData.clear();
      revalidate();
      repaint();
    }
  
    public boolean isEmpty() {
      return stagedData.isEmpty();
    }
  
    public ArrayList<Object> getAllData() {
      ArrayList<Object> dataList = new ArrayList<>();
      for (Object obj : stagedData.values()) {
        dataList.add(obj);
      }
      return dataList;
    }
  
    public void addActionListener(ActionListener al) {
      allist.add(al);
    }
  
    public JScrollPane getScrollPaneComponent() {
      return this.scrollPane;
    }
  
    @Override
    public void actionPerformed(ActionEvent e) {
      if (stagedData.isEmpty()) {
        thisPanel.removeAll();
      }
      MButton key = (MButton) e.getSource();
      Object data = stagedData.get(key);
      if (data != null) {
        stagedData.remove(key);
        thisPanel.remove(key);
      }
      for (ActionListener al : allist) {
        al.actionPerformed(e);
      }
      revalidate();
      repaint();
    }
  
    public static MButton createButton(Icon icon) {
      MButton button = new MButton();
      button.setBorderWidth(3);
      if (icon != null) {
        int size = 100 - button.getBorderWidth() * 2;
        button.setIcon(
          IconManager.getRoundedIcon(
            IconManager.resizeByAspectRatio(icon, size, size),
            16
          )
        );
      }
      button.setBorderRadius(16);
      button.setFont(FontManager.getFont("NunitoBold", 14f));
      button.setPreferredSize(new Dimension(150, 80));
      button.setMaximumSize(new Dimension(150, 80));
      button.setMaximumSize(new Dimension(150, 80));
      return button;
    }
  
    private void setup_button(MButton button) {
      button.addActionListener(this);
      button.setBackground(colors.m_bg_dim);
      button.setForeground(colors.m_fg_0);
      button.setHoverBorderColor(colors.m_red);
      button.setBorderColor(colors.m_bg_5);
    }
  
    private JScrollPane scrollPane;
    private ArrayList<ActionListener> allist = new ArrayList<>();
    private JPanel thisPanel = new JPanel();
    private ColorScheme colors;
    private HashMap<MButton, Object> stagedData = new HashMap<>();
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private JPanel thisPanel = new JPanel();
  private JLabel lblExportProduct = new JLabel("Export products to a CSV file");
  private MComboBox<CategoryRecord> cbCategory;
  private MComboBox<String> cbProduct;
  private MButton btnSubmit, btnDelete;
  private ColorScheme colors;
  private optionsSelectedPanel OptionsCategorySelectedPanel;
  private optionsSelectedPanel OptionsProductSelectedPanel;
}
