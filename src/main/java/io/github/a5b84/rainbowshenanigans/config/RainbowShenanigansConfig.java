package io.github.a5b84.rainbowshenanigans.config;

import io.github.a5b84.rainbowshenanigans.ColorSortableRegistry;
import io.github.a5b84.rainbowshenanigans.ColormaticUtil;
import io.github.a5b84.rainbowshenanigans.RainbowShenanigansMod;
import io.github.a5b84.rainbowshenanigans.SortedDyeColor;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.Background;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.minecraft.util.Formatting.DARK_AQUA;
import static net.minecraft.util.Formatting.DARK_BLUE;
import static net.minecraft.util.Formatting.DARK_GREEN;
import static net.minecraft.util.Formatting.DARK_PURPLE;
import static net.minecraft.util.Formatting.DARK_RED;
import static net.minecraft.util.Formatting.GOLD;
import static net.minecraft.util.Formatting.GREEN;
import static net.minecraft.util.Formatting.LIGHT_PURPLE;
import static net.minecraft.util.Formatting.RED;
import static net.minecraft.util.Formatting.YELLOW;

@Config(name = RainbowShenanigansMod.MOD_ID)
@Background(Background.TRANSPARENT)
public class RainbowShenanigansConfig implements ConfigData {

    @Excluded
    private static final Logger LOGGER = LogManager.getLogger();

    @Tooltip(count = 4)
    public String itemColorPermutation = join(SortedDyeColor.values(), SortedDyeColor::getName, 103);

    @Tooltip(count = 3)
    public String sheepColorOrder = "red,orange,yellow,lime,cyan,blue,purple,magenta";

    @Tooltip(count = 2)
    public String colormaticColorOrder = getDefaultColormaticOrderString();


    public void onChanged() {
        updateItemColorPermutation();
        updateSheepColorOrder();
        updateColormaticColorOrder();
    }

    private void updateItemColorPermutation() {
        String[] colorNames = splitColors(itemColorPermutation, SortedDyeColor.COUNT + 1);
        //      ^ limit = COUNT + 1 so the last value isn't invalid if there
        //      are more values
        SortedDyeColor[] remainingColors = SortedDyeColor.values();
        //      ^ null means the value has been added
        SortedDyeColor[] newOrder = new SortedDyeColor[SortedDyeColor.COUNT];
        int i = 0;

        // Add and filter colors
        for (String name : colorNames) {
            SortedDyeColor color = parseColor(name);
            if (color == null) {
                logInvalidColorWarning(name, "itemColorPermutation");
            } else if (remainingColors[color.ordinal()] == null) {
                LOGGER.warn("[Rainbow Shenanigans] Duplicate color '{}' for option 'itemColorPermutation'", name);
            } else {
                newOrder[i] = color;
                remainingColors[color.ordinal()] = null;
                color.itemPermutationIndex = i;
                i++;
            }
        }

        // Add the remaining colors
        for (SortedDyeColor color : remainingColors) {
            if (color != null) {
                newOrder[i] = color;
                color.itemPermutationIndex = i;
                i++;
            }
        }

        RainbowShenanigansMod.itemPermutation = newOrder;

        // Sort the item registry
        ColorSortableRegistry itemRegistry = (ColorSortableRegistry) Registry.ITEM;
        if (itemRegistry.isColorSorted()) { // See RainbowShenanigansMod#onInitializeClient
            itemRegistry.colorSort();
        }
    }

    private void updateSheepColorOrder() {
        RainbowShenanigansMod.sheepOrder = parseColorOrder(
                sheepColorOrder, 0, RainbowShenanigansConfig::parseColor,
                c -> true, "sheepColorOrder", () -> new SortedDyeColor[0],
                () -> RainbowShenanigansMod.itemPermutation);
    }

    private void updateColormaticColorOrder() {
        RainbowShenanigansMod.colormaticOrder = parseColorOrder(
                colormaticColorOrder, ColormaticUtil.LENGTH + 1,
                RainbowShenanigansConfig::parseFormatting, Formatting::isColor,
                "colormaticColorOrder", () -> new Formatting[0],
                RainbowShenanigansConfig::getDefaultColormaticOrder);
    }

    private static <T> T[] parseColorOrder(String list, int limit, Function<String, T> valueParser, Predicate<T> filter, String optionName, Supplier<T[]> arraySupplier, Supplier<T[]> fallback) {
        // Update the order of colors
        String[] colorNames = splitColors(list, limit);
        List<T> newOrder = new ArrayList<>(colorNames.length);

        // Add and filter colors
        for (String name : colorNames) {
            T value = valueParser.apply(name);
            if (value != null && filter.test(value)) {
                newOrder.add(value);
            } else {
                logInvalidColorWarning(name, optionName);
            }
        }

        return newOrder.isEmpty()
                ? fallback.get()
                : newOrder.toArray(arraySupplier.get());
    }


    private static String[] splitColors(String colors, int limit) {
        return colors.split("\\s*,[\\s,]*", limit);
    }

    @Nullable
    private static SortedDyeColor parseColor(String name) {
        return SortedDyeColor.byName(name.toLowerCase(Locale.ROOT));
    }

    @Nullable
    private static Formatting parseFormatting(String name) {
        Formatting formatting = Formatting.byName(name);
        return formatting != null && formatting.isColor() ? formatting : null;
    }


    private static Formatting[] getDefaultColormaticOrder() {
        return new Formatting[]{DARK_RED, RED, GOLD, YELLOW, GREEN, DARK_GREEN, DARK_AQUA, DARK_BLUE, DARK_PURPLE, LIGHT_PURPLE};
    }

    private static String getDefaultColormaticOrderString() {
        Formatting[] order = getDefaultColormaticOrder();
        return join(order, Formatting::getName, 13 * order.length);
    }

    private static <T> String join(T[] values, Function<T, String> toString, int capacity) {
        StringBuilder builder = new StringBuilder(capacity);
        for (T value : values) {
            if (!builder.isEmpty()) {
                builder.append(',');
            }
            builder.append(toString.apply(value));
        }
        return builder.toString();
    }


    private static void logInvalidColorWarning(String color, String optionName) {
        LOGGER.warn("[Rainbow Shenanigans] Invalid color '{}' for setting '{}'",
                color, I18n.translate("text.autoconfig.rainbow-shenanigans.option." + optionName));
    }

}
