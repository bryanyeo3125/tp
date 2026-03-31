package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

public class SummaryCompareCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryCompareCommand.class.getName());
    private final String fullCommand;

    public SummaryCompareCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
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
