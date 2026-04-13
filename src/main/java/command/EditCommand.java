package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;
import ui.UserInterface;

/**
 * Command to edit an existing food entry in the food list.
 * Allows updating name, calories, protein, and/or date of a specific food item by index.
 * Fields are optional but at least one must be provided.
 */
public class EditCommand extends Command {
    private static final Logger logger = Logger.getLogger(EditCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs an EditCommand with the user's full input.
     *
     * @param fullCommand The raw command string containing the index and fields to edit
     */
    public EditCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the edit command.
     * Parses the index and field arguments from the command, validates them,
     * retrieves the food item at the specified index, updates the provided fields,
     * and displays the updated food item to the user.
     *
     * @param context The application context containing FoodList and UserInterface
     * @return {@code false} always as this command does not terminate the application
     * @throws BitbitesException If the index is invalid, no fields are provided,
     *                           field values are invalid (negative numbers, empty name, wrong date format),
     *                           or the food item does not exist at the given index
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        // Format: edit INDEX [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE]
        String[] parts = fullCommand.split(" ", 3);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BitbitesException("Please specify an item number. Format: edit INDEX [fields]");
        }

        // Parse index
        int index;
        try {
            index = Integer.parseInt(parts[1].trim()) - 1;
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid index format in edit command: " + parts[1]);
            throw new BitbitesException("Invalid index format. Please enter a number."
                            + "Format: edit INDEX [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE]");
        }

        assert index >= 0 : "Index should be non-negative after conversion";

        // Check at least one field is provided
        if (parts.length < 3 || parts[2].trim().isEmpty()) {
            throw new BitbitesException(
                    "Please provide at least one field to edit. Format: edit INDEX [n/NAME] "
                            + "[c/CALORIES] [p/PROTEIN] [d/DATE]");
        }

        String args = parts[2].trim();

        // Avoid affecting storing
        if (fullCommand.contains("|")) {
            throw new BitbitesException("Input must not contain '|' as it is a reserved character.");
        }

        // Must have at least one valid prefix
        if (!args.contains("n/") && !args.contains("c/")
                && !args.contains("p/") && !args.contains("d/")) {
            throw new BitbitesException(BitbitesResponses.EDIT_FORMAT_REMINDER);
        }

        // Get the food item to edit
        Food food = foodList.getFood(index);

        // Parse and apply each field if present
        try {
            if (args.contains("n/")) {
                String name = extractField(args, "n/").trim();
                if (name.isEmpty()) {
                    throw new BitbitesException("Name should not be empty.");
                }
                food.setName(name);
                logger.log(Level.INFO, "Updated name to: " + name);
            }

            if (args.contains("c/")) {
                String caloriesStr = extractField(args, "c/").trim();
                long caloriesLong;
                try {
                    caloriesLong = Long.parseLong(caloriesStr);
                } catch (NumberFormatException e) {
                    throw new BitbitesException("Calories must be a whole number.");
                }
                if (caloriesLong < 0) {
                    throw new BitbitesException("Calories must be non-negative.");
                }
                if (caloriesLong > 10000) {
                    throw new BitbitesException("Calories value is too large. Please enter a realistic value (max 10000 kcal).");
                }
                food.setCalories((int) caloriesLong);
                logger.log(Level.INFO, "Updated calories to: " + caloriesLong);
            }

            if (args.contains("p/")) {
                String proteinStr = extractField(args, "p/").trim();
                double protein;
                try {
                    protein = Double.parseDouble(proteinStr);
                } catch (NumberFormatException e) {
                    throw new BitbitesException("Protein must be a number.");
                }
                if (protein < 0) {
                    throw new BitbitesException("Protein must be non-negative.");
                }
                if (protein > 1000) {
                    throw new BitbitesException("Protein value is too large. Please enter a realistic value (max 1000g).");
                }
                food.setProtein(protein);
                logger.log(Level.INFO, "Updated protein to: " + protein);
            }

            if (args.contains("d/")) {
                String date = extractField(args, "d/").trim();
                validateDate(date);
                food.setDate(date);
                logger.log(Level.INFO, "Updated date to: " + date);
            }

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid number format in edit command");
            throw new BitbitesException("Calories must be an integer and protein must be a number.");
        }

        logger.log(Level.INFO, "Successfully edited food at index: " + (index + 1));
        ui.showEditedFood(food);
        return false;
    }

    /**
     * Extracts the value after a given prefix, stopping at the next prefix or end of string.
     * This allows parsing of fields like "n/Apple c/100" to correctly extract "Apple".
     *
     * @param args   The full argument string containing multiple fields
     * @param prefix The prefix to extract the value for (e.g., "n/", "c/", "p/", "d/")
     * @return The extracted value as a trimmed string
     */
    private String extractField(String args, String prefix) {
        int start = args.indexOf(prefix) + prefix.length();
        int end = args.length();

        String[] prefixes = {"n/", "c/", "p/", "d/"};
        for (String p : prefixes) {
            if (p.equals(prefix)) {
                continue;
            }
            int pos = args.indexOf(p, start);
            if (pos != -1 && pos < end) {
                end = pos;
            }
        }
        return args.substring(start, end).trim();
    }
}
