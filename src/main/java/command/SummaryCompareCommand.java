package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

/**
 * Command to compare nutrition summaries between two dates.
 * Takes two d/ arguments and displays a side-by-side comparison
 * of calorie and nutrient intake for the specified dates.
 */
public class SummaryCompareCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryCompareCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs a SummaryCompareCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing two d/ arguments
     */
    public SummaryCompareCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the summary compare command.
     * Parses two dates from the command, validates that both dates have food entries,
     * retrieves the nutrition summaries for each date, and displays a comparison.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     * @throws BitbitesException If the command format is invalid, dates are missing,
     *         or if date parsing fails
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";
        String[] parts = fullCommand.split("d/");
        if (parts.length < 3) {
            throw new BitbitesException(
                    "Please use the correct format: summary compare d/DATE1 d/DATE2");
        }
        String date1 = parts[1].trim();
        String date2 = parts[2].trim();

        if (date1.isEmpty() || date2.isEmpty()) {
            throw new BitbitesException(
                    "Please use the correct format: summary compare d/DATE1 d/DATE2");
        }

        validateDate(date1);
        validateDate(date2);

        if (foodList.getItemCountByDate(date1) == 0) {
            System.out.println("No food items found for " + date1 + ".");
            return false;
        }
        if (foodList.getItemCountByDate(date2) == 0) {
            System.out.println("No food items found for " + date2 + ".");
            return false;
        }

        NutritionSummary summary1 = foodList.getSummaryByDate(date1);
        NutritionSummary summary2 = foodList.getSummaryByDate(date2);
        logger.log(Level.INFO, "Comparing " + date1 + " vs " + date2);
        ui.showSummaryCompare(summary1, summary2);
        return false;
    }
}
