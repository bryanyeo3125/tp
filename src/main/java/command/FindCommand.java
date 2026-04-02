package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Food;
import model.FoodList;
import seedu.bitbites.AppContext;
import ui.UserInterface;

/**
 * FindCommand.java
 *
 * Provides search functionality to find food items by name or other criteria.
 *
 * Supported commands:
 *   find [food] - Search for food items by name or keyword
 */
//@@author RayminQAQ
public class FindCommand extends Command {
    private static final Logger logger = Logger.getLogger(FindCommand.class.getName());

    private final String fullCommand;

    /**
     * Constructs a FindCommand with the full user input string.
     *
     * @param fullCommand The raw command string entered by the user.
     */
    public FindCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the find command and displays matching food items.
     *
     * @param context The application context containing FoodList and UserInterface.
     * @return false always, as this command does not trigger application exit.
     */
    @Override
    public boolean execute(AppContext context) {
        FoodList foodList = context.getFoodList();
        UserInterface ui = context.getUi();

        logger.log(Level.INFO, "User requested find/search command");

        // Extract search keyword from fullCommand
        String keyword = ""; // find burger -> keyword = "burger"
        if (fullCommand.contains("find ")) {
            keyword = fullCommand.substring(fullCommand.indexOf("find ") + 5).trim();
        } else {
            logger.log(Level.WARNING, "Find command received without a keyword");
            System.out.println("Please provide a keyword to search for. Usage: find [keyword]");
            return false;
        }

        // Search for food items matching the keyword
        java.util.List<Food> resultList = searchFoods(foodList, keyword);

        // Display the matching results
        displayResults(resultList, keyword);

        logger.log(Level.FINE, "Find command execution completed");
        return false;
    }

    /**
     * Searches for food items matching the keyword.
     *
     * @param foodList The list of food items to search.
     * @param keyword The search keyword.
     * @return A list of matching food items.
     */
    private java.util.List<Food> searchFoods(FoodList foodList, String keyword) {
        java.util.List<Food> resultList = new java.util.ArrayList<>();
        for (Food food : foodList.getFoodList()) {
            if (food.getName().toLowerCase().equals(keyword.toLowerCase())) {
                // Add to results
                resultList.add(food);
            }
        }
        return resultList;
    }

    /**
     * Displays the search results.
     *
     * @param results The list of matching food items.
     * @param keyword The search keyword.
     */
    private void displayResults(java.util.List<Food> results, String keyword) {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("Search Results for: \"" + keyword + "\"");
        System.out.println("========================================================");
        
        if (results.isEmpty()) {
            System.out.println("   No food items found matching: " + keyword);
        } else {
            System.out.println("   Found " + results.size() + " result" + (results.size() > 1 ? "s" : "") + ":\n");
            
            for (int i = 0; i < results.size(); i++) {
                Food food = results.get(i);
                System.out.printf("   %d. %-25s | %4d kcal | %.1fg protein%n",
                        i + 1,
                        food.getName(),
                        food.getCalories(),
                        food.getProtein());
            }
        }
        
        System.out.println("========================================================");
        System.out.println();
    }
}
//@@author
