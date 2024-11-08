package dev.mimgr;

import java.awt.CardLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

public class PanelManager {
  private static CardLayout          m_panel_switcher = new CardLayout();
  private static JPanel              m_main_panel     = new JPanel(m_panel_switcher);
  private static Map<String, JPanel> panelMap         = new HashMap<>();
  private static String              currentPanelId;

  public static Collection<JPanel> getAllPanels() {
    return panelMap.values();
  }

  public static void register_panel(JPanel panel, String id) {
    m_main_panel.add(panel, id);
    panelMap.put(id, panel);
  }

  public static void unregister_panel(String id) {
    JPanel panel = panelMap.remove(id);
    assert panel != null : 
      "Error: No panel found with ID '" + id + "'";

    m_main_panel.remove(panel);
//    if (currentPanelId.equals(id)) {
//      currentPanelId = null;
//    }
    m_main_panel.revalidate();
    m_main_panel.repaint();
  }

  public static void show(String id) {
    assert panelMap.containsKey(id) :
      "Error: No panel registered with ID '" + id + "'";
    m_panel_switcher.show(m_main_panel, id);
    currentPanelId = id;
  }

  public static String get_current_panel_id() {
    return currentPanelId;
  }

  public static JPanel get_main_panel() {
    return m_main_panel;
  }

  public static JPanel get_panel(String id) {
    return panelMap.get(id);  // Returns null if not found
  }
}
