package dev.mimgr;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.HierarchyEvent;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers;
import dev.shader.BuiltinShaders.TriLatticeShader;

public class FormHome extends JPanel {
  public FormHome() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(this.colors.m_bg_dim);

    this.setLayout(new BorderLayout());
    this.add(new LeftPanel(), BorderLayout.CENTER);
  }

  private class LeftPanel extends JPanel {
    public LeftPanel() {
      this.setLayout(new GridBagLayout());
      this.setBackground(null);
      this.setOpaque(false);
      GridBagConstraints gc = new GridBagConstraints();
      gc.gridx = 0;
      gc.gridy = 0;

      gc.fill = GridBagConstraints.BOTH;
      gc.weightx = 1.0;
      this.add(new GreetPanel(), gc);
    }
  }

  public class GreetPanel extends AnimatedPanel {
    public GreetPanel() {
      super(new GreetPanelShader(), 200, 100);
      this.setPreferredSize(new Dimension(200, 100));
      this.setBorderRadius(15);
      this.setBorderWidth(2);
      this.setBackground(null);
      this.setBorderColor(colors.m_bg_1);
      String greetString = String.format(
        "Good %s, %s.\nHere's what's happening with your store today",
        Helpers.getLocalTimesOfDayString(),
        SessionManager.getCurrentUser().m_username
      );
      taGreet = new JTextArea(greetString);
      taGreet.setEditable(false);
      taGreet.setLineWrap(true);
      taGreet.setWrapStyleWord(true);
      taGreet.setOpaque(false);

      this.addHierarchyListener(e -> {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
          if (GreetPanel.this.isShowing()) {
            GreetPanel.this.start();
          } else {
            GreetPanel.this.stop();
          }
        }
      });

      this.add(taGreet);
      this.start();
    }

    public JTextArea taGreet;
  }

  private ColorScheme colors;
}

class GreetPanelShader extends TriLatticeShader {
  public GreetPanelShader() {
    ColorScheme colors = ColorTheme.getInstance().getCurrentScheme();
    mult = 4.0f;

    int rgb1 = colors.m_bg_dim.getRGB();
    col.x = ((rgb1 >> 16) & 255) / 255.0f;
    col.y = ((rgb1 >> 8) & 255) / 255.0f;
    col.z = ((rgb1 >> 0) & 255) / 255.0f;

    int rgb2 = colors.m_bg_1.getRGB();
    lineCol.x = ((rgb2 >> 16) & 255) / 255.0f;
    lineCol.y = ((rgb2 >> 8) & 255) / 255.0f;
    lineCol.z = ((rgb2 >> 0) & 255) / 255.0f;
  }
}
