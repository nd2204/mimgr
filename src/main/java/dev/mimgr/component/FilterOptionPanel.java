package dev.mimgr.component;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.swing.JPanel;

import dev.mimgr.custom.MButton;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class FilterOptionPanel extends JPanel {
  public FilterOptionPanel() {
    colors = ColorTheme.getInstance().getCurrentScheme();
    this.setBackground(colors.m_fg_0);
    this.setOpaque(false);
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    this.filterButtons = new HashMap<>();
    this.setupButton = (button) -> {};
  }

  public void addFilterOption(String id, Consumer<MButton> setup, Consumer<ActionEvent> runnable) {
    MButton button = new MButton();
    if (setup != null)  {
      setup.accept(button);
    } else {
      setupButton.accept(button);
    }
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runnable.accept(e);
      }
    });
    filterButtons.put(id, button);
    this.add(button);
  }

  public void setSetupButtonFunction(Consumer<MButton> setupFunction) {
    this.setupButton = setupFunction;
  }

  private HashMap<String, MButton> filterButtons;
  private Consumer<MButton> setupButton;
  private ColorScheme colors;
}
