package com.drinchev.projectlabel.resources.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
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

    private List<ColorPaletteSelectionListener> listeners = new ArrayList<>();

    private Stream<ColorPaletteElement> elements() {
        return Stream.of(
                element01, element02, element03, element04, element05, element06, element07, element08, element09,
                element10);
    }

    public void preSelect(ColorPair colorPair) {
        if (colorPair == null) {
            elements().forEach(element -> element.setSelected(false));
            return;
        }
        elements().forEach(element -> element.setSelected(element.getColors().equals(colorPair)));
    }

    private void createUIComponents() {
        List<ColorPair> colorPairs = ColorPairFactory.createColorPairs();
        element01 = new ColorPaletteElement(colorPairs.get(0));
        element02 = new ColorPaletteElement(colorPairs.get(1));
        element03 = new ColorPaletteElement(colorPairs.get(2));
        element04 = new ColorPaletteElement(colorPairs.get(3));
        element05 = new ColorPaletteElement(colorPairs.get(4));
        element06 = new ColorPaletteElement(colorPairs.get(5));
        element07 = new ColorPaletteElement(colorPairs.get(6));
        element08 = new ColorPaletteElement(colorPairs.get(7));
        element09 = new ColorPaletteElement(colorPairs.get(8));
        element10 = new ColorPaletteElement(colorPairs.get(9));

        elements()
                .forEach(element -> element.addActionListener(e -> {
                    elements().forEach(element1 -> element1.setSelected(false));
                    element.setSelected(true);
                    listeners.forEach(listener -> listener.onColorPaletteSelected(element.getColors()));
                }));
    }

    public void addSelectionListener(ColorPaletteSelectionListener listener) {
        listeners.add(listener);
    }

    public void removeSelectionListener(ColorPaletteSelectionListener listener) {
        listeners.remove(listener);
    }

    public interface ColorPaletteSelectionListener {
        void onColorPaletteSelected(ColorPair colorPair);
    }
}
