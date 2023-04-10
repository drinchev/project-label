package com.drinchev.projectlabel.resources.ui;

import static java.util.Objects.requireNonNull;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.util.Map;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class ColorPaletteElement extends JPanel {

    private static final Map<Key, Object> RENDERING_HINTS = Map.of(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY,
            RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    private ColorPair colors;

    public ColorPaletteElement() {}

    public ColorPaletteElement(@NotNull ColorPair colors) {
        this.colors = requireNonNull(colors);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(40, 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(RENDERING_HINTS);

        int diameter = Math.min(getWidth(), getHeight()) - 4;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        g2d.setColor(colors.background());
        g2d.fillArc(x, y, diameter, diameter, 45, 180);

        g2d.setColor(colors.foreground());
        g2d.fillArc(x, y, diameter, diameter, 225, 180);
        g2d.dispose();
    }

    public void setColors(@NotNull ColorPair colors) {
        this.colors = requireNonNull(colors);
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        ColorPaletteElement paletteElement = new ColorPaletteElement(new ColorPair("#6369D1", "#F5F5F5"));
        paletteElement.setMinimumSize(new Dimension(20, 20));
        frame.add(paletteElement);
        frame.pack();
        // center frame on screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
