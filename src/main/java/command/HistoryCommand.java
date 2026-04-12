package command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import ui.UserInterface;

/**
 * Command to display the complete history of daily nutrition summaries.
 * Shows all days with food entries in reverse chronological order (most recent first).
 */
public class HistoryCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryCommand.class.getName());

    /**
     * Executes the history command.
     * Retrieves all daily nutrition summaries, reverses them to show most recent first,
     * and displays them to the user with an indicator if food was logged today.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        boolean recordedToday = foodList.getItemCountByDate(today) > 0;

        List<NutritionSummary> summaries = foodList.getAllDailySummaries();
        java.util.Collections.reverse(summaries);
        logger.log(Level.INFO, "Showing history, total days: " + summaries.size());
        ui.showHistory(summaries, recordedToday);
        return false;
    }
}
