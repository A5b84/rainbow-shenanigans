package io.github.a5b84.rainbowshenanigans.mixin;

import io.github.a5b84.rainbowshenanigans.SortedDyeColor;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.spongepowered.asm.mixin.injection.At.Shift.BY;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin {

    /** Replaces the length of the vanilla color array */
    @ModifyVariable(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DyeColor;values()[Lnet/minecraft/util/DyeColor;", shift = BY, by = 3),
            index = 16)
    private int modifyDyeColorCount(int old) {
        return SortedDyeColor.sheepOrder.length;
    }

    /** Replaces the current and next colors */
    @Redirect(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DyeColor;byId(I)Lnet/minecraft/util/DyeColor;"))
    private DyeColor dyeColorProxy(int id) {
        return SortedDyeColor.sheepOrder[id].dyeColor;
    }

}
