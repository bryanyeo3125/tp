package seedu.bitbites;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import command.AddCommand;
import command.Command;
import command.DeleteCommand;
import command.HelpCommand;
import command.ListByDateCommand;
import model.Food;
import model.FoodList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Parser;
import ui.UserInterface;

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
    void parser_listByDate_returnsCorrectCommand() {
        Command command = Parser.parse("list d/2025-03-14");
        assertInstanceOf(ListByDateCommand.class, command);
    }

    @Test
    void parser_add_returnsCorrectCommand() {
        Command command = Parser.parse("add n/Ramen c/600 p/30.0 d/2025-03-19");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    void parser_delete_returnsCorrectCommand() {
        Command command = Parser.parse("delete 1");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    void parser_help_returnsCorrectCommand() {
        Command command = Parser.parse("help");
        assertInstanceOf(HelpCommand.class, command);
    }

    @Test
    void parser_unknownCommand_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("unknowncommand")
        );
    }

    @Test
    void deleteCommand_validIndex_reducesSize() {
        int sizeBefore = foodList.size();
        Parser.parse("delete 1").execute(foodList, ui);
        assertEquals(sizeBefore - 1, foodList.size());
    }

    @Test
    void deleteCommand_removesCorrectItem() {
        Parser.parse("delete 1").execute(foodList, ui);
        assertEquals("Salad", foodList.get(0).getName());
    }

    @Test
    void deleteCommand_indexOutOfRange_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete 99").execute(foodList, ui)
        );
    }

    @Test
    void deleteCommand_nonNumericIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete abc").execute(foodList, ui)
        );
    }

    @Test
    void deleteCommand_missingIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete").execute(foodList, ui)
        );
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

