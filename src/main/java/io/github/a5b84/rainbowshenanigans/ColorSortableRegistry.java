package io.github.a5b84.rainbowshenanigans;

/**
 * Interface for registries that may sort their elements by color
 */
public interface ColorSortableRegistry {

    /**
     * @return {@code true} if the registry is currently sorting elements by
     * their color
     */
    boolean rainbowShenanigans$isColorSorted();

    /** Makes the registry sort current and future elements by their color */
    void rainbowShenanigans$makeColorSorted();

    /** Sorts the registry's current elements by their color */
    void rainbowShenanigans$colorSort();

}
