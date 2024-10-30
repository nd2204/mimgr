package dev.mimgr.custom;

import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.MTransferListener;

import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class DropPanel extends JPanel implements DropTargetListener {
  public DropPanel(ColorScheme colors) {
    this.colors = colors;
    this.setPreferredSize(new Dimension(200, 200));
    this.setBackground(this.colors.m_bg_0);
    this.drawBorderColor = this.colors.m_grey_0;
    this.borderColor = this.drawBorderColor;
    new DropTarget(this, this);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();

    // Set dashed stroke
    float[] dashPattern = {10, 10}; // Dash length, gap length
    g2d.setStroke(new BasicStroke(
      2,                     // Line width
      BasicStroke.CAP_BUTT,  // End cap
      BasicStroke.JOIN_BEVEL, // Line join style
      0,                     // Miter limit
      dashPattern,           // Dash pattern
      0                      // Dash phase
    ));

    g2d.setColor(drawBorderColor); // Set border color
    g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10); // Draw border with padding
    g2d.dispose();
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
    this.drawBorderColor = color;
    repaint();
  }

  public void setDragBorderColor(Color color) {
    this.drawBorderColor = color;
    repaint();
  }

  @Override
  public void dragEnter(DropTargetDragEvent dtde) {
    this.setDragBorderColor(colors.m_blue);
  }

  @Override
  public void dragOver(DropTargetDragEvent dtde) {
    this.setDragBorderColor(colors.m_blue);
  }

  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {}

  @Override
  public void dragExit(DropTargetEvent dte) {
    this.setBorderColor(borderColor);
  }

  public void drop(DropTargetDropEvent dtde) {
    this.setBorderColor(borderColor);

    try {
      dtde.acceptDrop(DnDConstants.ACTION_COPY);
      Transferable data = dtde.getTransferable();

      if (!(data.isDataFlavorSupported(DataFlavor.stringFlavor) ||
          data.isDataFlavorSupported(DataFlavor.imageFlavor) ||
          data.isDataFlavorSupported(DataFlavor.javaFileListFlavor))) {
          dtde.rejectDrop();
      }
      // Handle text data
      if (data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        String text = (String) data.getTransferData(DataFlavor.stringFlavor);
        for (MTransferListener listener : listeners) {
          listener.onStringImported(text);
        }
      }
      // Handle image data
      else if (data.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        Image image = (Image) data.getTransferData(DataFlavor.imageFlavor);
        for (MTransferListener listener : listeners) {
          listener.onImageImported(image);
        }
      }
      // Handle file list (for image files)
      else if (data.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
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
      JOptionPane.showMessageDialog(null, "Undataed data or format.", "Drop Error", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
      dtde.dropComplete(false);
    }

    dtde.dropComplete(true);
    // try {
    //   // Accept the drop and get the dropped data as a string
    //   dtde.acceptDrop(DnDConstants.ACTION_COPY);
    //   Transferable transferable = dtde.getTransferable();

    //   // Check if the dropped data is of type String
    //   if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
    //     String droppedText = (String) transferable.getTransferData(DataFlavor.stringFlavor);
    //     dropLabel.setText("Dropped: " + droppedText);
    //     dtde.dropComplete(true);
    //   } else {
    //     dtde.rejectDrop();
    //   }
    // } catch (Exception e) {
    //   e.printStackTrace();
    //   dtde.dropComplete(false);
    // }
  }

  public void addTransferListener(MTransferListener listener) {
    listeners.add(listener);
  }

  private ArrayList<MTransferListener> listeners = new ArrayList<>();
  private ColorScheme colors;
  private Color borderColor;
  private Color drawBorderColor;
}

