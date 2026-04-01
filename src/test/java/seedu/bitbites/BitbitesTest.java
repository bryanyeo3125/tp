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
import command.PresetCommand;

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

    @Test
    void storage_load_ioError() {
        Storage badStorage = new Storage(tempDir.toString());

        assertThrows(BitbitesException.class, () -> {
            badStorage.load();
        });
    }

    @Test
    void storage_save_foodIoError() {
        Storage badStorage = new Storage(tempDir.toString());

        FoodList dummyList = new FoodList();
        dummyList.addFood(new Food("Apple", 95, 0.5, "2026-04-02"));

        assertThrows(BitbitesException.class, () -> {
            badStorage.save(dummyList);
        });
    }

    @Test
    void storage_load_invalidFormat() throws IOException {
        FileWriter fw = new FileWriter(tempFilePath);
        fw.write("Only Two Parts | 200\n");
        fw.write("Way | Too | Many | Parts | Here | 2026-04-01\n");
        fw.close();

        ArrayList<Food> loadedFoods = storage.load();

        assertTrue(loadedFoods.isEmpty());
    }

    @Test
    void storage_save_presetValid() throws FileNotFoundException {
        PresetList listToSave = new PresetList();
        listToSave.addPreset(new Food("Banana", 105, 1.3, "PRESET"));
        listToSave.addPreset(new Food("Eggs", 140, 12.0, "PRESET"));

        storage.save(listToSave);

        Storage verifyStorage = new Storage(tempFilePath);
        ArrayList<Food> loadedPresets = verifyStorage.load();

        assertEquals(2, loadedPresets.size());
        assertEquals("Banana", loadedPresets.get(0).getName());
        assertEquals(105, loadedPresets.get(0).getCalories());
        assertEquals("Eggs", loadedPresets.get(1).getName());
        assertEquals(12.0, loadedPresets.get(1).getProtein());
        assertEquals("PRESET", loadedPresets.get(1).getDate());
    }

    @Test
    void storage_save_presetIoError() {
        Storage badStorage = new Storage(tempDir.toString());

        PresetList dummyList = new PresetList();
        dummyList.addPreset(new Food("Apple", 95, 0.5, "PRESET"));

        assertThrows(BitbitesException.class, () -> {
            badStorage.save(dummyList);
        });
    }

    @Test
    void storage_save_createsDirectory() {
        String nestedFilePath = tempDir.resolve("missing_data_folder/test_data.txt").toString();
        Storage nestedStorage = new Storage(nestedFilePath);

        FoodList dummyList = new FoodList();
        dummyList.addFood(new Food("Apple", 95, 0.5, "2026-04-02"));

        nestedStorage.save(dummyList);

        java.io.File savedFile = new java.io.File(nestedFilePath);
        assertTrue(savedFile.getParentFile().exists(), "The parent directory should have been created.");
        assertTrue(savedFile.exists(), "The data file should have been created inside the new directory.");
    }

    @Test
    void storage_save_nullParentDir() {
        Storage noParentStorage = new Storage("just_a_filename.txt");
        FoodList dummyList = new FoodList();
        dummyList.addFood(new Food("Apple", 95, 0.5, "2026-04-02"));

        noParentStorage.save(dummyList);

        java.io.File createdFile = new java.io.File("just_a_filename.txt");
        assertTrue(createdFile.exists(), "File should be created in the root directory.");
        createdFile.delete();
    }

    // ── PresetCommand ─────────────────────────────────────────
    @Test
    void parser_preset_returnsCommand() {
        Command command = Parser.parse("preset list");
        assertInstanceOf(PresetCommand.class, command);
    }

    @Test
    void preset_missingAction_throws() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset").execute(context)
        );
    }

    @Test
    void preset_unknownAction_throws() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset jump").execute(context)
        );
    }

    // -- Add Preset Tests --
    @Test
    void preset_add_valid() {
        Parser.parse("preset add n/Oats c/150 p/5.0").execute(context);
        assertEquals(1, presetList.size());
        assertEquals("Oats", presetList.getPreset(0).getName());
        assertEquals("PRESET", presetList.getPreset(0).getDate());
    }

    @Test
    void preset_add_missingPrefix() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/150").execute(context)
        );
    }

    @Test
    void preset_add_invalidValues() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/-50 p/5.0").execute(context)
        );

        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/ c/150 p/5.0").execute(context)
        );
    }

    @Test
    void preset_add_missingN() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add c/150 p/5.0").execute(context)
        );
    }

    @Test
    void preset_add_missingC() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats p/5.0").execute(context)
        );
    }

    @Test
    void preset_add_negProtein() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/150 p/-5.0").execute(context)
        );
    }

    @Test
    void preset_add_nonNumeric() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/abc p/5.0").execute(context)
        );
    }

    // -- List Preset Tests --
    @Test
    void preset_list_empty() {
        boolean isExit = Parser.parse("preset list").execute(context);
        assertFalse(isExit);
    }

    @Test
    void preset_list_populated() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        boolean isExit = Parser.parse("preset list").execute(context);
        assertFalse(isExit);
    }

    // -- Delete Preset Tests --
    @Test
    void preset_delete_valid() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        Parser.parse("preset delete 1").execute(context);
        assertEquals(0, presetList.size());
    }

    @Test
    void preset_delete_nonNumeric() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset delete abc").execute(context)
        );
    }

    // -- Use Preset Tests --
    @Test
    void preset_use_emptyList() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use 1").execute(context)
        );
    }

    @Test
    void preset_use_validToday() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        int sizeBefore = foodList.size();

        Parser.parse("preset use 1").execute(context);

        assertEquals(sizeBefore + 1, foodList.size());
        assertEquals("Oats", foodList.get(foodList.size() - 1).getName());
    }

    @Test
    void preset_use_customDate() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        int sizeBefore = foodList.size();

        Parser.parse("preset use 1 d/15-05-2026").execute(context);

        assertEquals(sizeBefore + 1, foodList.size());
        assertEquals("15-05-2026", foodList.get(foodList.size() - 1).getDate());
    }

    @Test
    void preset_use_invalidDate() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use 1 d/2026/05/15").execute(context)
        );
    }

    @Test
    void preset_use_nonNumeric() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use abc").execute(context)
        );
    }

    @Test
    void preset_use_extraText() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        int sizeBefore = foodList.size();

        Parser.parse("preset use 1 hello").execute(context);
        assertEquals(sizeBefore + 1, foodList.size());
    }

    // -- Architecture Test --
    @Test
    void preset_oldExecute_returnsFalse() {
        PresetCommand cmd = new PresetCommand("preset list");
        boolean isExit = cmd.execute(context);
        assertFalse(isExit);
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
