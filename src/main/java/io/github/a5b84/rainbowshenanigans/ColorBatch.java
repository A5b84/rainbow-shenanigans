package io.github.a5b84.rainbowshenanigans;

import io.github.a5b84.rainbowshenanigans.SortedDyeColor.ColorMatch;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

public class ColorBatch<T> {

    private String namespace;
    private String idPrefix, idSuffix;
    private boolean registered;

    @SuppressWarnings("unchecked")
    private final T[] entries = (T[]) new Object[SortedDyeColor.COUNT];


    public ColorBatch(Identifier id, ColorMatch match) {
        init(id, match);
    }

    /** Same as {@link #ColorBatch(Identifier, ColorMatch)} but without
     * creating a new object (to save a tiny bit of memory) */
    public void repurpose(Identifier id, ColorMatch match) {
        init(id, match);
        Arrays.fill(entries, null);
    }

    private void init(Identifier id, ColorMatch match) {
        namespace = id.getNamespace();
        idPrefix = id.getPath().substring(0, match.start());
        idSuffix = id.getPath().substring(match.end());
        registered = false;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean matches(Identifier id, ColorMatch match) {
        if (!id.getNamespace().equals(namespace)) return false;

        String path = id.getPath();
        return path.length() - match.end() == idSuffix.length()
                && path.endsWith(idSuffix)
                && match.start() == idPrefix.length()
                && path.startsWith(idPrefix);
    }

    public void addEntry(Identifier id, T entry, SortedDyeColor color) {
        if (entries[color.itemPermutationIndex] != null) {
            throw new IllegalStateException("Two identifiers with color " + color.dyeColor.getName()
                    + " in the same batch: " + entries[color.itemPermutationIndex] + ", " + id);
        }

        entries[color.itemPermutationIndex] = entry;
    }

    public void register(List<T> list, int startIndex) {
        if (registered) return;

        registered = true;
        for (T entry : entries) {
            if (entry != null) {
                list.set(startIndex, entry);
                startIndex++;
            }
        }
    }

}
