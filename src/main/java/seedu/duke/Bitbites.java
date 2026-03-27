/**
 * Bitbites.java
 * 
 * This is the main entry point for the Bitbites chatbot application.
 * It initializes and orchestrates the core components (UserInterface, Parser, FoodList)
 * and manages the main application loop for command processing.
 * 
 * Dependencies:
 * - UserInterface: For displaying messages and reading user input
 * - Parser: For parsing and executing user commands
 * - FoodList: For managing the list of food items
 * - BitbitesException: For custom exception handling
 */
package seedu.duke;

/**
 * Bitbites is the main chatbot class that manages the overall application flow.
 * It coordinates user input, command parsing, and food item management.
 */
public class Bitbites {

    /* The user interface for the chatbot. */
    private static UserInterface ui = new UserInterface();

    /* The parser for interpreting user commands. */
    private static Parser parser = new Parser();

    /* The list of food items tracked by the chatbot. */
    private static FoodList foods = new FoodList();

    /**
     * Constructor for the Bitbites chatbot. Initializes the chatbot and its components.
     */
    public Bitbites() {
        // No implementation needed for the constructor as of now
    }

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                isExit = Parser.parse(fullCommand, foods, ui);

                // Assertion
                assert !isExit || fullCommand.trim().equals("exit") : "Exit command should be 'exit'";
            } catch (BitbitesException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
