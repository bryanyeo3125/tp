package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import ui.UserInterface;

public class ExitCommand extends Command {
    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        logger.log(Level.INFO, "Exiting application");
        ui.showExit();
        return true;
    }
}
