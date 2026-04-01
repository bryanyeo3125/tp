package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * GoalsStorage.java
 *
 * Handles reading and writing of user goal data to disk.
 * Goals are stored per user in the data/ directory, allowing
 * each user to maintain independent goal settings across sessions.
 *
 * Goal files are stored in the format:
 *   dailyCalories=VALUE
 *   dailyProtein=VALUE
 *   weeklyCalories=VALUE
 *   weeklyProtein=VALUE
 */
// @@author bryanyeo3125
public class GoalsStorage {

    /**
     * Saves the given goals to disk for the specified user.
     *
     * @param name           The name of the user.
     * @param dailyCalories  The daily calorie goal.
     * @param dailyProtein   The daily protein goal.
     * @param weeklyCalories The weekly calorie goal.
     * @param weeklyProtein  The weekly protein goal.
     */
    public static void saveGoals(String name, int dailyCalories, double dailyProtein,
                                 int weeklyCalories, double weeklyProtein) {
        try {
            String safeName = name.toLowerCase().replaceAll("\\s+", "_");
            File file = new File("data/" + safeName + "_goals.txt");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write("dailyCalories=" + dailyCalories + "\n");
            writer.write("dailyProtein=" + dailyProtein + "\n");
            writer.write("weeklyCalories=" + weeklyCalories + "\n");
            writer.write("weeklyProtein=" + weeklyProtein + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving goals: " + e.getMessage());
        }
    }

    /**
     * Loads goals from disk for the given user.
     * Returns null if no goals file is found or cannot be parsed.
     *
     * @param name The name of the user whose goals should be loaded.
     * @return An int array [dailyCalories, weeklyCalories] and double array,
     *         or null if not found. Returns a double[] of
     *         {dailyCalories, dailyProtein, weeklyCalories, weeklyProtein}.
     */
    public static double[] loadGoals(String name) {
        try {
            String safeName = name.toLowerCase().replaceAll("\\s+", "_");
            File file = new File("data/" + safeName + "_goals.txt");
            if (!file.exists()) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            double dailyCalories = Double.parseDouble(reader.readLine().split("=")[1]);
            double dailyProtein = Double.parseDouble(reader.readLine().split("=")[1]);
            double weeklyCalories = Double.parseDouble(reader.readLine().split("=")[1]);
            double weeklyProtein = Double.parseDouble(reader.readLine().split("=")[1]);
            reader.close();
            return new double[]{dailyCalories, dailyProtein, weeklyCalories, weeklyProtein};
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Checks whether a goals file exists for the given user.
     *
     * @param name The name of the user to check.
     * @return true if a goals file exists, false otherwise.
     */
    public static boolean goalsExist(String name) {
        String safeName = name.toLowerCase().replaceAll("\\s+", "_");
        return new File("data/" + safeName + "_goals.txt").exists();
    }
}
// @@author
