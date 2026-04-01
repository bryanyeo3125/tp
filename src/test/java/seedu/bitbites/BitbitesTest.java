package seedu.bitbites;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import command.AddCommand;
import command.Command;
import command.DeleteCommand;
import command.HelpCommand;
import command.ListByDateCommand;
import command.EditCommand;
import command.ListCommand;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import model.Food;
import model.FoodList;
import model.PresetList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import parser.Parser;
import storage.Storage;
import ui.UserInterface;

class BitbitesTest {

    @TempDir
    private Path tempDir;
    private FoodList foodList;
    private PresetList presetList;
    private UserInterface ui;
    private AppContext context;
    private String tempFilePath;
    private Storage storage;

    @BeforeEach
    void setUp() {
        foodList = new FoodList();
        presetList = new PresetList();
        ui = new UserInterface();
        context = new AppContext(foodList, presetList, ui);
        foodList.addFood(new Food("Burger", 450, 30, "2026-03-16"));
        foodList.addFood(new Food("Salad", 200, 10, "2026-03-17"));

        tempFilePath = tempDir.resolve("test_data.text").toString();
        storage = new Storage(tempFilePath);
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
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
        Parser.parse("delete 1").execute(context);
        assertEquals(sizeBefore - 1, foodList.size());
    }

    @Test
    void deleteCommand_removesCorrectItem() {
        Parser.parse("delete 1").execute(context);
        assertEquals("Salad", foodList.get(0).getName());
    }

    @Test
    void deleteCommand_indexOutOfRange_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete 99").execute(context)
        );
    }

    @Test
    void deleteCommand_nonNumericIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete abc").execute(context)
        );
    }

    @Test
    void deleteCommand_missingIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete").execute(context)
        );
    }

    // ── EditCommand ───────────────────────────────────────
    @Test
    void parser_edit_returnsCorrectCommand() {
        Command command = Parser.parse("edit 1 n/New Name");
        assertInstanceOf(EditCommand.class, command);
    }

    @Test
    void editCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("edit 1 n/New Name").execute(context);
        assertFalse(isExit);
    }

    @Test
    void editCommand_changeName_correct() {
        Parser.parse("edit 1 n/Duck Rice").execute(context);
        assertEquals("Duck Rice", foodList.get(0).getName());
    }

    @Test
    void editCommand_changeCalories_correct() {
        Parser.parse("edit 1 c/999").execute(context);
        assertEquals(999, foodList.get(0).getCalories());
    }

    @Test
    void editCommand_changeProtein_correct() {
        Parser.parse("edit 1 p/99.9").execute(context);
        assertEquals(99.9, foodList.get(0).getProtein());
    }

    @Test
    void editCommand_changeDate_correct() {
        Parser.parse("edit 1 d/2026-01-01").execute(context);
        assertEquals("2026-01-01", foodList.get(0).getDate());
    }

    @Test
    void editCommand_changeMultipleFields_correct() {
        Parser.parse("edit 1 n/Duck Rice c/999 p/50.0").execute(context);
        assertEquals("Duck Rice", foodList.get(0).getName());
        assertEquals(999, foodList.get(0).getCalories());
        assertEquals(50.0, foodList.get(0).getProtein());
    }

    @Test
    void editCommand_indexOutOfRange_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit 99 n/Duck Rice").execute(context)
        );
    }

    @Test
    void editCommand_missingIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit").execute(context)
        );
    }

    @Test
    void editCommand_noFieldsProvided_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit 1").execute(context)
        );
    }

    //@@author j-kennethh
    // ── ListCommand ───────────────────────────────────────
    @Test
    void parser_list_returnsCorrectCommand() {
        Command command = Parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    void listCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("list").execute(context);
        assertFalse(isExit);
    }

    @Test
    void listCommand_emptyList_executesWithoutError() {
        boolean isExit = Parser.parse("list").execute(context);
        assertFalse(isExit);
    }

    // ── ListByDate Command ───────────────────────────────────────
    @Test
    void parser_listByDate_returnsCorrectCommand() {
        Command command = Parser.parse("list d/2026-03-14");
        assertInstanceOf(ListByDateCommand.class, command);
    }

    @Test
    void listByDateCommand_invalidPrefix_throwsAssertError() {
        ListByDateCommand badCommand = new ListByDateCommand("show d/2026-03-16");
        assertThrows(AssertionError.class, () -> badCommand.execute(context));
    }

    @Test
    void listByDateCommand_executeWithMatchingDate_returnsFalse() {
        boolean isExit = Parser.parse("list d/2026-03-16").execute(context);
        assertFalse(isExit);
    }

    @Test
    void listByDateCommand_noMatchingDate_executesWithoutError() {
        boolean isExit = Parser.parse("list d/2099-01-01").execute(context);
        assertFalse(isExit);
    }

    @Test
    void listByDateCommand_missingDate_throwsException() {
        assertThrows(BitbitesException.class, () -> Parser.parse("list d/").execute(context));
    }

    // ── Storage ───────────────────────────────────────────
    @Test
    void storage_load_missingFile() throws FileNotFoundException {
        ArrayList<Food> loadedFoods = storage.load();
        assertTrue(loadedFoods.isEmpty());
    }

    @Test
    void storage_load_validFile() throws IOException {
        FileWriter fw = new FileWriter(tempFilePath);
        fw.write("Burger | 443 | 27.2 | 31-03-2026\n");
        fw.write("Chicken Rice | 620 | 25.9 | 31-03-2026\n");
        fw.close();

        ArrayList<Food> loadedFoods = storage.load();
        assertEquals(2, loadedFoods.size());
        assertEquals("Burger", loadedFoods.get(0).getName());
        assertEquals(443, loadedFoods.get(0).getCalories());
        assertEquals(27.2, loadedFoods.get(0).getProtein());
        assertEquals("31-03-2026", loadedFoods.get(0).getDate());
    }

    @Test
    void storage_load_corruptedFile() throws IOException {
        FileWriter fw = new FileWriter(tempFilePath);
        fw.write("Pizza | abc | 15.0 | 01-04-2026\n");
        fw.close();

        assertThrows(BitbitesException.class, () -> {
            storage.load();
        });
    }

    @Test
    void storage_save_validList() throws FileNotFoundException {
        FoodList listToSave = new FoodList();
        listToSave.addFood(new Food("Salad", 30, 7.1, "01-04-2026"));

        storage.save(listToSave);

        Storage verifyStorage = new Storage(tempFilePath);
        ArrayList<Food> loadedFoods = verifyStorage.load();

        assertEquals(1, loadedFoods.size());
        assertEquals("Salad", loadedFoods.get(0).getName());
        assertEquals(30, loadedFoods.get(0).getCalories());
        assertEquals(7.1, loadedFoods.get(0).getProtein());
        assertEquals("01-04-2026", loadedFoods.get(0).getDate());
    }
    //@@author

    @Test
    public void sampleExit() {
        assertTrue(true);
    }
}

/*
Bryan: Added some comments in the JUnit code
 */
