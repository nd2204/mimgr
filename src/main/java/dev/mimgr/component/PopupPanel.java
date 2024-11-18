package dev.mimgr.component;

import javax.swing.*;

public class PopupPanel {
  public enum PopupType {
    NotificationPopup,
    ModalPopup
  }

  public interface IPopup {
    JPanel getPopupPanel();
    PopupType getPopupType();
    String getId();
    void onShow();
    void onClose();
    void repositionPopups(JPanel mainPanel, Iterable<JPanel> panels);
  }
}

