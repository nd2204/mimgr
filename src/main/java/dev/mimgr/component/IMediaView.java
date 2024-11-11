package dev.mimgr.component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Supplier;

import dev.mimgr.db.ImageRecord;

public interface IMediaView {
  public List<ImageRecord> getSelectedImages();
  public void refresh();
  public void reset();
  public void deleteSelectedImages();
  public void updateView(Supplier<ResultSet> queryInvoker);
  public Supplier<ResultSet> getCurrentQueryInvoker();
}
