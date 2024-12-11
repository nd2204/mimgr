package dev.mimgr;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import dev.mimgr.component.PopupPanel.IPopup;

public class PanelManager {
  // Main layered pane
  private static JLayeredPane m_main_panel = new JLayeredPane();

  // Base panel with CardLayout for main content
  private static CardLayout m_panel_switcher = new CardLayout();
  private static JPanel m_base_panel = new JPanel(m_panel_switcher);

  // Maps to store panels and popups
  private static Map<String, JPanel> panelMap = new HashMap<>();
  private static Map<String, JPanel> floatingPanelMap = new HashMap<>();
  private static Map<String, JPanel> popupMap = new LinkedHashMap<>();
  // private static Map<String, Map<String,IPopup>> popupMap;

  private static String currentPanelId;

  // Constants for popup positioning
  private static final int POPUP_PADDING = 10;

  // Static block to set up the main panel
  static {
    // Add the base panel to the main layered pane
    m_base_panel.setBounds(0, 0, 854, 480); // Initial size
    m_main_panel.add(m_base_panel, Integer.valueOf(JLayeredPane.DEFAULT_LAYER));
    m_main_panel.setPreferredSize(new Dimension(854, 480));

    // Add a ComponentListener to resize panels when the main panel is resized
    m_main_panel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        Dimension size = m_main_panel.getSize();
        m_base_panel.setBounds(0, 0, size.width, size.height);
        repositionPopups();
        repositionFloatingPanel();
        m_base_panel.revalidate();
      }
    });
  }

  /**
     * Registers a new panel with an ID.
     *
     * @param panel JPanel to register
     * @param id    Unique ID for the panel
     */
  public static void register_panel(JPanel panel, String id) {
    if (panelMap.containsKey(id)) {
      throw new IllegalArgumentException("Panel with ID '" + id + "' is already registered.");
    }
    m_base_panel.add(panel, id);
    panelMap.put(id, panel);
  }

  /**
     * Unregisters a panel by ID.
     *
     * @param id ID of the panel to remove
     */
  public static void unregister_panel(String id) {
    if (!panelMap.containsKey(id)) return;
    JPanel panel = panelMap.remove(id);
    m_base_panel.remove(panel);
    if (currentPanelId != null && currentPanelId.equals(id)) {
      currentPanelId = null;
    }
    m_main_panel.revalidate();
    m_main_panel.repaint();
  }

  /**
     * Shows a panel by ID.
     *
     * @param id ID of the panel to show
     */
  public static void show(String id) {
    if (!panelMap.containsKey(id)) {
      throw new IllegalArgumentException("Error: No panel registered with ID '" + id + "'");
    }
    m_panel_switcher.show(m_base_panel, id);
    currentPanelId = id;
  }

  public static void createPopup(IPopup popup) {
    // Generate a unique ID for each popup
    JPanel popupPanel = popup.getPopupPanel();
    String popupId = popup.getId();

    // Add the popupPanel to the main panel
    m_main_panel.add(popupPanel, Integer.valueOf(JLayeredPane.POPUP_LAYER));
    popupMap.put(popupId, popupPanel);

    popup.onShow();

    repositionPopups();
  }

  public static void registerFloatingPanel(JPanel panel, String id) {
    m_main_panel.add(panel, JLayeredPane.MODAL_LAYER);
    panel.setName(id);
    floatingPanelMap.put(id, panel);
    panel.setOpaque(false); // Ensure the popup background is visible
    SwingUtilities.invokeLater(() -> panel.setVisible(false));
    repositionFloatingPanel();
  }

  public static JPanel getFloatingPanel(String id) {
    return floatingPanelMap.get(id);
  }

  public static void unregisterFloatingPanel(String id) {
    JPanel popupPanel = floatingPanelMap.remove(id);
    if (popupPanel != null) {
      m_main_panel.remove(popupPanel);
      repositionPopups();
    }
  }

  /**
     * Removes a popup panel by ID.
     *
     * @param id ID of the popup to remove
     */
  public static void removePopup(String id) {
    JPanel popupPanel = popupMap.remove(id);
    if (popupPanel != null) {
      m_main_panel.remove(popupPanel);
      repositionPopups();
    }
  }

  /**
     * Repositions popups to prevent overlap.
     */
  private static void repositionPopups() {
    int yOffset = POPUP_PADDING;
    Dimension mainSize = m_main_panel.getSize();

    for (JPanel popupPanel : popupMap.values()) {
      Dimension popupSize = popupPanel.getPreferredSize();
      int x = mainSize.width - popupSize.width - POPUP_PADDING;
      int y = yOffset;

      popupPanel.setBounds(x, y, popupSize.width, popupSize.height);

      yOffset += popupSize.height + POPUP_PADDING;
    }

    m_main_panel.revalidate();
    m_main_panel.repaint();
  }

  private static void repositionFloatingPanel() {
    for (JPanel floatingPanel : floatingPanelMap.values()) {
      Dimension size = floatingPanel.getPreferredSize();
      int x = POPUP_PADDING;
      int y = POPUP_PADDING;

      floatingPanel.setBounds(x, y, size.width - POPUP_PADDING, size.height - POPUP_PADDING);
    }

    m_main_panel.revalidate();
    m_main_panel.repaint();
  }

  /**
     * Retrieves the ID of the currently visible panel.
     *
     * @return ID of the currently visible panel
     */
  public static String get_current_panel_id() {
    return currentPanelId;
  }

  /**
     * Returns the main layered pane.
     *
     * @return JLayeredPane
     */
  public static JLayeredPane get_main_panel() {
    return m_main_panel;
  }

  /**
     * Returns a panel by ID.
     *
     * @param id ID of the panel
     * @return JPanel or null if not found
     */
  public static JPanel get_panel(String id) {
    return panelMap.get(id);
  }

  /**
     * Returns all registered panels.
     *
     * @return Collection of JPanels
     */
  public static Collection<JPanel> getAllPanels() {
    return panelMap.values();
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("PanelManager Demo with Multiple Popups");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(854, 480));

    // Create the main content panel
    JPanel panel1 = new JPanel();
    panel1.setBackground(Color.GRAY);

    // Register the main panel
    PanelManager.register_panel(panel1, "MainPanel");

    // Show the main panel
    PanelManager.show("MainPanel");

    // Add a button to show popups
    JButton popupButton = new JButton("Show Popup");
    boolean modalVisible = false;
    popupButton.addActionListener(e -> {
      popupButton.setEnabled(false);
      showOverlayModal(frame);
      // PanelManager.createPopup(new NotificationPopup(
      //   """
      //   THIS IS A VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY
      //   VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY
      //   VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY
      //   VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY
      //   VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY
      //   VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY
      //   VERY VERY VERY VERY VERY VERY VERY VERY LONG TEXT
      //   """,
      //   NotificationPopup.NOTIFY_LEVEL_WARNING,
      //   5000
      // ));
    });
    panel1.add(popupButton);

    // Add main panel to the frame
    frame.setLayout(new BorderLayout());
    frame.add(PanelManager.get_main_panel(), BorderLayout.CENTER);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private static void showOverlayModal(JFrame parent) {
    // Create a glass pane for dimming the background
    JPanel glassPane = new JPanel();
    glassPane.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
    glassPane.setBounds(0, 0, parent.getWidth(), parent.getHeight());
    glassPane.setLayout(null);

    // Create the modal popup
    JDialog dialog = new JDialog(parent, "Overlay Modal", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setSize(300, 200);
    dialog.setUndecorated(true); // Remove title bar for cleaner look
    dialog.setLayout(new FlowLayout());

    // Position the dialog in the center of the parent frame
    dialog.setLocationRelativeTo(parent);

    // Add components to the dialog
    JLabel label = new JLabel("This is an overlay modal popup!");
    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(e -> {
      parent.remove(glassPane);
      dialog.dispose();
    });

    dialog.add(label);
    dialog.add(closeButton);

    // Add the glass pane and repaint
    parent.setGlassPane(glassPane);
    glassPane.setVisible(true);

    // Show the dialog
    dialog.setVisible(true);
  }
}
