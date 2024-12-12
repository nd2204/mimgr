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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ConfirmPopup implements PopupPanel.IPopup {
  private static final ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
  private static final int POPUP_WIDTH = 300;
  private static final int POPUP_HEIGHT = 100;
  private static final int POPUP_PADDING = 10;

  public MButton btnYes = new MButton("Yes");
  public MButton btnNo = new MButton("No");

  private String id;
  private RoundedPanel panel;

  static {
    FontManager.loadFont("NunitoBold", "Nunito-Bold.ttf");
  }

  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);

  public ConfirmPopup(String title, String message) {
    this.id = UUID.randomUUID().toString();
    this.panel = new RoundedPanel();
    this.panel.setBorderColor(colors.m_bg_5);
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
    sep.setBackground(colors.m_bg_5);
    sep.setForeground(colors.m_bg_5);

    JLabel messageTitle = new JLabel(title);
    messageTitle.setFont(nunito_bold_14);
    messageTitle.setForeground(colors.m_fg_0);
    messageTitle.setHorizontalAlignment(SwingConstants.LEFT);
    messageTitle.setVerticalAlignment(SwingConstants.CENTER);

    MTextArea messageLabel = new MTextArea();
    messageLabel.setFont(nunito_bold_14);
    messageLabel.setBackground(null);
    messageLabel.setEditable(false);
    messageLabel.setFocusable(false);
    messageLabel.setOpaque(false);
    messageLabel.setLineWrap(true);
    messageLabel.setWrapStyleWord(true);
    messageLabel.setBorderWidth(0);
    messageLabel.setBorderColor(null);
    messageLabel.setPadding(new Insets(0, 0, 0, 0));
    // messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
    // messageLabel.setVerticalAlignment(SwingConstants.CENTER);
    Pattern pattern = Pattern.compile("(\r\n|\n|\r)");
    Matcher matcher = pattern.matcher(message);
    int newlineCount = 0;
    // Find all newline characters and store their indexes
    while (matcher.find()) {
      newlineCount++;
    }
    int messageLenWithNoSpace = message.replaceAll(" ", "").length();
    messageLabel.setForeground(colors.m_grey_2); // Ensure text color is visible
    if (message != null && !message.isEmpty()) {
      messageLabel.setColumns(Math.min(messageLenWithNoSpace, 30));
      messageLabel.setRows(messageLenWithNoSpace / 30 + newlineCount);
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
    c.insets = new Insets(10, 15, 7, 15);
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    panel.add(messageTitle, c);
    c.gridx++;

    c.weightx = 1.0;
    panel.add(Box.createHorizontalGlue(), c);
    c.gridx++;

    c.weightx = 0.0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    closeButton.addActionListener(ev -> PanelManager.removePopup(id));
    panel.add(closeButton, c);

    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.gridx = 0;
    c.gridy++;

    c.weightx = 1.0;
    c.insets = new Insets(0, 0, 0, 0);
    c.gridwidth = 3;
    panel.add(sep, c);

    c.gridx = 0;
    c.gridy++;

    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets(5, 15, 5, 10);
    panel.add(messageLabel, c);

    c.gridx = 0;
    c.gridy++;
    c.weighty = 1.0;
    c.weightx = 0.0;
    c.fill = GridBagConstraints.NONE;
    c.insets = new Insets(5, 15, 15, 0);
    c.gridwidth = 1;

    ActionListener al = e -> {
      PanelManager.removePopup(this.id);
    };
    btnYes.addActionListener(al);
    btnYes.setBorderColor(colors.m_bg_2);
    btnYes.setBackground(colors.m_bg_1);
    btnYes.setForeground(colors.m_grey_0);
    panel.add(btnYes, c);
    c.gridx++;

    c.insets = new Insets(10, 10, 15, 0);
    btnNo.addActionListener(al);
    btnNo.setBorderColor(colors.m_bg_2);
    btnNo.setBackground(colors.m_bg_1);
    btnNo.setForeground(colors.m_grey_0);
    panel.add(btnNo, c);

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
  }

  @Override
  public void onClose() { }

}

