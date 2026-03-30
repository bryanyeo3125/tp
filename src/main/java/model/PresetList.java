package model;

import java.util.ArrayList;
import seedu.bitbites.BitbitesException;

//@@author j-kennethh
/**
 * PresetList.java
 * This file defines the PresetList container class that manages a collection of saved Food presets.
 * Provides methods to add, retrieve, and delete template food items.
 */
public class PresetList {
    private ArrayList<Food> presetList;

    public PresetList() {
        this.presetList = new ArrayList<>();
    }

    public PresetList(ArrayList<Food> loadedPresets) {
        this.presetList = loadedPresets;
    }

    public void addPreset(Food preset) {
        this.presetList.add(preset);
    }

    public Food deletePreset(int index) {
        if (index < 0 || index >= presetList.size()) {
            throw new BitbitesException("OOPS!!! Invalid preset index. Please provide a valid index number.");
        }
        return presetList.remove(index);
    }

    public Food getPreset(int index) {
        if (index < 0 || index >= presetList.size()) {
            throw new BitbitesException("OOPS!!! Invalid preset index. Please provide a valid index number.");
        }
        assert index >= 0 && index < presetList.size() : "Index out of bounds: " + index;
        return presetList.get(index);
    }

    public int size() {
        return presetList.size();
    }

    public ArrayList<Food> getPresetList() {
        return this.presetList;
    }
}
//@@author
