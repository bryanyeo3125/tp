package seedu.bitbites;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import command.SummaryByDateCommand;
import command.SummaryCompareCommand;
import command.SummaryRangeCommand;
import command.TipsCommand;
import command.GoalsCommand;
import command.ProfileCommand;
import model.NutritionSummary;
import ui.ProgressBar;

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
        foodList.addFood(new Food("Burger", 450, 30, "27-03-2026"));
        foodList.addFood(new Food("Salad", 200, 10, "28-03-2026"));

        tempFilePath = tempDir.resolve("test_data.text").toString();
        storage = new Storage(tempFilePath);
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

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
    @Test
    void helpCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("help").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    // ── TipsCommand ───────────────────────────────────────
    @Test
    void tipsCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("tips").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    // ── Delete Command ───────────────────────────────────────
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
        Parser.parse("edit 1 d/01-01-2026").execute(context);
        assertEquals("01-01-2026", foodList.get(0).getDate());
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

    @Test
    void listByDate_mixedDates_coversBothBranches() {
        FoodList testFoods = new FoodList();
        testFoods.addFood(new Food("Apple", 95, 0.5, "15-04-2026"));
        testFoods.addFood(new Food("Banana", 105, 1.3, "16-04-2026"));

        AppContext context = new AppContext(testFoods, new PresetList(), new UserInterface());

        ListByDateCommand command = new ListByDateCommand("list d/15-04-2026");
        boolean isExit = command.execute(context);

        assertFalse(isExit);
    }

    @Test
    void listByDate_noMatches_executesSafely() {
        FoodList testFoods = new FoodList();
        testFoods.addFood(new Food("Pizza", 300, 12.0, "20-04-2026"));

        AppContext context = new AppContext(testFoods, new PresetList(), new UserInterface());

        ListByDateCommand command = new ListByDateCommand("list d/01-01-2026");
        boolean isExit = command.execute(context);

        assertFalse(isExit);
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

    // ── NutritionSummary ──────────────────────────────────
    @Test
    void nutritionSummary_correctTotalCalories() {
        NutritionSummary summary = foodList.getSummaryByDate("27-03-2026");
        assertEquals(450, summary.getTotalCalories());
    }

    @Test
    void nutritionSummary_correctTotalProtein() {
        NutritionSummary summary = foodList.getSummaryByDate("27-03-2026");
        assertEquals(30.0, summary.getTotalProtein());
    }

    @Test
    void nutritionSummary_correctItemCount() {
        NutritionSummary summary = foodList.getSummaryByDate("27-03-2026");
        assertEquals(1, summary.getItemCount());
    }

    // ── ProgressBar ───────────────────────────────────────
    @Test
    void progressBar_singleItem_fullBar() {
        java.util.List<Food> items = foodList.getItemsByDate("27-03-2026");
        String bar = ProgressBar.generateSegmented(items, 450);
        assertEquals("[" + "=".repeat(30) + "]", bar);
    }

    @Test
    void progressBar_emptyItems_emptyBar() {
        String bar = ProgressBar.generateSegmented(
                new java.util.ArrayList<>(), 0);
        assertEquals("[]", bar);
    }

    @Test
    void progressBar_multipleItems_containsSeparator() {
        foodList.addFood(new Food("Tea", 50, 0.0, "27-03-2026"));
        java.util.List<Food> items = foodList.getItemsByDate("27-03-2026");
        String bar = ProgressBar.generateSegmented(items, foodList.getTotalCaloriesByDate("27-03-2026"));
        assertTrue(bar.contains("|"));
    }

    // ── FoodList summary methods ──────────────────────────
    @Test
    void getTotalCaloriesByDate_correctSum() {
        assertEquals(450, foodList.getTotalCaloriesByDate("27-03-2026"));
    }

    @Test
    void getTotalProteinByDate_correctSum() {
        assertEquals(30.0, foodList.getTotalProteinByDate("27-03-2026"));
    }

    @Test
    void getItemCountByDate_correctCount() {
        assertEquals(1, foodList.getItemCountByDate("27-03-2026"));
    }

    @Test
    void getItemCountByDate_noItems() {
        assertEquals(0, foodList.getItemCountByDate("01-01-2000"));
    }

    @Test
    void getSummariesInRange_correctCount() {
        java.util.List<NutritionSummary> summaries =
                foodList.getSummariesInRange("27-03-2026", "29-03-2026");
        assertEquals(2, summaries.size());
    }

    @Test
    void getTopDaysByCalories_correctOrder() {
        java.util.List<NutritionSummary> top = foodList.getTopDaysByCalories(2);
        assertEquals(2, top.size());
        assertTrue(top.get(0).getTotalCalories() >= top.get(1).getTotalCalories());
    }

    @Test
    void getTopDaysByCalories_nLargerThanAvailable() {
        java.util.List<NutritionSummary> top = foodList.getTopDaysByCalories(99);
        assertEquals(foodList.getUniqueDates().size(), top.size());
    }

    @Test
    void getBestDaysByCalories_correctOrder() {
        java.util.List<NutritionSummary> best = foodList.getBestDaysByCalories(2);
        assertEquals(2, best.size());
        assertTrue(best.get(0).getTotalCalories() <= best.get(1).getTotalCalories());
    }

    @Test
    void getCurrentStreak_correctValue() {
        assertEquals(0, foodList.getCurrentStreak());
    }

    @Test
    void getLongestStreak_correctValue() {
        assertEquals(2, foodList.getLongestStreak());
    }

    @Test
    void getLongestStreak_allConsecutive() {
        FoodList consecutive = new FoodList();
        consecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        consecutive.addFood(new Food("B", 100, 10.0, "28-03-2026"));
        consecutive.addFood(new Food("C", 100, 10.0, "29-03-2026"));
        consecutive.addFood(new Food("D", 100, 10.0, "30-03-2026"));
        assertEquals(4, consecutive.getLongestStreak());
    }

    // ── Summary Command ───────────────────────────────────
    @Test
    void parser_summaryByDate_returnsCorrectCommand() {
        Command command = Parser.parse("summary d/27-03-2026");
        assertInstanceOf(SummaryByDateCommand.class, command);
    }

    @Test
    void parser_summaryRange_returnsCorrectCommand() {
        Command command = Parser.parse("summary from/27-03-2026 to/29-03-2026");
        assertInstanceOf(SummaryRangeCommand.class, command);
    }

    @Test
    void parser_summaryCompare_returnsCorrectCommand() {
        Command command = Parser.parse("summary compare d/27-03-2026 d/28-03-2026");
        assertInstanceOf(SummaryCompareCommand.class, command);
    }

    @Test
    void summaryByDateCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("summary d/27-03-2026").execute(context);
        assertFalse(isExit);
    }

    @Test
    void summaryRangeCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse(
                "summary from/27-03-2026 to/29-03-2026").execute(context);
        assertFalse(isExit);
    }

    @Test
    void summaryRangeCommand_fromAfterTo_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("summary from/29-03-2026 to/27-03-2026").execute(context)
        );
    }

    @Test
    void summaryCompareCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse(
                "summary compare d/27-03-2026 d/28-03-2026").execute(context);
        assertFalse(isExit);
    }

    // ── history command execute ───────────────────────────
    @Test
    void historyCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history").execute(context));
    }

    @Test
    void historyCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("history").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    @Test
    void historyCommand_emptyList_doesNotThrow() {
        AppContext emptyContext = new AppContext(new FoodList(), presetList, ui);
        assertDoesNotThrow(() ->
                Parser.parse("history").execute(emptyContext)
        );
    }

    @Test
    void historyTopCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history /top 2").execute(context));
    }

    @Test
    void historyTopCommand_nLargerThanData_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("history /top 99").execute(context)
        );
    }

    @Test
    void historyTopCommand_zeroN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /top 0").execute(context)
        );
    }

    @Test
    void historyBestCommand_execute_returnsFalse() {
        assertFalse(Parser.parse("history /best 2").execute(context));
    }

    @Test
    void historyBestCommand_nLargerThanData_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("history /best 99").execute(context)
        );
    }

    @Test
    void historyBestCommand_zeroN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /best 0").execute(context)
        );
    }

    @Test
    void historyBestCommand_invalidN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /best abc").execute(context)
        );
    }

    @Test
    void historyTopCommand_invalidN_throwsException() {
        assertThrows(BitbitesException.class, () ->
                Parser.parse("history /top abc").execute(context)
        );
    }

    // ── HistoryStreakCommand ──────────────────────────────
    @Test
    void historyStreakCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("history streak").execute(context);
        assertFalse(isExit);
    }

    @Test
    void historyStreakCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("history streak").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    @Test
    void historyStreakCommand_emptyList_doesNotThrow() {
        AppContext emptyContext = new AppContext(new FoodList(), presetList, ui);
        assertDoesNotThrow(() ->
                Parser.parse("history streak").execute(emptyContext)
        );
    }

    @Test
    void historyStreakCommand_consecutiveDays_correctCurrentStreak() {
        assertEquals(0, foodList.getCurrentStreak());
    }

    @Test
    void historyStreakCommand_consecutiveDays_correctLongestStreak() {
        assertEquals(2, foodList.getLongestStreak());
    }

    @Test
    void historyStreakCommand_nonConsecutive_currentStreakOne() {
        FoodList nonConsecutive = new FoodList();
        nonConsecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        nonConsecutive.addFood(new Food("B", 100, 10.0, "29-03-2026"));
        assertEquals(0, nonConsecutive.getCurrentStreak());
    }

    @Test
    void historyStreakCommand_nonConsecutive_longestStreakOne() {
        FoodList nonConsecutive = new FoodList();
        nonConsecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        nonConsecutive.addFood(new Food("B", 100, 10.0, "29-03-2026")); // gap
        assertEquals(1, nonConsecutive.getLongestStreak());
    }

    // ── GoalsCommand ──────────────────────────────────────
// @@author bryanyeo3125
    @Test
    void parser_goals_returnsCorrectCommand() {
        Command command = Parser.parse("goals");
        assertInstanceOf(GoalsCommand.class, command);
    }

    @Test
    void goalsCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("goals").execute(context);
        assertFalse(isExit);
    }

    @Test
    void goalsCommand_doesNotModifyFoodList() {
        int sizeBefore = foodList.size();
        Parser.parse("goals").execute(context);
        assertEquals(sizeBefore, foodList.size());
    }

    @Test
    void goalsCommand_set_validDailyCalories() {
        boolean isExit = Parser.parse("goals set dc/2500").execute(context);
        assertFalse(isExit);
    }

    @Test
    void goalsCommand_set_validAllPrefixes() {
        boolean isExit = Parser.parse("goals set dc/2500 dp/60 wc/17500 wp/420").execute(context);
        assertFalse(isExit);
    }

    @Test
    void goalsCommand_set_missingPrefixes_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("goals set").execute(context)
        );
    }

    @Test
    void goalsCommand_set_invalidPrefix_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("goals set xx/100").execute(context)
        );
    }

    @Test
    void goalsCommand_set_negativeValue_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("goals set dc/-100").execute(context)
        );
    }

    @Test
    void goalsCommand_set_nonNumericValue_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("goals set dc/abc").execute(context)
        );
    }

    @Test
    void goalsCommand_unknownSubcommand_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("goals unknown").execute(context)
        );
    }

    // ── ProfileCommand ────────────────────────────────────
    @Test
    void parser_profile_returnsCorrectCommand() {
        Command command = Parser.parse("profile");
        assertInstanceOf(ProfileCommand.class, command);
    }

    @Test
    void profileCommand_execute_returnsFalse() {
        boolean isExit = Parser.parse("profile").execute(context);
        assertFalse(isExit);
    }

    @Test
    void profileCommand_noProfile_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile").execute(context)
        );
    }

    @Test
    void profileCommand_set_validAllFields() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set n/Bryan g/male a/22 w/80 h/181").execute(context)
        );
    }

    @Test
    void profileCommand_set_missingFields_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set").execute(context)
        );
    }

    @Test
    void profileCommand_set_invalidGender_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set n/Bryan g/unknown a/22 w/80 h/181").execute(context)
        );
    }

    @Test
    void profileCommand_set_negativeAge_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set n/Bryan g/male a/-1 w/80 h/181").execute(context)
        );
    }

    @Test
    void profileCommand_set_invalidPrefix_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile set xx/Bryan").execute(context)
        );
    }

    @Test
    void profileCommand_clear_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile clear").execute(context)
        );
    }

    @Test
    void profileCommand_unknownSubcommand_doesNotThrow() {
        assertDoesNotThrow(() ->
                Parser.parse("profile unknown").execute(context)
        );
    }

    // ── Profile Model ─────────────────────────────────────
    @Test
    void profile_bmi_correctValue() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 80, 181);
        assertEquals(24.4, profile.getBmi(), 0.1);
    }

    @Test
    void profile_bmr_male_correctValue() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 80, 181);
        assertEquals(1826, profile.getBmr());
    }

    @Test
    void profile_bmr_female_correctValue() {
        model.Profile profile = new model.Profile("Alice", "female", 22, 60, 165);
        assertEquals(1382, profile.getBmr());
    }

    @Test
    void profile_bmiCategory_normal() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 70, 175);
        assertEquals("Normal", profile.getBmiCategory());
    }

    @Test
    void profile_bmiCategory_underweight() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 45, 175);
        assertEquals("Underweight", profile.getBmiCategory());
    }

    @Test
    void profile_bmiCategory_overweight() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 85, 175);
        assertEquals("Overweight", profile.getBmiCategory());
    }

    @Test
    void profile_bmiCategory_obese() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 110, 175);
        assertEquals("Obese", profile.getBmiCategory());
    }

    @Test
    void profile_toString_containsName() {
        model.Profile profile = new model.Profile("Bryan", "male", 22, 80, 181);
        assertTrue(profile.toString().contains("Bryan"));
    }
    // @@author

    @Test
    public void sampleExit() {
        assertTrue(true);
    }
}
