package dev.mimgr.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class MComboBox<T> extends JComboBox<T> {
  public MComboBox(T[] items) {
    super(items);
    setRenderer(new MComboBoxRenderer());
    setUI(new MComboBoxUI());
  }

  public class MComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
      MButton button = new MButton("▼");
      button.setBorderRadius(0);
      button.setBorderWidth(2);
      button.setBorderColor(Color.BLACK);
      button.setFont(new Font("Arial", Font.BOLD, 12));
      button.setBackground(Color.DARK_GRAY);
      button.setForeground(Color.WHITE);
      return button;
    }

    @Override
    public void installUI(JComponent c) {
      super.installUI(c);
      comboBox.setBackground(Color.WHITE);
    }
  }

  public class MComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
    public MComboBoxRenderer() {
      setOpaque(false);
      setFont(new Font("Arial", Font.BOLD, 14));
      setHorizontalAlignment(LEFT);
      setVerticalAlignment(CENTER);
      setBorder(new EmptyBorder(new Insets(0, 10, 0, 10)));
      setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
      if (isSelected) {
        setBackground(backgroundColor);
        setForeground(Color.BLACK);
      } else {
        setBackground(cellBackgroundColor);
        setForeground(Color.WHITE);
      }

      setText(value != null ? value.toString() : ""); // Display item text

      return this;
    }

    @Override
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      if (isFocusOwner()) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, getWidth(), getHeight());


      }
      // g2.setColor(borderColor);
      // g2.setStroke(new BasicStroke(borderWidth));
      // g2.drawLine(0, getHeight(), getWidth(), getHeight());

      super.paintComponent(g);
      g2.dispose();
    }
  }

  @Override
  public void setBackground(Color colors) {
    this.backgroundColor = colors;
    repaint();
  }

  public void setBorderColor(Color colors) {
    this.borderColor = colors;
    repaint();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Custom JComboBox Demo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(300, 200);

      String[] items = {"Option 1", "Option 2", "Option 3", "Option 4"};
      MComboBox<String> comboBox = new MComboBox<>(items);
      comboBox.setPreferredSize(new Dimension(200, 30));

      frame.setLayout(new FlowLayout());
      frame.add(comboBox);
      frame.setVisible(true);
    });
  }

  private Color backgroundColor = Color.BLUE;
  private Color cellBackgroundColor = Color.BLUE;
  private Color borderColor = Color.BLACK;
  private int borderWidth = 1;
}
