package io.github.a5b84.rainbowshenanigans.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public abstract class SplashTextResourceSupplierMixin {

    @Shadow @Final private List<String> splashTexts;

    /** Replaces the vanilla 'Colormatic' splash with a differently colored one */
    @Inject(method = "apply", at = @At("RETURN"))
    private void onAfterApply(CallbackInfo ci) {
        int i = 0;
        for (String splash : splashTexts) {
            if ("§1C§2o§3l§4o§5r§6m§7a§8t§9i§ac".equals(splash)) {
                splashTexts.set(i, "§4C§co§6l§eo§ar§2m§3a§1t§5i§dc");
            }
            i++;
        }
    }

}
