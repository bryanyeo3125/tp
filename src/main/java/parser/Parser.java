package parser;

import java.util.logging.Logger;
import java.util.logging.Level;
import command.HistoryBestCommand;
import command.HistoryCommand;
import command.HistoryStreakCommand;
import command.HistoryTopCommand;
import command.SummaryByDateCommand;
import command.SummaryCompareCommand;
import command.SummaryRangeCommand;
import command.TipsCommand;
import command.ProfileCommand;
import command.LoginCommand;
import command.MotivateCommand;
import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;

import command.Command;
import command.DeleteCommand;
import command.HelpCommand;
import command.AddCommand;
import command.ListCommand;
import command.ListByDateCommand;
import command.EditCommand;
import command.ExitCommand;
import command.GoalsCommand;
import command.PresetCommand;

//@@author RayminQAQ
public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    public Parser() {}

    public static Command parse(String fullCommand) {
        assert fullCommand != null : "Command should not be null";
        fullCommand = fullCommand.trim();
        assert !fullCommand.isEmpty() : "Command should not be empty";

        if (fullCommand.startsWith("list d/")) {
            return new ListByDateCommand(fullCommand);
        } else if (fullCommand.equals("list")) {
            return new ListCommand();
        } else if (fullCommand.startsWith("add ")) {
            return new AddCommand(fullCommand);
        } else if (fullCommand.equals("help")) {
            return new HelpCommand();
        } else if (fullCommand.equals("goals") || fullCommand.startsWith("goals set")) {
            return new GoalsCommand(fullCommand);
        } else if (fullCommand.startsWith("delete")) {
            return new DeleteCommand(fullCommand);
        } else if (fullCommand.startsWith("edit")) {
            return new EditCommand(fullCommand);
        } else if (fullCommand.startsWith("preset")) {
            return new PresetCommand(fullCommand);
        } else if (fullCommand.startsWith("summary from/")) {
            return new SummaryRangeCommand(fullCommand);
        } else if (fullCommand.startsWith("summary compare")) {
            return new SummaryCompareCommand(fullCommand);
        } else if (fullCommand.startsWith("summary d/")) {
            return new SummaryByDateCommand(fullCommand);
        } else if (fullCommand.startsWith("history /top")) {
            return new HistoryTopCommand(fullCommand);
        } else if (fullCommand.startsWith("history /best")) {
            return new HistoryBestCommand(fullCommand);
        } else if (fullCommand.equals("history streak")) {
            return new HistoryStreakCommand();
        } else if (fullCommand.equals("history")) {
            return new HistoryCommand();
        } else if (fullCommand.equals("tips")) {
            return new TipsCommand();
        } else if (fullCommand.equals("profile") || fullCommand.startsWith("profile set")
                || fullCommand .equals("profile clear")) {
            return new ProfileCommand(fullCommand);
        } else if (fullCommand.equals("exit")) {
            logger.log(Level.INFO, "Attempting to exit application");
            logger.log(Level.FINE, "Exit command received from user");
            return new ExitCommand();
        } else if (fullCommand.equals("login")) {
            logger.log(Level.CONFIG, "Login command received from user");
            logger.log(Level.CONFIG, "Initiating user authentication and profile switching process");
            logger.log(Level.FINE, "Creating LoginCommand instance to handle user login flow");
            return new LoginCommand();
        } else if (fullCommand.equals("motivate")) {
            logger.log(Level.CONFIG, "Motivate command received from user");
            logger.log(Level.FINE, "Creating MotivateCommand instance to handle motivate flow");
            return new MotivateCommand(fullCommand);
        } else {
            logger.log(Level.WARNING, "Unknown command received: " + fullCommand);
            logger.log(Level.WARNING, "Command does not match any recognized command patterns");
            logger.log(Level.FINE, "Throwing BitbitesException due to invalid command input");
            throw new BitbitesException(BitbitesResponses.unknownCommand);
        }
    }
}
//@@author
