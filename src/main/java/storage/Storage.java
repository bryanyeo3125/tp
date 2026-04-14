package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


import model.Food;
import model.FoodList;
import model.PresetList;
import seedu.bitbites.BitbitesException;

//@@author j-kennethh
/**
 * Represents the file storage manager for the application.
 * Handles the reading and writing of data (such as daily food logs and saved presets)
 * to and from persistent text files on the local hard drive.
 */
public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String filePath;

    /**
     * Constructs a {@code Storage} object that reads from and writes to the specified file path.
     *
     * @param filePath The relative or absolute path to the text file used for storage.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    private void ensureDirectoryExists() {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Loads food data from the storage file.
     * Parses each formatted line in the text file and converts it into a {@code Food} object.
     * If the file does not exist, it returns an empty list and the file will be created upon saving.
     *
     * @return An {@code ArrayList} of {@code Food} objects loaded from the file.
     * @throws FileNotFoundException If the file cannot be accessed or read by the Scanner.
     * @throws BitbitesException If the data file is corrupted and numbers cannot be parsed properly.
     */
    public ArrayList<Food> load() throws FileNotFoundException {
        ArrayList<Food> loadedFoods = new ArrayList<>();
        boolean isPresetFile = filePath.contains("preset");
        File file = new File(filePath);

        if (!file.exists()) {
            logger.log(Level.INFO, "Data file not found. A new one will be created upon saving.");
            return loadedFoods;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");

                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String dateStr = parts[3].trim();

                    if (isPresetFile) {
                        if (!dateStr.equals("PRESET")) {
                            logger.log(Level.WARNING, "Skipping non-preset entry in preset file: " + line);
                            continue;
                        }
                    } else {
                        if (!dateStr.matches("\\d{2}-\\d{2}-\\d{4}")) {
                            logger.log(Level.WARNING, "Skipping entry with invalid date: " + line);
                            continue;
                        }
                    }

                    try {
                        int calories = Integer.parseInt(parts[1].trim());
                        double protein = Double.parseDouble(parts[2].trim());
                        loadedFoods.add(new Food(name, calories, protein, dateStr));
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Skipping entry with invalid numbers: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "Error loading file: " + e.getMessage());
            throw new BitbitesException("Error loading data file.");
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Corrupted data file.");
            throw new BitbitesException("Data file is corrupted. Could not parse numbers.");
        }

        return loadedFoods;
    }

    /**
     * Saves the current list of logged foods to the storage file.
     * Automatically creates any necessary parent directories if they do not exist.
     *
     * @param foodList The {@code FoodList} containing the user's daily food entries to be saved.
     * @throws BitbitesException If an I/O error occurs while writing data to the file.
     */
    public void save(FoodList foodList) {
        ensureDirectoryExists();

        try (FileWriter fw = new FileWriter(filePath)) {
            for (int i = 0; i < foodList.size(); i++) {
                Food food = foodList.getFood(i);
                String line = food.getName() + " | " +
                        food.getCalories() + " | " +
                        food.getProtein() + " | " +
                        food.getDate() + System.lineSeparator();
                fw.write(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving to file: " + e.getMessage());
            throw new BitbitesException("An error occurred while saving data.");
        }
    }

    /**
     * Saves the current list of saved food presets to the storage file.
     * Automatically creates any necessary parent directories if they do not exist.
     *
     * @param presetList The {@code PresetList} containing the user's saved food templates to be saved.
     * @throws BitbitesException If an I/O error occurs while writing data to the file.
     */
    public void save(PresetList presetList) {
        ensureDirectoryExists();

        try (FileWriter fw = new FileWriter(filePath)) {
            for (int i = 0; i < presetList.size(); i++) {
                Food preset = presetList.getPreset(i);
                String line = preset.getName() + " | " +
                        preset.getCalories() + " | " +
                        preset.getProtein() + " | " +
                        preset.getDate() + System.lineSeparator();
                fw.write(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving to file: " + e.getMessage());
            throw new BitbitesException("An error occurred while saving data.");
        }
    }

    // @@author bryanyeo3125
    /**
     * Returns a Storage instance pointing to the given user's food file.
     *
     * @param username The current user's name.
     * @return A Storage instance for that user's food file.
     */
    public static Storage forUser(String username) {
        String safeName = username.toLowerCase().replaceAll("\\s+", "_");
        return new Storage("data/" + safeName + "_foods.txt");
    }

    /**
     * Returns a Storage instance pointing to the given user's presets file.
     *
     * @param username The current user's name.
     * @return A Storage instance for that user's presets file.
     */
    public static Storage forUserPresets(String username) {
        String safeName = username.toLowerCase().replaceAll("\\s+", "_");
        return new Storage("data/" + safeName + "_presets.txt");
    }
    // @@author
}
//@@author
