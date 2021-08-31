package io.github.a5b84.rainbowshenanigans.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class RainbowShenanigansConfigSerializer<T extends RainbowShenanigansConfig> extends JanksonConfigSerializer<T> {

    public RainbowShenanigansConfigSerializer(Config definition, Class<T> configClass) {
        super(definition, configClass);
    }

    @Override
    public void serialize(T config) throws SerializationException {
        super.serialize(config);
        config.onChanged(); // Also called when the game is launched
    }
}
