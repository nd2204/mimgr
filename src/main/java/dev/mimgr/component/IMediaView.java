package dev.mimgr.component;

import java.util.List;

import dev.mimgr.db.ImageRecord;

public interface IMediaView {
  public List<ImageRecord> getSelectedImages();
  public void refresh();
  public void deleteSelectedImages();
}
