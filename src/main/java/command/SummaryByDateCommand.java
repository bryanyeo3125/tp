package command;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

public class SummaryByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryByDateCommand.class.getName());
    private final String fullCommand;

    public SummaryByDateCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";
        String[] parts = fullCommand.split("d/", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            logger.log(Level.WARNING, "Missing date in summary command");
            throw new BitbitesException("Please specify a date. Format: summary d/DATE");
        }
        String date = parts[1].trim();
        if (foodList.getItemCountByDate(date) == 0) {
            System.out.println("No food items found for " + date + ".");
            return false;
        }

        int calorieGoal = GoalsCommand.getDailyCalorieGoal();
        double proteinGoal = GoalsCommand.getDailyProteinGoal();

        NutritionSummary summary = foodList.getSummaryByDate(date);
        ui.showSummary(summary, calorieGoal, proteinGoal);
        return false;
    }
}
