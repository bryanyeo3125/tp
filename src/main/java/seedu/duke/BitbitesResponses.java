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
    static String welcomeMessage = "Hello from\n" + " ____        _        \n"
            + "|  _ \\ _   _| | _____ \n"
            + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";

    // Errors & Exceptions Handled by the chatbot section
    static String unknownCommand = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    static String errorMessage = "OOPS!!! I'm sorry, but I don't know what that means :-( ";

    // Features section
    static String addMessage = "Got it. I've added the food item!"; // EXAMPLE
    static String exitMessage = "Bye. Hope to see you again soon!";
}
