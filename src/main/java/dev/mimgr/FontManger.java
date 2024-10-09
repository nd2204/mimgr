import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManger {
  public Font FONT_PLACEHOLDER = null;
  private static boolean instantiated = false;
  private static FontManger instance = null;

  // Constructor
  private FontManger() {
    assert !instantiated : "Font manager already initialized";
    instantiated = true;

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Font font;
  }

  private void register_font_from_file(Font font, File file, GraphicsEnvironment ge) {
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(24f);
      ge.registerFont(font);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  public static FontManger get_instance() {
    if (instance == null) {
      instance = new FontManger();
    }
    return instance;
  }
}
