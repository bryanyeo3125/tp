package command;

import model.FoodList;
import model.Food;
import ui.UserInterface;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// @@author bryanyeo3125
public class GoalsCommand extends Command {

    private static int dailyCalorieGoal = 2000;
    private static double dailyProteinGoal = 50.0;
    private static int weeklyCalorieGoal = 14000;
    private static double weeklyProteinGoal = 350.0;

    private final String fullCommand;

    public GoalsCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(FoodList foodList, UserInterface ui) {
        if (fullCommand.equals("goals")) {
            showGoalsMenu(foodList);
        } else if (fullCommand.startsWith("goals set")) {
            if (fullCommand.trim().equals("goals set")) {
                // No prefixes provided, show usage
                System.out.println("Please specify at least one goal to set.");
                System.out.println("Format: goals set dc/CALORIES dp/PROTEIN wc/CALORIES wp/PROTEIN");
                System.out.println("Example: goals set dc/2500 dp/60 wc/17500 wp/420");
            } else if (!hasValidGoalPrefix(fullCommand)) {
                System.out.println("Unknown goal prefix detected.");
                System.out.println("Valid prefixes: dc/ (daily calories), dp/ (daily protein), " +
                        "wc/ (weekly calories), wp/ (weekly protein)");
                System.out.println("Example: goals set dc/2500 dp/60");
            } else {
                handleSetGoals(fullCommand);
            }
        } else {
            System.out.println("Unknown goals command: " + fullCommand);
            System.out.println("Available goals commands:");
            System.out.println("  goals                          - View your progress");
            System.out.println("  goals set dc/CAL dp/PROT       - Set daily goals");
            System.out.println("  goals set wc/CAL wp/PROT       - Set weekly goals");
        }
        return false;
    }

    private boolean hasValidGoalPrefix(String command) {
        return command.contains("dc/") || command.contains("dp/") ||
                command.contains("wc/") || command.contains("wp/");
    }

    private void showGoalsMenu(FoodList foodList) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        int dailyCalories = 0;
        double dailyProtein = 0.0;
        int weeklyCalories = 0;
        double weeklyProtein = 0.0;

        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            LocalDate foodDate = LocalDate.parse(food.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            if (food.getDate().equals(today)) {
                dailyCalories += food.getCalories();
                dailyProtein += food.getProtein();
            }

            if (!foodDate.isBefore(weekStart) && !foodDate.isAfter(weekEnd)) {
                weeklyCalories += food.getCalories();
                weeklyProtein += food.getProtein();
            }
        }

        System.out.println("========== YOUR GOALS ==========");
        System.out.println("--- Daily (" + today + ") ---");
        System.out.printf("Calories : %d / %d kcal %s%n",
                dailyCalories, dailyCalorieGoal,
                dailyCalories >= dailyCalorieGoal ? "(Goal reached!)" : "");
        System.out.printf("Protein  : %.1f / %.1fg %s%n",
                dailyProtein, dailyProteinGoal,
                dailyProtein >= dailyProteinGoal ? "(Goal reached!)" : "");

        System.out.println("--- Weekly (" + weekStart + " to " + weekEnd + ") ---");
        System.out.printf("Calories : %d / %d kcal %s%n",
                weeklyCalories, weeklyCalorieGoal,
                weeklyCalories >= weeklyCalorieGoal ? "(Goal reached!)" : "");
        System.out.printf("Protein  : %.1f / %.1fg %s%n",
                weeklyProtein, weeklyProteinGoal,
                weeklyProtein >= weeklyProteinGoal ? "(Goal reached!)" : "");
        System.out.println("================================");
    }

    private void handleSetGoals(String fullCommand) {
        // Format: goals set dc/CALORIES dp/PROTEIN wc/CALORIES wp/PROTEIN
        try {
            if (fullCommand.contains("dc/")) {
                int val = Integer.parseInt(extractValue(fullCommand, "dc/", nextPrefix(fullCommand, "dc/")));
                if (val < 0) throw new NumberFormatException();
                dailyCalorieGoal = val;
                System.out.println("Daily calorie goal set to " + val + " kcal.");
            }
            if (fullCommand.contains("dp/")) {
                double val = Double.parseDouble(extractValue(fullCommand, "dp/", nextPrefix(fullCommand, "dp/")));
                if (val < 0) throw new NumberFormatException();
                dailyProteinGoal = val;
                System.out.println("Daily protein goal set to " + val + "g.");
            }
            if (fullCommand.contains("wc/")) {
                int val = Integer.parseInt(extractValue(fullCommand, "wc/", nextPrefix(fullCommand, "wc/")));
                if (val < 0) throw new NumberFormatException();
                weeklyCalorieGoal = val;
                System.out.println("Weekly calorie goal set to " + val + " kcal.");
            }
            if (fullCommand.contains("wp/")) {
                double val = Double.parseDouble(extractValue(fullCommand, "wp/", nextPrefix(fullCommand, "wp/")));
                if (val < 0) throw new NumberFormatException();
                weeklyProteinGoal = val;
                System.out.println("Weekly protein goal set to " + val + "g.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid value. Goals must be non-negative numbers.");
        }
    }

    private String extractValue(String command, String prefix, String nextPfx) {
        int start = command.indexOf(prefix) + prefix.length();
        int end = nextPfx == null ? command.length() : command.indexOf(nextPfx);
        return command.substring(start, end).trim();
    }

    private String nextPrefix(String command, String currentPrefix) {
        String[] prefixes = {"dc/", "dp/", "wc/", "wp/"};
        int currentIndex = command.indexOf(currentPrefix);
        int nearest = Integer.MAX_VALUE;
        String nearestPrefix = null;
        for (String p : prefixes) {
            if (p.equals(currentPrefix)) continue;
            int idx = command.indexOf(p);
            if (idx > currentIndex && idx < nearest) {
                nearest = idx;
                nearestPrefix = p;
            }
        }
        return nearestPrefix;
    }
}
// @@author