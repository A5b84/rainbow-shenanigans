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
    public static ColorMatch byPath(String path) {
        SortedDyeColor color = null;
        int i;

        for (i = 0; i < path.length(); i++) {
            if (path.charAt(i) == 'b') {
                if (path.charAt(i + 1) == 'l') {
                    if (path.startsWith("ack", i + 2)) color = BLACK;
                    else if (path.startsWith("ue", i + 2)) color = BLUE;
                } else if (path.startsWith("rown", i + 1)) color = BROWN;
            } else if (path.startsWith("cyan", i)) color = CYAN;
            else if (path.startsWith("gr", i)) {
                if (path.startsWith("ay", i + 2)) color = GRAY;
                else if (path.startsWith("een", i + 2)) color = GREEN;
            } else if (path.startsWith("li", i)) {
                if (path.startsWith("ght_", i + 2)) {
                    if (path.startsWith("blue", i + 6)) color = LIGHT_BLUE;
                    else if (path.startsWith("gray", i + 6)) color = LIGHT_GRAY;
                } else if (path.startsWith("me", i + 2)) color = LIME;
            } else if (path.startsWith("magenta", i)) color = MAGENTA;
            else if (path.startsWith("orange", i)) color = ORANGE;
            else if (path.charAt(i) == 'p') {
                if (path.startsWith("ink", i + 1)) color = PINK;
                else if (path.startsWith("urple", i + 1)) color = PURPLE;
            } else if (path.startsWith("red", i)) color = RED;
            else if (path.startsWith("white", i)) color = WHITE;
            else if (path.startsWith("yellow", i)) color = YELLOW;

            if (color != null) {
                // Check that the color is not part of a word (e.g. filter out redstone)
                int colorEnd = i + color.getName().length();
                if (colorEnd < path.length() && isWordCharacter(path.charAt(colorEnd))) {
                    color = null;
                } else {
                    break; // Not part of a word
                }
            }

            // Move to the next word
            for (; i < path.length(); i++) {
                if (!isWordCharacter(path.charAt(i))) break;
            }
        }

        return color != null ? new ColorMatch(color, i) : null;
    }

    /**
     * @return {@code true} if {@code c} is a word character
     * @see Identifier#isPathCharacterValid(char)
     */
    private static boolean isWordCharacter(char c) {
        return c >= 'a' && c <= 'z' || c >= '0' && c <= '9';
    }

    public static record ColorMatch(SortedDyeColor color, int start) {
        public int end() {
            return start + color.getName().length();
        }
    }

}
