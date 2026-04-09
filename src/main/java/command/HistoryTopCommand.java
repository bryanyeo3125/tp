package command;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import ui.UserInterface;
import seedu.bitbites.BitbitesException;

/**
 * Command to display the top N days with highest calorie intake.
 * Retrieves and shows the days where the user consumed the most calories.
 */
public class HistoryTopCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryTopCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs a HistoryTopCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing the number N after "/top"
     */
    public HistoryTopCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the history top command.
     * Parses the number N from the command, validates it, retrieves the top N days
     * by calorie intake, and displays them to the user.
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
        String[] parts = fullCommand.split("/top");
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BitbitesException("Please use the correct format: history /top N");
        }
        try {
            int n = Integer.parseInt(parts[1].trim());
            if (n <= 0) {
                throw new BitbitesException("N must be a positive number.");
            }
            List<NutritionSummary> top = foodList.getTopDaysByCalories(n);
            ui.showHistoryTop(top, n);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid N in history /top: " + fullCommand);
            throw new BitbitesException("Please enter a valid number for N.");
        }
        return false;
    }
}
