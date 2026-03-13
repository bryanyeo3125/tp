package seedu.duke;

import java.util.Scanner;

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
        // TODO: Implement read command logic
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
}
