package com.drinchev.projectlabel.utils;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

public final class UtilsIcon {

    private UtilsIcon() {}

    public static Icon loadRasterizedIcon(String path) {
        return IconLoader.getIcon(path, UtilsIcon.class);
    }

    public static Icon disabledIcon(Icon icon) {
        return IconLoader.getDisabledIcon(icon);
    }
}
