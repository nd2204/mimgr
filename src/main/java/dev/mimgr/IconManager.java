package dev.mimgr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class IconManager {
  // Cache to store loaded icons
  private static final Map<String, Icon> iconCache = new HashMap<>();

  // Base path for icon resources (this could be a folder in your project)
  private static final String ICONS_PATH = "/icons/";

  /**
     * Retrieve an icon by filename. If the icon is already loaded,
     * it will return the cached version.
     * @param iconFileName The filename of the icon (e.g., "save.png", "delete.png")
     * @param iconFileName The name of the icon
     * @param width The width to scale to
     * @param height The height to scale to
     * @param color The color of the icon
     * @return The loaded icon, or null if the icon cannot be found
     */
  public static Icon getIcon(String iconFileName, int width, int height, Color color) {
    // Check if the icon is already cached
    Icon icon = null;
    if (iconCache.containsKey(iconFileName)) {
      icon = iconCache.get(iconFileName);
    } else {
      // Load the icon from resources
      String iconPath = ICONS_PATH + iconFileName;  // Example: "/icons/save.png"
      URL iconURL = IconManager.class.getResource(iconPath);

      if (iconURL == null) {
        System.err.println("Icon not found: " + iconPath);
        return null;
      }

      icon = new ImageIcon(iconURL);
      iconCache.put(iconFileName, icon);  // Cache the icon for future use
    }

    if (icon != null) {
      if (width >= 0 && height >= 0) {
        icon = changeIconSize(icon, width, height);
      }

      if (color != null) {
        icon = changeIconColor(icon, color);
      }
    }

    return icon;
  }

  public static Icon getIcon(String iconName, int width, int height) {
    return getIcon(iconName, width, height, null);
  }

  public static Icon getIcon(String iconName) {
    return getIcon(iconName, -1, -1, null);
  }

  public static Icon getIcon(String iconName, Color color) {
    return getIcon(iconName, -1, -1, color);
  }

  public static Icon changeIconSize(Icon icon, int width, int height) {
    ImageIcon imageIcon = (ImageIcon) icon;
    Image originalImage = imageIcon.getImage();
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = bi.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(originalImage, 0, 0, width, height, null);
    g2.dispose();
    return new ImageIcon(bi);
  }

  public static Icon loadIcon(File file) {
    try {
      // Load the image from the file path
      var image = ImageIO.read(file);
      return new ImageIcon(image);
    } catch (IOException e) {
      e.printStackTrace();
      return null; // Return null if the image couldn't be loaded
    }
  }

  /**
   * ONLY SUPPORT .png IMAGES
   * change the colors of all pixel's RGB chanel with retained Alpha chanel
   * @param icon the icon object
   * @param color the Color object of the icon
   * @return the modified icon
   */
  public static Icon changeIconColor(Icon icon, Color color) {
    BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bi.createGraphics();
    icon.paintIcon(null, g2d, 0, 0);
    g2d.dispose();

    // Extract the RGB components of the color to be applied
    int newRed = color.getRed();
    int newGreen = color.getGreen();
    int newBlue = color.getBlue();

    // Loop through each pixel and change the color while preserving alpha
    for (int y = 0; y < bi.getHeight(); y++) {
      for (int x = 0; x < bi.getWidth(); x++) {
        int pixel = bi.getRGB(x, y);
        int alpha = (pixel >> 24) & 0xff; // Extract the alpha component

        if (alpha != 0) { // If not fully transparent
          // Apply the new color while preserving the original alpha channel
          int newPixel = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
          bi.setRGB(x, y, newPixel);
        }
      }
    }
    return new ImageIcon(bi);
  }

  public static Icon resizeByAspectRatio(Icon icon, int targetWidth, int targetHeight) {
    BufferedImage originalImage = iconToBufferedImage(icon);
    int originalWidth = originalImage.getWidth();
    int originalHeight = originalImage.getHeight();

    float aspectRatio = (float) originalWidth / originalHeight;

    if (originalWidth > originalHeight) {
      targetHeight = -1;
    } else {
      targetWidth = -1;
    }

    // Calculate new dimensions while preserving aspect ratio
    if (targetWidth > 0 && targetHeight == -1) {
      // If targetWidth is specified, calculate targetHeight based on aspect ratio
      targetHeight = (int) (targetWidth / aspectRatio);
    } else if (targetHeight > 0 && targetWidth == -1) {
      // If targetHeight is specified, calculate targetWidth based on aspect ratio
      targetWidth = (int) (targetHeight * aspectRatio);
    } else if (targetWidth == -1 && targetHeight == -1) {
      throw new IllegalArgumentException("Either targetWidth or targetHeight must be specified.");
    }

    // Create a new BufferedImage with the calculated dimensions
    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());

    // Draw the original image into the resized image
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
    g.dispose();

    return new ImageIcon(resizedImage);
  }

  private static BufferedImage iconToBufferedImage(Icon icon) {
    // Create a BufferedImage with the icon's dimensions
    BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = bufferedImage.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    icon.paintIcon(null, g, 0, 0); // Paints the icon onto the BufferedImage
    g.dispose();
    return bufferedImage;
  }

  public static BufferedImage getRoundedImage(BufferedImage image, int cornerRadius) {
    // Create a rounded rectangle mask
    int w = image.getWidth();
    int h = image.getHeight();
    BufferedImage mask = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
    Graphics2D g2 = mask.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
    g2.dispose();

    // Apply the mask to the image
    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = output.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.setComposite(AlphaComposite.DstIn);
    g.drawImage(mask, 0, 0, null);
    g.dispose();

    return output;
  }

  public static ImageIcon getRoundedIcon(Icon icon, int cornerRadius) {
    BufferedImage image = iconToBufferedImage(icon);
    return new ImageIcon(getRoundedImage(image, cornerRadius)); 
  }

  /**
     * Clears the icon cache to free memory.
     */
  public static void clearCache() {
    iconCache.clear();
  }
}
