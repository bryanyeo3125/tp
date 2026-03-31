package command;

import java.util.List;
import model.FoodList;
import model.NutritionSummary;
import ui.UserInterface;

public class HistoryCommand extends Command {
    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        assert foodList != null : "FoodList should not be null";
        List<NutritionSummary> summaries = foodList.getAllDailySummaries();
        ui.showHistory(summaries);
        return false;
    }
}
