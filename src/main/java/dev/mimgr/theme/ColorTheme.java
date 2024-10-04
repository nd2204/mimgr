package dev.mimgr.theme;

import dev.mimgr.theme.builtin.*;

public class ColorTheme {
  public enum theme {
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
        return new DefaultDark();
    }
  }
}
