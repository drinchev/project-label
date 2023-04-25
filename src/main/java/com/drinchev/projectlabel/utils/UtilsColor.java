package com.drinchev.projectlabel.utils;

import java.awt.*;

public class UtilsColor {

    /**
     * Converts a color to hex string
     *
     * @param color The color provided
     * @return The hex string
     */
    public static String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Compares if two colors are the same
     *
     * @param colorA The first color to compare
     * @param colorB The second color to compare
     * @return The return value
     */
    public static Boolean isEqual(Color colorA, Color colorB) {
        return colorA.getRGB() == colorB.getRGB();
    }
}
