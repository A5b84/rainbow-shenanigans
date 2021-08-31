package io.github.a5b84.rainbowshenanigans;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

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

    public static SortedDyeColor[] order = values();
    public static SortedDyeColor[] sheepOrder = order;
    public static final int COLOR_COUNT = order.length;

    /** Index of the color in {@link #values()}
     * ({@code values()[this.index] == this}) */
    public final int defaultIndex;
    /** Index of the color in {@link #order}
     * ({@code currentOrder[this.index] == this}) */
    public int index;
    public final DyeColor dyeColor;

    SortedDyeColor(int defaultIndex, DyeColor dyeColor) {
        this.defaultIndex = index = defaultIndex;
        this.dyeColor = dyeColor;
    }

    public String getName() {
        return dyeColor.getName();
    }


    @Nullable
    public static SortedDyeColor byName(String name) {
        for (SortedDyeColor color : values()) {
            if (color.dyeColor.getName().equals(name)) {
                return color;
            }
        }
        return null;
    }

    /**
     * @return the color corresponding to {@code id} (the path part of an
     * {@link Identifier}) or null
     */
    public static SortedDyeColor byPath(String path) {
        SortedDyeColor color = null;

        if (path.charAt(0) == 'b') {
            if (path.charAt(1) == 'l') {
                if (path.startsWith("ack", 2)) color = BLACK;
                else if (path.startsWith("ue" , 2)) color = BLUE;
            } else if (path.startsWith("rown", 1)) color = BROWN;
        } else if (path.startsWith("cyan")) color = CYAN;
        else if (path.charAt(0) == 'g') {
            if (path.startsWith("ray", 1)) color = GRAY;
            else if (path.startsWith("reen", 1)) color = GREEN;
        } else if (path.startsWith("li")) {
            if (path.startsWith("ght_", 2)) {
                if (path.startsWith("blue", 6)) color = LIGHT_BLUE;
                else if (path.startsWith("gray", 6)) color = LIGHT_GRAY;
            } else if (path.startsWith("me", 2)) color = LIME;
        } else if (path.startsWith("magenta")) color = MAGENTA;
        else if (path.startsWith("orange")) color = ORANGE;
        else if (path.charAt(0) == 'p') {
            if (path.startsWith("ink", 1)) color = PINK;
            else if (path.startsWith("urple", 1)) color = PURPLE;
        } else if (path.startsWith("red")) color = RED;
        else if (path.startsWith("white")) color = WHITE;
        else if (path.startsWith("yellow")) color = YELLOW;

        // Check that the color is an actual color (e.g. filter out redstone)
        if (color != null) {
            int colorLength = color.getName().length();
            if (path.length() > colorLength && isWordCharacter(path.charAt(colorLength))) {
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
