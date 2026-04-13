package command;

import seedu.bitbites.AppContext;
import seedu.bitbites.BitbitesException;

/**
 * Command.java
 *
 * Abstract base class for all commands in the application.
 * Implements the Command design pattern, where each user action is
 * encapsulated as a concrete Command subclass.
 *
 * Also provides shared utility methods for parsing prefix-based input,
 * used by subclasses such as GoalsCommand and ProfileCommand.
 */
// @@author bryanyeo3125
public abstract class Command {

    /**
     * Executes the command using the provided application context.
     *
     * @param context The application context containing FoodList, PresetList, and UserInterface.
     * @return true if the application should exit, false otherwise.
     */
    public abstract boolean execute(AppContext context);

    /**
     * Extracts the value associated with a given prefix from a command string,
     * stopping at the next prefix or end of string.
     *
     * @param command  The full command string to extract from.
     * @param prefix   The prefix whose value should be extracted (e.g. "n/").
     * @param nextPfx  The next prefix that terminates the value, or null if none.
     * @return The extracted value as a trimmed string.
     */
    protected String extractValue(String command, String prefix, String nextPfx) {
        int start = command.indexOf(prefix) + prefix.length();
        int end = nextPfx == null ? command.length() : command.indexOf(nextPfx);
        return command.substring(start, end).trim();
    }

    /**
     * Finds the nearest prefix that appears after the current prefix in the command string.
     * Used to determine the boundary when extracting a prefix's value.
     *
     * @param command       The full command string.
     * @param currentPrefix The prefix currently being processed.
     * @param prefixes      The array of all known prefixes to search through.
     * @return The nearest following prefix string, or null if none found.
     */
    protected String nextPrefix(String command, String currentPrefix, String[] prefixes) {
        int currentIndex = command.indexOf(currentPrefix);
        int nearest = Integer.MAX_VALUE;
        String nearestPrefix = null;
        for (String p : prefixes) {
            if (p.equals(currentPrefix)) {
                continue;
            }
            int idx = command.indexOf(p);
            if (idx > currentIndex && idx < nearest) {
                nearest = idx;
                nearestPrefix = p;
            }
        }
        return nearestPrefix;
    }

    /**
     * Validates that the given date string is in DD-MM-YYYY format and represents
     * a real calendar date. Throws BitbitesException if validation fails.
     *
     * @param date The date string to validate.
     * @throws BitbitesException If the format is wrong or the date is not a real date.
     */
    protected static void validateDate(String date) {
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new BitbitesException("Date must be in DD-MM-YYYY format.");
        }
        java.time.format.DateTimeFormatter strictFormatter = java.time.format.DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(java.time.format.ResolverStyle.STRICT);
        try {
            java.time.LocalDate.parse(date, strictFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new BitbitesException("Invalid date: " + date +
                    ". Please enter a real date in DD-MM-YYYY format.");
        }
    }
}
// @@author
