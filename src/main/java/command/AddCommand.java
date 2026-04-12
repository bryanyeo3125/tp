package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesResponses;

/**
 * AddCommand.java
 *
 * Adds a new food item into the food list, updating goal values.
 * Parses user input for food name, calorie count, protein value, and date,
 * validates all fields, and appends the new item to the FoodList.
 *
 * Command format: add n/NAME c/CALORIES p/PROTEIN d/DD-MM-YYYY
 */
public class AddCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());
    private final String fullCommand;

    /**
     * Constructs an AddCommand with the full user input string.
     *
     * @param fullCommand The raw command string entered by the user.
     */
    public AddCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the add command by parsing and validating the user input,
     * then adds the new Food object to the FoodList.
     * Displays daily goal progress after a successful add.
     *
     * @param context The application context containing FoodList and UserInterface.
     * @return false always, as this command does not trigger application exit.
     */
// @@author bryanyeo3125
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();

        String correctFormat = BitbitesResponses.ADD_FORMAT_REMINDER;
        logger.log(Level.INFO, "Attempting to add food: " + fullCommand);

        // Check for reserved delimiter character
        if (fullCommand.contains("|")) {
            System.out.println("Input must not contain '|' as it is a reserved character.");
            return false;
        }

        // Check that all required prefixes exist (d/ is optional)
        if (!fullCommand.contains("n/") || !fullCommand.contains("c/") ||
                !fullCommand.contains("p/")) {
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

            String proteinStr;
            String date;
            if (fullCommand.contains("d/")) {
                proteinStr = fullCommand.substring(
                        fullCommand.indexOf("p/") + 2,
                        fullCommand.indexOf("d/")
                ).trim();
                date = fullCommand.substring(
                        fullCommand.indexOf("d/") + 2
                ).trim();
            } else {
                proteinStr = fullCommand.substring(
                        fullCommand.indexOf("p/") + 2
                ).trim();
                date = java.time.LocalDate.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }

            // Ensures that fields are not empty
            if (name.isEmpty() || caloriesStr.isEmpty() || proteinStr.isEmpty()) {
                logger.log(Level.WARNING, "Empty fields in add command");
                System.out.println(correctFormat);
                return false;
            }

            int calories = Integer.parseInt(caloriesStr);
            double protein = Double.parseDouble(proteinStr);

            // Ensures non-negative values
            if (calories < 0 || protein < 0) {
                logger.log(Level.WARNING, "Negative values in add command");
                System.out.println("Calories and protein must be non-negative.");
                return false;
            }

            // Ensures date format DD-MM-YYYY
            if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
                System.out.println("Date must be in DD-MM-YYYY format.");
                return false;
            }

            // Ensures date is a real calendar date (e.g. rejects 32-13-2026)
            try {
                java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date: " + date + ". Please enter a real date in DD-MM-YYYY format.");
                return false;
            }

            // Assert statements
            assert protein >= 0 : "Protein should not be negative";
            assert date.matches("\\d{2}-\\d{2}-\\d{4}") : "Date format should be DD-MM-YYYY";

            Food newFood = new Food(name, calories, protein, date);
            foodList.addFood(newFood);

            assert foodList.size() > 0 : "FoodList should not be empty after adding";
            logger.log(Level.INFO, "Successfully added food: " + name +
                    " | Calories: " + calories + " | Protein: " + protein);
            System.out.println(BitbitesResponses.ADD_MESSAGE);
            String todayStr = java.time.LocalDate.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (date.equals(todayStr)) {
                GoalsCommand.showDailyProgress(foodList);
            }

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid number format in: " + fullCommand);
            System.out.println("Calories must be an integer and protein must be a number.");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(correctFormat);
        }
        return false;
    }
    // @@author
}
