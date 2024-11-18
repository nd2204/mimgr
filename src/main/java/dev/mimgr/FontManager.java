package dev.mimgr;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
  private static final Map<String, Font> loadedFonts = new HashMap<>();

  public static Font loadVariableFont(String fontName, String resourcePath, int style, float fontSize) {
    if (loadedFonts.containsKey(fontName)) {
      return loadedFonts.get(fontName).deriveFont(style, fontSize);
    }

    String resourceURL = "/fonts/" + resourcePath;
    try (InputStream fontStream = FontManager.class.getResourceAsStream(resourceURL)) {
      if (fontStream == null) {
        throw new IOException("Font resource not found: " + resourceURL);
      }

      Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontSize);
      loadedFonts.put(fontName, font);

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(font);

      return font;
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
      return null;
    }

  }
  public static Font loadFont(String fontName, String resourcePath, float fontSize) {
    return loadVariableFont(fontName, resourcePath, Font.PLAIN, fontSize);
  }

  public static Font loadFont(String fontName, String resourcePath) {
    return loadVariableFont(fontName, resourcePath, Font.PLAIN, 14f);
  }

  public static Font getVariableFont(String fontName, int style, float fontSize) {
    Font font = loadedFonts.get(fontName);
    if (font != null) {
      return font.deriveFont(style, fontSize);
    }
    return null;
  }

  public static Font getFont(String fontName, float fontSize) {
    return getVariableFont(fontName, Font.PLAIN, fontSize);
  }
}
