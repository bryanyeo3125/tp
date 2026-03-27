package seedu.duke;

public abstract class Command {
    public abstract boolean execute(FoodList foodList, UserInterface ui);
}