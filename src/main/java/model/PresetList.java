package model;

import java.util.ArrayList;
import seedu.bitbites.BitbitesException;

//@@author j-kennethh
/**
 * Represents a collection of saved food presets.
 * This class manages a list of {@code Food} templates that users can
 * quickly log without re-entering nutritional details.
 */
public class PresetList {
    private ArrayList<Food> presetList;

    /**
     * Constructs an empty {@code PresetList}.
     * Initialises the underlying list to store new food presets.
     */
    public PresetList() {
        this.presetList = new ArrayList<>();
    }

    /**
     * Constructs a {@code PresetList} using an existing list of loaded presets.
     * This is primarily used when loading saved presets from persistent storage.
     *
     * @param loadedPresets An {@code ArrayList} of {@code Food} objects to initialise the preset list.
     */
    public PresetList(ArrayList<Food> loadedPresets) {
        this.presetList = loadedPresets;
    }

    /**
     * Adds a new food preset to the collection.
     *
     * @param preset The {@code Food} object to be saved as a preset.
     */
    public void addPreset(Food preset) {
        this.presetList.add(preset);
    }

    /**
     * Removes and returns the preset food item at the specified index.
     *
     * @param index The zero-based index of the preset to be deleted.
     * @return The {@code Food} object that was removed from the list.
     * @throws BitbitesException If the provided index is out of bounds.
     */
    public Food deletePreset(int index) {
        if (index < 0 || index >= presetList.size()) {
            throw new BitbitesException("OOPS!!! Invalid preset index. Please provide a valid index number.");
        }
        return presetList.remove(index);
    }

    /**
     * Retrieves the preset food item at the specified index without removing it.
     *
     * @param index The zero-based index of the preset to retrieve.
     * @return The requested {@code Food} preset.
     * @throws BitbitesException If the provided index is out of bounds.
     */
    public Food getPreset(int index) {
        if (index < 0 || index >= presetList.size()) {
            throw new BitbitesException("OOPS!!! Invalid preset index. Please provide a valid index number.");
        }
        assert index >= 0 && index < presetList.size() : "Index out of bounds: " + index;
        return presetList.get(index);
    }

    /**
     * Returns the total number of presets currently saved in the list.
     *
     * @return The size of the preset list.
     */
    public int size() {
        return presetList.size();
    }

    public ArrayList<Food> getPresetList() {
        return this.presetList;
    }
}
//@@author
