package com.drinchev.projectlabel.resources.ui;

import java.awt.*;

public record ColorPair(Color background, Color text, String colorName) {

    public ColorPair {
        if (background == null) {
            throw new IllegalArgumentException("background color cannot be null");
        }
        if (text == null) {
            throw new IllegalArgumentException("foreground color cannot be null");
        }
    }

    public ColorPair(String background, String text, String colorName) {
        this(Color.decode(background), Color.decode(text), colorName);
    }

    public boolean sameColors(ColorPair other) {
        return background.equals(other.background) && text.equals(other.text);
    }
}
