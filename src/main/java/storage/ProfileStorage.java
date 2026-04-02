package storage;

import model.Profile;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * ProfileStorage.java
 *
 * Handles reading and writing of user profile data to disk.
 * Each profile is stored as a separate text file named after the user,
 * allowing multiple users to maintain independent profiles on the same system.
 *
 * Profile files are stored in the data/ directory in the format:
 *   name=VALUE
 *   age=VALUE
 *   weight=VALUE
 *   height=VALUE
 */
// @@author bryanyeo3125
public class ProfileStorage {
    private static final String FILE_PATH = "data/profile.txt";

    /**
     * Saves the given profile to disk.
     * The file is named after the user's name in lowercase with spaces replaced by underscores.
     * Creates the data/ directory if it does not already exist.
     *
     * @param profile The Profile object to save.
     */
    public static void saveProfile(Profile profile) {
        try {
            String safeName = profile.getName().toLowerCase().replaceAll("\\s+", "_");
            File file = new File("data/" + safeName + "_profile.txt");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write("name=" + profile.getName() + "\n");
            writer.write("gender=" + profile.getGender() + "\n");
            writer.write("age=" + profile.getAge() + "\n");
            writer.write("weight=" + profile.getWeight() + "\n");
            writer.write("height=" + profile.getHeight() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    /**
     * Loads a profile from disk for the given username.
     * Returns null if no profile file is found or if the file cannot be parsed.
     *
     * @param name The name of the user whose profile should be loaded.
     * @return The loaded Profile object, or null if not found.
     */
    public static Profile loadProfile(String name) {
        try {
            String safeName = name.toLowerCase().replaceAll("\\s+", "_");
            File file = new File("data/" + safeName + "_profile.txt");
            if (!file.exists()) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String profileName = reader.readLine().split("=")[1];
            String gender = reader.readLine().split("=")[1];
            int age = Integer.parseInt(reader.readLine().split("=")[1]);
            double weight = Double.parseDouble(reader.readLine().split("=")[1]);
            double height = Double.parseDouble(reader.readLine().split("=")[1]);
            reader.close();
            return new Profile(profileName, gender, age, weight, height);
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Loads a profile directly from a given file object.
     * Used internally by listProfiles() to read each profile file in the data/ directory.
     * Returns null if the file cannot be parsed or an IO error occurs.
     *
     * @param file The profile file to load.
     * @return The loaded Profile object, or null if loading fails.
     */
    private static Profile loadProfile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String profileName = reader.readLine().split("=")[1];
            String gender = reader.readLine().split("=")[1];
            int age = Integer.parseInt(reader.readLine().split("=")[1]);
            double weight = Double.parseDouble(reader.readLine().split("=")[1]);
            double height = Double.parseDouble(reader.readLine().split("=")[1]);
            reader.close();
            return new Profile(profileName, gender, age, weight, height);
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Checks whether a profile file exists for the given username.
     *
     * @param name The name of the user to check.
     * @return true if a profile file exists, false otherwise.
     */
    public static boolean profileExists(String name) {
        String safeName = name.toLowerCase().replaceAll("\\s+", "_");
        return new File("data/" + safeName + "_profile.txt").exists();
    }

    /**
     * Lists all saved profiles stored in the data/ directory.
     * Displays each profile's name, BMI and BMR in a formatted summary.
     * Does nothing if no profiles are found or the data/ directory does not exist.
     */
    public static void listProfiles() {
        File dataDir = new File("data/");
        if (!dataDir.exists()) {
            System.out.println("No profiles found.");
            return;
        }
        File[] profileFiles = dataDir.listFiles((dir, name) -> name.endsWith("_profile.txt"));
        if (profileFiles == null || profileFiles.length == 0) {
            System.out.println("No profiles found.");
            return;
        }
        System.out.println("========== SAVED PROFILES ==========");
        for (int i = 0; i < profileFiles.length; i++) {
            Profile p = loadProfile(profileFiles[i]);
            if (p != null) {
                System.out.printf("%d. %s (BMI: %.1f, BMR: %d kcal/day)%n",
                        i + 1, p.getName(), p.getBmi(), p.getBmr());
            }
        }
        System.out.println("====================================");
    }

    /**
     * Deletes the profile file for the given username.
     * Does nothing if no profile file exists.
     *
     * @param name The name of the user whose profile should be deleted.
     */
    public static void deleteProfile(String name) {
        String safeName = name.toLowerCase().replaceAll("\\s+", "_");
        new File("data/" + safeName + "_profile.txt").delete();
    }
}
// @@author
