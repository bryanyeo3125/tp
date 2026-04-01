package command;

import seedu.bitbites.AppContext;

public abstract class Command {
    public abstract boolean execute(AppContext context);

    protected String extractValue(String command, String prefix, String nextPfx) {
        int start = command.indexOf(prefix) + prefix.length();
        int end = nextPfx == null ? command.length() : command.indexOf(nextPfx);
        return command.substring(start, end).trim();
    }

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
}
