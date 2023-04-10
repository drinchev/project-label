package com.drinchev.projectlabel.resources.ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ColorPaletteChooser {
    private ColorPaletteElement element01;
    private ColorPaletteElement element02;
    private ColorPaletteElement element03;
    private ColorPaletteElement element04;
    private ColorPaletteElement element05;
    private ColorPaletteElement element06;
    private ColorPaletteElement element07;
    private ColorPaletteElement element08;
    private ColorPaletteElement element09;
    private ColorPaletteElement element10;
    private JPanel rootPanel;

    private List<ColorPaletteElement> elements = List.of(
            element01, element02, element03, element04, element05, element06, element07, element08, element09,
            element10);

    public void setColors(List<ColorPair> pairs) {
        if (pairs.size() != 10) {
            throw new IllegalArgumentException("ColorPaletteChooser requires 10 colors");
        }
        for (int i = 0; i < pairs.size(); i++) {
            elements.get(i).setColors(pairs.get(i));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        ColorPaletteChooser chooser = new ColorPaletteChooser();
        chooser.elements.forEach(e -> e.setMinimumSize(new Dimension(20, 20)));
        chooser.setColors(ColorPairFactory.createColorPairs());
        frame.add(chooser.rootPanel);
        frame.pack();
        // center frame on screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
