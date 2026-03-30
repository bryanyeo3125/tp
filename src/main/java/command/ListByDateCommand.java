package command;

import java.util.logging.Logger;
import model.FoodList;
import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;
import ui.UserInterface;

//@@author j-kennethh
public class ListByDateCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListByDateCommand.class.getName());
    private final String fullCommand;

    public ListByDateCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        String[] words = fullCommand.split("d/");
        if (words.length < 2) {
            throw new BitbitesException("OOPS!!! Missing date. Please provide a valid date.");
        }

        assert words[0].equals("list") : "List command should be 'list d/DATE'";
        assert !words[1].isEmpty() : "Date should not be empty";

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
