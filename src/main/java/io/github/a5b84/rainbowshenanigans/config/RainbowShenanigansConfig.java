package io.github.a5b84.rainbowshenanigans.config;

import io.github.a5b84.rainbowshenanigans.ColorSortableRegistry;
import io.github.a5b84.rainbowshenanigans.RainbowShenanigansMod;
import io.github.a5b84.rainbowshenanigans.SortedDyeColor;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Config(name = RainbowShenanigansMod.MOD_ID)
public class RainbowShenanigansConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip(count = 4)
    public String colorOrder = "";

    @ConfigEntry.Gui.Tooltip(count = 3)
    public String sheepColorOrder = "red,orange,yellow,lime,cyan,blue,purple,magenta";


    public void onChanged() {
        updateColorOrder();
        updateSheepColorOrder();
    }

    private void updateColorOrder() {
        String[] colorNames = splitColors(colorOrder, SortedDyeColor.COLOR_COUNT);
        SortedDyeColor[] remainingColors = SortedDyeColor.values();
        //      ^ null means the value has been added
        SortedDyeColor[] newOrder = new SortedDyeColor[SortedDyeColor.COLOR_COUNT];
        int i = 0;

        // Add the colors in the config
        for (String name : colorNames) {
            SortedDyeColor color = parseColor(name);
            if (color != null && remainingColors[color.defaultIndex] != null) {
                newOrder[i] = color;
                remainingColors[color.defaultIndex] = null;
                color.index = i;
                i++;
            }
        }

        // Add the remaining colors
        for (SortedDyeColor color : remainingColors) {
            if (color != null) {
                newOrder[i] = color;
                color.index = i;
                i++;
            }
        }

        SortedDyeColor.order = newOrder;

        // Sort the item registry
        ColorSortableRegistry itemRegistry = (ColorSortableRegistry) Registry.ITEM;
        if (itemRegistry.isColorSorted()) { // See RainbowShenanigansMod#onInitializeClient
            itemRegistry.colorSort();
        }
    }

    private void updateSheepColorOrder() {
        // Update the order of colors
        String[] colorNames = splitColors(sheepColorOrder, 0);
        List<SortedDyeColor> newOrder = new ArrayList<>(colorNames.length);

        // Add the colors in the config
        for (String name : colorNames) {
            SortedDyeColor color = parseColor(name);
            if (color != null) {
                newOrder.add(color);
            }
        }

        SortedDyeColor.sheepOrder = newOrder.isEmpty()
                ? SortedDyeColor.order
                : newOrder.toArray(new SortedDyeColor[0]);
    }



    private static String[] splitColors(String colors, int limit) {
        return colors.split("\\s*,[\\s,]*", limit);
    }

    @Nullable
    private static SortedDyeColor parseColor(String name) {
        return SortedDyeColor.byName(name.toLowerCase(Locale.ROOT));
    }

}
