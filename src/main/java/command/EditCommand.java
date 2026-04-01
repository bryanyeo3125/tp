package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;
import ui.UserInterface;
import seedu.bitbites.AppContext;

public class EditCommand extends Command {
    private static final Logger logger = Logger.getLogger(EditCommand.class.getName());
    private final String fullCommand;

    public EditCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        assert foodList != null : "FoodList should not be null";

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
            throw new BitbitesException("Invalid index format. Please enter a number.");
        }

        assert index >= 0 : "Index should be non-negative after conversion";

        // Check at least one field is provided
        if (parts.length < 3 || parts[2].trim().isEmpty()) {
            throw new BitbitesException(
                    "Please provide at least one field to edit. Format: edit INDEX [n/NAME] "
                            + "[c/CALORIES] [p/PROTEIN] [d/DATE]");
        }

        String args = parts[2].trim();

        // Must have at least one valid prefix
        if (!args.contains("n/") && !args.contains("c/")
                && !args.contains("p/") && !args.contains("d/")) {
            throw new BitbitesException(BitbitesResponses.editFormatReminder);
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
                assert !name.isEmpty() : "Name should not be empty";
                food.setName(name);
                logger.log(Level.INFO, "Updated name to: " + name);
            }

            if (args.contains("c/")) {
                String caloriesStr = extractField(args, "c/").trim();
                int calories = Integer.parseInt(caloriesStr);
                if (calories < 0) {
                    throw new BitbitesException("Calories must be non-negative.");
                }
                assert calories >= 0 : "Calories should not be negative";
                food.setCalories(calories);
                logger.log(Level.INFO, "Updated calories to: " + calories);
            }

            if (args.contains("p/")) {
                String proteinStr = extractField(args, "p/").trim();
                double protein = Double.parseDouble(proteinStr);
                if (protein < 0) {
                    throw new BitbitesException("Protein must be non-negative.");
                }
                assert protein >= 0 : "Protein should not be negative";
                food.setProtein(protein);
                logger.log(Level.INFO, "Updated protein to: " + protein);
            }

            if (args.contains("d/")) {
                String date = extractField(args, "d/").trim();
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new BitbitesException("Date must be in YYYY-MM-DD format.");
                }
                assert date.matches("\\d{4}-\\d{2}-\\d{2}") : "Date format should be YYYY-MM-DD";
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
