package command;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

public class SummaryRangeCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryRangeCommand.class.getName());
    private final String fullCommand;

    public SummaryRangeCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        assert foodList != null : "FoodList should not be null";
        if (!fullCommand.contains("from/") || !fullCommand.contains("to/")) {
            throw new BitbitesException(
                    "Please use the correct format: summary from/DATE1 to/DATE2");
        }
        try {
            String fromDate = fullCommand.substring(
                    fullCommand.indexOf("from/") + 5,
                    fullCommand.indexOf("to/")
            ).trim();
            String toDate = fullCommand.substring(
                    fullCommand.indexOf("to/") + 3
            ).trim();

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                throw new BitbitesException(
                        "Please use the correct format: summary from/DATE1 to/DATE2");
            }
            if (fromDate.compareTo(toDate) > 0) {
                throw new BitbitesException("Start date must not be after end date.");
            }
            List<NutritionSummary> summaries = foodList.getSummariesInRange(fromDate, toDate);
            if (summaries.isEmpty()) {
                System.out.println("No food items found between " + fromDate + " and " + toDate + ".");
                return false;
            }
            ui.showSummaryRange(summaries, fromDate, toDate);
        } catch (BitbitesException e) {
            throw e;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error parsing summary range: " + fullCommand);
            throw new BitbitesException(
                    "Please use the correct format: summary from/DATE1 to/DATE2");
        }
        return false;
    }
}
