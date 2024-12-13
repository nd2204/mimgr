package dev.mimgr.theme;
import dev.mimgr.theme.builtin.*;

public class ColorTheme {
  public static final int THEME_DARK_GRUVBOX     = 1;
  public static final int THEME_LIGHT_GRUVBOX    = 2;
  public static final int THEME_DARK_CATPUCCIN   = 3;
  public static final int THEME_LIGHT_CATPUCCIN  = 4;
  public static final int THEME_DARK_EVERFOREST  = 5;
  public static final int THEME_LIGHT_EVERFOREST = 6;
  public static final int THEME_DARK_DEFAULT     = 7;
  public static final int THEME_LIGHT_DEFAULT    = 8;

  public static ColorScheme getColorScheme(int theme) {
    switch(theme) {
      case THEME_DARK_DEFAULT:
        return new DefaultDark();
      case THEME_DARK_GRUVBOX:
        return new GruvboxDark();
      case THEME_DARK_CATPUCCIN:
        return new CatpuccinDark();
      case THEME_DARK_EVERFOREST:
        return new EverforestDark();
      case THEME_LIGHT_DEFAULT:
        return new DefaultLight();
      case THEME_LIGHT_GRUVBOX:
        return new GruvboxLight();
      case THEME_LIGHT_CATPUCCIN:
        return new CatpuccinLight();
      case THEME_LIGHT_EVERFOREST:
        return new EverforestLight();
      default:
        return new DefaultLight();
    }
  }

  private ColorTheme() {
    this.currentScheme = new DefaultDark();
  }

  private class ColorThemeHolder {
    public static final ColorTheme INSTANCE = new ColorTheme();
  }

  public static ColorTheme getInstance() {
    return ColorThemeHolder.INSTANCE;
  }

  public void setColorScheme(int scheme) {
    this.currentScheme = getColorScheme(scheme);
 }

  public ColorScheme getCurrentScheme() {
    if (currentScheme == null) return new EverforestDark();
    return currentScheme;
  }

  private ColorScheme currentScheme = null;
}
