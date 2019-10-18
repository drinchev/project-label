package com.drinchev.projectlabel.resources.ui;

import com.intellij.ui.ColorPicker;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColorFieldMouseListener implements MouseListener {

    private final ColorField colorField;

    /**
     * Constructor
     */
    ColorFieldMouseListener(ColorField colorField) {
        this.colorField = colorField;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Color color = ColorPicker.showDialog(
                this.colorField.getField(),
                this.colorField.getName(),
                this.colorField.getColor(),
                false,
                null,
                false
        );
        if (color != null) {
            this.colorField.setColor(color);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

}
