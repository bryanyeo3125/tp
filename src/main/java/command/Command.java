package command;

import model.FoodList;
import model.PresetList;
import ui.UserInterface;

public abstract class Command {
    public abstract boolean execute(FoodList foodList, UserInterface ui);

    public boolean execute(FoodList foodList, PresetList presets, UserInterface ui) {
        return execute(foodList, ui);
    }
}
