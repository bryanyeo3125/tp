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
package model;


import java.util.ArrayList;
import java.util.List;

import seedu.bitbites.BitbitesException;
import seedu.bitbites.BitbitesResponses;

/**
 * FoodList manages a collection of Food objects.
 * Provides functionality to add, retrieve, and manage food items in the application.
 */
public class FoodList {
    private java.util.ArrayList<Food> foodList;

    public FoodList() {
        this.foodList = new ArrayList<>();
    }

    //@@author j-kennethh
    public FoodList(ArrayList<Food> loadedFoods) {
        this.foodList = loadedFoods;
    }
    //@@author

    public void addFood(Food food) {
        this.foodList.add(food);
    }

    public Food deleteFood(int index) {
        if (index < 0 || index >= foodList.size()) {
            throw new BitbitesException(BitbitesResponses.DELETE_ERROR_MESSAGE);
        }
        return foodList.remove(index);
    }

    public java.util.ArrayList<Food> getFoodList() {
        return this.foodList;
    }

    public Food getFood(int index) {
        if (index < 0 || index >= foodList.size()) {
            throw new BitbitesException("Invalid index. Please provide a valid item number.");
        }
        return foodList.get(index);
    }

    //@@author j-kennethh
    public int size() {
        return foodList.size();
    }

    public Food get(int i) {
        return foodList.get(i);
    }
    //@@author


    // ── Summary ───────────────────────────────────────────
    public List<Food> getItemsByDate(String date) {
        assert date != null && !date.isEmpty() : "Date should not be null or empty";
        List<Food> result = new ArrayList<>();
        for (Food food : foodList) {
            if (food.getDate().equals(date)) {
                result.add(food);
            }
        }
        return result;
    }

    public int getTotalCaloriesByDate(String date) {
        assert date != null && !date.isEmpty() : "Date should not be null or empty";
        int total = 0;
        for (Food food : foodList) {
            if (food.getDate().equals(date)) {
                total += food.getCalories();
            }
        }
        assert total >= 0 : "Total calories should not be negative";
        return total;
    }

    public double getTotalProteinByDate(String date) {
        assert date != null && !date.isEmpty() : "Date should not be null or empty";
        double total = 0;
        for (Food food : foodList) {
            if (food.getDate().equals(date)) {
                total += food.getProtein();
            }
        }
        assert total >= 0 : "Total protein should not be negative";
        return total;
    }

    public int getItemCountByDate(String date) {
        assert date != null && !date.isEmpty() : "Date should not be null or empty";
        int count = 0;
        for (Food food : foodList) {
            if (food.getDate().equals(date)) {
                count++;
            }
        }
        return count;
    }

    public List<String> getUniqueDates() {
        List<String> dates = new ArrayList<>();
        for (Food food : foodList) {
            if (!dates.contains(food.getDate())) {
                dates.add(food.getDate());
            }
        }
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dates.sort((a, b) -> {
            java.time.LocalDate d1 = java.time.LocalDate.parse(a, formatter);
            java.time.LocalDate d2 = java.time.LocalDate.parse(b, formatter);
            return d1.compareTo(d2);
        });
        return dates;
    }

    public NutritionSummary getSummaryByDate(String date) {
        List<Food> items = getItemsByDate(date);
        int totalCalories = getTotalCaloriesByDate(date);
        double totalProtein = getTotalProteinByDate(date);
        return new NutritionSummary(date, totalCalories, totalProtein, items.size(), items);
    }

    public List<NutritionSummary> getAllDailySummaries() {
        List<NutritionSummary> summaries = new ArrayList<>();
        for (String date : getUniqueDates()) {
            summaries.add(getSummaryByDate(date));
        }
        return summaries;
    }

    public List<NutritionSummary> getSummariesInRange(String fromDate, String toDate) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        java.time.LocalDate from = java.time.LocalDate.parse(fromDate, formatter);
        java.time.LocalDate to = java.time.LocalDate.parse(toDate, formatter);

        List<NutritionSummary> summaries = new ArrayList<>();
        for (String date : getUniqueDates()) {
            java.time.LocalDate d = java.time.LocalDate.parse(date, formatter);
            if (!d.isBefore(from) && !d.isAfter(to)) {
                summaries.add(getSummaryByDate(date));
            }
        }
        return summaries;
    }

    public List<NutritionSummary> getDaysClosestToGoal(int n, int calorieGoal) {
        assert n > 0 : "N should be positive";
        List<NutritionSummary> summaries = new ArrayList<>(getAllDailySummaries());
        summaries.sort((a, b) -> {
            int diffA = Math.abs(a.getTotalCalories() - calorieGoal);
            int diffB = Math.abs(b.getTotalCalories() - calorieGoal);
            return diffA - diffB;
        });
        return summaries.subList(0, Math.min(n, summaries.size()));
    }

    public List<NutritionSummary> getTopDaysByCalories(int n) {
        List<NutritionSummary> summaries = new ArrayList<>(getAllDailySummaries());
        summaries.sort((a, b) -> b.getTotalCalories() - a.getTotalCalories());
        return summaries.subList(0, Math.min(n, summaries.size()));
    }

    public List<NutritionSummary> getBestDaysByCalories(int n) {
        List<NutritionSummary> summaries = new ArrayList<>(getAllDailySummaries());
        summaries.sort((a, b) -> a.getTotalCalories() - b.getTotalCalories());
        return summaries.subList(0, Math.min(n, summaries.size()));
    }

    public int getLongestStreak() {
        List<String> dates = getUniqueDates();
        if (dates.isEmpty()) {
            return 0;
        }
        int longestStreak = 1;
        int currentStreak = 1;
        for (int i = 1; i < dates.size(); i++) {
            if (isConsecutive(dates.get(i - 1), dates.get(i))) {
                currentStreak++;
                longestStreak = Math.max(longestStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }
        assert longestStreak >= 1 : "Streak should be at least 1 if dates exist";
        return longestStreak;
    }

    public int getCurrentStreak() {
        List<String> dates = getUniqueDates();
        if (dates.isEmpty()) {
            return 0;
        }

        String today = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String yesterday = java.time.LocalDate.now().minusDays(1)
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String lastRecorded = dates.get(dates.size() - 1);

        if (!lastRecorded.equals(today) && !lastRecorded.equals(yesterday)) {
            return 0;
        }

        int currentStreak = 1;
        for (int i = dates.size() - 1; i > 0; i--) {
            if (isConsecutive(dates.get(i - 1), dates.get(i))) {
                currentStreak++;
            } else {
                break;
            }
        }
        return currentStreak;
    }

    private boolean isConsecutive(String date1, String date2) {
        assert date1 != null && date2 != null : "Dates should not be null";
        try {
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.LocalDate d1 = java.time.LocalDate.parse(date1, formatter);
            java.time.LocalDate d2 = java.time.LocalDate.parse(date2, formatter);
            return d1.plusDays(1).equals(d2);
        } catch (Exception e) {
            return false;
        }
    }

}
