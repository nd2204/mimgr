package dev.mimgr.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MCheckBox extends JCheckBox {
  public MCheckBox() {
    super();
    Init();
  }

  public MCheckBox(String label) {
    super(label);
    Init();
  }

  public MCheckBox(MCheckBox other) {
    this.boxWidth      = other.boxWidth;
    this.borderWidth   = other.borderWidth;
    this.boxRadius     = other.boxRadius;
    this.checkColor    = other.checkColor;
    this.boxColor      = other.boxColor;
    this.boxHoverColor = other.boxHoverColor;
    this.spacing       = other.spacing;
    this.setBackground(other.getBackground());
    this.setForeground(other.getForeground());
    Init();
  }

  public void Init() {
    setOpaque(false);
    setFocusPainted(false);

    setCursor(new Cursor(Cursor.HAND_CURSOR));
    // Add a mouse listener to handle hover effect
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        repaint();
      }

      @Override
      public void mouseExited(MouseEvent e) {
        repaint();
      }
    });
  }

  public int getBoxWidth() {
    return this.boxWidth;
  }

  public void setBoxWidth(int width) {
    this.boxWidth = width;
    repaint();
  }

  public Color getBoxBackground() {
    return this.boxBackground;
  }

  public void setBoxBackground(Color color) {
    this.boxBackground = color;
    repaint();
  }

  public Color getBoxColor() {
    return this.boxColor;
  }

  public void setBoxColor(Color color) {
    this.boxColor = color;
    repaint();
  }

  public void setBoxSelectedColor(Color color) {
    this.boxSelectedColor = color;
    repaint();
  }

  public Color getCheckColor() {
    return this.checkColor;
  }

  public void setCheckColor(Color color) {
    this.checkColor = color;
    repaint();
  }

  public Color getBoxHoverColor() {
    return this.boxHoverColor;
  }

  public void setBoxHoverColor(Color color) {
    this.boxHoverColor = color;
    repaint();
  }

  public int getSpacing() {
    return this.spacing;
  }

  public void setSpacing(int spacing) {
    this.spacing = spacing;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    // Cast Graphics to Graphics2D for better control
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Determine the size of the checkbox
    int boxSize = boxWidth;
    int boxX = (getWidth() - boxSize) / 2;
    int boxY = (getHeight() - boxSize) / 2;
    int alignment = getHorizontalAlignment();
    switch (alignment) {
      case SwingConstants.CENTER:
      boxX = (getWidth() - boxSize) / 2;
      break;
      case SwingConstants.LEFT:
      boxX = 2;
      break;
      case SwingConstants.RIGHT:
      boxX = (getWidth() - boxSize - 2);
      break;
      default:
      break;
    }

    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());
    if (boxBackground != null) {
      g2d.setColor(boxBackground);
    }
    g2d.fillRoundRect(boxX + borderWidth, boxY + borderWidth, boxSize - borderWidth, boxSize - borderWidth, this.boxRadius, this.boxRadius);

    g2d.setColor(boxColor);
    if (getModel().isRollover()) {
      g2d.setColor(boxHoverColor);
    }
    if (isSelected()) {
      g2d.setColor(boxSelectedColor);
    }
    // Draw the checkbox box
    g2d.setStroke(new BasicStroke(borderWidth));
    g2d.drawRoundRect(boxX + borderWidth, boxY + borderWidth, boxSize - borderWidth, boxSize - borderWidth, this.boxRadius, this.boxRadius);

    // Draw the check mark if selected
    if (isSelected()) {
      // g2d.fillRoundRect(boxX + borderWidth, boxY + borderWidth, boxSize - borderWidth, boxSize - borderWidth, this.boxRadius, this.boxRadius);
      // g2d.drawRoundRect(boxX + borderWidth, boxY + borderWidth, boxSize - borderWidth, boxSize - borderWidth, this.boxRadius, this.boxRadius);
      g2d.setColor(checkColor);
      g2d.setStroke(new BasicStroke(borderWidth));
      g2d.drawLine(boxX + 6, boxY + boxSize / 2, boxX + boxSize / 2, boxY + boxSize - 4);
      g2d.drawLine(boxX + boxSize / 2, boxY + boxSize - 4, boxX + boxSize - 4, boxY + 6);
    }

    // Draw the label
    g2d.setColor(getForeground());
    g2d.drawString(getText(), boxX + boxSize + spacing, getHeight() / 2 + 6);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(100, 30);  // Adjust preferred size
  }

  private int boxWidth = 16;
  private int borderWidth = 2;
  private int boxRadius = 8;
  private Color checkColor = Color.GREEN;
  private Color boxSelectedColor = Color.DARK_GRAY;
  private Color boxColor = Color.LIGHT_GRAY;
  private Color boxHoverColor = Color.DARK_GRAY;
  private Color boxBackground = null;
  private int spacing = 15;
}
