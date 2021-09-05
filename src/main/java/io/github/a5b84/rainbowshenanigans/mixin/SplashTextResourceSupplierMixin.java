package io.github.a5b84.rainbowshenanigans.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashTextResourceSupplier.class)
public abstract class SplashTextResourceSupplierMixin {

    /** Replaces the vanilla 'Colormatic' splash with a differently colored one */
    @Inject(method = "get", at = @At("RETURN"), cancellable = true)
    private void onGet(CallbackInfoReturnable<String> cir) {
        if ("§1C§2o§3l§4o§5r§6m§7a§8t§9i§ac".equals(cir.getReturnValue())) {
            cir.setReturnValue("§4C§co§6l§eo§ar§2m§3a§1t§5i§dc");
        }
    }

}
