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
            + "██████╗ ██╗████████╗██████╗ ██╗████████╗███████╗███████╗\n"
            + "██╔══██╗██║╚══██╔══╝██╔══██╗██║╚══██╔══╝██╔════╝██╔════╝\n"
            + "██████╦╝██║   ██║   ██████╔╝██║   ██║   █████╗  ███████╗\n"
            + "██╔══██╗██║   ██║   ██╔══██╗██║   ██║   ██╔══╝  ╚════██║\n"
            + "██████╦╝██║   ██║   ██████╔╝██║   ██║   ███████╗███████║\n"
            + "╚═════╝ ╚═╝   ╚═╝   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝\n";
    // Errors & Exceptions Handled by the chatbot section
    static String unknownCommand = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    static String errorMessage = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    static String deleteErrorMessage = "OOPS!!! Invalid index. Please provide a valid item number.";

    // Features section
    static String addMessage = "Got it. I've added the food item!"; // EXAMPLE
    static String deleteMessage = "Got it. I've removed the food item!";
    static String exitMessage = "Bye. Hope to see you again soon!";
    static String helpMessage = "Here are the available commands:\n"
            + "  list                                    - List all food items\n"
            + "  list d/DATE                             - List food items for a specific date\n"
            + "  add n/NAME c/CALORIES p/PROTEIN d/DATE  - Add a food item\n"
            + "  delete INDEX                            - Delete a food item by index\n"
            + "  help                                    - Show this help message\n"
            + "  exit                                    - Exit the application";
}
