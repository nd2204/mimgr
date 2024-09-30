package dev.theme;

import java.awt.Color;

import dev.theme.builtin.*;

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

  private theme m_cur_theme;
  private static ColorTheme m_instance;
  private ColorScheme m_colors;

  private ColorTheme(theme t) {
    m_cur_theme = t;
    switch(m_cur_theme) {
      case THEME_DARK_DEFAULT:
        m_colors = new DefaultDark();
        break;
      case THEME_DARK_GRUVBOX:
        m_colors = new GruvboxDark();
        break;
      case THEME_DARK_CATPUCCIN:
        m_colors = new CatpuccinDark();
        break;
      case THEME_DARK_EVERFOREST:
        m_colors = new EverforestDark();
        break;
      case THEME_LIGHT_DEFAULT:
        m_colors = new DefaultLight();
        break;
      case THEME_LIGHT_GRUVBOX:
        m_colors = new GruvboxLight();
        break;
      case THEME_LIGHT_CATPUCCIN:
        m_colors = new CatpuccinLight();
        break;
      case THEME_LIGHT_EVERFOREST:
        m_colors = new EverforestLight();
        break;
      default:
        m_colors = new DefaultDark();
        break;
    }
  }

  public ColorScheme get_colors() {
    return m_colors;
  }

  public void set_colors(ColorScheme colors) {
    m_colors = colors;
    m_cur_theme = theme.THEME_CUSTOM;
  }

  public static ColorTheme get_instance() {
    if (m_instance == null) {
      m_instance = new ColorTheme(theme.THEME_DARK_DEFAULT);
    }
    return m_instance;
  }
}
