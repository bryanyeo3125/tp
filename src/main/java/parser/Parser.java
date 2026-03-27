/**
 * Parser.java
 * 
 * This file contains the Parser class responsible for interpreting user commands.
 * Routes user input to appropriate command handlers (list, add, exit).
 * 
 * Dependencies:
 * - FoodList: For accessing and modifying the food items list
 * - UserInterface: For providing feedback to users
 * - BitbitesException: For error handling
 * - BitbitesResponses: For error messages
 */

package parser;

import java.util.logging.Logger;
import java.util.logging.Level;
import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;

import command.Command;
import command.DeleteCommand;
import command.HelpCommand;
import command.AddCommand;
import command.ListCommand;
import command.ListByDateCommand;
import command.ExitCommand;

/**
 * Parser interprets user commands and executes the appropriate handlers.
 * Supports commands: list, list d/DATE, add, and exit.
 * Throws BitbitesException for unknown commands.
 */
public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    public Parser() {
        // No implementation needed for the constructor as of now
    }

    /**
     * Parses the user input and executes the corresponding command.
     * Returns true if the command is "exit", false otherwise.
     */
    public static Command parse(String fullCommand) {
        assert fullCommand != null : "Command should not be null";
        fullCommand = fullCommand.trim();
        assert !fullCommand.isEmpty() : "Command should not be empty";

        if (fullCommand.startsWith("list d/")) {
            logger.log(Level.INFO, "Attempting to list food items from date");
            return new ListByDateCommand(fullCommand);
        } else if (fullCommand.equals("list")) {
            logger.log(Level.INFO, "Attempting to list all food items");
            return new ListCommand();
        } else if (fullCommand.startsWith("add ")) {
            return new AddCommand(fullCommand);
        } else if (fullCommand.equals("help")) {
            return new HelpCommand();
        } else if (fullCommand.startsWith("delete")) {
            return new DeleteCommand(fullCommand);
        } else if (fullCommand.equals("exit")) {
            logger.log(Level.INFO, "Attempting to exit");
            return new ExitCommand();
        } else {
            logger.log(Level.WARNING, "Unknown command: " + fullCommand);
            throw new BitbitesException(BitbitesResponses.unknownCommand);
        }
    }
}


