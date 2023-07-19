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
                new ColorPair("#b12f2f", "#ffffff", "Default Dark"),
                new ColorPair("#3A60A7", "#F5F5F5", "Moonlit Ocean"),
                new ColorPair("#0F9B8E", "#ffffff", "Emerald Dusk"),
                new ColorPair("#6A057F", "#D8D8D8", "Grape Twilight"),
                new ColorPair("#A83C09", "#F9F6F7", "Spicy Night"),
                new ColorPair("#7261A3", "#ffffff", "Lavender Haze"),
                new ColorPair("#355070", "#f8f8f2", "Midnight Blue"),
                new ColorPair("#6D597A", "#f8f8f2", "Smoky Amethyst"),
                new ColorPair("#FF6700", "#202020", "Tangerine Dream"),
                new ColorPair("#50CB93", "#202020", "Minted Shadow"));
    }

    private static List<ColorPair> createLightColorPairs() {
        return List.of(
                new ColorPair("#b12f2f", "#ffffff", "Default Light"),
                new ColorPair("#5F0F40", "#E9E9F5", "Berry Fusion"),
                new ColorPair("#D63447", "#E3F2FD", "Sunset Delight"),
                new ColorPair("#106466", "#F3F9FB", "Ocean Spritz"),
                new ColorPair("#56A3A6", "#2A2B2A", "Aqua Harmony"),
                new ColorPair("#36558F", "#E8F1F2", "Blue Iris"),
                new ColorPair("#DB504A", "#F5F5F5", "Spiced Coral"),
                new ColorPair("#F45B69", "#F5F5F5", "Strawberry Fizz"),
                new ColorPair("#364F6B", "#ffffff", "Nautical Echo"),
                new ColorPair("#588B8B", "#F7F7F7", "Sea Salt Mist"));
    }
}
