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


    // ── Summary and History ───────────────────────────────────────────
    /**
     * Returns all Food items recorded on a specific date.
     *
     * @param date The date string in "dd-MM-yyyy" format to filter by
     * @return A List of Food objects that were consumed on the specified date
     * @throws AssertionError If date is null or empty
     */
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

    /**
     * Calculates the total calories consumed on a specific date.
     *
     * @param date The date string in "dd-MM-yyyy" format to calculate for
     * @return The sum of calories from all food items on the specified date
     * @throws AssertionError If date is null or empty, or if total is negative
     */
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

    /**
     * Calculates the total protein consumed on a specific date.
     *
     * @param date The date string in "dd-MM-yyyy" format to calculate for
     * @return The sum of protein (in grams) from all food items on the specified date
     * @throws AssertionError If date is null or empty, or if total is negative
     */
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

    /**
     * Counts the number of food items recorded on a specific date.
     *
     * @param date The date string in "dd-MM-yyyy" format to count for
     * @return The number of Food objects associated with the specified date
     * @throws AssertionError If date is null or empty
     */
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

    /**
     * Returns all unique dates present in the food list, sorted chronologically.
     * Dates are returned in ascending order from earliest to latest.
     *
     * @return A List of unique date strings in "dd-MM-yyyy" format, sorted chronologically
     */
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

    /**
     * Creates a comprehensive nutritional summary for a specific date.
     * Includes total calories, total protein, item count, and the list of food items.
     *
     * @param date The date string in "dd-MM-yyyy" format to summarize
     * @return A NutritionSummary object containing all nutritional data for the date
     * @see NutritionSummary
     */
    public NutritionSummary getSummaryByDate(String date) {
        List<Food> items = getItemsByDate(date);
        int totalCalories = getTotalCaloriesByDate(date);
        double totalProtein = getTotalProteinByDate(date);
        return new NutritionSummary(date, totalCalories, totalProtein, items.size(), items);
    }

    /**
     * Returns daily summaries for all dates up to and including today.
     * Future dates are excluded from the history view.
     *
     * @return A list of NutritionSummary objects for past and present dates only.
     */
    public List<NutritionSummary> getPastAndTodaySummaries() {
        List<NutritionSummary> summaries = new ArrayList<>();
        for (String date : getPastAndTodayDates()) {
            summaries.add(getSummaryByDate(date));
        }
        return summaries;
    }

    /**
     * Returns nutritional summaries for dates falling within a specified range.
     * Both start and end dates are inclusive in the range.
     *
     * @param fromDate The start date in "dd-MM-yyyy" format
     * @param toDate The end date in "dd-MM-yyyy" format
     * @return A list of NutritionSummary objects for dates between fromDate and toDate
     */
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

    /**
     * Returns the N days where total calories were closest to the specified goal.
     * Days are sorted by the absolute difference from the calorie goal.
     *
     * @param n The maximum number of days to return
     * @param calorieGoal The target calorie value to compare against
     * @return A list of the N days whose calorie totals are closest to the goal
     * @throws AssertionError If n is not positive
     */
    public List<NutritionSummary> getDaysClosestToGoal(int n, int calorieGoal) {
        assert n > 0 : "N should be positive";
        List<NutritionSummary> summaries = new ArrayList<>(getPastAndTodaySummaries());
        summaries.sort((a, b) -> {
            int diffA = Math.abs(a.getTotalCalories() - calorieGoal);
            int diffB = Math.abs(b.getTotalCalories() - calorieGoal);
            return diffA - diffB;
        });
        return summaries.subList(0, Math.min(n, summaries.size()));
    }

    /**
     * Returns the top N days with the highest total calorie consumption.
     * Days are sorted in descending order of calorie count.
     *
     * @param n The maximum number of days to return
     * @return A list of the N days with the highest calorie totals
     */
    public List<NutritionSummary> getTopDaysByCalories(int n) {
        List<NutritionSummary> summaries = new ArrayList<>(getPastAndTodaySummaries());
        summaries.sort((a, b) -> b.getTotalCalories() - a.getTotalCalories());
        return summaries.subList(0, Math.min(n, summaries.size()));
    }

    /**
     * Calculates the longest consecutive streak of logged days.
     * A streak counts consecutive calendar days where at least one food item was logged.
     *
     * @return The length of the longest streak of consecutive logged days
     */
    public int getLongestStreak() {
        List<String> dates = getPastAndTodayDates();
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
        return longestStreak;
    }

    /**
     * Calculates the current active streak of logged days.
     * A streak is considered active if the last logged day is either today or yesterday.
     * If the last logged day is older than yesterday, the streak is broken.
     *
     * @return The length of the current streak, or 0 if the streak is broken
     */
    public int getCurrentStreak() {
        List<String> dates = getPastAndTodayDates();
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

    /**
     * Returns a list of unique dates up to and including today, sorted chronologically.
     * Future dates are excluded to prevent them from affecting streak calculations.
     *
     * @return A sorted list of date strings in DD-MM-YYYY format, excluding future dates.
     */
    private List<String> getPastAndTodayDates() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return getUniqueDates().stream()
                .filter(d -> !java.time.LocalDate.parse(d, formatter).isAfter(today))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Checks if two dates are consecutive calendar days.
     *
     * @param date1 The first date string in "dd-MM-yyyy" format
     * @param date2 The second date string in "dd-MM-yyyy" format
     * @return true if date2 is exactly one day after date1, false otherwise
     * @throws AssertionError If either date is null
     */
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
