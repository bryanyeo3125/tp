package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

/**
 * Command to delete a food entry from the food list.
 * Removes a specific food item by its index (1-based) from the list
 * and shows daily progress again if the deleted item was from today.
 */
public class DeleteCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs a DeleteCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing the index to delete
     */
    public DeleteCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the delete command.
     * Parses the index from the command, validates it, removes the food item
     * at that index from the food list, and displays the deleted item to the user.
     * If the deleted item was from today, updates and shows the daily progress.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     * @throws BitbitesException If the index is missing, invalid (not a number),
     *                           or out of range (less than 1 or greater than list size)
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

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

        String todayStr = java.time.LocalDate.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if (removed.getDate().equals(todayStr)) {
            GoalsCommand.showDailyProgress(foodList);
        }
        return false;
    }
}
