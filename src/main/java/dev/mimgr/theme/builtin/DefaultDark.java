package dev.mimgr.theme.builtin;

import java.awt.Color;

public class DefaultDark extends ColorScheme {
  public DefaultDark() {
    m_bg_dim = Color.decode("#1e1e1e");
    m_bg_0   = Color.decode("#252526");
    m_bg_1   = Color.decode("#2d2d30");
    m_bg_2   = Color.decode("#333333");
    m_bg_3   = Color.decode("#3c3c3c");
    m_bg_4   = Color.decode("#454545");
    m_bg_5   = Color.decode("#4f4f4f");

    m_grey_0 = Color.decode("#808080");
    m_grey_1 = Color.decode("#9b9b9b");
    m_grey_2 = Color.decode("#bdbdbd");

    m_fg_0   = Color.decode("#dddddd");
    m_fg_1   = Color.decode("#1e1e1e");

    m_red    = Color.decode("#f44747");
    m_orange = Color.decode("#d19a66");
    m_yellow = Color.decode("#e5c07b");
    m_green  = Color.decode("#89ca78");
    m_aqua   = Color.decode("#56b6c2");
    m_blue   = Color.decode("#61afef");
    m_purple = Color.decode("#c678dd");
    m_accent = m_aqua;

    // m_red    = Color.decode("#f38ba8");
    // m_orange = Color.decode("#fab387");
    // m_yellow = Color.decode("#f9e2af");
    // m_green  = Color.decode("#a6e3a1");
    // m_aqua   = Color.decode("#94e2d5");
    // m_blue   = Color.decode("#89b4fa");
    // m_purple = Color.decode("#cba6f7");
  }
}
