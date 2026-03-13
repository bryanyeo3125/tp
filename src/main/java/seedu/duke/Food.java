/**
 * Food.java
 * 
 * This file defines the Food data model representing a single food item.
 * Stores food information including name, calories, protein, and date.
 * 
 * Dependencies:
 * - None (standalone data model)
 */
package seedu.duke;

/**
 * Food represents a single food item with nutritional and temporal information.
 * Design follows the format: add n/NAME c/CALORIES_IN_KCAL p/PROTEIN_IN_G d/DATE
 */
public class Food {
    private String name;
    private int calories;
    private double protein;
    private String date;

    public Food(String name, int calories, double protein, String date) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.date = date;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
