package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import ui.UserInterface;

public class HelpCommand extends Command {
    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        assert ui != null : "UserInterface should not be null";
        logger.log(Level.INFO, "Showing help message");
        ui.showHelp();
        return false;
    }
}
