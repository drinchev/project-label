package com.drinchev.projectlabel.resources.ui;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

public enum BackgroundImagePosition {
    HIDDEN("Hidden"),
    TOP_LEFT("Top Left"),
    TOP_RIGHT("Top Right"),
    BOTTOM_LEFT("Bottom Left"),
    BOTTOM_RIGHT("Bottom Right"),
    CENTER("Center");

    private final String displayName;

    BackgroundImagePosition(@NotNull String displayName) {
        this.displayName = Objects.requireNonNull(displayName);
    }

    public String displayName() {
        return this.displayName;
    }

    public static BackgroundImagePosition findByDisplayName(String displayName) {
        return Stream.of(values())
                .filter(position -> Objects.equals(position.displayName, displayName))
                .findFirst()
                .orElse(BackgroundImagePosition.HIDDEN);
    }
}
