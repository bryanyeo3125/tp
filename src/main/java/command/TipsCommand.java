package command;

import model.FoodList;
import ui.UserInterface;

public class TipsCommand extends Command {

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        ui.showTips();
        return false;
    }
}
