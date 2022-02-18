package io.github.a5b84.rainbowshenanigans;

import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public final class ColormaticUtil {

    private ColormaticUtil() {}


    private static final String STRING = "Colormatic";
    public static final int LENGTH = STRING.length();

    /** Checks whether a string spells "Colormatic" with a formatting code
     * before every letter */
    public static boolean isColormatic(@Nullable String s) {
        if (s == null || s.length() != LENGTH * 3) {
            return false;
        }

        for (int i = 0; i < LENGTH; i++) {
            if (s.charAt(3 * i) != Formatting.FORMATTING_CODE_PREFIX
                    || s.charAt(3 * i + 2) != STRING.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /** @return the recolored "Colormatic" splash text */
    public static String getNewColormatic() {
        StringBuilder builder = new StringBuilder(3 * LENGTH);
        int colorCount = RainbowShenanigansMod.colormaticOrder.length;
        for (int i = 0; i < LENGTH; i++) {
            builder.append(RainbowShenanigansMod.colormaticOrder[i % colorCount].toString());
            builder.append(STRING.charAt(i));
        }
        return builder.toString();
    }

}
