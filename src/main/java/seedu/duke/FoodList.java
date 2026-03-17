/**
 * FoodList.java
 * 
 * This file defines the FoodList container class that manages a collection of Food items.
 * Provides methods to add and retrieve food items.
 * 
 * Dependencies:
 * - Food: Data model for individual food items
 * - java.util.ArrayList: For storing food items
 */
package seedu.duke;

/**
 * FoodList manages a collection of Food objects.
 * Provides functionality to add, retrieve, and manage food items in the application.
 */
public class FoodList {
    private java.util.ArrayList<Food> foodList;

    public FoodList() {
        this.foodList = new java.util.ArrayList<Food>();

        // For testing handleListAll only, delete once handleAdd has been implemented
        Food initialFood = new Food("Burger", 443, 27, "16/03/2026");
        foodList.add(initialFood);
    }

    public void addFood(Food food) {
        this.foodList.add(food);
    }

    public Food deleteFood(int index) {
        if (index < 0 || index >= foodList.size()) {
            throw new BitbitesException(BitbitesResponses.deleteErrorMessage);
        }
        return foodList.remove(index);
    }

    public java.util.ArrayList<Food> getFoodList() {
        return this.foodList;
    }

    public int size() {
        return foodList.size();
    }

    public Food get(int i) {
        return foodList.get(i);
    }
}
