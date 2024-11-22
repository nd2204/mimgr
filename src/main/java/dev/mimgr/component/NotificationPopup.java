package dev.mimgr.component;

import dev.mimgr.FontManager;
import dev.mimgr.IconManager;
import dev.mimgr.PanelManager;
import dev.mimgr.component.PopupPanel.PopupType;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

import java.awt.*;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class NotificationPopup implements PopupPanel.IPopup {
  private static final ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
  private static final int POPUP_WIDTH = 300;
  private static final int POPUP_HEIGHT = 100;
  private static final int POPUP_PADDING = 10;

  private String id;
  private RoundedPanel panel;
  private long timeout;

  public static final int NOTIFY_LEVEL_DEBUG     = 0;
  public static final int NOTIFY_LEVEL_NORMAL    = 1;
  public static final int NOTIFY_LEVEL_INFO      = 2;
  public static final int NOTIFY_LEVEL_WARNING   = 3;
  public static final int NOTIFY_LEVEL_ERROR     = 4;
  public static final int NOTIFY_LEVEL_MAX_LEVEL = 5;

  static {
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
  }

  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);

  private static String[] notify_str = {
    "DEBUG",
    "NORMAL",
    "INFO",
    "WARNING",
    "ERROR"
  };

  private static Color[] notify_color = {
    colors.m_blue,
    colors.m_fg_0,
    colors.m_green,
    colors.m_yellow,
    colors.m_red
  };

  private static Icon[] notify_icon = {
    IconManager.getIcon("info.png"    , 18, 16, notify_color[0]),
    IconManager.getIcon("info.png"    , 18, 16, notify_color[1]),
    IconManager.getIcon("info.png"    , 18, 16, notify_color[2]),
    IconManager.getIcon("warning.png" , 18, 16, notify_color[3]),
    IconManager.getIcon("error.png"   , 18, 16, notify_color[4])
  };

  public NotificationPopup(String message, final int notifyLevel, long timeout) {
    if (notifyLevel >= NOTIFY_LEVEL_MAX_LEVEL || notifyLevel < 0) {
      throw new IllegalArgumentException("Invalid Notify Level: '" + notifyLevel + "'");
    }

    this.timeout = timeout;
    this.id = UUID.randomUUID().toString();
    // set timeout
    this.panel = new RoundedPanel();
    this.panel.setBorderColor(notify_color[notifyLevel]);
    this.panel.setBorderWidth(2);
    this.panel.setBorderRadius(15);
    this.panel.setOpaque(false); // Ensure the popup background is visible

    MButton closeButton = new MButton(IconManager.getIcon("x_icon.png", 16, 16, colors.m_grey_0));
    closeButton.setMinimumSize(new Dimension(16, 20));
    closeButton.setPreferredSize(new Dimension(16, 20));
    closeButton.setMaximumSize(new Dimension(16, 20));
    closeButton.setBorderWidth(0);
    closeButton.setBackground(null);
    closeButton.setBorderWidth(0);
    closeButton.setBorderColor(null);
    closeButton.setOpaque(false);
    closeButton.setBorderPainted(false);

    JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
    sep.setBackground(notify_color[notifyLevel]);
    sep.setForeground(notify_color[notifyLevel]);

    JLabel messageTitle = new JLabel(notify_str[notifyLevel]);
    messageTitle.setFont(nunito_bold_14);
    messageTitle.setForeground(notify_color[notifyLevel]);
    messageTitle.setHorizontalAlignment(SwingConstants.LEFT);
    messageTitle.setVerticalAlignment(SwingConstants.CENTER);
    messageTitle.setIcon(notify_icon[notifyLevel]);

    MTextArea messageLabel = new MTextArea();
    messageLabel.setFont(nunito_bold_14);
    messageLabel.setBackground(null);
    messageLabel.setEditable(false);
    messageLabel.setFocusable(false);
    messageLabel.setOpaque(false);
    messageLabel.setLineWrap(true);
    messageLabel.setWrapStyleWord(true);
    // messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
    // messageLabel.setVerticalAlignment(SwingConstants.CENTER);
    int messageLenWithNoSpace = message.replaceAll(" ", "").length();
    messageLabel.setForeground(colors.m_grey_2); // Ensure text color is visible
    if (message != null && !message.isEmpty()) {
      messageLabel.setColumns(Math.min(messageLenWithNoSpace, 30));
      messageLabel.setRows(messageLenWithNoSpace / 30);
      messageLabel.setText(message);
    } else {
      messageLabel.setText("Empty Message");
    }
    messageLabel.revalidate();

    panel.setName(id);

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;

    panel.setLayout(new GridBagLayout());
    c.weightx = 1.0;
    c.weighty = 0.0;
    c.insets = new Insets(10, 15, 7, 15);
    c.anchor = GridBagConstraints. FIRST_LINE_START;
    panel.add(messageTitle, c);

    c.gridx = 1;
    c.weightx = 0.0;
    c.anchor = GridBagConstraints. FIRST_LINE_END;
    closeButton.addActionListener(ev -> PanelManager.removePopup(id));
    panel.add(closeButton, c);

    c.gridx = 0;
    c.gridy++;

    c.weightx = 1.0;
    c.insets = new Insets(0, 0, 0, 0);
    c.gridwidth = 2;
    panel.add(sep, c);

    c.gridx = 0;
    c.gridy++;

    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = 2;
    c.insets = new Insets(10, 15, 10, 10);
    panel.add(messageLabel, c);

    c.gridx = 0;
    c.gridy++;

    panel.setMinimumSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
    panel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
  }

  @Override
  public void repositionPopups(JPanel mainPanel, Iterable<JPanel> panels) {
    int yOffset = POPUP_PADDING;
    Dimension mainSize = mainPanel.getSize();

    for (JPanel panel : panels) {
      Dimension popupSize = panel.getPreferredSize();
      int x = mainSize.width - popupSize.width - POPUP_PADDING;
      int y = yOffset;

      panel.setBounds(x, y, popupSize.width, popupSize.height);

      yOffset += popupSize.height + POPUP_PADDING;
    }
  }

  @Override
  public JPanel getPopupPanel() {
    return this.panel;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public PopupType getPopupType() {
    return PopupType.NotificationPopup;
  }

  @Override
  public void onShow() {
    SwingUtilities.invokeLater(() -> panel.setVisible(true));
    if (this.timeout > 0) {
      Timer timer = new Timer(5000, ev -> PanelManager.removePopup(this.id));
      timer.setRepeats(false);
      timer.start();
    }
  }

  @Override
  public void onClose() { }

}

