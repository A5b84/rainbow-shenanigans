package io.github.a5b84.rainbowshenanigans;

import io.github.a5b84.rainbowshenanigans.config.RainbowShenanigansConfig;
import io.github.a5b84.rainbowshenanigans.config.RainbowShenanigansConfigSerializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;

public class RainbowShenanigansMod implements ClientModInitializer {

    public static final String MOD_ID = "rainbow-shenanigans";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static RainbowShenanigansConfig config;

    /** Main color permutation, should contain every {@link SortedDyeColor}
     * exactly once and should be coherent with {@link SortedDyeColor#itemPermutationIndex} */
    public static SortedDyeColor[] itemPermutation;

    /** List of sheep colors, may have duplicates but may not be empty */
    public static SortedDyeColor[] sheepOrder;

    public static Formatting[] colormaticOrder;

    @Nullable
    public static Reference<TitleScreen> titleScreenReference;


    @Override
    public void onInitializeClient() {
        AutoConfig.register(RainbowShenanigansConfig.class, RainbowShenanigansConfigSerializer::new);
        config = AutoConfig.getConfigHolder(RainbowShenanigansConfig.class).getConfig();

        // Sort the item registry (not doing this earlier because the config
        // wasn't loaded yet and the registry would have been sorted twice)
        long sortStartTime = Util.getMeasuringTimeNano();
        ((ColorSortableRegistry) Registry.ITEM).rainbowShenanigans$makeColorSorted();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            long sortDuration = Util.getMeasuringTimeNano() - sortStartTime;
            LOGGER.info("[Rainbow Shenanigans] Registry sorted in {} ms", sortDuration / 1e6);
        }
    }

}
