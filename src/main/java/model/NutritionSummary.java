package model;

import java.util.List;
import ui.ProgressBar;
/**
 * NutritionSummary stores aggregated nutritional data for a given date or overall.
 * Provides formatted output with per-item calorie breakdowns and a segmented bar.
 */
public class NutritionSummary {
    private final String date;
    private final int totalCalories;
    private final double totalProtein;
    private final int itemCount;
    private final List<Food> items;

    /**
     * Constructor for a date-specific summary.
     *
     * @param date          The date this summary is for.
     * @param totalCalories Total calories for the date.
     * @param totalProtein  Total protein for the date.
     * @param itemCount     Number of food items for the date.
     * @param items         List of food items for the date.
     */
    public NutritionSummary(String date, int totalCalories,
                            double totalProtein, int itemCount, List<Food> items) {
        assert date != null && !date.isEmpty() : "Date should not be null or empty";
        assert totalCalories >= 0 : "Total calories should not be negative";
        assert totalProtein >= 0 : "Total protein should not be negative";
        assert itemCount >= 0 : "Item count should not be negative";
        assert items != null : "Items list should not be null";

        this.date = date;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.itemCount = itemCount;
        this.items = items;
    }

    /**
     * Constructor for an all-time summary (no specific date).
     */
    public NutritionSummary(int totalCalories, double totalProtein,
                            int itemCount, List<Food> items) {
        this("all-time", totalCalories, totalProtein, itemCount, items);
    }

    public String getDate() {
        return date;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<Food> getItems() {
        return items;
    }

    /**
     * Returns a formatted summary string with per-item breakdown and segmented bar.
     * Bar is always full width (30 chars) since maxCalories == totalCalories.
     */
    @Override
    public String toString() {
        assert items != null : "Items should not be null";

        StringBuilder sb = new StringBuilder();
        sb.append("Summary for ").append(date).append(":\n");
        sb.append(String.format("  Total: %d kcal | %.1fg protein (%d items)\n",
                totalCalories, totalProtein, itemCount));

        if (items.isEmpty()) {
            sb.append("  No food items recorded.\n");
            return sb.toString();
        }

        // Per-item breakdown
        sb.append("\n  Items:\n");
        for (int i = 0; i < items.size(); i++) {
            Food food = items.get(i);
            assert food != null : "Food item should not be null";
            double calPercent = totalCalories == 0 ? 0
                    : (double) food.getCalories() / totalCalories * 100;
            double proteinPercent = totalProtein == 0 ? 0
                    : food.getProtein() / totalProtein * 100;
            sb.append(String.format("  %d. %-20s %4d kcal (%.2f%%) | %.1fg protein (%.2f%%)\n",
                    i + 1, food.getName(),
                    food.getCalories(), calPercent,
                    food.getProtein(), proteinPercent));
        }

        // Segmented bar
        String bar = ProgressBar.generateSegmented(items, totalCalories);
        sb.append("\n  ").append(bar).append("\n");

        return sb.toString();
    }
}
