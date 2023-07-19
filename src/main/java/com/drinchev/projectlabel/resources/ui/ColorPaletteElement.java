package com.drinchev.projectlabel.resources.ui;

import static java.util.Objects.requireNonNull;

import com.intellij.util.ui.JBUI;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.util.Map;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class ColorPaletteElement extends JButton {

    private static final Map<Key, Object> RENDERING_HINTS = Map.of(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY,
            RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    private ColorPair colors;

    public ColorPaletteElement(@NotNull ColorPair colors) {
        super();
        setContentAreaFilled(false); // don't paint button background
        setBorderPainted(false); // don't paint button border
        setFocusPainted(false); // don't paint focus indicator
        //        setBorder(null); // remove border
        //        setMargin(JBUI.emptyInsets()); // remove insets
        this.colors = requireNonNull(colors);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(RENDERING_HINTS);

        final int border = 4;
        int diameter = Math.min(getWidth(), getHeight()) - border;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        g2d.setColor(colors.background());
        g2d.fillArc(x, y, diameter, diameter, 45, 180);

        g2d.setColor(colors.foreground());
        g2d.fillArc(x, y, diameter, diameter, 225, 180);

        if (isSelected()) {
            g2d.setColor(JBUI.CurrentTheme.Focus.focusColor());
            g2d.setStroke(new BasicStroke(JBUI.scale(2)));
            g2d.drawArc(x, y, diameter, diameter, 0, 360);
        }
        g2d.dispose();
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

    public ColorPair getColors() {
        return colors;
    }

    // make sure to keep the button square
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width = size.height;
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.width = size.height;
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.width = size.height;
        return size;
    }
}
