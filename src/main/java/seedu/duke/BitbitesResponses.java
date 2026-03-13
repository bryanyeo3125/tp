// BitbitesResponses only store the responses used by the chatbot. It is used by the UserInterface class to display messages to the user.
package seedu.duke;

public class BitbitesResponses {
    // Greetings section
    static String WELCOME_MESSAGE = "Hello from\n" + " ____        _        \n"
            + "|  _ \\ _   _| | _____ \n"
            + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";

    // Errors & Exceptions Handled by the chatbot section
    static String UNKNOWN_COMMAND = "OOPS!!! I'm sorry, but I don't know what that means :-(";
    static String ERROR_MESSAGE = "OOPS!!! I'm sorry, but I don't know what that means :-(";

    // Features section
    static String ADD_MESSAGE = "Got it. I've added the food item!"; // EXAMPLE
    static String EXIT_MESSAGE = "Bye. Hope to see you again soon!";
}
