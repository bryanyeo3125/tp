package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import ui.UserInterface;
import seedu.bitbites.AppContext;

// @@author RayminQAQ
public class ExitCommand extends Command {
    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        logger.log(Level.INFO, "Exiting application");
        ui.showExit();
        return true;
    }
}
// @@author
