package io.github.a5b84.rainbowshenanigans.mixin;

import com.mojang.serialization.Lifecycle;
import io.github.a5b84.rainbowshenanigans.ColorSortableRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(Registry.class)
public abstract class RegistryMixin {

    /** Sorts the item registry */
    // Note: this happens before items are registered
    @Inject(method = "create(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/MutableRegistry;Ljava/util/function/Supplier;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/util/registry/MutableRegistry;", at = @At("HEAD"))
    private static <T, R extends MutableRegistry<T>> void onCreate(RegistryKey<? extends Registry<T>> key, R registry, Supplier<T> defaultEntry, Lifecycle lifecycle, CallbackInfoReturnable<R> cir) {
        //noinspection RedundantCast
        if (key == (Object) Registry.ITEM_KEY) { // Casting to remove a compilation error
            ((ColorSortableRegistry) registry).makeColorSorted();
        }
    }

}
