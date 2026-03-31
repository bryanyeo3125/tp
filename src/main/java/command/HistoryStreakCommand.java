package command;

import java.util.logging.Logger;
import java.util.logging.Level;
import model.FoodList;
import ui.UserInterface;

public class HistoryStreakCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryStreakCommand.class.getName());

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        assert foodList != null : "FoodList should not be null";
        int current = foodList.getCurrentStreak();
        int longest = foodList.getLongestStreak();
        logger.log(Level.INFO, "Current streak: " + current + ", Longest: " + longest);
        ui.showStreak(current, longest);
        return false;
    }
}
