package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.utils.UtilsColor;

import javax.swing.*;
import java.awt.*;

class ColorField {

    private JTextField field;
    private JPanel panel;
    private Color color;
    private String name;

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
    }

    JTextField getField() {
        return this.field;
    }

    String getName() {
        return this.name;
    }

}
