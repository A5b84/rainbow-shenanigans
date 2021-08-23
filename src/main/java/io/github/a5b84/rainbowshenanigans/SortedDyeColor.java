package io.github.a5b84.rainbowshenanigans;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public enum SortedDyeColor {
    WHITE(0, DyeColor.WHITE),
    LIGHT_GRAY(1, DyeColor.LIGHT_GRAY),
    GRAY(2, DyeColor.GRAY),
    BLACK(3, DyeColor.BLACK),
    BROWN(4, DyeColor.BROWN),
    RED(5, DyeColor.RED),
    ORANGE(6, DyeColor.ORANGE),
    YELLOW(7, DyeColor.YELLOW),
    LIME(8, DyeColor.LIME),
    GREEN(9, DyeColor.GREEN),
    CYAN(10, DyeColor.CYAN),
    LIGHT_BLUE(11, DyeColor.LIGHT_BLUE),
    BLUE(12, DyeColor.BLUE),
    PURPLE(13, DyeColor.PURPLE),
    MAGENTA(14, DyeColor.MAGENTA),
    PINK(15, DyeColor.PINK);

    public static final int COLOR_COUNT = values().length;

    public final int order;
    public final DyeColor dyeColor;

    SortedDyeColor(int order, DyeColor dyeColor) {
        this.order = order;
        this.dyeColor = dyeColor;
    }

    public String getName() {
        return dyeColor.getName();
    }

    public static SortedDyeColor getSortedDyeColor(String id) {
        SortedDyeColor color = null;

        if (id.charAt(0) == 'b') {
            if (id.charAt(1) == 'l') {
                if (id.startsWith("ack", 2)) color = BLACK;
                else if (id.startsWith("ue" , 2)) color = BLUE;
            } else if (id.startsWith("rown", 1)) color = BROWN;
        } else if (id.startsWith("cyan")) color = CYAN;
        else if (id.charAt(0) == 'g') {
            if (id.startsWith("ray", 1)) color = GRAY;
            else if (id.startsWith("reen", 1)) color = GREEN;
        } else if (id.startsWith("li")) {
            if (id.startsWith("ght_", 2)) {
                if (id.startsWith("blue", 6)) color = LIGHT_BLUE;
                else if (id.startsWith("gray", 6)) color = LIGHT_GRAY;
            } else if (id.startsWith("me", 2)) color = LIME;
        } else if (id.startsWith("magenta")) color = MAGENTA;
        else if (id.startsWith("orange")) color = ORANGE;
        else if (id.charAt(0) == 'p') {
            if (id.startsWith("ink", 1)) color = PINK;
            else if (id.startsWith("urple", 1)) color = PURPLE;
        } else if (id.startsWith("red")) color = RED;
        else if (id.startsWith("white")) color = WHITE;
        else if (id.startsWith("yellow")) color = YELLOW;

        // Check that the color is an actual color (e.g. filter out redstone)
        if (color != null) {
            int colorLength = color.getName().length();
            if (id.length() > colorLength && isWordCharacter(id.charAt(colorLength))) {
                color = null;
            }
        }

        return color;
    }

    /**
     * @return {@code true} if {@code c} is a word character
     * @see Identifier#isPathCharacterValid(char)
     */
    private static boolean isWordCharacter(char c) {
        return c >= 'a' && c <= 'z' || c >= '0' && c <= '9';
    }

}
