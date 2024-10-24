package dev.mimgr;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

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

  public static Icon changeIconSize(Icon icon, int width, int height) {
    ImageIcon imageIcon = (ImageIcon) icon;
    Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    return new ImageIcon(scaledImage);
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


  /**
     * Clears the icon cache to free memory.
     */
  public static void clearCache() {
    iconCache.clear();
  }
}
