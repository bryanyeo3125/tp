/**
 * Parser.java
 * 
 * This file contains the Parser class responsible for interpreting user commands.
 * Routes user input to appropriate command handlers (list, add, exit).
 * 
 * Dependencies:
 * - FoodList: For accessing and modifying the food items list
 * - UserInterface: For providing feedback to users
 * - BitbitesException: For error handling
 * - BitbitesResponses: For error messages
 */
package seedu.duke;

/**
 * Parser interprets user commands and executes the appropriate handlers.
 * Supports commands: list, list d/DATE, add, and exit.
 * Throws BitbitesException for unknown commands.
 */
public class Parser {

    public Parser() {
        // No implementation needed for the constructor as of now
    }

    /**
     * Parses the user input and executes the corresponding command.
     * Returns true if the command is "exit", false otherwise.
     */
    public static boolean parse(String fullCommand, FoodList foodList, UserInterface ui) {
        fullCommand = fullCommand.trim();

        if (fullCommand.equals("list")) {   
            handleListAll(fullCommand, foodList, ui);
        } else if (fullCommand.equals("list d/")) {
            handleListFromDate(fullCommand, foodList, ui);
        } else if (fullCommand.equals("add ")) {
            handleAdd(fullCommand, foodList, ui);
        } else if (fullCommand.equals("exit")) {
            ui.showExit();
            return true;
        } else {
            throw new BitbitesException(BitbitesResponses.unknownCommand);
        }

        return false;
    }

    // TODO: List all food items
    private static void handleListAll(String fullCommand, FoodList foodList, UserInterface ui) {
        // Implementation for listing all food items
        System.out.println("Here are the food items in your list:");

        for (int i = 0; i < foodList.size(); i++) {
            System.out.println((i + 1) + ". " + foodList.get(i));
        }
    }

    // TODO: List food items for a specific date
    private static void handleListFromDate(String fullCommand, FoodList foodList, UserInterface ui) {
        // Implementation for listing food items
    }

    // TODO: Add - Bryan
    private static void handleAdd(String fullCommand, FoodList foodList, UserInterface ui) {
        /*
            Command - Add (Bryan)
            Adds a food item to the list with its calorie and protein information

            Format:
            add n/NAME c/CALORIES_IN_KCAL p/PROTEIN_IN_G d/DATE
        */
    }

    private static void handleHelp(UserInterface ui) {
        ui.showHelp();
    }

    private static void handleDelete(String fullCommand, FoodList foodList, UserInterface ui) {
        String[] parts = fullCommand.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BitbitesException("Please specify an item number. Format: delete INDEX");
        }
        int index;
        try {
            index = Integer.parseInt(parts[1].trim()) - 1;
        } catch (NumberFormatException e) {
            throw new BitbitesException("Invalid index format. Please enter a number.");
        }
        Food removed = foodList.deleteFood(index);
        ui.showDeletedFood(removed, foodList.size());
    }
}
