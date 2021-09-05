package io.github.a5b84.rainbowshenanigans;

import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

public class ColorBatch<T> {

    private String namespace;
    private String idSuffix;
    private boolean registered;

    @SuppressWarnings("unchecked")
    private final T[] entries = (T[]) new Object[SortedDyeColor.COUNT];


    public ColorBatch(Identifier id, SortedDyeColor color) {
        init(id, color);
    }

    /** Same as {@link #ColorBatch(Identifier, SortedDyeColor)} but without
     * creating a new object (to save a tiny bit of memory) */
    public void repurpose(Identifier id, SortedDyeColor color) {
        init(id, color);
        Arrays.fill(entries, null);
    }

    private void init(Identifier id, SortedDyeColor color) {
        namespace = id.getNamespace();
        idSuffix = id.getPath().substring(color.getName().length());
        registered = false;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean matches(Identifier id, SortedDyeColor color) {
        return id.getNamespace().equals(namespace)
                && id.getPath().length() - color.getName().length() == idSuffix.length()
                && id.getPath().endsWith(idSuffix);
    }

    public void addEntry(Identifier id, T entry, SortedDyeColor color) {
        if (entries[color.mainIndex] != null) {
            throw new IllegalStateException("Two identifiers with color " + color.dyeColor.getName()
                    + " in the same batch: " + entries[color.mainIndex] + ", " + id);
        }

        entries[color.mainIndex] = entry;
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
