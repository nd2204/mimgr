package dev.mimgr.component;

import java.awt.CardLayout;
import java.sql.ResultSet;
import java.util.function.Supplier;

import javax.swing.JPanel;

import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaViewSwitcher {
  public MediaViewSwitcher(ColorScheme colors) {
    this.colors = (colors == null)
      ? ColorTheme.get_colorscheme(ColorTheme.THEME_DARK_EVERFOREST)
      : colors;

    panelSwitcher = new CardLayout();
    this.mainPanel = new JPanel(panelSwitcher);
    setCurrentView(VIEW_TABLE, () -> ImageRecord.selectAll());
  }

  public JPanel getPanel() {
    return this.mainPanel;
  }

  public void setCurrentView(String view, Supplier<ResultSet> queryInvoker) {
    Object mediaView = MediaViewFactory.createMediaView(view, colors, queryInvoker);
    this.mainPanel.add((JPanel) mediaView, view);
    this.panelSwitcher.show(this.mainPanel, view);
    this.currentMediaInterface = (IMediaView) mediaView;
  }

  public IMediaView getCurrentMediaInterface() {
    return this.currentMediaInterface;
  }

  private class MediaViewFactory {
    public static Object createMediaView(String view, ColorScheme colors, Supplier<ResultSet> queryInvoker) {
      if (view.equals(VIEW_TABLE)) {
        MediaTableView mediaTableView = new MediaTableView(colors);
        mediaTableView.updateTable(queryInvoker, mediaTableView.model);
        return mediaTableView;
      }
      if (view.equals(VIEW_GRID)) {
        MediaGridView mediaGridView = new MediaGridView(colors);
        mediaGridView.updateGrid(queryInvoker);
        return mediaGridView;
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
