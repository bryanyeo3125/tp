package command;

import java.util.logging.Logger;

import model.FoodList;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;
import ui.UserInterface;

//@@author j-kennethh
/**
 * Represents a command to list all food items recorded on a specific date.
 * When executed, this command filters the user's food diary and displays
 * only the entries that match the user-provided date.
 */
public class ListByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListByDateCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs a ListByDateCommand with the specified user input.
     *
     * @param fullCommand The complete command string entered by the user.
     */
    public ListByDateCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the list-by-date command.
     * Parses the full command string to extract the target date, then iterates
     * through the food list to find and print all items recorded on that date.
     *
     * @param context The application context containing the user's data, including the FoodList and UI.
     * @return false, indicating that the application should continue running after execution.
     * @throws BitbitesException If the user fails to provide the "d/" prefix or leaves the date blank.
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        String[] words = fullCommand.split("d/");
        if (words.length < 2) {
            throw new BitbitesException("OOPS!!! Missing date. Please provide a valid date.");
        }

        assert words[0].trim().equals("list") : "List command should be 'list d/DATE'";
        assert !words[1].isEmpty() : "Date should not be empty.";

        String date = words[1].trim();
        System.out.println(BitbitesResponses.listFromDateMessage + date + ":");

        int count = 1;
        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getDate().equals(date)) {
                System.out.println(count + ". " + foodList.get(i));
                count++;
            }
        }

        return false;
    }
}
//@@author
