/**
 * BitbitesResponses.java
 * 
 * This file stores all predefined response messages used throughout the application.
 * Centralizes message strings for consistency and easier maintenance.
 * 
 * Dependencies:
 * - None (contains only static string constants)
 */
package seedu.duke;

/**
 * BitbitesResponses provides centralized storage for all application messages.
 * Includes greetings, error messages, and feature confirmation messages.
 */
public class BitbitesResponses {
    // Greetings section
    static String welcomeMessage = "Hello from\n"
            + "__________._____________________________.___.______________________\n"
            + "\\______   \\   \\__    ___/\\______   \\__  |   |\\__    ___/\\_   _____/\n"
            + " |    |  _/   | |    |    |    |  _//   |   |  |    |    |    __)_\n"
            + " |    |   \\   | |    |    |    |   \\\\____   |  |    |    |        \\\n"
            + " |______  /___| |____|    |______  // ______|  |____|   /_______  /\n"
            + "        \\/                       \\/ \\/                          \\/\n";
    // Errors & Exceptions Handled by the chatbot section
    static String unknownCommand = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    static String errorMessage = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    static String deleteErrorMessage = "OOPS!!! Invalid index. Please provide a valid item number.";

    // Features section
    // Command-Add Messages
    static String addMessage = "Got it. I've added the food item!";
    static String addFormatReminder = "Please use the correct format: " +
            "add n/NAME c/CALORIES_IN_KCAL p/PROTEIN_IN_G d/DATE";
    // List Message
    static String listMessage = "Here are all the food items in your list:";
    // List From Date Message
    static String listFromDateMessage = "Here are the food items from ";
    // Delete Message
    static String deleteMessage = "Got it. I've removed the food item!";
    // Exit Message
    static String exitMessage = "Bye. Hope to see you again soon!";
    // Help Message
    static String helpMessage = "Here are the available commands:\n"
            + "  list                                    - List all food items\n"
            + "  list d/DATE                             - List food items for a specific date\n"
            + "  add n/NAME c/CALORIES p/PROTEIN d/DATE  - Add a food item\n"
            + "  delete INDEX                            - Delete a food item by index\n"
            + "  help                                    - Show help message\n"
            + "  exit                                    - Exit the application";
}
