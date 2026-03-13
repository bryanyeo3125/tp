package seedu.duke;

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
            } catch (BitbitesException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
