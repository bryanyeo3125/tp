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
    }

    public void addFood(Food food) {
        this.foodList.add(food);
    }

    public java.util.ArrayList<Food> getFoodList() {
        return this.foodList;
    }
}
