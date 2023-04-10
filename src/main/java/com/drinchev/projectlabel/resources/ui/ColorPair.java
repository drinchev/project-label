package com.drinchev.projectlabel.resources.ui;

import java.awt.*;

public record ColorPair(Color background, Color foreground) {

    public ColorPair {
        if (background == null) {
            throw new IllegalArgumentException("background color cannot be null");
        }
        if (foreground == null) {
            throw new IllegalArgumentException("foreground color cannot be null");
        }
    }

    public ColorPair(String background, String foreground) {
        this(Color.decode(background), Color.decode(foreground));
    }
}
