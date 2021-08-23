package io.github.a5b84.rainbowshenanigans;

import io.github.a5b84.rainbowshenanigans.config.ExampleModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class RainbowShenanigansMod implements ClientModInitializer {

    public static final String ID = "rainbow-shenanigans";

    public static ExampleModConfig config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ExampleModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ExampleModConfig.class).getConfig();
    }

}
