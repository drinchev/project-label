package com.drinchev.projectlabel.utils;

import com.intellij.openapi.util.registry.Registry;
import com.intellij.util.ui.JBFont;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class UtilsFont {

    /**
     * Gets a font by a name
     *
     * @param name The font name to look for
     * @return The font itself
     */
    public static Font getFontByName(String name) {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font font : fonts) {
            if (font.getFamily().equals(name)) {
                return font;
            }
        }
        return null;
    }

    /**
     * Sets weight and size of a font
     *
     * @param font   The font to modify
     * @param weight The weight to set
     * @param size   The size to set
     * @return The font itself
     */
    public static Font setAttributes(Font font, Float weight, Float size) {
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.WEIGHT, weight);
        attributes.put(TextAttribute.SIZE, size);
        return font.deriveFont(attributes);
    }

    public static JBFont getStatusBarItemFont() {
        try {
            // while the new UI is experimental, we have to carefully choose the font size
            if (JBFont.class.getDeclaredMethod("smallOrNewUiMedium") != null) {
                return Registry.is("ide.experimental.ui") ? JBFont.medium() : JBFont.small();
            }
        } catch (NoSuchMethodException e) {
            return JBFont.medium();
        }
        throw new IllegalStateException("Unable to determine the status bar font size");
    }

}
