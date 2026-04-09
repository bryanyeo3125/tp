package command;

import java.util.logging.Logger;
import model.FoodList;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesResponses;

//@@author j-kennethh
/**
 * Represents a command to list all currently recorded food items.
 * When executed, this command displays the user's entire food diary
 * in a numbered and formatted list to the console.
 */
public class ListCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    /**
     * Executes the list command.
     * Retrieves the food list from the application context and prints each
     * recorded food item with its corresponding index.
     *
     * @param context The application context containing the user's data, including the FoodList.
     * @return false, indicating that the application should continue running after execution.
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();

        System.out.println(BitbitesResponses.LIST_MESSAGE);

        if (foodList.size() == 0) {
            System.out.println("No items in your list.");
            return false;
        }

        for (int i = 0; i < foodList.size(); i++) {
            System.out.println((i + 1) + ". " + foodList.get(i));
        }

        return false;
    }
}
//@@author
