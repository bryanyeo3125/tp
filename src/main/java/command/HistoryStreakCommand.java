package command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import model.FoodList;
import seedu.bitbites.AppContext;
import ui.UserInterface;

public class HistoryStreakCommand extends Command {
    private static final Logger logger = Logger.getLogger(HistoryStreakCommand.class.getName());

    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        boolean recordedToday = foodList.getItemCountByDate(today) > 0;

        int current = foodList.getCurrentStreak();
        int longest = foodList.getLongestStreak();
        logger.log(Level.INFO, "Current streak: " + current + ", Longest: " + longest);
        ui.showStreak(current, longest, recordedToday);
        return false;
    }
}
