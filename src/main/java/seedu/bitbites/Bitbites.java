/**
 * Bitbites.java
 * <p>
 * This is the main entry point for the Bitbites chatbot application.
 * It initializes and orchestrates the core components (UserInterface, Parser, FoodList)
 * and manages the main application loop for command processing.
 * <p>
 * Dependencies:
 * - UserInterface: For displaying messages and reading user input
 * - Parser: For parsing and executing user commands
 * - FoodList: For managing the list of food items
 * - BitbitesException: For custom exception handling
 */
package seedu.bitbites;

import java.io.FileNotFoundException;

import command.Command;
import model.FoodList;
import model.PresetList;
import parser.Parser;
import storage.Storage;
import ui.UserInterface;

/**
 * Bitbites is the main chatbot class that manages the overall application flow.
 * It coordinates user input, command parsing, and food item management.
 */
public class Bitbites {
    private UserInterface ui;
    private FoodList foods;
    private PresetList presets;
    private Storage foodStorage;
    private Storage presetStorage;

    //@@author j-kennethh
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

    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);

                isExit = command.execute(foods, presets, ui);

                foodStorage.save(foods);
                presetStorage.save(presets);

                assert !isExit || fullCommand.trim().equals("exit") : "Exit command should be 'exit'";
            } catch (BitbitesException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    //@@author j-kennethh
    public static void main(String[] args) {
        new Bitbites("./data.txt", "./presets.txt").run();
    }
    //@@author
}
