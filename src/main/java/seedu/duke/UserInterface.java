/**
 * UserInterface.java
 * 
 * This file handles all user input/output operations for the chatbot.
 * Displays messages to the user and reads commands from standard input.
 * 
 * Dependencies:
 * - java.util.Scanner: For reading user input from console
 * - BitbitesResponses: For accessing predefined response messages
 */
package seedu.duke;

import java.util.Scanner;

/**
 * UserInterface manages all interactions between the chatbot and the user.
 * Handles display of messages and reading of user commands from the console.
 */
public class UserInterface {
    /* The responses used by the chatbot. */
    static BitbitesResponses bitbitesResponses = new BitbitesResponses();

    /** The scanner used to read user input from the console. */
    private final Scanner scanner;
    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    //// READ section ////
    /* Reads a command from the user input. */
    public String readCommand() {
//        // TODO: Implement read command logic
//        String input = scanner.nextLine();
//
//        if (input.startsWith("list")) {
//            return "list";
//        }
          return this.scanner.nextLine();
    }

    //// SHOW section ////
    /* Displays the welcome message and prompts the user for their name. */
    public void showWelcome() {
        System.out.println(bitbitesResponses.welcomeMessage);
        System.out.println("What is your name?");

        // Get user input for name and greet them
        System.out.println("Hello " + this.scanner.nextLine());
    }

    /* Show an error message to the user. */
    public void showError(String message) {
        System.out.println(bitbitesResponses.errorMessage);
    }

    /* Show the exit message to the user. */
    public void showExit() {
        System.out.println(bitbitesResponses.exitMessage);
    }

    public void showHelp() {
        System.out.println(BitbitesResponses.helpMessage);
    }

    public void showDeletedFood(Food food, int remaining) {
        System.out.println(bitbitesResponses.deleteMessage);
        System.out.println("  " + food);
        System.out.println("Now you have " + remaining + " item(s) in the list.");
    }
}
