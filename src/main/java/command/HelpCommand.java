package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import seedu.bitbites.AppContext;
import ui.UserInterface;

public class HelpCommand extends Command {
    private static final Logger logger = Logger.getLogger(HelpCommand.class.getName());

    @Override
    public boolean execute(AppContext context) {
        UserInterface ui = context.getUi();

        assert ui != null : "UserInterface should not be null";
        logger.log(Level.INFO, "Showing help message");
        ui.showHelp();
        return false;
    }
}
