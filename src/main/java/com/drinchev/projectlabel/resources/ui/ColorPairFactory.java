package com.drinchev.projectlabel.resources.ui;

import com.drinchev.projectlabel.utils.UtilsUI;
import java.util.List;

public final class ColorPairFactory {

    private ColorPairFactory() {
        // prevent instantiation
    }

    public static List<ColorPair> createColorPairs() {
        return UtilsUI.isDarkTheme() ? createDarkColorPairs() : createLightColorPairs();
    }

    private static List<ColorPair> createDarkColorPairs() {
        return List.of(
                new ColorPair("#6369D1", "#F5F5F5"),
                new ColorPair("#F8E9A1", "#252525"),
                new ColorPair("#F4A261", "#F7F7F7"),
                new ColorPair("#A7E9AF", "#212121"),
                new ColorPair("#F7CAC9", "#292929"),
                new ColorPair("#9BC995", "#F7F7F7"),
                new ColorPair("#E2B7B5", "#F7F7F7"),
                new ColorPair("#F6B6D1", "#F7F7F7"),
                new ColorPair("#B5EAD7", "#212121"),
                new ColorPair("#D6D6D6", "#212121"));
    }

    private static List<ColorPair> createLightColorPairs() {

        return List.of(
                new ColorPair("#FFE0C7", "#454545"),
                new ColorPair("#D4F4DD", "#2B2D42"),
                new ColorPair("#E3B5A4", "#303C6C"),
                new ColorPair("#F5D5CB", "#4E4B66"),
                new ColorPair("#FFD6BA", "#1A1A1D"),
                new ColorPair("#F0EAD6", "#6B5B95"),
                new ColorPair("#BEE9E8", "#364F6B"),
                new ColorPair("#FFE8D6", "#3F3F3F"),
                new ColorPair("#C7CEEA", "#3E3F3A"),
                new ColorPair("#F6E7D2", "#2E2D4D"));
    }
}
