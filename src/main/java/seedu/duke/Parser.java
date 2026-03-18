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

        if (fullCommand.startsWith("list d/")) {
            handleListFromDate(fullCommand, foodList, ui);
        } else if (fullCommand.equals("list")) {
            handleListAll(fullCommand, foodList, ui);
        } else if (fullCommand.startsWith("add ")) {
            handleAdd(fullCommand, foodList, ui);
        } else if (fullCommand.equals("help")) {
            handleHelp(ui);
        } else if (fullCommand.startsWith("delete ")) {
            handleDelete(fullCommand, foodList, ui);
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
        System.out.println(BitbitesResponses.listMessage);

        for (int i = 0; i < foodList.size(); i++) {
            System.out.println((i + 1) + ". " + foodList.get(i));
        }
    }

    // TODO: List food items for a specific date
    private static void handleListFromDate(String fullCommand, FoodList foodList, UserInterface ui) {
        // Implementation for listing food items
        String[] words = fullCommand.split("d/");
        if (words.length < 2) {
            throw new BitbitesException("OOPS!!! Missing date. Please provide a valid date.");
        }

        String date = words[1];
        System.out.println(BitbitesResponses.listFromDateMessage + date + ":");

        int count = 1;

        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getDate().equals(date)) {
                System.out.println(count + ". " + foodList.get(i));
                count++;
            }
        }
    }

    private static void handleAdd(String fullCommand, FoodList foodList, UserInterface ui) {
        String correctFormat = BitbitesResponses.addFormatReminder;

        // Check that all required prefixes exist
        if (!fullCommand.contains("n/") || !fullCommand.contains("c/") ||
                !fullCommand.contains("p/") || !fullCommand.contains("d/")) {
            System.out.println(correctFormat);
            return;
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
                System.out.println(correctFormat);
                return;
            }

            int calories = Integer.parseInt(caloriesStr);
            double protein = Double.parseDouble(proteinStr);

            // Validate non-negative values
            if (calories < 0 || protein < 0) {
                System.out.println("Calories and protein must be non-negative.");
                return;
            }

            // Validate date format YYYY-MM-DD
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Date must be in YYYY-MM-DD format.");
                return;
            }

            Food newFood = new Food(name, calories, protein, date);
            foodList.addFood(newFood);
            System.out.println(BitbitesResponses.addMessage);

        } catch (NumberFormatException e) {
            System.out.println("Calories must be an integer and protein must be a number.");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(correctFormat);
        }
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
