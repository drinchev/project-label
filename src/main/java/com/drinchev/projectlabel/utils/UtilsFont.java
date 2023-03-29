package com.drinchev.projectlabel.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.ui.JBFont;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class UtilsFont {

    private final static Logger LOG = Logger.getInstance(UtilsFont.class);

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
            return JBFont.smallOrNewUiMedium();
        } catch (NoSuchMethodError e) {
            LOG.warn("JBFont.smallOrNewUiMedium() is not available in this version of IntelliJ", e);
            return JBFont.medium();
        }
    }

}
