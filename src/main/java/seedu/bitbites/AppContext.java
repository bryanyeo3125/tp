package seedu.bitbites;

import model.FoodList;
import model.PresetList;
import ui.UserInterface;

public class AppContext {
    private final FoodList foodList;
    private final PresetList presetList;
    private final UserInterface ui;

    public AppContext(FoodList foodList, PresetList presetList, UserInterface ui) {
        this.foodList = foodList;
        this.presetList = presetList;
        this.ui = ui;
    }

    public FoodList getFoodList() {
        return foodList;
    }

    public PresetList getPresetList() {
        return presetList;
    }

    public UserInterface getUi() {
        return ui;
    }
}
