package dev.mimgr.theme;

import dev.mimgr.theme.builtin.*;

public class ColorTheme {
  private static ColorScheme currentScheme = null;

  public static enum theme {
    THEME_DARK_GRUVBOX,
    THEME_DARK_CATPUCCIN,
    THEME_DARK_EVERFOREST,
    THEME_DARK_DEFAULT,
    THEME_LIGHT_GRUVBOX,
    THEME_LIGHT_CATPUCCIN,
    THEME_LIGHT_EVERFOREST,
    THEME_LIGHT_DEFAULT,
    THEME_CUSTOM,
  };

  public static ColorScheme get_colorscheme(theme t) {
    switch(t) {
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
