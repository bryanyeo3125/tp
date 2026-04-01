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
import command.HistoryBestCommand;
import command.HistoryCommand;
import command.HistoryStreakCommand;
import command.HistoryTopCommand;
import command.SummaryByDateCommand;
import command.SummaryCompareCommand;
import command.SummaryRangeCommand;
import command.TipsCommand;
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
        assertEquals(2, foodList.getCurrentStreak());
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
        assertEquals(2, foodList.getCurrentStreak());
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
        assertEquals(1, nonConsecutive.getCurrentStreak());
    }

    @Test
    void historyStreakCommand_nonConsecutive_longestStreakOne() {
        FoodList nonConsecutive = new FoodList();
        nonConsecutive.addFood(new Food("A", 100, 10.0, "27-03-2026"));
        nonConsecutive.addFood(new Food("B", 100, 10.0, "29-03-2026")); // gap
        assertEquals(1, nonConsecutive.getLongestStreak());
    }

    @Test
    public void sampleExit() {
        assertTrue(true);
    }
}

/*
Bryan: Added some comments in the JUnit code
 */

