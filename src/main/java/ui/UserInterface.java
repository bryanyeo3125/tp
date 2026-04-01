/**
 * UserInterface.java
 * <p>
 * This file handles all user input/output operations for the chatbot.
 * Displays messages to the user and reads commands from standard input.
 * <p>
 * Dependencies:
 * - java.util.Scanner: For reading user input from console
 * - BitbitesResponses: For accessing predefined response messages
 */
package ui;

import java.util.List;
import java.util.Scanner;
import model.Food;
import model.NutritionSummary;
import storage.ProfileStorage;
import model.Profile;
import seedu.bitbites.BitbitesResponses;

/**
 * UserInterface manages all interactions between the chatbot and the user.
 * Handles display of messages and reading of user commands from the console.
 */
public class UserInterface {
    /**
     * The scanner used to read user input from the console.
     */
    private final Scanner scanner;
    private String currentUser;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    /// / READ section ////
    /* Reads a command from the user input. */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /// / SHOW section ////
    /* Displays the welcome message and prompts the user for their name. */
    public void showWelcome() {
        System.out.println(BitbitesResponses.welcomeMessage);
        System.out.println("What is your name?");
        String name = this.scanner.nextLine();
        this.currentUser = name;

        Profile profile = ProfileStorage.loadProfile(name);
        if (profile != null) {
            System.out.println("Welcome back " + name + "!");
        } else {
            System.out.println("Hello " + name + "! Set up your profile with:");
            System.out.println("  profile set n/" + name + " a/AGE w/WEIGHT h/HEIGHT");
        }
        System.out.println("Type 'help' for a list of available commands.");
    }

    public String getCurrentUser() {
        return currentUser;
    }

    /* Show an error message to the user. */
    public void showError(String message) {
        System.out.println(message);
    }

    /* Show the edit message to the user. */
    public void showEditedFood(Food food) {
        assert food != null : "Food should not be null";
        System.out.println(BitbitesResponses.editMessage);
        System.out.println("  " + food);
    }

    /* Show the summary to the user. */
    public void showSummary(NutritionSummary summary) {
        System.out.println(summary.toString());
    }

    public void showSummaryCompare(NutritionSummary s1, NutritionSummary s2) {
        int maxCal = Math.max(s1.getTotalCalories(), s2.getTotalCalories());
        int maxBarLength = 30;

        System.out.println("Comparing " + s1.getDate() + " vs " + s2.getDate() + ":");
        System.out.printf("  %-12s  %4d kcal | %.1fg protein  %s%n",
                s1.getDate(), s1.getTotalCalories(), s1.getTotalProtein(),
                ProgressBar.generateSegmented(s1.getItems(),
                        s1.getTotalCalories(), maxCal, maxBarLength));
        System.out.printf("  %-12s  %4d kcal | %.1fg protein  %s%n",
                s2.getDate(), s2.getTotalCalories(), s2.getTotalProtein(),
                ProgressBar.generateSegmented(s2.getItems(),
                        s2.getTotalCalories(), maxCal, maxBarLength));

        int calDiff = s1.getTotalCalories() - s2.getTotalCalories();
        double proteinDiff = s1.getTotalProtein() - s2.getTotalProtein();
        System.out.printf("  Calorie difference : %+d kcal%n", calDiff);
        System.out.printf("  Protein difference : %+.1fg%n", proteinDiff);
    }

    public void showSummaryRange(List<NutritionSummary> summaries, String from, String to) {
        System.out.println("Trend from " + from + " to " + to + ":");
        if (summaries.isEmpty()) {
            System.out.println("  No food items found in this range.");
            return;
        }

        int maxCalories = summaries.stream()
                .mapToInt(NutritionSummary::getTotalCalories)
                .max().orElse(1);
        int maxBarLength = 30;

        System.out.printf("  %-12s  %-10s  %-10s  %s%n",
                "Date", "Calories", "Protein", "Breakdown");
        System.out.println("  " + "-".repeat(66));

        for (NutritionSummary s : summaries) {
            String bar = ProgressBar.generateSegmented(
                    s.getItems(), s.getTotalCalories(), maxCalories, maxBarLength);
            System.out.printf("  %-12s  %-10s  %-10s  %s%n",
                    s.getDate(),
                    s.getTotalCalories() + " kcal",
                    String.format("%.1fg", s.getTotalProtein()),
                    bar);
        }
    }

    /* Show the history count to the user. */
    public void showHistory(List<NutritionSummary> summaries) {
        assert summaries != null : "Summaries should not be null";
        if (summaries.isEmpty()) {
            System.out.println("No food history found.");
            return;
        }
        System.out.println("Food History:");
        System.out.printf("  %-12s  %-12s  %-10s  %s%n",
                "Date", "Calories", "Protein", "Items");
        System.out.println("  " + "-".repeat(46));
        for (NutritionSummary s : summaries) {
            System.out.printf("  %-12s  %-12s  %-10s  %d%n",
                    s.getDate(),
                    s.getTotalCalories() + " kcal",
                    String.format("%.1fg", s.getTotalProtein()),
                    s.getItemCount());
        }
    }

    public void showHistoryTop(List<NutritionSummary> summaries, int n) {
        System.out.println("Top " + n + " Highest Calorie Days:");
        if (summaries.isEmpty()) {
            System.out.println("  No food history found.");
            return;
        }
        for (int i = 0; i < summaries.size(); i++) {
            NutritionSummary s = summaries.get(i);
            System.out.printf("  %d. %s  %d kcal | %.1fg protein%n",
                    i + 1, s.getDate(), s.getTotalCalories(), s.getTotalProtein());
        }
    }

    public void showHistoryBest(List<NutritionSummary> summaries, int n) {
        System.out.println("Top " + n + " Lowest Calorie Days:");
        if (summaries.isEmpty()) {
            System.out.println("  No food history found.");
            return;
        }
        for (int i = 0; i < summaries.size(); i++) {
            NutritionSummary s = summaries.get(i);
            System.out.printf("  %d. %s  %d kcal | %.1fg protein%n",
                    i + 1, s.getDate(), s.getTotalCalories(), s.getTotalProtein());
        }
    }

    public void showStreak(int current, int longest) {
        System.out.println("Tracking Streak:");
        System.out.println("  Current streak : " + current + " day(s)");
        System.out.println("  Longest streak : " + longest + " day(s)");
        if (current == longest && current > 1) {
            System.out.println("  You're on your best streak ever! Keep it up!");
        } else if (current >= 7) {
            System.out.println("  Amazing! A week-long streak!");
        } else if (current >= 3) {
            System.out.println("  Great consistency! Keep going!");
        }
    }

    public void showTips() {
        System.out.println(BitbitesResponses.tipsMessage);
    }

    /* Show the exit message to the user. */
    public void showExit() {
        System.out.println(BitbitesResponses.exitMessage);
    }

    public void showHelp() {
        System.out.println(BitbitesResponses.helpMessage);
    }

    public void showDeletedFood(Food food, int remaining) {
        System.out.println(BitbitesResponses.deleteMessage);
        System.out.println("  " + food);
        System.out.println("Now you have " + remaining + " item(s) in the list.");
    }
}
