package command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Food;
import model.FoodList;
import model.PresetList;
import seedu.bitbites.BitbitesException;
import ui.UserInterface;

//@@author j-kennethh
public class PresetCommand extends Command {
    private static final Logger logger = Logger.getLogger(PresetCommand.class.getName());
    private final String fullCommand;

    public PresetCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, PresetList presets, UserInterface ui) {
        String[] parts = fullCommand.split(" ", 3);
        if (parts.length < 2) {
            throw new BitbitesException("Invalid command. Use: preset add, preset list, preset delete, preset use");
        }

        String action = parts[1].trim();
        String arguments;

        if (parts.length > 2) {
            arguments = parts[2].trim();
        } else {
            arguments = "";
        }

        switch (action) {
        case "add":
            handleAdd(arguments, presets);
            break;
        case "list":
            handleList(presets);
            break;
        case "delete":
            handleDelete(arguments, presets);
            break;
        case "use":
            handleUse(arguments, presets, foodList);
            break;
        default:
            throw new BitbitesException("Unknown preset action: " + action);
        }

        return false;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        return false;
    }

    // Helper Methods
    private void handleAdd(String args, PresetList presets) {
        if (!args.contains("n/") || !args.contains("c/") || !args.contains("p/")) {
            throw new BitbitesException("Format: preset add n/NAME c/CALORIES p/PROTEIN");
        }

        try {
            String name = extractField(args, "n/");
            int calories = Integer.parseInt(extractField(args, "c/"));
            double protein = Double.parseDouble(extractField(args, "p/"));

            if (name.isEmpty() || calories < 0 || protein < 0) {
                throw new BitbitesException("Invalid values. Name cannot be empty, numbers must be positive.");
            }

            Food presetFood = new Food(name, calories, protein, "PRESET");
            presets.addPreset(presetFood);

            logger.log(Level.INFO, "Added new preset: " + name);
            System.out.println("Got it. I've added this to your presets:");
            System.out.println("  " + name + " (" + calories + "kcal, " + protein + "g protein)");
        } catch (NumberFormatException e) {
            throw new BitbitesException("Calories must be an integer and protein must be a number.");
        }
    }

    private void handleList(PresetList presets) {
        if (presets.size() == 0) {
            System.out.println("Your preset database is currently empty!");
            return;
        }

        System.out.println("Here are your saved presets:");

        for (int i = 0; i < presets.size(); i++) {
            Food p = presets.getPreset(i);
            System.out.println((i + 1) + ". " + p.getName() + " (" + p.getCalories() + "kcal, "
                    + p.getProtein() + "g protein)");
        }
    }

    private void handleDelete(String args, PresetList presets) {
        try {
            int index = Integer.parseInt(args.trim()) - 1;
            Food removed = presets.deletePreset(index);
            System.out.println("Got it. I've removed this preset:");
            System.out.println("  " + removed.getName());
        } catch (NumberFormatException e) {
            throw new BitbitesException("Please provide a valid preset index number.");
        }
    }

    private void handleUse(String args, PresetList presets, FoodList foodList) {
        if (presets.size() == 0) {
            throw new BitbitesException("You have no presets saved!");
        }

        try {
            String[] useParts = args.split(" ", 2);
            int index = Integer.parseInt(useParts[0].trim()) - 1;
            Food presetTemplate = presets.getPreset(index);

            String dateToUse = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            if (useParts.length > 1 && useParts[1].contains("d/")) {
                dateToUse = extractField(useParts[1], "d/");
                if (!dateToUse.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    throw new BitbitesException("Custom date must be in DD-MM-YYYY format.");
                }
            }

            Food newFood = new Food(presetTemplate.getName(), presetTemplate.getCalories(),
                    presetTemplate.getProtein(), dateToUse);
            foodList.addFood(newFood);

            System.out.println("Successfully logged from preset:");
            System.out.println("  " + newFood);

        } catch (NumberFormatException e) {
            throw new BitbitesException("Please provide a valid preset index number.");
        }
    }

    private String extractField(String args, String prefix) {
        int start = args.indexOf(prefix) + prefix.length();
        if (args.indexOf(prefix) == -1) {
            return "";
        }
        int end = args.length();

        String[] prefixes = {"n/", "c/", "p/", "d/"};
        for (String p : prefixes) {
            if (p.equals(prefix)) {
                continue;
            }
            int pos = args.indexOf(p, start);
            if (pos != -1 && pos < end) {
                end = pos;
            }
        }

        return args.substring(start, end).trim();
    }
}
//@@author
