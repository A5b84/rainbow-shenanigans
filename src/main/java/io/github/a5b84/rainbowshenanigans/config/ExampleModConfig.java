package io.github.a5b84.rainbowshenanigans.config;

import io.github.a5b84.rainbowshenanigans.RainbowShenanigansMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = RainbowShenanigansMod.ID)
public class ExampleModConfig implements ConfigData {

    public String something = "example";

}
