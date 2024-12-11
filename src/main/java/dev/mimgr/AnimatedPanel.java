package dev.mimgr;

import javax.swing.*;

import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.shader.ShaderInputs;
import dev.shader.BuiltinShaders.*;
import dev.shader.ShaderFunctions.IShaderEntry; import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec4;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.*;

public class AnimatedPanel extends JPanel {
  private ShaderInputs shaderInputs;
  private IShaderEntry entry;

  private final int targetFPS = 100; // Target frame rate
  private volatile BufferedImage currentBuffer;
  private boolean useBuffer0 = true;

  private final ExecutorService executorService;
  private final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); private volatile boolean running = false;
  private ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();

  // Variables to track panel size changes
  private int panelWidth;
  private int panelHeight;

  private Color borderColor;
  private int borderRadius = 0;
  private int borderWidth = 0;

  public AnimatedPanel(IShaderEntry entry, int width, int height) {
    this.setMinimumSize(new Dimension(854, 480));
    this.setPreferredSize(new Dimension(854, 480));
    this.entry = entry;
    this.executorService = Executors.newFixedThreadPool(NUM_THREADS);

    shaderInputs = new ShaderInputs(this, width, height);
    shaderInputs.UpdateResolution();
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    super.paintComponent(g);
    g.setColor(this.getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    if (currentBuffer != null) {
      g.drawImage(currentBuffer, 0, 0, this);
    }
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if (borderColor != null) {
      g2d.setColor(borderColor);
    }
    g2d.setStroke(new BasicStroke(borderWidth));
    g2d.drawRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth, getHeight() - borderWidth, this.borderRadius, this.borderRadius);
  }

  public void updateFrame() {
    // Check if the buffers need to be updated (in case of resizing)
    if (shaderInputs.buffers[0].getWidth() != panelWidth || shaderInputs.buffers[0].getHeight() != panelHeight) {
      shaderInputs.UpdateResolution();
    }

    BufferedImage renderingBuffer = useBuffer0 ? shaderInputs.buffers[0] : shaderInputs.buffers[1];
    int width = (int) shaderInputs.iResolution.x;
    int height = (int) shaderInputs.iResolution.y;

    // Access pixel data directly
    DataBufferInt dataBuffer = (DataBufferInt) renderingBuffer.getRaster().getDataBuffer();
    int[] pixels = dataBuffer.getData();

    int chunkSize = height / NUM_THREADS;
    CountDownLatch latch = new CountDownLatch(NUM_THREADS);

    for (int i = 0; i < NUM_THREADS; i++) {
      int startY = i * chunkSize;
      int endY = (i == NUM_THREADS - 1) ? height : startY + chunkSize;

      executorService.submit(() -> {
        processChunk(pixels, startY, endY, width);
        latch.countDown();
      });
    }

    // Wait for all tasks to complete
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    shaderInputs.iTime = (System.currentTimeMillis() - shaderInputs.startTime) * 0.001f;
    shaderInputs.iFrame++;

    currentBuffer = IconManager.getRoundedImage(renderingBuffer, borderRadius);
    useBuffer0 = !useBuffer0;
  }

  private void processChunk(final int[] pixels, final int startY, final int endY, final int width) {
    final vec2 fragCoord = new vec2(0, 0);
    final vec4 fragColor = new vec4(0.0f); 

    for (int y = startY; y < endY; y++) {
      fragCoord.y = y;
      int offset = y * width;
      for (int x = 0; x < width; x++) {
        fragCoord.x = x;
        // Calculate the fragment color
        entry.mainImage(shaderInputs, fragColor, fragCoord);
        // Convert vec4 to RGB and populate pixelBuffer
        int a = (int) (fragColor.w * 255) & 0xFF;
        int r = (int) (fragColor.x * 255) & 0xFF;
        int g = (int) (fragColor.y * 255) & 0xFF;
        int b = (int) (fragColor.z * 255) & 0xFF;
        pixels[offset + x] = (a << 24) | (r << 16) | (g << 8) | b;
      }
    }
  }

  public void start() {
    running = true;
    Thread renderThread = new Thread(() -> {
      long frameTime = 1000 / targetFPS;

      while (running) {
        long startTime = System.currentTimeMillis();

        updateFrame();
        // Schedule repaint safely
        SwingUtilities.invokeLater(this::repaint);

        long elapsedTime = System.currentTimeMillis() - startTime;
        long sleepTime = frameTime - elapsedTime;

        if (sleepTime > 0) {
          try {
            Thread.sleep(sleepTime);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }
    });

    renderThread.setDaemon(true);
    renderThread.start();
  }

  public void stop() {
    running = false;
    executorService.shutdownNow();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Shader Simulator");
      AnimatedPanel shaderPanel = new AnimatedPanel(new TriLatticeShader(), Entry.m_width, Entry.m_height);
      frame.add(shaderPanel, BorderLayout.CENTER);
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      shaderPanel.start();
    });
  }

  public void setBorderRadius(int radius) {
    this.borderRadius = radius;
    repaint();
  }

  public void setBorderWidth(int width) {
    this.borderWidth = width;
    repaint();
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
    repaint();
  }

}
