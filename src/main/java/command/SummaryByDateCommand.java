package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

public class SummaryByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryByDateCommand.class.getName());
    private final String fullCommand;

    public SummaryByDateCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
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
        NutritionSummary summary = foodList.getSummaryByDate(date);
        ui.showSummary(summary);
        return false;
    }
}
