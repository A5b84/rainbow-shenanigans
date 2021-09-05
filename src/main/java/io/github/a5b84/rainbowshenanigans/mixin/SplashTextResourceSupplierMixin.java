package io.github.a5b84.rainbowshenanigans.mixin;

import io.github.a5b84.rainbowshenanigans.ColormaticUtil;
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
        if (ColormaticUtil.isColormatic(cir.getReturnValue())) {
            cir.setReturnValue(ColormaticUtil.getNewColormatic());
        }
    }

}
