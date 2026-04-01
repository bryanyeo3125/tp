package seedu.bitbites;

import java.io.FileNotFoundException;

import command.Command;
import model.FoodList;
import model.PresetList;
import parser.Parser;
import storage.Storage;
import ui.UserInterface;

/**
 * The main entry point and driver class for the Bitbites application.
 * This class handles the initialisation of all core components (UI, Storage, Data Models)
 * and contains the main execution loop that processes user commands.
 */
public class Bitbites {
    private UserInterface ui;
    private FoodList foods;
    private PresetList presets;
    private Storage foodStorage;
    private Storage presetStorage;

    //@@author j-kennethh
    /**
     * Constructs a {@code Bitbites} application instance.
     * Initialises the user interface and storage handlers, and attempts to load
     * any previously saved food logs and presets from the local disk. If no saved
     * data is found or if the files are corrupted, it initialises empty lists.
     *
     * @param foodFilePath   The file path where daily food logs are stored.
     * @param presetFilePath The file path where saved food presets are stored.
     */
    public Bitbites(String foodFilePath, String presetFilePath) {
        ui = new UserInterface();
        foodStorage = new Storage(foodFilePath);
        presetStorage = new Storage(presetFilePath);

        try {
            foods = new FoodList(foodStorage.load());
        } catch (BitbitesException | FileNotFoundException e) {
            ui.showError("Could not load daily food data: " + e.getMessage());
            foods = new FoodList();
        }

        try {
            presets = new PresetList(presetStorage.load());
        } catch (BitbitesException | FileNotFoundException e) {
            ui.showError("Could not load presets: " + e.getMessage());
            presets = new PresetList();
        }
    }
    //@@author

    /**
     * Runs the main execution loop of the application.
     * Displays a welcome message, then continuously reads, parses, and executes
     * user commands until the user issues the exit command. It also automatically
     * saves data to the disk after every successful command execution.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        AppContext context = new AppContext(foods, presets, ui);

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);

                isExit = command.execute(context);
                foodStorage.save(foods);
                presetStorage.save(presets);
            } catch (BitbitesException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    //@@author j-kennethh
    /**
     * The main method that launches the Bitbites application.
     * Instantiates the app with the default file paths and begins execution.
     *
     * @param args Command-line arguments (not currently used).
     */
    public static void main(String[] args) {
        new Bitbites("./data/foods.txt", "./data/presets.txt").run();
    }
    //@@author
}
