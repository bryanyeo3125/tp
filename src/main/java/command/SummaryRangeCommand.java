package command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FoodList;
import model.NutritionSummary;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

/**
 * Command to display nutrition summary for a date range.
 * Parses from/ and to/ arguments and shows aggregated nutrition information
 * for all food items between the specified dates (inclusive).
 */
public class SummaryRangeCommand extends Command {
    private static final Logger logger = Logger.getLogger(SummaryRangeCommand.class.getName());
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final String fullCommand;

    /**
     * Constructs a SummaryRangeCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing from/ and to/ arguments
     */
    public SummaryRangeCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the summary range command.
     * Parses the from/ and to/ dates, validates them, retrieves summaries
     * for the date range, and displays them to the user.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     * @throws BitbitesException If date format is invalid, start date is after end date,
     *         or required arguments are missing
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";

        if (!fullCommand.contains("from/") || !fullCommand.contains("to/")) {
            throw new BitbitesException(
                    "Please use the correct format: summary from/DATE1 to/DATE2");
        }

        try {
            String fromDate = fullCommand.substring(
                    fullCommand.indexOf("from/") + 5,
                    fullCommand.indexOf("to/")
            ).trim();
            String toDate = fullCommand.substring(
                    fullCommand.indexOf("to/") + 3
            ).trim();

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                throw new BitbitesException(
                        "Please use the correct format: summary from/DATE1 to/DATE2");
            }

            LocalDate from;
            LocalDate to;
            try {
                from = LocalDate.parse(fromDate, FORMATTER);
                to = LocalDate.parse(toDate, FORMATTER);
            } catch (Exception e) {
                throw new BitbitesException("Invalid date format. Please use DD-MM-YYYY.");
            }

            if (from.isAfter(to)) {
                throw new BitbitesException("Start date must not be after end date.");
            }

            List<NutritionSummary> summaries = foodList.getSummariesInRange(fromDate, toDate);
            if (summaries.isEmpty()) {
                System.out.println("No food items found between "
                        + fromDate + " and " + toDate + ".");
                return false;
            }

            logger.log(Level.INFO, "Showing summary range from " + fromDate + " to " + toDate);
            ui.showSummaryRange(summaries, fromDate, toDate);

        } catch (BitbitesException e) {
            throw e;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error parsing summary range: " + fullCommand);
            throw new BitbitesException(
                    "Please use the correct format: summary from/DATE1 to/DATE2");
        }

        return false;
    }
}
