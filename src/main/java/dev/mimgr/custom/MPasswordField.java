package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.ColorTheme.theme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MPasswordField extends JPasswordField implements FocusListener { 
  // Constructor with default settings
  public MPasswordField(int columns) {
    super(columns);
    Init();
  }

  public MPasswordField() {
    super();
    Init();
  }

  private void Init() {
    this.setPadding(padding);
    this.setBorderColor(Color.BLACK);
    this.setFocusBorderColor(Color.BLACK);
    this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    this.addFocusListener(this);
    this.setEchoChar('\0');
    this.setText(this.placeholder);
    setOpaque(false); // Make the background transparent for custom painting
  }

  // Override the paintComponent method to customize the drawing
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();

    // Enable anti-aliasing for smooth edges
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw background
    g2d.setColor(getBackground());
    g2d.fillRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius, borderRadius);

    // Draw the text
    super.paintComponent(g2d);

    // Draw Icon
    if (prefixIcon != null) {
      Image image = ((ImageIcon) prefixIcon).getImage();
      g2d.drawImage(image, this.iconPadding, padding.top, null);
    }
    if (postfixIcon != null) {
      Image image = ((ImageIcon) postfixIcon).getImage();
      g2d.drawImage(
        image,
        getWidth() - image.getWidth(this) - this.iconPadding,
        padding.top,
        null
      );
    }

    g2d.setColor(this.borderColor);
    if (isFocusOwner()) {
      g2d.setColor(this.focusBorderColor);
    }
    g2d.setStroke(new BasicStroke(this.borderWidth));
    g2d.drawRoundRect(borderWidth, borderWidth, getWidth() - (borderWidth * 2), getHeight() - (borderWidth * 2), borderRadius, borderRadius);

    g2d.dispose();
  }

  // Customization methods
  public Color getBorderColor() {
    return this.borderColor;
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
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

  public Color getFocusBorderColor() {
    return this.focusBorderColor;
  }

  public void setFocusBorderColor(Color color) {
    this.focusBorderColor = color;
    repaint();
  }

  public String getPlaceholder() {
    return this.placeholder;
  }

  public void setPlaceholder(String text) {
    this.placeholder = text;
    this.setText(placeholder);
    repaint();
  }

  public Color getPlaceholderForeground() {
    return this.placeholderForeground;
  }

  public void setPlaceholderForeground(Color color) {
    this.placeholderForeground = color;
    this.setForeground(color);
    repaint();
  }

  public Icon getIcon(int direction) {
    switch(direction) {
      case ICON_PREFIX:
        return this.prefixIcon;
      case ICON_POSTFIX:
        return this.postfixIcon;
      default:
        return null;
    }
  }

  public void setIcon(Icon icon, int direction) {
    switch(direction) {
      case ICON_PREFIX:
        this.prefixIcon = icon;
        break;
      case ICON_POSTFIX:
        this.postfixIcon = icon;
        break;
    }
    this.setPadding(this.padding);
    repaint();
  }

  public void setShowingPassword(boolean isShowingPassword) {
    this.isShowingPassword = isShowingPassword;
  }

  public String getTextString() {
    String password = String.valueOf(this.getPassword());
    if (password.equals(this.placeholder)) {
      return "";
    }
    return password;
  }

  public Color getInputForeground() {
    return this.inputForeground;
  }

  public void setInputForeground(Color color) {
    this.setCaretColor(color);
    this.inputForeground = color;
    repaint();
  }


  @Override
  public void focusLost(FocusEvent e) {
    if (this.getPassword().length == 0) {
      this.setForeground(this.placeholderForeground);
      this.setEchoChar('\0');
      this.setText(this.placeholder);
    }
    repaint();
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (String.valueOf(this.getPassword()).equals(this.placeholder)) {
      this.setForeground(this.inputForeground);
      if (!this.isShowingPassword) {
        this.setEchoChar('*');
      } else {
        this.setEchoChar('\0');
      }
      this.setText("");
    }
    repaint();
  }

  private String placeholder;
  private boolean isShowingPassword;
  private Color placeholderForeground;
  private Color inputForeground;
  private Color borderColor;
  private Color focusBorderColor;
  private int borderRadius = 15;
  private int borderWidth = 1;
  private Insets padding = new Insets(10, 20, 10, 20);
  private Icon prefixIcon;
  private Icon postfixIcon;
  private final int iconPadding = 20;
  public static final int ICON_PREFIX = 1;
  public static final int ICON_POSTFIX = 2;
}
