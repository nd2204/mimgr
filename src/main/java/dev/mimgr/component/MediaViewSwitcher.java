package dev.mimgr.component;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaViewSwitcher {
  public MediaViewSwitcher(ColorScheme colors) {
    this.colors = (colors == null)
      ? ColorTheme.get_colorscheme(ColorTheme.THEME_DARK_EVERFOREST)
      : colors;

    panelSwitcher = new CardLayout();
    this.mainPanel = new JPanel(panelSwitcher);
    setCurrentView(VIEW_TABLE);
  }

  public JPanel getPanel() {
    return this.mainPanel;
  }

  public void setCurrentView(String view) {
    Object mediaView = MediaViewFactory.createMediaView(view, colors);
    this.mainPanel.add((JPanel) mediaView, view);
    this.panelSwitcher.show(this.mainPanel, view);
    this.currentMediaInterface = (IMediaView) mediaView;
  }

  public IMediaView getCurrentMediaInterface() {
    return this.currentMediaInterface;
  }

  private class MediaViewFactory {
    public static Object createMediaView(String view, ColorScheme colors) {
      if (view.equals(VIEW_TABLE)) {
        return new MediaTableView(colors);
      }
      if (view.equals(VIEW_GRID)) {
        return new MediaGridView(colors);
      }
      return new MediaTableView(colors);
    }
  }

  public static final String VIEW_GRID = "GRID";
  public static final String VIEW_TABLE = "TABLE";

  private ColorScheme colors;
  private JPanel mainPanel;
  private IMediaView currentMediaInterface;
  private CardLayout panelSwitcher;
}
