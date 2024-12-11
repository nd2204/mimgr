package dev.mimgr.custom;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

/**
 * @author nd2204
 */
public class ActionPanel extends JPanel {

  public ActionPanel(int orientation) {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new GridBagLayout());
    c.gridx = 0;
    c.gridy = 0;
    switch(orientation) {
      case SwingConstants.RIGHT:
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        glue.setVisible(true);
        this.add(glue, c);
        c.gridx++;

        // Reset
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.insets = new Insets(0, 0, 0, 20);
      break;
      default: break;
    }
  }


  public ActionPanel() {
    this(SwingConstants.CENTER);
  }

  public void addButton(JButton button) {
    this.add(button, c);
    c.gridx++;
    c.insets = new Insets(0, 10, 0, 0);
  }

  GridBagConstraints c = new GridBagConstraints();

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(colors.m_bg_1);
    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

    g2.dispose();
  }

  private Component glue = Box.createHorizontalGlue();
  private ColorScheme colors;
}
