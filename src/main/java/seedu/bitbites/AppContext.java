package seedu.bitbites;

import model.FoodList;
import model.PresetList;
import ui.UserInterface;

/**
 * AppContext.java
 *
 * Acts as a shared context object passed to all Command objects during execution.
 * Encapsulates the core application state — FoodList, PresetList, and UserInterface —
 * into a single object, avoiding the need to update every command's method signature
 * when new components are added to the application.
 *
 * This follows the Context Object design pattern, which improves extensibility
 * and keeps command interfaces clean and consistent.
 */
// @@author bryanyeo3125
public class AppContext {
    private FoodList foodList;
    private PresetList presetList;
    private final UserInterface ui;

    /**
     * Constructs an AppContext with the core application components.
     *
     * @param foodList   The list of food items being tracked.
     * @param presetList The list of saved food presets.
     * @param ui         The user interface for input and output.
     */
    public AppContext(FoodList foodList, PresetList presetList, UserInterface ui) {
        this.foodList = foodList;
        this.presetList = presetList;
        this.ui = ui;
    }

    /**
     * Returns the FoodList from the application context.
     *
     * @return The current FoodList.
     */
    public FoodList getFoodList() {
        return foodList;
    }

    /**
     * Replaces the current FoodList in the application context.
     *
     * @param foodList The new FoodList to set.
     */
    public void setFoodList(FoodList foodList) {
        this.foodList = foodList;
    }

    /**
     * Returns the PresetList from the application context.
     *
     * @return The current PresetList.
     */
    public PresetList getPresetList() {
        return presetList;
    }

    /**
     * Replaces the current PresetList in the application context.
     *
     * @param presetList The new PresetList to set.
     */
    public void setPresetList(PresetList presetList) {
        this.presetList = presetList;
    }

    /**
     * Returns the UserInterface from the application context.
     *
     * @return The current UserInterface.
     */
    public UserInterface getUi() {
        return ui;
    }
}
// @@author
