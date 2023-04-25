package com.drinchev.projectlabel.utils;

import com.intellij.openapi.util.registry.Registry;

public class UtilsUI {

    public static Boolean isNewUI() {
        return Registry.is("ide.experimental.ui");
    }
}
