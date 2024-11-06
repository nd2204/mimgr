package dev.mimgr.custom;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import dev.mimgr.FontManager;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MComboBox<T> extends JComboBox<T> implements FocusListener {

  public MComboBox(T[] items, ColorScheme colors) {
    super(items);
    this.colors = colors;
    setRenderer(new MComboBoxRenderer());
    setUI(this.comboBoxUI);
    setEditable(true);
    this.comboBoxTextField = (JTextField) this.getEditor().getEditorComponent();
    comboBoxTextField.setBackground(colors.m_bg_dim);
    comboBoxTextField.setForeground(colors.m_grey_2);
    comboBoxTextField.setCaretColor(colors.m_grey_2);
    comboBoxTextField.setFont(nunito_bold_14);
    comboBoxTextField.setBorder(BorderFactory.createCompoundBorder(
      new LineBorder(colors.m_bg_5, 1),
      new EmptyBorder(0, 10, 0, 10)
    ));
    comboBoxTextField.addFocusListener(this);
  }

  public class MComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
      return new ArrowButton();
    }

    @Override
    public void installUI(JComponent c) {
      super.installUI(c);
      comboBox.setBackground(colors.m_bg_0);
    }

    @Override
    protected ComboPopup createPopup() {
      BasicComboPopup pop = new BasicComboPopup(comboBox) {
        @Override
        protected JScrollPane createScroller() {
          JScrollPane scroll = new JScrollPane(
            list,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
          );
          // list.setFixedCellHeight(50);
          scroll.setBackground(colors.m_bg_dim);
          scroll.setBorder(new LineBorder(colors.m_bg_5, 1));

          MScrollBar vsb = new MScrollBar(colors);
          vsb.setPreferredSize(new Dimension(5, 5));
          vsb.setForeground(colors.m_grey_0);
          vsb.setUnitIncrement(30);
          scroll.setVerticalScrollBar(vsb);

          MScrollBar hsb = new MScrollBar(colors);
          hsb.setForeground(colors.m_grey_0);
          hsb.setUnitIncrement(30);
          hsb.setOrientation(MScrollBar.HORIZONTAL);
          scroll.setHorizontalScrollBar(hsb);

          return scroll;
        }
      };
      pop.setBorder(new LineBorder(colors.m_bg_5, 1));
      pop.setBackground(colors.m_bg_dim);
      pop.setOpaque(true);
      return pop;
    }
  }

  public class MComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
    public MComboBoxRenderer() {
      super();
      setOpaque(true);
      setFont(nunito_bold_14);
    }

    @Override
    public Component getListCellRendererComponent(
      JList<?> list,
      Object value,
      int index,
      boolean isSelected,
      boolean cellHasFocus
    ) {
      setBorder(new EmptyBorder(0, 10, 0, 10));
      if (index >= 0) {
        this.setBorder(new EmptyBorder(10, 8, 10, 8));
      } else {
        this.setBorder(null);
      }
      if (isSelected) {
        setBackground(colors.m_bg_2);
        setForeground(colors.m_grey_2);
      } else {
        setBackground(colors.m_bg_dim);
        setForeground(colors.m_grey_0);
      }
      setText(value != null ? value.toString() : ""); // Display item text
      return this;
    }
  }

  private class ArrowButton extends MButton {
    public ArrowButton() {
      setContentAreaFilled(false);
      setBackground(colors.m_bg_0);
      setForeground(colors.m_grey_0);
      setBorderRadius(0);
      setBorderWidth(1);
      setBorder(BorderFactory.createEmptyBorder());
      setBorderColor(colors.m_bg_5);
    }

    @Override
    public void paint(Graphics grphcs) {
      super.paint(grphcs);
      Graphics2D g2 = (Graphics2D) grphcs;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      int width = getWidth();
      int height = getHeight();
      int size = 10;
      int x = (width - size) / 2;
      int y = (height - size) / 2;
      int px[] = {x, x + size, x + size / 2};
      int py[] = {y, y, y + size};
      g2.setColor(getForeground());
      g2.fillPolygon(px, py, px.length);
      g2.dispose();
    }
  }

  @Override
  public void focusGained(FocusEvent e) {
    comboBoxTextField.setBorder(BorderFactory.createCompoundBorder(
      new LineBorder(colors.m_bg_3, 1),
      new EmptyBorder(0, 10, 0, 10)
    ));
  }

  @Override
  public void focusLost(FocusEvent e) {
    comboBoxTextField.setBorder(BorderFactory.createCompoundBorder(
      new LineBorder(colors.m_bg_5, 1),
      new EmptyBorder(0, 10, 0, 10)
    ));
  }

  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private ColorScheme colors = ColorTheme.get_currentScheme();
  private ComboBoxUI comboBoxUI = new MComboBoxUI();
  private JTextField comboBoxTextField;
}
