# BitBites Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

---

## Design & Implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

---

### 1. Design: Command Pattern and AppContext

BitBites uses the **Command design pattern** to encapsulate each user action as a discrete object. This makes it straightforward to add new features without modifying existing parsing or execution logic.

#### 1.1 The `Command` Abstract Class

All executable actions extend the abstract `Command` class in `command/Command.java`. Each subclass must implement:

```
execute(AppContext context) → boolean
```

The return value signals whether the application should exit (`true`) or continue (`false`). This keeps the main application loop simple and decoupled from individual command logic.

`Command` also provides two shared utility methods used by subclasses that parse prefix-based input (e.g., `n/NAME`, `dc/CALORIES`):

- `extractValue(command, prefix, nextPrefix)` — extracts the value between two known prefixes.
- `nextPrefix(command, currentPrefix, prefixes[])` — finds the closest following prefix to determine value boundaries.

These utility methods are inherited by `GoalsCommand`, `ProfileCommand`, `AddCommand`, and others, avoiding duplicated parsing logic across the codebase.

The class diagram below shows the relationship between `Command` and its concrete subclasses:

![Command class diagram](uml/command_class.png)

#### 1.2 The `AppContext` Class

Rather than passing `FoodList`, `PresetList`, and `UserInterface` as separate parameters to every command, BitBites wraps them in a single `AppContext` object (following the **Context Object pattern**). This means:

- Adding a new component to the application does not require updating every `execute()` method signature.
- Each command retrieves only what it needs from the context.

A typical command begins like this:

```java
FoodList foodList = context.getFoodList();
UserInterface ui = context.getUi();
```

`AppContext` is constructed once at application startup and passed through to every command execution.

---

### 2. Listing Food Items `list`

The `list` feature provides users with the ability to view their logged food items. It is implemented with two primary execution paths to handle different user needs:
1. **List All:** Displays the entire history of logged food items.
2. **List by Date:** Filters and displays only the food items consumed on a specified date.

#### 2.1 Implementation Details

The feature is driven by two main methods: `handleListAll()` and `handleListFromDate()`. Both methods depend on the `FoodList` component to retrieve data and the `UserInterface` to display the results to the user.

**Executing `list` (All Items):**
When the user inputs the `list` command without any arguments, `handleListAll()` is invoked.
1. The system prints a standard header message (`BitbitesResponses.listMessage`).
2. It loops through the `FoodList` from index `0` to `foodList.size() - 1`.
3. It prints each `Food` object sequentially, utilising the object's overridden `toString()` method, prefixed by a 1-based index.

**Executing `list d/DATE` (Filtered by Date):**
When the user inputs the `list` command followed by the date parameter (e.g., `list d/2026-03-27`), `handleListFromDate()` is invoked. The execution follows these steps:
1. **Parsing and Validation:** The raw command string is split using the `d/` delimiter. The method verifies that the array length is at least 2. If the date argument is missing, it throws a `BitbitesException`.
2. **Defensive Programming:** Internal `assert` statements are used to guarantee that the command strictly follows the expected `list` prefix and that the extracted date string is not empty.
3. **Filtering:** The method extracts the target `date` string. It initialises a local `count` variable at 1 to ensure the printed list maintains a continuous numerical sequence.
4. **Execution:** The system iterates over every item in the `FoodList`. For each item, it compares the item's stored date with the target `date`. If a match is found, it is printed to the console and the `count` is incremented.

Below is the sequence diagram illustrating the execution flow of the `handleListFromDate` method:

![handleListFromDate sequence diagram](uml/listFromDate.png)

---

### 3. Adding a Food Item `add`

The `add` feature allows users to log a new food entry into the tracker. It is implemented with a single execution path that handles parsing, validation, and storage of the new food item.

#### 3.1 Implementation Details

The feature is driven by the `execute()` method in `AddCommand.java`. It depends on the `FoodList` component to store data and the `UserInterface` to display feedback to the user.

**Executing `add n/NAME c/CALORIES p/PROTEIN d/DATE`:**
When the user inputs the `add` command followed by the required parameters, `execute()` is invoked. The execution follows these steps:
1. **Prefix Validation:** The method checks that all four required prefixes (`n/`, `c/`, `p/`, `d/`) are present in the command. If any are missing, the correct format reminder is printed and execution stops early.
2. **Field Extraction:** Each field is extracted using `String.substring()` based on the positions of the prefixes. Extracted values are trimmed of leading and trailing whitespace.
3. **Empty Field Check:** If any extracted field is empty after trimming, the format reminder is displayed again and execution stops early.
4. **Type Parsing and Validation:** `calories` is parsed as an `int` and `protein` as a `double`. Both values must be non-negative. A `NumberFormatException` is caught if parsing fails, and a descriptive error is shown to the user.
5. **Date Validation:** The date string is validated against the regex `\d{2}-\d{2}-\d{4}`. If the format does not match, an error message is shown.
6. **Defensive Programming:** Internal `assert` statements verify that protein is non-negative and the date format is correct before the object is created.
7. **Food Creation and Goal Feedback:** A new `Food` object is constructed with the validated fields and added to the `FoodList` via `addFood()`. A confirmation message is printed, followed by a daily progress summary from `GoalsCommand.showDailyProgress()`.

Below is the sequence diagram illustrating the execution flow of the `add` command:

![add sequence diagram](uml/add_sequence.png)

---

### 4. Editing a Food Item `edit`

The `edit` feature allows users to update one or more fields of an existing food item without deleting and re-adding it. Only the specified fields are changed.

**Format:** `edit INDEX [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE]`

At least one field must be provided.

#### 4.1 Implementation Details

**Executing `edit INDEX [fields]`:**
1. **Parsing:** The command is split into three parts: keyword, index, and fields string.
2. **Index Conversion:** Same as `DeleteCommand`.
3. **Field Detection:** The fields string is checked for `n/`, `c/`, `p/`, `d/`. At least one must be present or a `BitbitesException` is thrown.
4. **In-place Update:** `FoodList.getFood(index)` returns a reference to the existing `Food` object. Each field is extracted using `extractField()` which stops at the next prefix, then applied via the corresponding setter.
5. **Validation:** Calories and protein must be non-negative. Date must match `\d{2}-\d{2}-\d{4}`.
6. **Confirmation:** `ui.showEditedFood()` prints the updated food item.
7. **Persistence:** `foodStorage.save(foods)` is called in the main loop.

![edit sequence diagram](uml/edit.png)

---

### 5. Deleting a Food Item `delete`

The `delete` feature allows users to remove a logged food item from the list by its displayed index. After deletion, a daily progress summary is shown to reflect the updated intake against the user's goals.

#### 5.1 Implementation Details

The feature is implemented in `DeleteCommand`, following the Command Pattern. `Parser` creates the command object and `Bitbites` calls `execute(context)`.

**Executing `delete INDEX`:**
1. **Parsing and Validation:** The command is split by space. If the index is missing, a `BitbitesException` is thrown.
2. **Index Conversion:** The index is parsed to `int` and converted from 1-based to 0-based. Non-numeric input throws a `BitbitesException`.
3. **Defensive Programming:** An `assert` verifies the converted index is non-negative.
4. **Deletion:** `FoodList.deleteFood(index)` performs bounds checking internally and removes the item.
5. **Postcondition Check:** An `assert` verifies `foodList.size()` decreased by 1.
6. **Confirmation:** `ui.showDeletedFood()` prints the removed item and remaining count.
7. **Goal Progress:** `GoalsCommand.showDailyProgress()` prints today's intake against the daily goal.
8. **Persistence:** `foodStorage.save(foods)` is called in the main loop after execution.

![delete sequence diagram](uml/delete.png)

---

### 6. Exiting the Application `exit`

The `exit` feature allows users to terminate the application safely when they are done. It is implemented as a direct command branch in `Parser.parse(...)` and integrates with the main application loop in `Bitbites`.

#### 6.1 Implementation Details

When the user inputs `exit`, the following execution flow occurs:

1. **Command Matching:** `Parser.parse(...)` checks whether the trimmed input is exactly `exit`.
2. **User Feedback:** The parser invokes `ui.showExit()` to display a farewell message.

---

### 7. Help Command `help`

The `help` command displays a summary of all available commands and their formats.

**Format:** `help`

#### 7.1 Implementation Details

`HelpCommand.execute()` delegates entirely to `ui.showHelp()`, which prints `BitbitesResponses.helpMessage`. No data access or modification occurs.

---

### 8. Managing User Profiles `profile`

The `profile` feature allows each user to store and retrieve their personal physical attributes. It integrates with the `goals` feature to automatically set sensible calorie targets when a profile is saved.

#### 8.1 Implementation Details

The feature spans three classes: `ProfileCommand` (command logic), `Profile` (data model), and `ProfileStorage` (disk persistence).

**Supported sub-commands:**

| Command | Behaviour |
|---|---|
| `profile` | View the current user's saved profile |
| `profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT` | Create or update profile fields |
| `profile clear` | Delete the current user's profile file |

**Executing `profile set ...`:**

When the user provides one or more profile fields, `handleSetProfile()` is invoked. The steps are:

1. **Load Existing Profile:** `ProfileStorage.loadProfile(name)` is called first. If an existing profile is found, its current values are used as defaults so that only the supplied fields are changed — unchanged fields are preserved.
2. **Field Extraction:** Each field is extracted from the command string using the inherited `extractValue()` and `nextPrefix()` utilities from `Command`.
3. **Gender Validation:** The `g/` value must be exactly `"male"` or `"female"` (case-insensitive). Any other value causes an early return with an error message.
4. **Range Validation:** Age, weight, and height must all be non-negative. A check is performed before the `Profile` object is constructed.
5. **Persistence:** The updated `Profile` is saved to disk via `ProfileStorage.saveProfile(profile)`.
6. **BMR Auto-Set:** After saving, `GoalsCommand.autoSetGoalsFromBmr(name, profile.getBmr())` is called automatically to update the user's daily and weekly calorie goals based on their new BMR.

The sequence diagram below illustrates the execution of `profile set ...`:

```
User → ProfileCommand.execute()
         → ProfileStorage.loadProfile()    // load existing or use defaults
         → extractValue() [repeated]       // parse each prefix
         → new Profile(...)
         → ProfileStorage.saveProfile()    // persist to disk
         → GoalsCommand.autoSetGoalsFromBmr()  // update calorie goals
```

#### 8.2 The `Profile` Model

`Profile` stores five fields: `name`, `gender`, `age`, `weight` (kg), and `height` (cm). It derives two computed values:

- **BMI** — calculated as `weight / (height in metres)²`
- **BMR** — calculated using the Mifflin-St Jeor formula:
   - Male: `(10 × weight) + (6.25 × height) − (5 × age) + 5`
   - Female: `(10 × weight) + (6.25 × height) − (5 × age) − 161`

BMI is also categorised into `Underweight`, `Normal`, `Overweight`, or `Obese` based on standard thresholds.

---

### 9. Managing Nutritional Goals `goals`

The `goals` feature allows users to set daily and weekly calorie and protein targets, and view their current progress against those targets. Goals are persisted per user and are automatically set when a profile is saved.

#### 9.1 Implementation Details

The feature is driven by `GoalsCommand.java`, with persistence handled by `GoalsStorage.java`.

**Supported sub-commands:**

| Command | Behaviour |
|---|---|
| `goals` | View daily and weekly progress against current targets |
| `goals set dc/CAL dp/PROT wc/CAL wp/PROT` | Set one or more goal values |

All four goal values (`dc/`, `dp/`, `wc/`, `wp/`) are optional in a single `goals set` command — the user may supply any combination and only the specified fields are updated.

**Default goal values** (applied when no saved goals exist):

| Goal | Default |
|---|---|
| Daily calories | 2000 kcal |
| Daily protein | 50.0 g |
| Weekly calories | 14000 kcal |
| Weekly protein | 350.0 g |

**Executing `goals` (view progress):**

`showGoalsMenu()` is called. It computes today's totals and the current week's totals by iterating over the `FoodList` and comparing each item's date to today and to the Monday–Sunday window containing today. Results are printed alongside the saved targets, with a `(Goal reached!)` indicator when a target is met or exceeded.

**Executing `goals set ...`:**

`handleSetGoals()` parses each recognised prefix using the inherited `extractValue()` utility. For each prefix present, the value is parsed and validated (must be non-negative), the corresponding static field is updated, and a confirmation message is printed. All four values are then written to disk via `GoalsStorage.saveGoals()`.

**Goal persistence across sessions:**

Goals are loaded lazily at the start of each `GoalsCommand.execute()` call via `loadGoalsIfNeeded()`. This method calls `GoalsStorage.loadGoals()` and, if a saved file exists, overwrites the static defaults with the saved values.

**Static daily progress summary:**

`GoalsCommand.showDailyProgress(FoodList)` is a static utility method called by `AddCommand` (and any other command that modifies the food list) to print a brief progress summary after each change. This gives the user immediate feedback on how their latest entry affects their daily targets.

**Auto-setting goals from BMR:**

`GoalsCommand.autoSetGoalsFromBmr(name, bmr)` is called by `ProfileCommand` after a profile is saved. It sets the daily calorie goal to the user's BMR and the weekly goal to `BMR × 7`, then saves to disk. Protein goals are not changed by this auto-set.

The sequence diagram below illustrates the execution of `goals set ...`:

```
User → GoalsCommand.execute()
         → GoalsStorage.loadGoals()        // load persisted goals (if any)
         → handleSetGoals()
              → extractValue() [per prefix]
              → validate each value
              → update static fields
              → GoalsStorage.saveGoals()   // persist updated goals
```

---

### 10. Summary Commands `summary`

The `summary` feature provides nutritional breakdowns for logged food items. It supports three sub-commands.

| Command | Description |
|---------|-------------|
| `summary d/DATE` | Summary for a specific date |
| `summary from/DATE1 to/DATE2` | Trend across a date range |
| `summary compare d/DATE1 d/DATE2` | Comparison of two days |

#### 10.1 Implementation Details

Each sub-command is a dedicated Command class. All retrieve `NutritionSummary` objects from `FoodList` and pass them to `UserInterface`.

`NutritionSummary` stores aggregated `totalCalories`, `totalProtein`, `itemCount`, and the list of `Food` items. `ProgressBar.generateSegmented()` generates a bar where each segment's width represents that meal's calorie share of the day's total.

**`summary d/DATE`:**
1. The date is extracted after `d/`.
2. If no items exist for the date, a message is shown and execution stops.
3. Goal values are retrieved from `GoalsCommand.getDailyCalorieGoal()` and `getDailyProteinGoal()`.
4. `ui.showSummary(summary, calorieGoal, proteinGoal)` prints the breakdown and goal status.

![summary by date sequence diagram](uml/summaryByDate.png)

**`summary from/DATE1 to/DATE2`:**
1. Both `from/` and `to/` prefixes must be present.
2. Dates are parsed using `LocalDate.parse()` with `dd-MM-yyyy` formatter.
3. If `from` is after `to`, a `BitbitesException` is thrown.
4. `FoodList.getSummariesInRange()` returns daily summaries within the range.
5. If no summaries found, a message is shown and execution stops.
6. `ui.showSummaryRange()` displays each day's bar scaled to the highest-calorie day.

**`summary compare d/DATE1 d/DATE2`:**
1. The command is split by `d/` — at least 3 parts must exist.
2. Both dates are extracted and checked for emptiness.
3. If either date has no items, a message is shown and execution stops early.
4. `FoodList.getSummaryByDate()` is called for each date.
5. `ui.showSummaryCompare()` displays both days side by side with calorie and protein differences.

---

### 11. History Commands `history`

The `history` feature shows a chronological log of all recorded days. It supports four sub-commands.

| Command | Description |
|---------|-------------|
| `history` | All recorded days with breakdown bars |
| `history /top N` | Top N highest calorie days |
| `history /best N` | Top N days closest to daily calorie goal |
| `history streak` | Current and longest consecutive recording streak |

#### 11.1 Implementation Details

**`history` and `history /top N`:**

`HistoryCommand` and `HistoryTopCommand` retrieve `NutritionSummary` lists from `FoodList`. Each day's bar in `showHistory()` is scaled relative to the highest-calorie day — the busiest day fills the full bar width and others are proportionally shorter.

**`history`:**
`HistoryCommand` checks whether any food has been logged today using `LocalDate.now()`. It retrieves all daily summaries and passes them with a `recordedToday` flag to `ui.showHistory()`, which appends a reminder if today has not been logged.

**`history /top N`:**
`HistoryTopCommand` splits the command by `/top` to extract `N`. It calls `foodList.getTopDaysByCalories(N)` which sorts summaries by total calories descending and returns the top N.

**`history /best N`:**
`HistoryBestCommand` calls `foodList.getDaysClosestToGoal(n, calorieGoal)`, which sorts summaries by `|totalCalories - dailyCalorieGoal|` ascending, surfacing the days where intake was closest to the user's target.

**`history streak`:**
`HistoryStreakCommand` calls `getCurrentStreak()` and `getLongestStreak()`. Streak calculation uses `LocalDate.parse()` with `dd-MM-yyyy` format to compare consecutive dates. `getCurrentStreak()` also checks whether the last recorded date is today or yesterday using `LocalDate.now()` — if neither, the streak returns 0.

![history streak sequence diagram](uml/historyStreak.png)

---

### 12. Storage Components

BitBites uses two independent storage classes — `ProfileStorage` and `GoalsStorage` — both located in the `storage` package. Each class reads and writes plain-text key-value files stored in the `data/` directory.

#### 12.1 File Naming Convention

Both storage classes derive a safe filename from the user's name by converting it to lowercase and replacing spaces with underscores:

```
data/<safeName>_profile.txt
data/<safeName>_goals.txt
```

For example, a user named `"John Doe"` produces the files `data/john_doe_profile.txt` and `data/john_doe_goals.txt`. This allows multiple users to maintain independent data on the same system.

#### 12.2 File Formats

**Profile file** (`<n>_profile.txt`):
```
name=VALUE
gender=VALUE
age=VALUE
weight=VALUE
height=VALUE
```

**Goals file** (`<n>_goals.txt`):
```
dailyCalories=VALUE
dailyProtein=VALUE
weeklyCalories=VALUE
weeklyProtein=VALUE
```

Both files are read line-by-line in a fixed order. Each line is split on the `=` character and the second element is parsed into the appropriate type (`int`, `double`, or `String`).

#### 12.3 Error Handling

Both `loadProfile()` and `loadGoals()` return `null` if the file does not exist or if any line fails to parse (caught via `IOException` or `NumberFormatException`). Callers treat a `null` return as "no saved data" and fall back to defaults. This means a corrupted or missing file degrades gracefully without crashing the application.

#### 12.4 Key Public Methods

| Class | Method | Description |
|---|---|---|
| `ProfileStorage` | `saveProfile(Profile)` | Writes profile fields to disk |
| `ProfileStorage` | `loadProfile(String name)` | Reads and returns a `Profile`, or `null` |
| `ProfileStorage` | `profileExists(String name)` | Checks if a profile file exists |
| `ProfileStorage` | `deleteProfile(String name)` | Deletes the profile file |
| `GoalsStorage` | `saveGoals(name, dc, dp, wc, wp)` | Writes all four goal values to disk |
| `GoalsStorage` | `loadGoals(String name)` | Returns a `double[]` of four values, or `null` |
| `GoalsStorage` | `goalsExist(String name)` | Checks if a goals file exists |

---

## Appendix A: Product Scope

### Target User Profile

{Describe the target user profile}

### Value Proposition

{Describe the value proposition: what problem does it solve?}

---

## Appendix B: User Stories

| Version | As a ... | I want to ... | So that I can ... |
|---------|----------|---------------|-------------------|
| v1.0 | new user | see usage instructions | refer to them when I forget how to use the application |
| v2.0 | user | find a to-do item by name | locate a to-do without having to go through the entire list |

---

## Appendix C: Non-Functional Requirements

{Give non-functional requirements}

---

## Appendix D: Glossary

* *glossary item* - Definition

---

## Appendix E: Instructions for Manual Testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
