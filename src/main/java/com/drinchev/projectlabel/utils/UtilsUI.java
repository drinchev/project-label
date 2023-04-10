package com.drinchev.projectlabel.utils;

import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.JBColor;

public class UtilsUI {

    public static Boolean isNewUI() {
        return Registry.is("ide.experimental.ui");
    }

    public static Boolean isDarkTheme() {
        return !JBColor.isBright();
    }
}
