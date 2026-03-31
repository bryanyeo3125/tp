package command;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import model.FoodList;
import model.NutritionSummary;
import ui.UserInterface;
import seedu.bitbites.BitbitesException;

public class HistoryTopCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryTopCommand.class.getName());
    private final String fullCommand;

    public HistoryTopCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
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
            assert n > 0 : "N should be positive";
            List<NutritionSummary> top = foodList.getTopDaysByCalories(n);
            ui.showHistoryTop(top, n);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid N in history /top: " + fullCommand);
            throw new BitbitesException("Please enter a valid number for N.");
        }
        return false;
    }
}
