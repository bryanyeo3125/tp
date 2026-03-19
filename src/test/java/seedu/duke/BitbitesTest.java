package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class BitbitesTest {

    private FoodList foodList;
    private UserInterface ui;

    @BeforeEach
    void setUp() {
        foodList = new FoodList();
        ui = new UserInterface();
        foodList.addFood(new Food("Burger", 450, 30, "2026-03-16"));
        foodList.addFood(new Food("Salad", 200, 10, "2026-03-17"));
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void helpCommand_doesNotExit() {
        boolean isExit = Parser.parse("help", foodList, ui);
        assertFalse(isExit);
    }

    @Test
    void deleteCommand_validIndex_reducesSize() {
        int sizeBefore = foodList.size();
        Parser.parse("delete 1", foodList, ui);
        assertEquals(sizeBefore - 1, foodList.size());
    }

    @Test
    void deleteCommand_doesNotExit() {
        boolean isExit = Parser.parse("delete 1", foodList, ui);
        assertFalse(isExit);
    }
    
    @Test
    public void listTest() {
        assertTrue(true);
    }

    @Test
    public void sampleExit() {
        assertTrue(true);
    }
}

/*
Bryan: Added some comments in the JUnit code
 */

