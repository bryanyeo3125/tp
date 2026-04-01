package command;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NutritionSummary;
import model.Food;
import model.FoodList;
import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesResponses;
import ui.UserInterface;

/**
 * MotivateCommand.java
 *
 * Provides personalized motivational messages and encouragement to users.
 *
 * Supported commands:
 *   motivate - Display random motivational message
 */
//@@author RayminQAQ
public class MotivateCommand extends Command {
    private static final Logger logger = Logger.getLogger(MotivateCommand.class.getName());
    private static final Random random = new Random();

    private final String fullCommand;

    /**
     * Constructs a MotivateCommand with the full user input string.
     *
     * @param fullCommand The raw command string entered by the user.
     */
    public MotivateCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the motivate command and displays appropriate encouragement.
     *
     * @param context The application context containing FoodList and UserInterface.
     * @return false always, as this command does not trigger application exit.
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();
        String currentUser = ui.getCurrentUser();

        logger.log(Level.INFO, "User requested motivational message");

        String motivationType = extractMotivationType(fullCommand);

        // Process date
        String date = extractDate(fullCommand);

        switch (motivationType) {
            // case "progress":
            //     showProgressMotivation(foodList, currentUser, date);
            //     break;
            // case "goals":
            //     showGoalsMotivation(foodList, currentUser, date);
            //     break;
            case "random":
            default:
                showRandomMotivation();
                break;
        }

        logger.log(Level.FINE, "Motivation displayed for type: " + motivationType);
        return false;
    }

    /**
     * Displays a random motivational message from the predefined list.
     */
    private void showRandomMotivation() {
        String[] randomMessages = BitbitesResponses.motivationRandomMessages;
        String message = randomMessages[random.nextInt(randomMessages.length)];
        System.out.println("\n" + message + "\n");
        logger.log(Level.FINE, "Random motivation displayed");
    }

    /**
     * Extracts the motivation type from the command string.
     *
     * @param command The full command string.
     * @return The motivation type (random, progress, or goals).
     */
    private String extractMotivationType(String command) {
        if (command.contains("progress")) {
            return "progress";
        } else if (command.contains("goals")) {
            return "goals";
        }
        return "random";
    }

    /**
     * Extracts the date from the command string if provided.
     *
     * @param command The full command string.
     * @return The date in DD-MM-YYYY format, or null if not found.
     */
    private String extractDate(String command) {
        int dateIndex = command.indexOf("d/");
        if (dateIndex == -1) {
            return null;
        }
        int start = dateIndex + 2;
        int end = command.indexOf(" ", start);
        if (end == -1) {
            end = command.length();
        }
        String date = command.substring(start, end).trim();
        return date.isEmpty() ? null : date;
    }

    /**
     * Gets a NutritionSummary for a specific date.
     *
     * @param foodList The list of food items.
     * @param date The date in DD-MM-YYYY format.
     * @return A NutritionSummary for the specified date.
     */
    private NutritionSummary getSummaryForDate(FoodList foodList, String date) {
        int totalCalories = 0;
        double totalProtein = 0;
        List<Food> dateFoods = new ArrayList<>();

        for (Food food : foodList.getFoodList()) {
            if (food.getDate().equals(date)) {
                totalCalories += food.getCalories();
                totalProtein += food.getProtein();
                dateFoods.add(food);
            }
        }

        return new NutritionSummary(date, totalCalories, totalProtein, 
                                     dateFoods.size(), dateFoods);
    }
}
//@@author
