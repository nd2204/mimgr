package dev.mimgr.custom;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.ColorTheme.theme;
import dev.mimgr.theme.builtin.ColorScheme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MButton extends JButton implements MouseListener {
  public MButton() {
    super();
    Init();
  }

  public MButton(Icon icon) {
    super(icon);
    Init();
  }

  public MButton(String text) {
    super(text);
    Init();
  }

  public MButton(String text, Icon icon) {
    super(text, icon);
    Init();
  }

  private void Init() {
    // Remove the default area border
    this.setBorder(null);
    super.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.setPadding(padding);
    this.setContentAreaFilled(false);
    this.addMouseListener(this);
    this.setFocusable(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();

    // Enable anti-aliasing for smooth edges
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Set the button background color
    drawBackground(g2);

    // Draw the button border
    drawBorder(g2);

    // Draw text
    drawText(g2);

    g2.dispose();

    super.paintComponent(g); // Ensure button text is rendered
  }

  private void drawBackground(Graphics2D g2) {
    if (this.clickBackgroundColor != null && getModel().isPressed()) {
      g2.setColor(this.clickBackgroundColor);
    } else if (this.hoverBackgroundColor != null && getModel().isRollover()) {
      g2.setColor(this.hoverBackgroundColor);
    } else {
      g2.setColor(getBackground());
    }
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);
  }

  private void drawBorder(Graphics2D g2) {
    if (this.clickBorderColor != null && getModel().isPressed()) {
      g2.setColor(this.clickBorderColor);
    } else if (this.hoverBorderColor != null && getModel().isRollover()) {
      g2.setColor(this.hoverBorderColor);
    } else {
      g2.setColor(this.borderColor);
    }
    g2.setStroke(new BasicStroke(this.borderWidth));
    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
  }

  private void drawText(Graphics g) {
    if (this.foregroundColor == null) {
      this.foregroundColor = this.getForeground();
    }
    if (this.clickForegroundColor != null && getModel().isPressed()) {
      this.setForeground(this.clickForegroundColor);
    } else if (this.hoverForegroundColor != null && getModel().isRollover()) {
      this.setForeground(this.hoverForegroundColor);
    } else {
      this.setForeground(this.foregroundColor);
    }
  }

  // Customization methods
  public Color getBorderColor() {
    return this.borderColor;
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
    repaint();
  }

  public Color getClickBorderColor() {
    return this.clickBorderColor;
  }

  public void setClickBorderColor(Color color) {
    this.clickBorderColor = color;
    repaint();
  }

  public Color getHoverBorderColor() {
    return this.hoverBorderColor;
  }

  public void setHoverBorderColor(Color color) {
    this.hoverBorderColor = color;
    repaint();
  }

  public Color getClickBackgroundColor() {
    return this.clickBackgroundColor;
  }

  public void setClickBackgroundColor(Color color) {
    this.clickBackgroundColor = color;
    repaint();
  }

  public Color getHoverBackgroundColor() {
    return this.hoverBackgroundColor;
  }

  public void setHoverBackgroundColor(Color color) {
    this.hoverBackgroundColor = color;
    repaint();
  }

  public void setDefaultForeground(Color color) {
    this.foregroundColor = color;
    repaint();
  }

  public Color getClickForegroundColor() {
    return this.clickForegroundColor;
  }

  public void setClickForegroundColor(Color color) {
    this.clickForegroundColor = color;
    repaint();
  }

  public Color getHoverForegroundColor() {
    return this.hoverForegroundColor;
  }

  public void setHoverForegroundColor(Color color) {
    this.hoverForegroundColor = color;
    repaint();
  }

  public int getBorderRadius() {
    return this.borderRadius;
  }

  public void setBorderRadius(int radius) {
    this.borderRadius = radius;
    repaint();
  }

  public int getBorderWidth() {
    return this.borderWidth;
  }

  public void setBorderWidth(int width) {
    padding.set(padding.top + borderWidth, padding.left + borderWidth, padding.bottom + borderWidth, padding.right + borderWidth);
    this.setBorder(new EmptyBorder(padding));

    this.borderWidth = width;
    repaint();
  }

  public Insets getPadding() {
    return this.padding;
  }

  public void setPadding(Insets padding) {
    if (prefixIcon != null) {
      padding.left += prefixIcon.getIconWidth() + (int) padding.left / 2;
    }
    if (postfixIcon != null) {
      padding.right += postfixIcon.getIconWidth() + (int) padding.right / 2;
    }
    this.padding.set(padding.top + borderWidth, padding.left + borderWidth, padding.bottom + borderWidth, padding.right + borderWidth);
    this.setBorder(new EmptyBorder(padding));
    repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    repaint();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    repaint();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    repaint();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    repaint();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    repaint();
  }

  private ColorScheme colors         = ColorTheme.get_colorscheme(theme.THEME_LIGHT_DEFAULT);

  private Color borderColor          = colors.m_bg_5;
  private Color clickBorderColor     = null;
  private Color hoverBorderColor     = null;

  private Color clickBackgroundColor = null;
  private Color hoverBackgroundColor = null;

  private Color foregroundColor      = null;
  private Color clickForegroundColor = null;
  private Color hoverForegroundColor = null;

  private int borderRadius           = 15;
  private int borderWidth            = 1;

  private Insets padding             = new Insets(10, 20, 10, 20);

  private final int iconPadding      = 20;
  private Icon prefixIcon            = null;
  private Icon postfixIcon           = null;

  public static final int ICON_PREFIX  = 1;
  public static final int ICON_POSTFIX = 2;
}
