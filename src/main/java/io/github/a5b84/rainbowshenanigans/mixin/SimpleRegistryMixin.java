package io.github.a5b84.rainbowshenanigans.mixin;

import com.google.common.collect.Iterators;
import com.mojang.serialization.Lifecycle;
import io.github.a5b84.rainbowshenanigans.ColorBatch;
import io.github.a5b84.rainbowshenanigans.ColorSortableRegistry;
import io.github.a5b84.rainbowshenanigans.SortedDyeColor;
import io.github.a5b84.rainbowshenanigans.SortedDyeColor.ColorMatch;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry.Reference;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Objects;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements ColorSortableRegistry {

    @Shadow @Final private ObjectList<Reference<T>> rawIdToEntry;

    @Shadow @Nullable public abstract Identifier getId(T value);

    @Unique private ObjectList<T> sortedEntries;
    @Unique private ColorBatch<T> batch;
    @Unique private int batchStartIndex;


    // @Injects

    /** Adds the new value to {@link #sortedEntries} */
    @Inject(method = "set(ILnet/minecraft/util/registry/RegistryKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;Z)Lnet/minecraft/util/registry/RegistryEntry;",
            at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectList;size(I)V", shift = At.Shift.AFTER, remap = false))
    private <V extends T> void onSet(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle, boolean checkDuplicateKeys, CallbackInfoReturnable<V> cir) {
        if (rainbowShenanigans$isColorSorted()) {
            if (rawId >= sortedEntries.size()) {
                sortedEntries.size(rawIdToEntry.size());
                addEntry(rawId, key.getValue(), entry);
            } else {
                sortedEntries.set(rawId, entry); // Not retroactively sorting
            }
        }
    }

    /** Replaces the default iterator with the sorted one if applicable */
    @Inject(method = "iterator", at = @At("HEAD"), cancellable = true)
    private void onIterator(CallbackInfoReturnable<Iterator<T>> cir) {
        if (rainbowShenanigans$isColorSorted()) {
            if (batch != null) {
                registerBatch();
                batch = null;
            }
            cir.setReturnValue(Iterators.filter(sortedEntries.iterator(), Objects::nonNull));
        }
    }


    // Sorting logic

    @Unique
    private void addEntry(int rawId, Identifier id, T entry) {
        ColorMatch match = SortedDyeColor.byPath(id.getPath());

        if (match == null) {
            // Regular item, register the last batch
            if (batch != null) {
                registerBatch();
                // Not setting batch to null in order to reuse it later
            }
            sortedEntries.set(rawId, entry);

        } else {
            if (batch != null && !batch.matches(id, match)) {
                // Item doesn't fit in the current batch, start a new one
                registerBatch();
                batch.repurpose(id, match);
                batchStartIndex = rawId;
            }

            if (batch == null) {
                // Start a new batch
                batch = new ColorBatch<>(id, match);
                batchStartIndex = rawId;
            }

            batch.addEntry(id, entry, match.color());
        }
    }

    @Unique
    private void registerBatch() {
        batch.register(sortedEntries, batchStartIndex);
    }


    // ColorSortableRegistry implementation

    @Override
    public boolean rainbowShenanigans$isColorSorted() {
        return sortedEntries != null;
    }

    @Override
    public void rainbowShenanigans$makeColorSorted() {
        int capacity = ((ObjectArrayList<T>) rawIdToEntry).elements().length;
        sortedEntries = new ObjectArrayList<>(capacity);
        rainbowShenanigans$colorSort();
    }

    @Override
    public void rainbowShenanigans$colorSort() {
        if (!rainbowShenanigans$isColorSorted()) {
            throw new IllegalStateException("Tried to sort a registry that isn't color-sorted");
        }

        sortedEntries.clear(); // Maybe not needed
        sortedEntries.size(rawIdToEntry.size());

        if (!rawIdToEntry.isEmpty()) {
            // Sort existing entries
            int rawId = 0;
            for (Reference<T> reference : rawIdToEntry) {
                if (reference != null) {
                    T entry = reference.value();
                    Identifier id = getId(entry);
                    //noinspection ConstantConditions
                    addEntry(rawId, id, entry);
                }
                rawId++;
            }
        }
    }
}
