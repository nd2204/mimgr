package dev.mimgr.theme.builtin;

import java.awt.Color;

public class DefaultLight extends ColorScheme {
  public DefaultLight() {
    // Background Colors
    m_bg_dim = Color.decode("#f0f0f0");
    m_bg_0   = Color.decode("#ffffff");
    m_bg_1   = Color.decode("#e3e3e3");
    m_bg_2   = Color.decode("#eaeaea");
    m_bg_3   = Color.decode("#e1e1e1");
    m_bg_4   = Color.decode("#d9d9d9");
    m_bg_5   = Color.decode("#cfcfcf");

    // Grey Colors
    m_grey_0 = Color.decode("#5a5a5a");
    m_grey_1 = Color.decode("#777777");
    m_grey_2 = Color.decode("#9e9e9e");

    // Foreground Colors
    m_fg_0   = Color.decode("#1f1f1f");
    m_fg_1   = Color.decode("#ffffff");

    // Accent Colors
    m_red    = Color.decode("#d73a49");
    m_orange = Color.decode("#d18616");
    m_yellow = Color.decode("#b89500");
    m_green  = Color.decode("#22863a");
    m_aqua   = Color.decode("#1b7c83");
    m_blue   = Color.decode("#005cc5");
    m_purple = Color.decode("#6f42c1");
    m_accent = m_aqua;
  }
}
