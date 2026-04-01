package command;

import java.util.List;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import ui.UserInterface;

public class HistoryCommand extends Command {
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";
        List<NutritionSummary> summaries = foodList.getAllDailySummaries();
        ui.showHistory(summaries);
        return false;
    }
}
