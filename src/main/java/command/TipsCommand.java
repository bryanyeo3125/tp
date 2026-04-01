package command;

import seedu.bitbites.AppContext;
import ui.UserInterface;

public class TipsCommand extends Command {

    @Override
    public boolean execute(AppContext context) {
        UserInterface ui = context.getUi();
        ui.showTips();
        return false;
    }
}
