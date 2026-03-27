package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.BitbitesResponses;
import ui.UserInterface;

public class AddCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());
    private final String fullCommand;

    public AddCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        String correctFormat = BitbitesResponses.addFormatReminder;
        logger.log(Level.INFO, "Attempting to add food: " + fullCommand);

        // Check that all required prefixes exist
        if (!fullCommand.contains("n/") || !fullCommand.contains("c/") ||
                !fullCommand.contains("p/") || !fullCommand.contains("d/")) {
            logger.log(Level.WARNING, "Missing required fields in add command");
            System.out.println(correctFormat);
            return false;
        }

        try {
            String name = fullCommand.substring(
                    fullCommand.indexOf("n/") + 2,
                    fullCommand.indexOf("c/")
            ).trim();

            String caloriesStr = fullCommand.substring(
                    fullCommand.indexOf("c/") + 2,
                    fullCommand.indexOf("p/")
            ).trim();

            String proteinStr = fullCommand.substring(
                    fullCommand.indexOf("p/") + 2,
                    fullCommand.indexOf("d/")
            ).trim();

            String date = fullCommand.substring(
                    fullCommand.indexOf("d/") + 2
            ).trim();

            // Validate fields are not empty
            if (name.isEmpty() || caloriesStr.isEmpty() || proteinStr.isEmpty() || date.isEmpty()) {
                logger.log(Level.WARNING, "Empty fields in add command");
                System.out.println(correctFormat);
                return false;
            }

            int calories = Integer.parseInt(caloriesStr);
            double protein = Double.parseDouble(proteinStr);

            // Validate non-negative values
            if (calories < 0 || protein < 0) {
                logger.log(Level.WARNING, "Negative values in add command");
                System.out.println("Calories and protein must be non-negative.");
                return false;
            }

            // Validate date format YYYY-MM-DD
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                logger.log(Level.WARNING, "Invalid date format: " + date);
                System.out.println("Date must be in YYYY-MM-DD format.");
                return false;
            }

            // Assert statements
            assert !name.isEmpty() : "Name should not be empty";
            assert calories >= 0 : "Calories should not be negative";
            assert protein >= 0 : "Protein should not be negative";
            assert date.matches("\\d{4}-\\d{2}-\\d{2}") : "Date format should be YYYY-MM-DD";

            Food newFood = new Food(name, calories, protein, date);
            foodList.addFood(newFood);

            assert foodList.size() > 0 : "FoodList should not be empty after adding";
            logger.log(Level.INFO, "Successfully added food: " + name +
                    " | Calories: " + calories + " | Protein: " + protein);
            System.out.println(BitbitesResponses.addMessage);

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid number format in: " + fullCommand);
            System.out.println("Calories must be an integer and protein must be a number.");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(correctFormat);
        }
        return false;
    }
}
