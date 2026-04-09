package command;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

/**
 * Command to display nutrition summary for a specific date.
 * Retrieves and shows aggregated nutrition information
 * for all food items logged on the given date, comparing against daily goals.
 */
public class SummaryByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryByDateCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs a SummaryByDateCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing the date after "d/"
     */
    public SummaryByDateCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the summary by date command.
     * Parses the date from the command, validates that food items exist for that date,
     * retrieves the daily nutrition summary, and displays it with goal comparisons.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     * @throws BitbitesException If the date is missing from the command format
     */
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
