package dev.mimgr.custom;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;

public class MScrollPane extends JScrollPane {

    public MScrollPane(MTextArea textArea) {
        super(textArea);
        Init();
    }

    public void Init() {
        // Customize the vertical scrollbar
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setUI(new SmallCustomScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(5, Integer.MAX_VALUE));  // Set width to 8 pixels

        // Customize the horizontal scrollbar (optional)
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setUI(new SmallCustomScrollBarUI());
        horizontalScrollBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 5)); // Set height to 8 pixels
    }

    // Custom ScrollBar UI class with small size
    static class SmallCustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = colors.m_blue;            // Thumb color
            this.trackColor = colors.m_bg_1;      // Track color
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        private ColorScheme colors = ColorTheme.get_colorscheme(ColorTheme.theme.THEME_DARK_EVERFOREST);
    }
}
