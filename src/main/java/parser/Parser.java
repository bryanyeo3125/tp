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
        } else if (fullCommand.equals("exit")) {
            logger.log(Level.INFO, "Attempting to exit");
            return new ExitCommand();
        } else {
            logger.log(Level.WARNING, "Unknown command: " + fullCommand);
            throw new BitbitesException(BitbitesResponses.unknownCommand);
        }
    }
}
