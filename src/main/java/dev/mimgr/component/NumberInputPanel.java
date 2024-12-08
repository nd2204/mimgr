package dev.mimgr.component;

import dev.mimgr.custom.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Graphics; import java.awt.Graphics2D;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.FontManager;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class NumberInputPanel extends JPanel {
  public NumberInputPanel() {
    this.setLayout(new GridBagLayout());

    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.tfNumber = new MTextField(1);
    this.tfNumber.setPadding(new Insets(5, 0, 5, 0));
    this.tfNumber.setForeground(colors.m_fg_0);
    this.tfNumber.setBackground(colors.m_bg_dim);
    this.tfNumber.setBorderColor(null);
    this.tfNumber.setBorderWidth(0);
    this.tfNumber.setFont(FontManager.getFont("NunitoBold", 14.0f));
    setValue(1);

    Consumer<MButton> setupButton = btn -> {
      btn.setBackground(null);
      btn.setBorderWidth(0);
      btn.setBorderColor(null);
      btn.setOpaque(false);
      btn.setBorderPainted(false);
      btn.setPreferredSize(new Dimension(20, 20));
      btn.setForeground(colors.m_bg_5);
      btn.setFont(FontManager.getFont("NunitoBold", 14.0f));
    };

    this.plus = new MButton("+");
    setupButton.accept(NumberInputPanel.this.plus);
    this.plus.setPadding(new Insets(0, 0, 0, 5));

    this.minus = new MButton("-");
    setupButton.accept(NumberInputPanel.this.minus);
    this.minus.setPadding(new Insets(0, 5, 0, 0));

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(30, 10, 30, 10);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.weighty = 1.0;
    this.add(new WrapperPanel(), c);
  }

  private class WrapperPanel extends RoundedPanel {
    public WrapperPanel() {
      super();
      this.setBackground(colors.m_bg_dim);
      this.setBorderColor(colors.m_bg_5);
      this.setPreferredSize(new Dimension(110, this.getPreferredSize().height));
      this.setMaximumSize(new Dimension(110, this.getPreferredSize().height));
      this.setMinimumSize(new Dimension(110, this.getPreferredSize().height));
      this.setLayout(new GridBagLayout());

      JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
      sep1.setForeground(colors.m_bg_5);
      sep1.setBackground(null);

      JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
      sep2.setForeground(colors.m_bg_5);
      sep2.setBackground(null);

      GridBagConstraints c = new GridBagConstraints();

      // ---------
      c.gridx = 0;
      c.gridy = 0;
      // ---------

      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1.0;
      c.weighty = 1.0;
      c.insets = new Insets(1, 1, 1, 0);
      this.add(minus, c);
      c.gridx++;

      c.fill = GridBagConstraints.VERTICAL;
      c.insets = new Insets(1, 0, 1, 4);
      c.weightx = 0.0;
      c.weighty = 1.0;
      c.anchor = GridBagConstraints.CENTER;
      this.add(sep1, c);
      c.gridx++;

      c.fill = GridBagConstraints.BOTH;
      c.insets = new Insets(1, 0, 1, 1);
      c.weightx = 1.0;
      c.weighty = 1.0;
      this.add(tfNumber, c);
      c.gridx++;

      c.fill = GridBagConstraints.VERTICAL;
      c.insets = new Insets(1, 4, 1, 0);
      c.weightx = 0.0;
      c.weighty = 1.0;
      c.anchor = GridBagConstraints.CENTER;
      this.add(sep2, c);
      c.gridx++;

      c.fill = GridBagConstraints.BOTH;
      c.insets = new Insets(1, 0, 1, 1);
      c.weightx = 1.0;
      c.weighty = 1.0;
      this.add(plus, c);
      c.gridx++;
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(colors.m_bg_3);
    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

    g2.dispose();
  }

  public int getValue() {
    try {
      return Integer.parseInt(tfNumber.getTextString());
    } catch (NumberFormatException ex) {
      System.err.println(ex);
      return 1;
    }
  }

  public void setValue(int v) {
    if (v < 1 || v > 99) return;
    tfNumber.setText(String.valueOf(v));
  }

  public MTextField tfNumber;
  public MButton plus;
  public MButton minus;

  private ColorScheme colors;
}
