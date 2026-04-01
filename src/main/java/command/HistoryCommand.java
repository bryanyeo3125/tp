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

public class HistoryCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryCommand.class.getName());

    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        boolean recordedToday = foodList.getItemCountByDate(today) > 0;

        List<NutritionSummary> summaries = foodList.getAllDailySummaries();
        logger.log(Level.INFO, "Showing history, total days: " + summaries.size());
        ui.showHistory(summaries, recordedToday);
        return false;
    }
}
