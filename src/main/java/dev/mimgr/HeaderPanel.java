package dev.mimgr;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.builtin.ColorScheme;

public class HeaderPanel extends JPanel {
  HeaderPanel(ColorScheme colors) {
    this.colors = colors;
    this.setBackground(colors.m_bg_0);
    this.setLayout(new GridBagLayout());

    Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);

    btnToggleSidebar = new MButton(IconManager.getIcon("tripple_dash.png", 20, 18, colors.m_grey_0));
    btnToggleSidebar.setBackground(null);
    btnToggleSidebar.setBorderWidth(0);
    btnToggleSidebar.setBorderColor(null);
    btnToggleSidebar.setOpaque(false);
    btnToggleSidebar.setBorderPainted(false);
    btnToggleSidebar.setAlignmentX(SwingConstants.LEFT);

    tfSearch = new MTextField(30);
    tfSearch.setInputForeground(colors.m_fg_0);
    tfSearch.setBackground(colors.m_bg_dim);
    tfSearch.setFont(nunito_bold_14);
    tfSearch.setBorderColor(colors.m_bg_2);
    tfSearch.setBorderWidth(2);
    tfSearch.setPlaceholderForeground(colors.m_bg_2);
    tfSearch.setCaretColor(colors.m_grey_0);
    tfSearch.setPlaceholder("Search");
    tfSearch.setIcon(IconManager.getIcon("search.png", 16, 16, colors.m_bg_2), MTextField.ICON_PREFIX);

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(10, 10, 10, 20);
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.fill = GridBagConstraints.BOTH;
    this.add(btnToggleSidebar, c);

    c.insets = new Insets(0, 20, 0, 40);
    c.gridx = 1;
    c.weightx = 0.5;
    c.weighty = 1.0;
    this.add(Box.createHorizontalGlue(), c);

    c.insets = new Insets(10, 20, 10, 40);
    c.gridx = 2;
    c.weightx = 1.0;
    c.weighty = 1.0;
    this.add(tfSearch, c);

    c.insets = new Insets(0, 20, 0, 40);
    c.gridx = 3;
    c.weightx = 0.5;
    c.weighty = 1.0;
    this.add(Box.createHorizontalGlue(), c);

    c.gridx = 4;
    c.weightx = 0.0;
    c.weighty = 1.0;
    c.ipadx = 25;
    c.ipady = 25;
    c.insets = new Insets(10, 10, 10, 25);
    this.add(new AvatarPanel("TG", colors.m_aqua, colors.m_fg_1), c);
  }

  private class AvatarPanel extends JPanel {
    private String text;
    private Color circleColor;
    private Color textColor;

    public AvatarPanel(String text, Color circleColor, Color textColor) {
      this.setPreferredSize(new Dimension(200, 200));
      this.text = text;
      this.circleColor = circleColor;
      this.textColor = textColor;
      setOpaque(false); // Makes the panel background transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      // Cast to Graphics2D for advanced drawing features
      Graphics2D g2d = (Graphics2D) g.create();

      // Enable anti-aliasing for smoother drawing
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Calculate the diameter of the circle
      int diameter = Math.min(getWidth(), getHeight());
      int x = (getWidth() - diameter) / 2;
      int y = (getHeight() - diameter) / 2;

      // Draw the circular background
      g2d.setColor(circleColor);
      g2d.fillOval(x, y, diameter, diameter);

      // Set font and color for the text
      g2d.setColor(textColor);
      Font font = g2d.getFont().deriveFont(Font.BOLD, diameter / 3f); // Adjust font size based on diameter
      g2d.setFont(font);

      // Calculate position to center the text
      FontMetrics metrics = g2d.getFontMetrics(font);
      int textX = getWidth() / 2 - metrics.stringWidth(text) / 2;
      int textY = getHeight() / 2 + metrics.getAscent() / 2 - metrics.getDescent() / 2 - 1;

      // Draw the text in the center
      g2d.drawString(text, textX, textY);

      // Dispose graphics to avoid memory leaks
      g2d.dispose();
    }}

  public MButton getToggleSidebarComponent() {
    return this.btnToggleSidebar;
  }

  private ColorScheme colors;
  MButton btnAccount;
  MTextField tfSearch;
  MButton btnToggleSidebar;
}
