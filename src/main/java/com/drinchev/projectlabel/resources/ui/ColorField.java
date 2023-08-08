package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.utils.UtilsColor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

class ColorField {

    private JTextField field;
    private JPanel panel;
    private Color color;
    private String name;

    private final List<ColorFieldListener> colorFieldListeners = new ArrayList<>();

    ColorField(JTextField field, JPanel panel, String name) {
        this.field = field;
        this.panel = panel;
        this.name = name;
        this.field.setEditable(false);
        this.field.addMouseListener(new ColorFieldMouseListener(this));
    }

    Color getColor() {
        return this.color;
    }

    void setColor(Color color) {
        this.color = color;
        field.setText(UtilsColor.toHex(this.color));
        panel.setBackground(this.color);
        notifyColorListeners();
    }

    JTextField getField() {
        return this.field;
    }

    String getName() {
        return this.name;
    }

    void addColorFieldListener(ColorFieldListener listener) {
        this.colorFieldListeners.add(listener);
    }

    void removeColorFieldListener(ColorFieldListener listener) {
        this.colorFieldListeners.remove(listener);
    }

    void notifyColorListeners() {
        this.colorFieldListeners.forEach(listener -> listener.onColorChanged());
    }

    interface ColorFieldListener {
        void onColorChanged();
    }
}
