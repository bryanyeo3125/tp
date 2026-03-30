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
import command.GoalsCommand;

public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    public Parser() {}

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
        } else if (fullCommand.equals("goals") || fullCommand.startsWith("goals set")) {
            return new GoalsCommand(fullCommand);
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
