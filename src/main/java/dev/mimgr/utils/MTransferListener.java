package dev.mimgr.utils;

import java.awt.Image;
import java.io.File;
import java.util.List;

public interface MTransferListener {
  public void onStringImported(String importedString);
  public void onImageImported(Image importedImage);
  public void onFileListImported(List<File> importedFile);
}
