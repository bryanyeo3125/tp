/**
 * BitbitesResponses.java
 * 
 * This file stores all predefined response messages used throughout the application.
 * Centralizes message strings for consistency and easier maintenance.
 * 
 * Dependencies:
 * - None (contains only static string constants)
 */
package seedu.bitbites;

/**
 * BitbitesResponses provides centralized storage for all application messages.
 * Includes greetings, error messages, and feature confirmation messages.
 */
public class BitbitesResponses {
    // Greetings section
    public static String welcomeMessage = "Hello from\n"
            + "__________._____________________________.___.______________________\n"
            + "\\______   \\   \\__    ___/\\______   \\__  |   |\\__    ___/\\_   _____/\n"
            + " |    |  _/   | |    |    |    |  _//   |   |  |    |    |    __)_\n"
            + " |    |   \\   | |    |    |    |   \\\\____   |  |    |    |        \\\n"
            + " |______  /___| |____|    |______  // ______|  |____|   /_______  /\n"
            + "        \\/                       \\/ \\/                          \\/\n";
    // Errors & Exceptions Handled by the chatbot section
    public static String unknownCommand = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    public static String errorMessage = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    public static String deleteErrorMessage = "OOPS!!! Invalid index. Please provide a valid item number.";

    // Features section
    // Add Messages
    public static String addMessage = "Got it. I've added the food item!";
    public static String addFormatReminder = "Please use the correct format: " +
            "add n/NAME c/CALORIES_IN_KCAL p/PROTEIN_IN_G d/DD-MM-YYYY";
    // List Message
    public static String listMessage = "Here are all the food items in your list:";
    // List From Date Message
    public static String listFromDateMessage = "Here are the food items from ";
    // Delete Message
    public static String deleteMessage = "Got it. I've removed the food item!";
    // Edit Message
    public static String editMessage = "Got it. I've updated the food item!";
    public static String editFormatReminder = "Please use: edit INDEX [n/NAME] "
            + "[c/CALORIES] [p/PROTEIN] [d/DATE]";
    // Exit Message
    public static String exitMessage = "Bye. Hope to see you again soon!";
    // Help Message
    public static String helpMessage = "Here are the available commands:\n"
            + "  list                                    - List all food items\n"
            + "  list d/DATE                             - List food items for a specific date\n"
            + "  add n/NAME c/CALORIES p/PROTEIN d/DATE  - Add a food item\n"
            + "  delete INDEX                            - Delete a food item by index\n"
            + "  edit INDEX                              - Edit an existing food item\n"
            + "  (Optional: [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE])\n"
            + "  preset add n/NAME c/CALORIES p/PROTEIN  - Save a new food preset\n"
            + "  preset list                             - View all saved presets\n"
            + "  preset delete INDEX                     - Delete a preset by index\n"
            + "  preset use INDEX [d/DATE]               - Log a preset (date defaults to today)\n"
            + "  goals                                   - View daily and weekly goal progress\n"
            + "  goals set dc/CAL dp/PROT wc/CAL wp/PROT - Set daily/weekly calorie and protein goals\n"
            + "  help                                    - Show help message\n"
            + "  exit                                    - Exit the application\n"
            + "  (Date format: DD-MM-YYYY)";
}
