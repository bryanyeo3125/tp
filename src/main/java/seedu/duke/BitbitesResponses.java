// BitbitesResponses stores the responses used by the chatbot.
// It is used by the UserInterface class to display messages to the user.
package seedu.duke;

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
