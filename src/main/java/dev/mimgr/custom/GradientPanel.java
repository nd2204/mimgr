package dev.mimgr.custom;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;

    public GradientPanel(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cast Graphics to Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        // Create a GradientPaint object for linear gradient
        int width = getWidth();
        int height = getHeight();
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, width, height, endColor);

        // Apply the GradientPaint
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);
    }
}
