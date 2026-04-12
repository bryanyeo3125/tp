package command;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

/**
 * Command to display the top N days closest to calorie and protein goals.
 * Finds days where the user's intake was nearest to their daily targets.
 */
public class HistoryBestCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryBestCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs a HistoryBestCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing the number N after "/best"
     */
    public HistoryBestCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the history best command.
     * Parses the number N from the command, validates it, retrieves the N days
     * where calorie intake was closest to the daily goal, and displays them
     * along with protein goal comparison.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     * @throws BitbitesException If the format is invalid, N is not a positive number,
     *         or N is not a valid integer
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";
        String[] parts = fullCommand.split("/best");
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BitbitesException("Please use the correct format: history /best N");
        }
        try {
            int n = Integer.parseInt(parts[1].trim());
            if (n <= 0) {
                throw new BitbitesException("N must be a positive number.");
            }

            int calorieGoal = GoalsCommand.getDailyCalorieGoal();
            double proteinGoal = GoalsCommand.getDailyProteinGoal();

            List<NutritionSummary> best = foodList.getDaysClosestToGoal(n, calorieGoal);
            ui.showHistoryBest(best, n, calorieGoal, proteinGoal);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid N in history /best: " + fullCommand);
            throw new BitbitesException("Please enter a valid number for N.");
        }
        return false;
    }
}
