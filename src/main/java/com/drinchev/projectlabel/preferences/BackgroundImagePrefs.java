package com.drinchev.projectlabel.preferences;

import com.drinchev.projectlabel.resources.ui.BackgroundImagePosition;

public record BackgroundImagePrefs(int opacity, BackgroundImagePosition position) {

    public static BackgroundImagePrefs from(boolean inherited, int opacity, BackgroundImagePosition backgroundPosition) {
        return inherited ? null : new BackgroundImagePrefs(opacity, backgroundPosition);
    }

    public static BackgroundImagePrefs from(int opacity, BackgroundImagePosition backgroundPosition) {
        return new BackgroundImagePrefs(opacity, backgroundPosition);
    }
}
