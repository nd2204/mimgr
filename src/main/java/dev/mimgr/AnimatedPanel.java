package dev.mimgr;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AnimatedPanel extends JPanel {
  private float time;
  private final Dimension resolution;
  private BufferedImage image;

  public AnimatedPanel(int width, int height) {
    this.resolution = new Dimension(width, height);
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  // iTime
  public float getTime() {
    return time;
  }

  public void setTime(float time) {
    this.time = time;
  }

  // iResolution
  public Dimension getResolution() {
    return resolution;
  }

  // Get the generated image for display
  public BufferedImage getImage() {
    return image;
  }

}
