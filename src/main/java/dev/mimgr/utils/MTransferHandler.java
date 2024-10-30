package dev.mimgr.utils;

import javax.swing.TransferHandler;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class MTransferHandler extends TransferHandler{
  @Override
  public boolean canImport(TransferHandler.TransferSupport support) {
    // Accept text or image files
    return support.isDataFlavorSupported(DataFlavor.stringFlavor) ||
           support.isDataFlavorSupported(DataFlavor.imageFlavor) ||
           support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
  }

  @Override
  public boolean importData(TransferHandler.TransferSupport support) {
    if (!canImport(support)) {
      return false;
    }

    Transferable data = support.getTransferable();

    try {
      // Handle text data
      if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        String text = (String) data.getTransferData(DataFlavor.stringFlavor);
        for (MTransferListener listener : listeners) {
          listener.onStringImported(text);
        }
      }
      // Handle image data
      else if (support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        Image image = (Image) data.getTransferData(DataFlavor.imageFlavor);
        for (MTransferListener listener : listeners) {
          listener.onImageImported(image);
        }
      }
      // Handle file list (for image files)
      else if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        ArrayList<File> dataArrayList = new ArrayList<>();
        Object transferData = data.getTransferData(DataFlavor.javaFileListFlavor);
        if (transferData instanceof List) {
          List<?> dataList = (List<?>) transferData;
          for (Object d : dataList) {
            if (d instanceof File) {
              dataArrayList.add((File) d);
            }
          }
        }
        for (MTransferListener listener : listeners) {
          listener.onFileListImported(dataArrayList);
        }
      }
    } catch (UnsupportedFlavorException | IOException e) {
      JOptionPane.showMessageDialog(null, "Unsupported data or format.", "Drop Error", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean isImageFile(File file) {
    String fileName = file.getName().toLowerCase();
    if (fileName.endsWith(".png")
    || fileName.endsWith(".jpg")) {
      return true;
    }
    return false;
  }

  public void addTransferListener(MTransferListener listener) {
    listeners.add(listener);
  }

  private ArrayList<MTransferListener> listeners = new ArrayList<>();
}
