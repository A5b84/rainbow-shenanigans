package io.github.a5b84.rainbowshenanigans.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TitleScreen.class)
public interface TitleScreenAccessor {

    @Accessor
    String getSplashText();

    @Accessor
    void setSplashText(String splashText);

}
