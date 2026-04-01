package command;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import ui.UserInterface;
import seedu.bitbites.BitbitesException;

public class HistoryBestCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryBestCommand.class.getName());
    private final String fullCommand;

    public HistoryBestCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

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
            assert n > 0 : "N should be positive";
            List<NutritionSummary> best = foodList.getBestDaysByCalories(n);
            ui.showHistoryBest(best, n);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid N in history /best: " + fullCommand);
            throw new BitbitesException("Please enter a valid number for N.");
        }
        return false;
    }
}
