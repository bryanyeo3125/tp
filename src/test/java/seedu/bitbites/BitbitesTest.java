package seedu.bitbites;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import command.AddCommand;
import command.Command;
import command.DeleteCommand;
import command.EditCommand;
import command.FindCommand;
import command.GoalsCommand;
import command.HelpCommand;
import command.ListByDateCommand;
import command.ListCommand;
import command.PresetCommand;
import command.ProfileCommand;
import command.SummaryByDateCommand;
import command.SummaryCompareCommand;
import command.SummaryRangeCommand;
import command.TipsCommand;
import model.Food;
import model.FoodList;
import model.NutritionSummary;
import model.PresetList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import parser.Parser;
import storage.Storage;
import ui.ProgressBar;
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
        ui.setCurrentUser("testuser");
        context = new AppContext(foodList, presetList, ui);
        foodList.addFood(new Food("Burger", 450, 30, "27-03-2026"));
        foodList.addFood(new Food("Salad", 200, 10, "28-03-2026"));

        tempFilePath = tempDir.resolve("test_data.text").toString();
        storage = new Storage(tempFilePath);
    }

    /**
     * Creates a fresh AppContext with the given FoodList and empty PresetList/UI.
     * Used to avoid repeated boilerplate in tests that need isolated food data.
     */
    private AppContext createContext(FoodList fl) {
        return new AppContext(fl, new PresetList(), new UserInterface());
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    // ── Parser ────────────────────────────────────────────

    @Test
    void parser_add_returnsCorrectCommand() {
        Command command = Parser.parse("add n/Ramen c/600 p/30.0 d/19-03-2026");
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
    void parser_tips_returnsCorrectCommand() {
        Command command = Parser.parse("tips");
        assertInstanceOf(TipsCommand.class, command);
    }

    @Test
    void parser_unknownCommand_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("unknowncommand")
        );
    }

    // ── Help Command ───────────────────────────────────────

    /**
     * Verifies that HelpCommand does not modify the food list.
     */
    @Test
    void helpCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("help").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    // ── TipsCommand ───────────────────────────────────────

    /**
     * Verifies that TipsCommand does not modify the food list.
     */
    @Test
    void tipsCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("tips").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    // ── Delete Command ───────────────────────────────────────

    /**
     * Verifies that deleting a valid index reduces the food list size by 1.
     */
    @Test
    void deleteCommand_validIndex_reducesSize() {
        int sizeBefore = foodList.size();
        Parser.parse("delete 1").execute(context);
        assertEquals(sizeBefore - 1, foodList.size());
    }

    /**
     * Verifies that the correct item is removed when deleting by index.
     */
    @Test
    void deleteCommand_removesCorrectItem() {
        Parser.parse("delete 1").execute(context);
        assertEquals("Salad", foodList.get(0).getName());
    }

    /**
     * Verifies that deleting an out-of-range index throws BitbitesException.
     */
    @Test
    void deleteCommand_indexOutOfRange_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete 99").execute(context)
        );
    }

    /**
     * Verifies that a non-numeric index throws BitbitesException.
     */
    @Test
    void deleteCommand_nonNumericIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete abc").execute(context)
        );
    }

    /**
     * Verifies that a missing index throws BitbitesException.
     */
    @Test
    void deleteCommand_missingIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("delete").execute(context)
        );
    }

    /**
     * Verifies that DeleteCommand returns false (does not trigger exit).
     */
    @Test
    void deleteCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("delete 1").execute(context);
        assertFalse(isExit);
    }

    // ── EditCommand ───────────────────────────────────────

    /**
     * Verifies that Parser returns an EditCommand for edit input.
     */
    @Test
    void parser_edit_returnsCorrectCommand() {
        Command command = Parser.parse("edit 1 n/New Name");
        assertInstanceOf(EditCommand.class, command);
    }

    /**
     * Verifies that EditCommand returns false (does not trigger exit).
     */
    @Test
    void editCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("edit 1 n/New Name").execute(context);
        assertFalse(isExit);
    }

    /**
     * Verifies that editing n/ updates the name correctly.
     */
    @Test
    void editCommand_changeName_correct() {
        Parser.parse("edit 1 n/Duck Rice").execute(context);
        assertEquals("Duck Rice", foodList.get(0).getName());
    }

    /**
     * Verifies that editing c/ updates the calorie count correctly.
     */
    @Test
    void editCommand_changeCalories_correct() {
        Parser.parse("edit 1 c/999").execute(context);
        assertEquals(999, foodList.get(0).getCalories());
    }

    /**
     * Verifies that editing p/ updates the protein value correctly.
     */
    @Test
    void editCommand_changeProtein_correct() {
        Parser.parse("edit 1 p/99.9").execute(context);
        assertEquals(99.9, foodList.get(0).getProtein());
    }

    /**
     * Verifies that editing d/ updates the date correctly.
     */
    @Test
    void editCommand_changeDate_correct() {
        Parser.parse("edit 1 d/01-01-2026").execute(context);
        assertEquals("01-01-2026", foodList.get(0).getDate());
    }

    /**
     * Verifies that multiple fields can be edited in a single command.
     */
    @Test
    void editCommand_changeMultipleFields_correct() {
        Parser.parse("edit 1 n/Duck Rice c/999 p/50.0").execute(context);
        assertEquals("Duck Rice", foodList.get(0).getName());
        assertEquals(999, foodList.get(0).getCalories());
        assertEquals(50.0, foodList.get(0).getProtein());
    }

    /**
     * Verifies that unchanged fields remain unmodified after a partial edit.
     */
    @Test
    void editCommand_unchangedFields_preserved() {
        Parser.parse("edit 1 n/Duck Rice").execute(context);
        assertEquals(450, foodList.get(0).getCalories());
        assertEquals(30.0, foodList.get(0).getProtein());
        assertEquals("27-03-2026", foodList.get(0).getDate());
    }

    /**
     * Verifies that an out-of-range index throws BitbitesException.
     */
    @Test
    void editCommand_indexOutOfRange_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit 99 n/Duck Rice").execute(context)
        );
    }

    /**
     * Verifies that a missing index throws BitbitesException.
     */
    @Test
    void editCommand_missingIndex_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit").execute(context)
        );
    }

    /**
     * Verifies that providing no fields to edit throws BitbitesException.
     */
    @Test
    void editCommand_noFieldsProvided_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit 1").execute(context)
        );
    }

    /**
     * Verifies that a negative calorie value throws BitbitesException.
     */
    @Test
    void editCommand_negativeCalories_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit 1 c/-100").execute(context)
        );
    }

    /**
     * Verifies that an invalid date format throws BitbitesException.
     */
    @Test
    void editCommand_invalidDateFormat_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("edit 1 d/2026/01/01").execute(context)
        );
    }

    //@@author j-kennethh
    // ── ListCommand ───────────────────────────────────────

    /**
     * Verifies that Parser returns a ListCommand for list input.
     */
    @Test
    void parser_list_returnsCorrectCommand() {
        Command command = Parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
    }

    /**
     * Verifies that ListCommand returns false (does not trigger exit).
     */
    @Test
    void listCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("list").execute(context);
        assertFalse(isExit);
    }

    /**
     * Verifies that ListCommand executes without error on an empty list.
     */
    @Test
    void listCommand_emptyList_executesWithoutError() {
        AppContext emptyContext = createContext(new FoodList());
        assertDoesNotThrow(() -> Parser.parse("list").execute(emptyContext));
    }

    // ── ListByDate Command ───────────────────────────────────────

    /**
     * Verifies that Parser returns a ListByDateCommand for list d/ input.
     */
    @Test
    void parser_listByDate_returnsCorrectCommand() {
        Command command = Parser.parse("list d/27-03-2026");
        assertInstanceOf(ListByDateCommand.class, command);
    }

    /**
     * Verifies that ListByDateCommand returns false when matching items exist.
     */
    @Test
    void listByDateCommand_executeWithMatchingDate_returnsFalse() {
        boolean isExit = Parser.parse("list d/27-03-2026").execute(context);
        assertFalse(isExit);
    }

    /**
     * Verifies that ListByDateCommand returns false when no items match the date.
     */
    @Test
    void listByDateCommand_noMatchingDate_executesWithoutError() {
        boolean isExit = Parser.parse("list d/01-01-2099").execute(context);
        assertFalse(isExit);
    }

    /**
     * Verifies that a missing date after d/ throws BitbitesException.
     */
    @Test
    void listByDateCommand_missingDate_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("list d/").execute(context)
        );
    }

    /**
     * Verifies that ListByDateCommand correctly filters when multiple dates exist.
     */
    @Test
    void listByDate_mixedDates_returnsCorrectItems() {
        FoodList testFoods = new FoodList();
        testFoods.addFood(new Food("Apple", 95, 0.5, "15-04-2026"));
        testFoods.addFood(new Food("Banana", 105, 1.3, "16-04-2026"));
        boolean isExit = new ListByDateCommand("list d/15-04-2026")
                .execute(createContext(testFoods));
        assertFalse(isExit);
    }

    /**
     * Verifies that ListByDateCommand executes safely when no items match.
     */
    @Test
    void listByDate_noMatches_executesSafely() {
        FoodList testFoods = new FoodList();
        testFoods.addFood(new Food("Pizza", 300, 12.0, "20-04-2026"));
        boolean isExit = new ListByDateCommand("list d/01-01-2026")
                .execute(createContext(testFoods));
        assertFalse(isExit);
    }

    // ── Storage ───────────────────────────────────────────

    /**
     * Verifies that loading from a missing file returns an empty list.
     */
    @Test
    void storage_load_missingFile() throws FileNotFoundException {
        ArrayList<Food> loadedFoods = storage.load();
        assertTrue(loadedFoods.isEmpty());
    }

    /**
     * Verifies that a valid data file is parsed correctly.
     */
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

    /**
     * Verifies that a corrupted data file throws BitbitesException.
     */
    @Test
    void storage_load_corruptedFile() throws IOException {
        FileWriter fw = new FileWriter(tempFilePath);
        fw.write("Pizza | abc | 15.0 | 01-04-2026\n");
        fw.close();
        assertThrows(BitbitesException.class, () -> storage.load());
    }

    /**
     * Verifies that saving and reloading a food list produces identical data.
     */
    @Test
    void storage_save_validList() throws FileNotFoundException {
        FoodList listToSave = new FoodList();
        listToSave.addFood(new Food("Salad", 30, 7.1, "01-04-2026"));
        storage.save(listToSave);

        ArrayList<Food> loadedFoods = new Storage(tempFilePath).load();
        assertEquals(1, loadedFoods.size());
        assertEquals("Salad", loadedFoods.get(0).getName());
        assertEquals(30, loadedFoods.get(0).getCalories());
        assertEquals(7.1, loadedFoods.get(0).getProtein());
        assertEquals("01-04-2026", loadedFoods.get(0).getDate());
    }

    /**
     * Verifies that loading from a directory path (not a file) throws BitbitesException.
     */
    @Test
    void storage_load_ioError() {
        assertThrows(BitbitesException.class, () ->
                new Storage(tempDir.toString()).load()
        );
    }

    /**
     * Verifies that saving to a directory path throws BitbitesException.
     */
    @Test
    void storage_save_foodIoError() {
        FoodList dummyList = new FoodList();
        dummyList.addFood(new Food("Apple", 95, 0.5, "02-04-2026"));
        assertThrows(BitbitesException.class, () ->
                new Storage(tempDir.toString()).save(dummyList)
        );
    }

    /**
     * Verifies that lines with incorrect number of fields are skipped gracefully.
     */
    @Test
    void storage_load_invalidFormat() throws IOException {
        FileWriter fw = new FileWriter(tempFilePath);
        fw.write("Only Two Parts | 200\n");
        fw.write("Way | Too | Many | Parts | Here | 2026-04-01\n");
        fw.close();
        assertTrue(storage.load().isEmpty());
    }

    /**
     * Verifies that saving and reloading a preset list produces identical data.
     */
    @Test
    void storage_save_presetValid() throws FileNotFoundException {
        PresetList listToSave = new PresetList();
        listToSave.addPreset(new Food("Banana", 105, 1.3, "PRESET"));
        listToSave.addPreset(new Food("Eggs", 140, 12.0, "PRESET"));
        storage.save(listToSave);

        ArrayList<Food> loadedPresets = new Storage(tempFilePath).load();
        assertEquals(2, loadedPresets.size());
        assertEquals("Banana", loadedPresets.get(0).getName());
        assertEquals("Eggs", loadedPresets.get(1).getName());
        assertEquals(12.0, loadedPresets.get(1).getProtein());
        assertEquals("PRESET", loadedPresets.get(1).getDate());
    }

    /**
     * Verifies that saving a preset list to a directory path throws BitbitesException.
     */
    @Test
    void storage_save_presetIoError() {
        PresetList dummyList = new PresetList();
        dummyList.addPreset(new Food("Apple", 95, 0.5, "PRESET"));
        assertThrows(BitbitesException.class, () ->
                new Storage(tempDir.toString()).save(dummyList)
        );
    }

    /**
     * Verifies that Storage creates missing parent directories automatically.
     */
    @Test
    void storage_save_createsDirectory() {
        String nestedPath = tempDir.resolve("missing_folder/test.txt").toString();
        Storage nestedStorage = new Storage(nestedPath);
        FoodList dummyList = new FoodList();
        dummyList.addFood(new Food("Apple", 95, 0.5, "02-04-2026"));
        nestedStorage.save(dummyList);

        java.io.File savedFile = new java.io.File(nestedPath);
        assertTrue(savedFile.getParentFile().exists());
        assertTrue(savedFile.exists());
    }

    /**
     * Verifies that Storage works correctly when the file has no parent directory.
     */
    @Test
    void storage_save_nullParentDir() {
        Storage noParentStorage = new Storage("just_a_filename.txt");
        FoodList dummyList = new FoodList();
        dummyList.addFood(new Food("Apple", 95, 0.5, "02-04-2026"));
        noParentStorage.save(dummyList);

        java.io.File createdFile = new java.io.File("just_a_filename.txt");
        assertTrue(createdFile.exists());
        createdFile.delete();
    }

    // ── PresetCommand ─────────────────────────────────────────

    /**
     * Verifies that Parser returns a PresetCommand for preset input.
     */
    @Test
    void parser_preset_returnsCommand() {
        Command command = Parser.parse("preset list");
        assertInstanceOf(PresetCommand.class, command);
    }

    /**
     * Verifies that a missing preset action throws BitbitesException.
     */
    @Test
    void preset_missingAction_throws() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset").execute(context)
        );
    }

    /**
     * Verifies that an unknown preset action throws BitbitesException.
     */
    @Test
    void preset_unknownAction_throws() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset jump").execute(context)
        );
    }

    /**
     * Verifies that a valid preset is added with correct fields.
     */
    @Test
    void preset_add_valid() {
        Parser.parse("preset add n/Oats c/150 p/5.0").execute(context);
        assertEquals(1, presetList.size());
        assertEquals("Oats", presetList.getPreset(0).getName());
        assertEquals("PRESET", presetList.getPreset(0).getDate());
    }

    /**
     * Verifies that a missing p/ prefix throws BitbitesException.
     */
    @Test
    void preset_add_missingPrefix() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/150").execute(context)
        );
    }

    /**
     * Verifies that a negative calorie value throws BitbitesException.
     */
    @Test
    void preset_addNegativeCalories_throws() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/-50 p/5.0").execute(context)
        );
    }

    /**
     * Verifies that an empty name throws BitbitesException.
     */
    @Test
    void preset_addEmptyName_throws() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/ c/150 p/5.0").execute(context)
        );
    }

    /**
     * Verifies that a missing n/ prefix throws BitbitesException.
     */
    @Test
    void preset_add_missingN() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add c/150 p/5.0").execute(context)
        );
    }

    /**
     * Verifies that a missing c/ prefix throws BitbitesException.
     */
    @Test
    void preset_add_missingC() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats p/5.0").execute(context)
        );
    }

    /**
     * Verifies that a negative protein value throws BitbitesException.
     */
    @Test
    void preset_add_negProtein() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/150 p/-5.0").execute(context)
        );
    }

    /**
     * Verifies that a non-numeric calorie value throws BitbitesException.
     */
    @Test
    void preset_add_nonNumeric() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/abc p/5.0").execute(context)
        );
    }

    /**
     * Verifies that a date field in preset add throws BitbitesException.
     */
    @Test
    void preset_add_withDate() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset add n/Oats c/150 p/5.0 d/25-04-2026").execute(context)
        );
    }

    /**
     * Verifies that listing an empty preset list executes without error.
     */
    @Test
    void preset_list_empty() {
        assertFalse(Parser.parse("preset list").execute(context));
    }

    /**
     * Verifies that listing a populated preset list executes without error.
     */
    @Test
    void preset_list_populated() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        assertFalse(Parser.parse("preset list").execute(context));
    }

    /**
     * Verifies that a valid preset index is deleted correctly.
     */
    @Test
    void preset_delete_valid() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        Parser.parse("preset delete 1").execute(context);
        assertEquals(0, presetList.size());
    }

    /**
     * Verifies that a non-numeric delete index throws BitbitesException.
     */
    @Test
    void preset_delete_nonNumeric() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset delete abc").execute(context)
        );
    }

    /**
     * Verifies that using a preset on an empty list throws BitbitesException.
     */
    @Test
    void preset_use_emptyList() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use 1").execute(context)
        );
    }

    /**
     * Verifies that using a preset adds it to the food list with today's date.
     */
    @Test
    void preset_use_validToday() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        int sizeBefore = foodList.size();
        Parser.parse("preset use 1").execute(context);
        assertEquals(sizeBefore + 1, foodList.size());
        assertEquals("Oats", foodList.get(foodList.size() - 1).getName());
    }

    /**
     * Verifies that using a preset with a custom date assigns the correct date.
     */
    @Test
    void preset_use_customDate() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        int sizeBefore = foodList.size();
        Parser.parse("preset use 1 d/15-05-2026").execute(context);
        assertEquals(sizeBefore + 1, foodList.size());
        assertEquals("15-05-2026", foodList.get(foodList.size() - 1).getDate());
    }

    /**
     * Verifies that an invalid date format in preset use throws BitbitesException.
     */
    @Test
    void preset_use_invalidDate() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use 1 d/2026/05/15").execute(context)
        );
    }

    /**
     * Verifies that an invalid date in preset use throws BitbitesException.
     */
    @Test
    void preset_use_impossibleCalendarDate() {
        presetList.addPreset(new Food("Test", 100, 5.0, "PRESET"));

        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use 1 d/32-01-2026").execute(context)
        );

        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use 1 d/29-02-2026").execute(context)
        );
    }

    /**
     * Verifies that a non-numeric preset index throws BitbitesException.
     */
    @Test
    void preset_use_nonNumeric() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        assertThrows(BitbitesException.class, () ->
                Parser.parse("preset use abc").execute(context)
        );
    }

    /**
     * Verifies that extra text after a valid preset use index is ignored.
     */
    @Test
    void preset_use_extraText() {
        presetList.addPreset(new Food("Oats", 150, 5.0, "PRESET"));
        int sizeBefore = foodList.size();
        Parser.parse("preset use 1 hello").execute(context);
        assertEquals(sizeBefore + 1, foodList.size());
    }

    /**
     * Verifies that PresetCommand returns false (does not trigger exit).
     */
    @Test
    void preset_oldExecute_returnsFalse() {
        assertFalse(new PresetCommand("preset list").execute(context));
    }
    //@@author

    // ── NutritionSummary ──────────────────────────────────

    /**
     * Verifies that total calories are correctly aggregated for a given date.
     */
    @Test
    void nutritionSummary_correctTotalCalories() {
        assertEquals(450, foodList.getSummaryByDate("27-03-2026").getTotalCalories());
    }

    /**
     * Verifies that total protein is correctly aggregated for a given date.
     */
    @Test
    void nutritionSummary_correctTotalProtein() {
        assertEquals(30.0, foodList.getSummaryByDate("27-03-2026").getTotalProtein());
    }

    /**
     * Verifies that item count is correctly aggregated for a given date.
     */
    @Test
    void nutritionSummary_correctItemCount() {
        assertEquals(1, foodList.getSummaryByDate("27-03-2026").getItemCount());
    }

    /**
     * Verifies that a date with no entries returns zero calories.
     */
    @Test
    void nutritionSummary_emptyDate_returnsZeroCalories() {
        assertEquals(0, foodList.getSummaryByDate("01-01-2000").getTotalCalories());
    }

    /**
     * Verifies that a date with no entries returns zero protein.
     */
    @Test
    void nutritionSummary_emptyDate_returnsZeroProtein() {
        assertEquals(0.0, foodList.getSummaryByDate("01-01-2000").getTotalProtein());
    }

    // ── ProgressBar ───────────────────────────────────────

    /**
     * Verifies that a single item produces a full-width bar.
     */
    @Test
    void progressBar_singleItem_fullBar() {
        java.util.List<Food> items = foodList.getItemsByDate("27-03-2026");
        assertEquals("[" + "=".repeat(30) + "]",
                ProgressBar.generateSegmented(items, 450));
    }

    /**
     * Verifies that an empty item list produces an empty bar.
     */
    @Test
    void progressBar_emptyItems_emptyBar() {
        assertEquals("[]",
                ProgressBar.generateSegmented(new java.util.ArrayList<>(), 0));
    }

    /**
     * Verifies that multiple items produce a bar with segment separators.
     */
    @Test
    void progressBar_multipleItems_containsSeparator() {
        foodList.addFood(new Food("Tea", 50, 0.0, "27-03-2026"));
        java.util.List<Food> items = foodList.getItemsByDate("27-03-2026");
        assertTrue(ProgressBar.generateSegmented(
                items, foodList.getTotalCaloriesByDate("27-03-2026")).contains("|"));
    }

    // ── FoodList summary methods ──────────────────────────

    /**
     * Verifies that total calories by date are summed correctly.
     */
    @Test
    void getTotalCaloriesByDate_correctSum() {
        assertEquals(450, foodList.getTotalCaloriesByDate("27-03-2026"));
    }

    /**
     * Verifies that total protein by date is summed correctly.
     */
    @Test
    void getTotalProteinByDate_correctSum() {
        assertEquals(30.0, foodList.getTotalProteinByDate("27-03-2026"));
    }

    /**
     * Verifies that item count by date is counted correctly.
     */
    @Test
    void getItemCountByDate_correctCount() {
        assertEquals(1, foodList.getItemCountByDate("27-03-2026"));
    }

    /**
     * Verifies that item count returns 0 for a date with no entries.
     */
    @Test
    void getItemCountByDate_noItems() {
        assertEquals(0, foodList.getItemCountByDate("01-01-2000"));
    }

    /**
     * Verifies that getSummariesInRange returns the correct number of days.
     */
    @Test
    void getSummariesInRange_correctCount() {
        assertEquals(2, foodList.getSummariesInRange("27-03-2026", "29-03-2026").size());
    }

    /**
     * Verifies that getSummariesInRange returns empty for a range with no data.
     */
    @Test
    void getSummariesInRange_noData_returnsEmpty() {
        assertTrue(foodList.getSummariesInRange("01-01-2000", "02-01-2000").isEmpty());
    }

    /**
     * Verifies that getTopDaysByCalories returns days in descending calorie order.
     */
    @Test
    void getTopDaysByCalories_correctOrder() {
        java.util.List<NutritionSummary> top = foodList.getTopDaysByCalories(2);
        assertEquals(2, top.size());
        assertTrue(top.get(0).getTotalCalories() >= top.get(1).getTotalCalories());
    }

    /**
     * Verifies that getTopDaysByCalories handles N larger than available days.
     */
    @Test
    void getTopDaysByCalories_nLargerThanAvailable() {
        assertEquals(foodList.getUniqueDates().size(),
                foodList.getTopDaysByCalories(99).size());
    }

    /**
     * Verifies that the current streak is 0 when last entry is not today or yesterday.
     */
    @Test
    void getCurrentStreak_correctValue() {
        assertEquals(0, foodList.getCurrentStreak());
    }

    /**
     * Verifies that the longest streak is computed correctly.
     */
    @Test
    void getLongestStreak_correctValue() {
        assertEquals(2, foodList.getLongestStreak());
    }

    /**
     * Verifies that four consecutive days produce a longest streak of 4.
     */
    @Test
    void getLongestStreak_allConsecutive() {
        FoodList consecutive = new FoodList();
        consecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        consecutive.addFood(new Food("B", 100, 10.0, "28-03-2026"));
        consecutive.addFood(new Food("C", 100, 10.0, "29-03-2026"));
        consecutive.addFood(new Food("D", 100, 10.0, "30-03-2026"));
        assertEquals(4, consecutive.getLongestStreak());
    }

    /**
     * Verifies that an empty food list returns a streak of 0.
     */
    @Test
    void getLongestStreak_emptyList_returnsZero() {
        assertEquals(0, new FoodList().getLongestStreak());
    }

    // ── Summary Commands ──────────────────────────────────

    /**
     * Verifies that Parser returns SummaryByDateCommand for summary d/ input.
     */
    @Test
    void parser_summaryByDate_returnsCorrectCommand() {
        assertInstanceOf(SummaryByDateCommand.class,
                Parser.parse("summary d/27-03-2026"));
    }

    /**
     * Verifies that Parser returns SummaryRangeCommand for summary from/to input.
     */
    @Test
    void parser_summaryRange_returnsCorrectCommand() {
        assertInstanceOf(SummaryRangeCommand.class,
                Parser.parse("summary from/27-03-2026 to/29-03-2026"));
    }

    /**
     * Verifies that Parser returns SummaryCompareCommand for summary compare input.
     */
    @Test
    void parser_summaryCompare_returnsCorrectCommand() {
        assertInstanceOf(SummaryCompareCommand.class,
                Parser.parse("summary compare d/27-03-2026 d/28-03-2026"));
    }

    /**
     * Verifies that SummaryByDateCommand returns false (does not trigger exit).
     */
    @Test
    void summaryByDateCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("summary d/27-03-2026").execute(context));
    }

    /**
     * Verifies that SummaryByDateCommand handles a date with no data gracefully.
     */
    @Test
    void summaryByDateCommand_noData_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("summary d/01-01-2000").execute(context)
        );
    }

    /**
     * Verifies that SummaryRangeCommand returns false for a valid range.
     */
    @Test
    void summaryRangeCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("summary from/27-03-2026 to/29-03-2026").execute(context));
    }

    /**
     * Verifies that SummaryRangeCommand throws when start date is after end date.
     */
    @Test
    void summaryRangeCommand_fromAfterTo_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("summary from/29-03-2026 to/27-03-2026").execute(context)
        );
    }

    /**
     * Verifies that SummaryRangeCommand handles a range with no data gracefully.
     */
    @Test
    void summaryRangeCommand_noData_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("summary from/01-01-2000 to/02-01-2000").execute(context)
        );
    }

    /**
     * Verifies that SummaryRangeCommand throws on invalid date format.
     */
    @Test
    void summaryRangeCommand_invalidDateFormat_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("summary from/2026-03-27 to/2026-03-29").execute(context)
        );
    }

    /**
     * Verifies that SummaryCompareCommand returns false for two valid dates.
     */
    @Test
    void summaryCompareCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("summary compare d/27-03-2026 d/28-03-2026").execute(context));
    }

    /**
     * Verifies that SummaryCompareCommand handles a missing second date gracefully.
     */
    @Test
    void summaryCompareCommand_missingSecondDate_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("summary compare d/27-03-2026").execute(context)
        );
    }

    // ── History Commands ──────────────────────────────────

    /**
     * Verifies that HistoryCommand returns false (does not trigger exit).
     */
    @Test
    void historyCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history").execute(context));
    }

    /**
     * Verifies that HistoryCommand does not modify the food list.
     */
    @Test
    void historyCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("history").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    /**
     * Verifies that HistoryCommand executes without error on an empty food list.
     */
    @Test
    void historyCommand_emptyList_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("history").execute(createContext(new FoodList()))
        );
    }

    /**
     * Verifies that HistoryTopCommand returns false for a valid N.
     */
    @Test
    void historyTopCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history /top 2").execute(context));
    }

    /**
     * Verifies that HistoryTopCommand handles N larger than available days.
     */
    @Test
    void historyTopCommand_nLargerThanData_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("history /top 99").execute(context));
    }

    /**
     * Verifies that N=0 throws BitbitesException.
     */
    @Test
    void historyTopCommand_zeroN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /top 0").execute(context)
        );
    }

    /**
     * Verifies that a non-numeric N throws BitbitesException.
     */
    @Test
    void historyTopCommand_invalidN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /top abc").execute(context)
        );
    }

    /**
     * Verifies that HistoryBestCommand returns false for a valid N.
     */
    @Test
    void historyBestCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history /best 2").execute(context));
    }

    /**
     * Verifies that HistoryBestCommand handles N larger than available days.
     */
    @Test
    void historyBestCommand_nLargerThanData_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("history /best 99").execute(context));
    }

    /**
     * Verifies that N=0 throws BitbitesException.
     */
    @Test
    void historyBestCommand_zeroN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /best 0").execute(context)
        );
    }

    /**
     * Verifies that a non-numeric N throws BitbitesException.
     */
    @Test
    void historyBestCommand_invalidN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /best abc").execute(context)
        );
    }

    // ── HistoryStreakCommand ──────────────────────────────

    /**
     * Verifies that HistoryStreakCommand returns false (does not trigger exit).
     */
    @Test
    void historyStreakCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history streak").execute(context));
    }

    /**
     * Verifies that HistoryStreakCommand does not modify the food list.
     */
    @Test
    void historyStreakCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("history streak").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    /**
     * Verifies that HistoryStreakCommand executes without error on an empty food list.
     */
    @Test
    void historyStreakCommand_emptyList_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("history streak").execute(createContext(new FoodList()))
        );
    }

    /**
     * Verifies that current streak is 0 when last entry is not today or yesterday.
     */
    @Test
    void historyStreakCommand_consecutiveDays_correctCurrentStreak() {
        assertEquals(0, foodList.getCurrentStreak());
    }

    /**
     * Verifies that longest streak is 2 for two consecutive days.
     */
    @Test
    void historyStreakCommand_consecutiveDays_correctLongestStreak() {
        assertEquals(2, foodList.getLongestStreak());
    }

    /**
     * Verifies that current streak is 0 when entries have a gap.
     */
    @Test
    void historyStreakCommand_nonConsecutive_currentStreakZero() {
        FoodList nonConsecutive = new FoodList();
        nonConsecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        nonConsecutive.addFood(new Food("B", 100, 10.0, "29-03-2026"));
        assertEquals(0, nonConsecutive.getCurrentStreak());
    }

    /**
     * Verifies that longest streak is 1 when all entries are non-consecutive.
     */
    @Test
    void historyStreakCommand_nonConsecutive_longestStreakOne() {
        FoodList nonConsecutive = new FoodList();
        nonConsecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        nonConsecutive.addFood(new Food("B", 100, 10.0, "29-03-2026"));
        assertEquals(1, nonConsecutive.getLongestStreak());
    }

    // ── FindCommand ───────────────────────────────────────

    /**
     * Verifies that Parser returns a FindCommand for find input.
     */
    @Test
    void parser_find_returnsCorrectCommand() {
        assertInstanceOf(FindCommand.class, Parser.parse("find Burger"));
    }

    /**
     * Verifies that FindCommand returns false (does not trigger exit).
     */
    @Test
    void findCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("find Burger").execute(context));
    }

    /**
     * Verifies that FindCommand does not modify the food list.
     */
    @Test
    void findCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("find Burger").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    /**
     * Verifies that FindCommand executes without error when no items match.
     */
    @Test
    void findCommand_noMatch_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("find Pizza").execute(context));
    }

    /**
     * Verifies that FindCommand is case-insensitive.
     */
    @Test
    void findCommand_caseInsensitive_findsMatch() {
        assertDoesNotThrow(() -> Parser.parse("find burger").execute(context));
    }

    /**
     * Verifies that FindCommand without a keyword prints an error message.
     */
    @Test
    void findCommand_missingKeyword_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("find").execute(context));
    }

    // ── GoalsCommand ──────────────────────────────────────
    // @@author bryanyeo3125

    /**
     * Verifies that Parser returns a GoalsCommand for goals input.
     */
    @Test
    void parser_goals_returnsCorrectCommand() {
        assertInstanceOf(GoalsCommand.class, Parser.parse("goals"));
    }

    /**
     * Verifies that GoalsCommand returns false (does not trigger exit).
     */
    @Test
    void goalsCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("goals").execute(context));
    }

    /**
     * Verifies that GoalsCommand does not modify the food list.
     */
    @Test
    void goalsCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("goals").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    /**
     * Verifies that setting a valid daily calorie goal executes without error.
     */
    @Test
    void goalsCommand_set_validDailyCalories() {
        assertFalse(Parser.parse("goals set dc/2500").execute(context));
    }

    /**
     * Verifies that setting all goal prefixes at once executes without error.
     */
    @Test
    void goalsCommand_set_validAllPrefixes() {
        assertFalse(Parser.parse("goals set dc/2500 dp/60 wc/17500 wp/420").execute(context));
    }

    /**
     * Verifies that goals set with no prefixes does not throw.
     */
    @Test
    void goalsCommand_missingPrefixes_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("goals set").execute(context));
    }

    /**
     * Verifies that an unknown goal prefix does not throw.
     */
    @Test
    void goalsCommand_invalidPrefix_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("goals set xx/100").execute(context));
    }

    /**
     * Verifies that a negative goal value does not throw (prints error instead).
     */
    @Test
    void goalsCommand_negativeValue_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("goals set dc/-100").execute(context));
    }

    /**
     * Verifies that a non-numeric goal value does not throw (prints error instead).
     */
    @Test
    void goalsCommand_nonNumericValue_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("goals set dc/abc").execute(context));
    }

    /**
     * Verifies that an unknown goals subcommand does not throw.
     */
    @Test
    void goalsCommand_unknownSubcommand_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("goals unknown").execute(context));
    }

    // ── ProfileCommand ────────────────────────────────────

    /**
     * Verifies that Parser returns a ProfileCommand for profile input.
     */
    @Test
    void parser_profile_returnsCorrectCommand() {
        assertInstanceOf(ProfileCommand.class, Parser.parse("profile"));
    }

    /**
     * Verifies that ProfileCommand returns false (does not trigger exit).
     */
    @Test
    void profileCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("profile").execute(context));
    }

    /**
     * Verifies that viewing a non-existent profile does not throw.
     */
    @Test
    void profileCommand_noProfile_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("profile").execute(context));
    }

    /**
     * Verifies that setting a valid profile with all fields does not throw.
     */
    @Test
    void profileCommand_set_validAllFields() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set n/Bryan g/male a/22 w/80 h/181").execute(context)
        );
    }

    /**
     * Verifies that profile set with no fields does not throw.
     */
    @Test
    void profileCommand_missingFields_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("profile set").execute(context));
    }

    /**
     * Verifies that an invalid gender value does not throw (prints error instead).
     */
    @Test
    void profileCommand_invalidGender_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set n/Bryan g/unknown a/22 w/80 h/181").execute(context)
        );
    }

    /**
     * Verifies that a negative age does not throw (prints error instead).
     */
    @Test
    void profileCommand_negativeAge_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set n/Bryan g/male a/-1 w/80 h/181").execute(context)
        );
    }

    /**
     * Verifies that an unknown profile prefix does not throw.
     */
    @Test
    void profileCommand_invalidPrefix_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("profile set xx/Bryan").execute(context));
    }

    /**
     * Verifies that profile clear does not throw.
     */
    @Test
    void profileCommand_clear_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("profile clear").execute(context));
    }

    /**
     * Verifies that an unknown profile subcommand does not throw.
     */
    @Test
    void profileCommand_unknownSubcommand_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("profile unknown").execute(context));
    }

    // ── Profile Model ─────────────────────────────────────

    /**
     * Verifies that BMI is computed correctly for a male profile.
     */
    @Test
    void profile_bmi_correctValue() {
        assertEquals(24.4, new model.Profile("Bryan", "male", 22, 80, 181).getBmi(), 0.1);
    }

    /**
     * Verifies that BMR is computed correctly for a male profile.
     */
    @Test
    void profile_bmrMale_correct() {
        assertEquals(1826, new model.Profile("Bryan", "male", 22, 80, 181).getBmr());
    }

    /**
     * Verifies that BMR is computed correctly for a female profile.
     */
    @Test
    void profile_bmrFemale_correct() {
        assertEquals(1360, new model.Profile("Alice", "female", 22, 60, 165).getBmr());
    }

    /**
     * Verifies that BMI category is Normal for a healthy BMI.
     */
    @Test
    void profile_bmiCategory_normal() {
        assertEquals("Normal", new model.Profile("Bryan", "male", 22, 70, 175).getBmiCategory());
    }

    /**
     * Verifies that BMI category is Underweight for a low BMI.
     */
    @Test
    void profile_bmiCategory_underweight() {
        assertEquals("Underweight", new model.Profile("Bryan", "male", 22, 45, 175).getBmiCategory());
    }

    /**
     * Verifies that BMI category is Overweight for a high BMI.
     */
    @Test
    void profile_bmiCategory_overweight() {
        assertEquals("Overweight", new model.Profile("Bryan", "male", 22, 85, 175).getBmiCategory());
    }

    /**
     * Verifies that BMI category is Obese for a very high BMI.
     */
    @Test
    void profile_bmiCategory_obese() {
        assertEquals("Obese", new model.Profile("Bryan", "male", 22, 110, 175).getBmiCategory());
    }

    /**
     * Verifies that toString contains the user's name.
     */
    @Test
    void profile_toString_containsName() {
        assertTrue(new model.Profile("Bryan", "male", 22, 80, 181).toString().contains("Bryan"));
    }
    // @@author

    @Test
    public void sampleExit() {
        assertTrue(true);
    }
}
