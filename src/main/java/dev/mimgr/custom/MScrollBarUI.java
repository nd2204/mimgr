package dev.mimgr.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class MScrollBarUI extends BasicScrollBarUI {
  @Override
  protected void configureScrollBarColors() {
    this.thumbColor = Color.BLACK; // Thumb color
    this.trackColor = null; // Track color
    this.scrollbar.setForeground(Color.black);
  }

  @Override
  protected void paintThumb(Graphics g, JComponent jc, Rectangle rect) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    int x = rect.x;
    int y = rect.y;
    int width = rect.width;
    int height = rect.height;
    g2.setColor(scrollbar.getForeground());
    g2.fillRoundRect(x, y, width, height, 6, 6);
  }

  @Override
  protected void paintTrack(Graphics g, JComponent jc, Rectangle rect) {
  }

  @Override
  protected Dimension getMaximumThumbSize() {
    if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
      return new Dimension(0, THUMB_SIZE);
    } else {
      return new Dimension(THUMB_SIZE, 0);
    }
  }

  @Override
  protected Dimension getMinimumThumbSize() {
    if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
      return new Dimension(0, THUMB_SIZE);
    } else {
      return new Dimension(THUMB_SIZE, 0);
    }
  }

  @Override
  protected JButton createIncreaseButton(int i) {
    return new ScrollBarButton();
  }

  @Override
  protected JButton createDecreaseButton(int i) {
    return new ScrollBarButton();
  }

  private class ScrollBarButton extends JButton {
    public ScrollBarButton() {
      setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public void paint(Graphics grphcs) {
    }
  }

  private final int THUMB_SIZE = 80;
}

