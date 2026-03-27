package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

public class DeleteCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());
    private final String fullCommand;

    public DeleteCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        assert foodList != null : "FoodList should not be null";
        logger.log(Level.INFO, "Attempting to delete: " + fullCommand);

        String[] parts = fullCommand.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            logger.log(Level.WARNING, "Missing index in delete command");
            throw new BitbitesException("Please specify an item number. Format: delete INDEX");
        }

        int index;
        try {
            index = Integer.parseInt(parts[1].trim()) - 1;
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid index format: " + parts[1]);
            throw new BitbitesException("Invalid index format. Please enter a number.");
        }

        assert index >= 0 : "Index should be non-negative after conversion";
        int sizeBefore = foodList.size();
        Food removed = foodList.deleteFood(index);

        assert foodList.size() == sizeBefore - 1 : "FoodList size should decrease by 1";
        logger.log(Level.INFO, "Successfully deleted food: " + removed.getName());
        ui.showDeletedFood(removed, foodList.size());
        return false;
    }
}
