package dev.mimgr.theme;

import dev.mimgr.theme.builtin.*;

public class ColorTheme {
  private static ColorScheme currentScheme = null;

  public static final int THEME_DARK_GRUVBOX     = 0;
  public static final int THEME_DARK_CATPUCCIN   = 1;
  public static final int THEME_DARK_EVERFOREST  = 2;
  public static final int THEME_DARK_DEFAULT     = 3;
  public static final int THEME_LIGHT_GRUVBOX    = 4;
  public static final int THEME_LIGHT_CATPUCCIN  = 5;
  public static final int THEME_LIGHT_EVERFOREST = 6;
  public static final int THEME_LIGHT_DEFAULT    = 7;

  public static ColorScheme get_colorscheme(int theme) {
    switch(theme) {
      case THEME_DARK_DEFAULT:
        currentScheme = new DefaultDark();
        break;
      case THEME_DARK_GRUVBOX:
        currentScheme = new GruvboxDark();
        break;
      case THEME_DARK_CATPUCCIN:
        currentScheme = new CatpuccinDark();
        break;
      case THEME_DARK_EVERFOREST:
        currentScheme = new EverforestDark();
        break;
      case THEME_LIGHT_DEFAULT:
        currentScheme = new DefaultLight();
        break;
      case THEME_LIGHT_GRUVBOX:
        currentScheme = new GruvboxLight();
        break;
      case THEME_LIGHT_CATPUCCIN:
        currentScheme = new CatpuccinLight();
        break;
      case THEME_LIGHT_EVERFOREST:
        currentScheme = new EverforestLight();
        break;
      default:
        currentScheme = new DefaultDark();
        break;
    }
    return currentScheme;
  }

  public static ColorScheme get_currentScheme() {
    if (currentScheme == null) return new EverforestDark();
    return currentScheme;
  }
}
