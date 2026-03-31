package ui;

import java.util.List;
import model.Food;

/**
 * ProgressBar generates ASCII-style segmented progress bars.
 * Each segment represents one meal's proportion of the daily total.
 * Total bar length scales relative to the maximum calories across all days.
 */
public class ProgressBar {

    private static final int DEFAULT_MAX_BAR_LENGTH = 30;

    /**
     * Generates a segmented progress bar where each segment represents one meal's
     * proportion of the daily total. Total bar length scales relative to maxCalories.
     *
     * @param items         List of food items for the day.
     * @param totalCalories Total calories for the day.
     * @param maxCalories   The maximum calories across all days (for scaling).
     * @param maxBarLength  The maximum bar length (for the highest calorie day).
     * @return A segmented bar string like [====|===|=====]
     */
    public static String generateSegmented(List<Food> items, int totalCalories,
                                           int maxCalories, int maxBarLength) {
        assert items != null : "Items should not be null";
        assert totalCalories >= 0 : "Total calories should not be negative";
        assert maxCalories > 0 : "Max calories should be positive";
        assert maxBarLength > 0 : "Max bar length should be positive";

        if (items.isEmpty() || totalCalories == 0) {
            return "[]";
        }

        // Scale total bar length relative to maxCalories
        int barLength = (int) Math.round((double) totalCalories / maxCalories * maxBarLength);
        barLength = Math.max(barLength, 1);

        assert barLength > 0 : "Bar length should be positive";

        StringBuilder bar = new StringBuilder("[");
        int filledSoFar = 0;

        for (int i = 0; i < items.size(); i++) {
            Food food = items.get(i);
            int segLength;

            if (i == items.size() - 1) {
                // Last segment takes remaining space to avoid rounding gaps
                segLength = barLength - filledSoFar;
            } else {
                segLength = (int) Math.round(
                        (double) food.getCalories() / totalCalories * barLength);
            }

            segLength = Math.max(segLength, 1);
            bar.append("=".repeat(segLength));
            filledSoFar += segLength;

            if (i < items.size() - 1) {
                bar.append("|");
            }
        }

        bar.append("]");
        return bar.toString();
    }

    /**
     * Generates a segmented bar using default max bar length of 30.
     * Used for summary d/DATE where totalCalories == maxCalories (always full width).
     *
     * @param items         List of food items for the day.
     * @param totalCalories Total calories for the day.
     * @return A segmented bar string like [====|===|=====]
     */
    public static String generateSegmented(List<Food> items, int totalCalories) {
        assert items != null : "Items should not be null";
        assert totalCalories >= 0 : "Total calories should not be negative";
        if (totalCalories == 0) {
            return "[]";
        }
        return generateSegmented(items, totalCalories, totalCalories, DEFAULT_MAX_BAR_LENGTH);
    }
}
