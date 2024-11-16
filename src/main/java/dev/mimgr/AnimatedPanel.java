package dev.mimgr;

import javax.swing.*;

import dev.shader.ShaderInputs;
import dev.shader.BuiltinShaders.*;
import dev.shader.ShaderFunctions.IShaderEntry;
import dev.shader.ShaderTypes.vec2;
import dev.shader.ShaderTypes.vec4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class AnimatedPanel extends JPanel {
  private ShaderInputs shaderInputs;
  private IShaderEntry entry;

  private final int targetFPS = 30; // Target frame rate
  private volatile BufferedImage currentBuffer;
  private boolean useBuffer0 = true;

  private final ExecutorService executorService;
  private final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
  private volatile boolean running = false;
  private volatile boolean paused = false; // Pause control
  private final Object pauseLock = new Object(); // Lock for pause/resume synchronization
  private static final ThreadLocal<int[]> threadLocalPixelBuffers = ThreadLocal.withInitial(() -> new int[4]);

  public AnimatedPanel(IShaderEntry entry) {
    this.setMinimumSize(new Dimension(800, 600));
    this.setPreferredSize(new Dimension(800, 600));
    this.entry = entry;
    this.executorService = Executors.newFixedThreadPool(NUM_THREADS);
    shaderInputs = new ShaderInputs(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (currentBuffer != null) {
      g.drawImage(currentBuffer, 0, 0, this);
    }
  }

  public void updateFrame() {
    BufferedImage renderingBuffer = useBuffer0 ? shaderInputs.buffers[0] : shaderInputs.buffers[1];
    if (renderingBuffer == null) return;
    WritableRaster raster = renderingBuffer.getRaster();
    int width = (int) shaderInputs.iResolution.x;
    int height = (int) shaderInputs.iResolution.y;

    // List to store futures for each task
    List<Future<?>> futures = new ArrayList<>();
    int chunkSize = height / NUM_THREADS;

    for (int i = 0; i < NUM_THREADS; i++) {
      int startY = i * chunkSize;
      int endY = (i == NUM_THREADS - 1) ? height : startY + chunkSize;

      // Submit a task for each chunk
      Future<?> future = executorService.submit(() -> processChunk(raster, startY, endY, width));
      futures.add(future);
    }
    // Wait for all tasks to complete
    for (Future<?> future : futures) {
      try {
        future.get(); // Block until task is complete
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    shaderInputs.tick();

    currentBuffer = renderingBuffer;
    useBuffer0 = !useBuffer0;
  }

  private void processChunk(final WritableRaster raster, final int startY, final int endY, final int width) {
    final vec2 fragCoord = new vec2(0, 0);
    final int[] pixelBuffer = threadLocalPixelBuffers.get(); // Buffer to store RGBA values per pixel
    for (int y = startY; y < endY; y++) {
      fragCoord.y = y;
      for (int x = 0; x < width; x++) {
        fragCoord.x = x;
        // Calculate the fragment color
        vec4 fragColor = entry.mainImage(shaderInputs, fragCoord);
        // Convert vec4 to RGB and populate pixelBuffer
        pixelBuffer[0] = (int) (fragColor.x * 255);
        pixelBuffer[1] = (int) (fragColor.y * 255);
        pixelBuffer[2] = (int) (fragColor.z * 255);
        pixelBuffer[3] = (int) (fragColor.w * 255);
        // Write the pixel to the raster
        raster.setPixel(x, y, pixelBuffer);
      }
    }
  }

  public void start() {
    running = true;
    Thread renderThread = new Thread(() -> {
      long frameTime = 1000 / targetFPS;

      while (running) {
        long startTime = System.currentTimeMillis();

        synchronized (pauseLock) {
          while (paused) {
            try {
              pauseLock.wait(); // Wait until resumed
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          }
        }

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

  public void pause() {
    paused = true;
  }

  public void resume() {
    synchronized (pauseLock) {
      paused = false;
      pauseLock.notifyAll(); // Wake up the rendering thread
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Shader Simulator");
      AnimatedPanel shaderPanel = new AnimatedPanel(new TriLatticeShader());
      frame.add(shaderPanel, BorderLayout.CENTER);
      frame.setSize(800, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      shaderPanel.start();
    });
  }
}
