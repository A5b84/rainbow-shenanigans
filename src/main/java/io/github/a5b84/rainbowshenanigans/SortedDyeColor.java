package io.github.a5b84.rainbowshenanigans;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public enum SortedDyeColor {
    WHITE(DyeColor.WHITE),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY),
    GRAY(DyeColor.GRAY),
    BLACK(DyeColor.BLACK),
    BROWN(DyeColor.BROWN),
    RED(DyeColor.RED),
    ORANGE(DyeColor.ORANGE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    GREEN(DyeColor.GREEN),
    CYAN(DyeColor.CYAN),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    BLUE(DyeColor.BLUE),
    PURPLE(DyeColor.PURPLE),
    MAGENTA(DyeColor.MAGENTA),
    PINK(DyeColor.PINK);


    public static final int COUNT = values().length;


    /** Index of the color in {@link RainbowShenanigansMod#itemPermutation} */
    public int itemPermutationIndex;

    public final DyeColor dyeColor;


    SortedDyeColor(DyeColor dyeColor) {
        itemPermutationIndex = ordinal();
        this.dyeColor = dyeColor;
    }


    public String getName() {
        return dyeColor.getName();
    }

    @Nullable
    public static SortedDyeColor byName(String name) {
        for (SortedDyeColor color : values()) {
            if (color.getName().equals(name)) {
                return color;
            }
        }
        return null;
    }

    /**
     * @return the color corresponding to {@code path} (the path part of an
     * {@link Identifier}) or null
     */
    @Nullable
    public static SortedDyeColor byPath(String path) {
        SortedDyeColor color = null;

        if (path.charAt(0) == 'b') {
            if (path.charAt(1) == 'l') {
                if (path.startsWith("ack", 2)) color = BLACK;
                else if (path.startsWith("ue", 2)) color = BLUE;
            } else if (path.startsWith("rown", 1)) color = BROWN;
        } else if (path.startsWith("cyan")) color = CYAN;
        else if (path.startsWith("gr")) {
            if (path.startsWith("ay", 2)) color = GRAY;
            else if (path.startsWith("een", 2)) color = GREEN;
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
