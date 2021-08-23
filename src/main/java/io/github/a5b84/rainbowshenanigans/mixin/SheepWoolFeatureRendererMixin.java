package io.github.a5b84.rainbowshenanigans.mixin;

import io.github.a5b84.rainbowshenanigans.SortedDyeColor;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin {

    @Redirect(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DyeColor;byId(I)Lnet/minecraft/util/DyeColor;"))
    private DyeColor dyeColorProxy(int id) {
        return SortedDyeColor.values()[id].dyeColor;
    }

}
