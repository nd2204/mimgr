package dev.shader;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import dev.shader.ShaderTypes.vec2;

public class ShaderInputs {
  public ShaderInputs(JPanel viewport, int width, int height) {
    this.startTime = System.currentTimeMillis();

    if (width <= 0 || height <= 0) {
      width = viewport.getPreferredSize().width;
      height = viewport.getPreferredSize().height;
    }

    this.viewport = viewport;
    this.viewport.addComponentListener(new ShaderResolutionListener());
    // this.viewport.addMouseMotionListener(new ShaderMouseListener());
    this.iTime = 0;
    // this.iMouse = new vec2(0);
    this.iFrame = 0;
    this.iResolution = new vec2(width, height);
    this.iTimeDelta = 1.0f / 60.0f;
    this.iFrameRate = 1.0f / iTimeDelta;
    this.buffers = new BufferedImage[2];
    this.buffers[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    this.buffers[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  private class ShaderResolutionListener implements ComponentListener {
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void componentResized(ComponentEvent e) {
      UpdateResolution();
    }
  }

  private class ShaderMouseListener implements MouseMotionListener {
    @Override
    public void mouseMoved(MouseEvent e) {
      // Update iMouse
      iMouse.x = e.getPoint().x;
      iMouse.y = e.getPoint().y;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      mouseMoved(e);
    }
  }

  public void UpdateResolution() {
    int newWidth = viewport.getWidth();
    int newHeight = viewport.getHeight();

    // Only update if the size has actually changed
    if (newWidth > 0 && newHeight > 0 && (newWidth != iResolution.x || newHeight != iResolution.y)) {
      iResolution.x = newWidth;
      iResolution.y = newHeight;

      for (int i = 0; i < buffers.length; i++) {
        if (buffers[i] != null) buffers[i].flush();
        buffers[i] = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
      }
      System.gc();
    }
  }

  public void tick() {
    iTime = (System.currentTimeMillis() - startTime) * 0.001f;
    iFrame++;
//     System.out.println(iFrame + " " + iMouse.x + " " + iMouse.y);
//     System.out.println(iTime);
  }

  public JPanel viewport;
  public float iTimeDelta;
  public float iFrameRate;
  public float iFrame;
  public volatile vec2 iResolution;
  public volatile float iTime;
  public volatile vec2 iMouse;
  public volatile BufferedImage[] buffers;

  public final long startTime;
}
