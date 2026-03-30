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
public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Food> load() throws FileNotFoundException {
        ArrayList<Food> loadedFoods = new ArrayList<>();
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
                    int calories = Integer.parseInt(parts[1].trim());
                    double protein = Double.parseDouble(parts[2].trim());
                    String date = parts[3].trim();
                    loadedFoods.add(new Food(name, calories, protein, date));
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

    public void save(FoodList foodList) {
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

    public void save(PresetList presetList) {
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
}
//@@author
