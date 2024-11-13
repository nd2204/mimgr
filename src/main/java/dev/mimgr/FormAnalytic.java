package dev.mimgr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.component.TotalOrderPanel;
import dev.mimgr.component.TotalSalePanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormAnalytic extends JPanel {
  public FormAnalytic() {
    super();
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_bg_dim);
    this.setLayout(new GridBagLayout());

    InitComponents();

    GridBagConstraints c = new GridBagConstraints();
    int padding = 25;
    c.insets = new Insets(25, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(lblAnalytic, c);

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.HORIZONTAL;

    c.insets = new Insets(0, padding, 25, padding);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 0.5;
    // c.weighty = 1.0;
    c.gridwidth = 1;
    this.add(new TotalSalePanel(), c);

    c.insets = new Insets(0, 0, 25, padding);
    c.gridx = 1;
    this.add(new TotalOrderPanel(), c);
  }

  private void InitComponents() {
    this.lblAnalytic = new JLabel("Analytics Dashboard");
    this.lblAnalytic.setFont(nunito_bold_20);
    this.lblAnalytic.setForeground(colors.m_fg_0);
  }

  private Font nunito_extrabold_14 = FontManager.getFont("NunitoExtraBold", 14f);
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_16 = FontManager.getFont("NunitoBold", 16f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private ColorScheme colors;
  private JLabel lblAnalytic;
}
