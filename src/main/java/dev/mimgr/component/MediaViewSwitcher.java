package dev.mimgr.component;

import java.awt.CardLayout;
import java.sql.ResultSet;
import java.util.function.Supplier;

import javax.swing.JPanel;

import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaViewSwitcher {
  public MediaViewSwitcher() {
    panelSwitcher = new CardLayout();
    this.mainPanel = new JPanel(panelSwitcher);
    setCurrentView(VIEW_TABLE, () -> ImageRecord.selectAll());
  }

  public JPanel getPanel() {
    return this.mainPanel;
  }

  public void setCurrentView(String view, Supplier<ResultSet> queryInvoker) {
    Object mediaView = MediaViewFactory.createMediaView(view, queryInvoker);
    this.mainPanel.add((JPanel) mediaView, view);
    this.panelSwitcher.show(this.mainPanel, view);
    this.currentMediaInterface = (IMediaView) mediaView;
  }

  public IMediaView getCurrentMediaInterface() {
    return this.currentMediaInterface;
  }

  private class MediaViewFactory {
    public static Object createMediaView(String view, Supplier<ResultSet> queryInvoker) {
      if (view.equals(VIEW_TABLE)) {
        MediaTableView mediaTableView = new MediaTableView();
        mediaTableView.updateTable(queryInvoker);
        return mediaTableView;
      }
      if (view.equals(VIEW_GRID)) {
        MediaGridView mediaGridView = new MediaGridView();
        mediaGridView.updateGrid(queryInvoker);
        return mediaGridView;
      }
      return new MediaTableView();
    }
  }

  public static final String VIEW_GRID = "GRID";
  public static final String VIEW_TABLE = "TABLE";

  private JPanel mainPanel;
  private IMediaView currentMediaInterface;
  private CardLayout panelSwitcher;
}
