# BitBites User Guide

## Introduction

**Bitbites** is a command-line chatbot for tracking your daily food intake, calories, and protein. It is designed for users who prefer a fast, keyboard-driven workflow over graphical interfaces.

With Bitbites you can:
- Log meals with calorie and protein counts
- Set and track daily and weekly nutritional goals
- Save food presets to quickly re-log frequent meals
- View summaries, trends, and streaks across your food history
- Maintain a personal profile to compute your BMI and BMR

---

## Table of Contents
- [Quick Start](#quick-start)
- [Notes on Command Format](#notes-on-command-format)
- [Features](#features)
    - [Viewing help: `help`](#viewing-help--help)
    - [Adding a food item: `add`](#adding-a-food-item--add)
    - [Listing food items: `list`](#listing-food-items--list)
    - [Deleting a food item: `delete`](#deleting-a-food-item--delete)
    - [Editing a food item: `edit`](#editing-a-food-item--edit)
    - [Finding food items: `find`](#finding-food-items--find)
    - [Viewing a nutrition summary: `summary`](#viewing-a-nutrition-summary--summary)
    - [Viewing food history: `history`](#viewing-food-history--history)
    - [Managing food presets: `preset`](#managing-food-presets--preset)
    - [Managing nutritional goals: `goals`](#managing-nutritional-goals--goals)
    - [Managing your profile: `profile`](#managing-your-profile--profile)
    - [Switching users: `login`](#switching-users--login)
    - [Viewing nutrition tips: `tips`](#viewing-nutrition-tips--tips)
    - [Getting motivated: `motivate`](#getting-motivated--motivate)
    - [Exiting the application: `exit`](#exiting-the-application--exit)
- [Data Storage](#data-storage)
- [FAQ](#faq)
- [Known Issues](#known-issues)
- [Command Summary](#command-summary)

---

## Quick Start

1. Ensure you have **Java 17** or above installed on your computer.
2. Download the latest version of `BitBites` from [here](https://github.com/AY2526S2-CS2113-F14-2/tp/releases).
3. Open a terminal and navigate to the folder containing `bitbites.jar`.
4. Run the application with:
   ```
   java -jar bitbites.jar
   ```
5. When prompted, enter your name. Bitbites will greet you as a returning user if a profile is found, or guide you to set one up if not.
6. Type `help` to see a list of all available commands.

---

## Notes on Command Format

- Words in `UPPER_CASE` are parameters to be supplied by you.
    - e.g., in `add n/NAME c/CALORIES p/PROTEIN d/DATE`, `NAME`, `CALORIES`, `PROTEIN`, and `DATE` are parameters you fill in.
- Parameters with a prefix (e.g., `n/`, `c/`) can be provided in any order, unless otherwise stated.
- Parameters in square brackets `[...]` are optional.
- All dates must be in `DD-MM-YYYY` format (e.g., `25-12-2024`).
- Commands are case-sensitive and must be typed in lowercase.
- Extra spaces between words in a command are ignored.

---

## Features

---

### Viewing help : `help`

Displays a summary of all available commands and their formats.

**Format:** `help`

---

### Adding a food item : `add`

Adds a new food entry to your food log for a given date.

**Format:** `add n/NAME c/CALORIES p/PROTEIN [d/DATE]`

| Parameter   | Description                                  |
|-------------|----------------------------------------------|
| `NAME`      | Name of the food item.                       |
| `CALORIES`  | Calorie count in kcal (must be a whole number ≥ 0). |
| `PROTEIN`   | Protein content in grams (decimal allowed, ≥ 0). |
| `DATE`      | Date of consumption in `DD-MM-YYYY` format.  |

Date is optional and will use today's date if left empty. After a successful add, Bitbites will display your updated progress toward today's daily calorie and protein goals.

**Examples:**
```
add n/Chicken Rice c/500 p/30 d/01-04-2025
add n/Banana c/105 p/1.3 d/01-04-2025
```

> **Note:** Calories must be a whole integer, protein may be a decimal. Both must be non-negative. The date must strictly follow `DD-MM-YYYY` format.

---

### Listing food items : `list`

Displays all food items currently in your log, or only those from a specific date.

**Format (all items):** `list`

**Format (by date):** `list d/DATE`

**Examples:**
```
list
list d/01-04-2025
```

> **Note:** If no food items exist for the specified date, the list will be empty with no error.

---

### Deleting a food item : `delete`

Removes a food item from your log by its index number (as shown in `list`).

**Format:** `delete INDEX`

- `INDEX` must be a positive integer corresponding to a listed item.
- After deletion, Bitbites will display your updated progress toward today's daily goals.

**Example:**
```
delete 2
```

---

### Editing a food item : `edit`

Modifies one or more fields of an existing food entry, identified by its index number (as shown in `list`).

**Format:** `edit INDEX [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE]`

- At least one field must be provided.
- Only the fields you specify will be changed; the rest remain unchanged.

**Examples:**
```
edit 1 n/Grilled Chicken c/350
edit 3 p/25.5 d/02-04-2025
```

> **Note:** `INDEX` must refer to a valid item in the list. The same validation rules apply as in `add` (non-negative numbers, `DD-MM-YYYY` date format).

---

### Finding food items : `find`

Searches your food log for items whose name exactly matches the keyword (case-insensitive).

**Format:** `find KEYWORD`

- The search matches the **full name** of the food item exactly. Partial matches are not returned.
- Results show each matching entry's name, calories, and protein.

**Example:**
```
find Chicken Rice
```

> **Note:** `find chicken rice` and `find Chicken Rice` return the same results. However, `find chicken` will **not** match an item named `Chicken Rice`.

---

### Viewing a nutrition summary : `summary`

Displays a nutritional breakdown for a specific date or date range.

#### Summary for a specific date

**Format:** `summary d/DATE`

Shows total calories and protein for the date, a per-item percentage breakdown, and a segmented ASCII progress bar. Also shows how your intake compares to your daily goals.

**Example:**
```
summary d/01-04-2025
```

#### Summary comparing two dates

**Format:** `summary compare d/DATE1 d/DATE2`

Shows the calorie and protein totals for each date side by side, along with the difference between them.

**Example:**
```
summary compare d/01-04-2025 d/02-04-2025
```

#### Summary trend over a date range

**Format:** `summary from/DATE1 to/DATE2`

Displays a day-by-day table of calorie and protein intake between two dates (inclusive), with a segmented bar for each day. Days that are within 20% of your daily calorie goal are flagged with `GOAL reached!`.

- `DATE1` must not be after `DATE2`.

**Example:**
```
summary from/01-04-2025 to/07-04-2025
```

> **Note:** If no food is logged on a particular date within the range, that date is omitted from the output.

---

### Viewing food history : `history`

Displays a historical overview of all days you have logged food.

#### Full history

**Format:** `history`

Shows a table of all logged dates with total calories, protein, and a segmented bar. Also indicates whether you have recorded food today.

#### Top N highest calorie days

**Format:** `history /top N`

Displays the `N` days with the highest total calorie intake, in descending order.

**Example:**
```
history /top 5
```

#### Top N days closest to your daily goal

**Format:** `history /best N`

Displays the `N` days whose total calorie intake was closest to your daily calorie goal (regardless of over or under).

**Example:**
```
history /best 3
```

#### Tracking streak

**Format:** `history streak`

Shows your current logging streak (consecutive days with at least one food entry) and your all-time longest streak. A streak counts today if you have already logged food today, or yesterday if today has no entries yet.

---

### Managing food presets : `preset`

Presets are saved food templates you can quickly log without re-entering nutritional details.

#### Saving a new preset

**Format:** `preset add n/NAME c/CALORIES p/PROTEIN`

Date is not stored in a preset — it is applied when you use the preset.

**Example:**
```
preset add n/Protein Shake c/200 p/30
```

#### Viewing all presets

**Format:** `preset list`

Displays all saved presets with their index, name, calories, and protein.

#### Deleting a preset

**Format:** `preset delete INDEX`

**Example:**
```
preset delete 2
```

#### Logging a preset as a food entry

**Format:** `preset use INDEX [d/DATE]`

Logs the preset at `INDEX` as a new food entry. If no date is provided, today's date is used.

**Examples:**
```
preset use 1
preset use 1 d/01-04-2025
```

> **Note:** Preset indices are shown by `preset list`. Using a preset does not remove it from the preset list.

---

### Managing nutritional goals : `goals`

Set and track your daily and weekly calorie and protein targets.

#### Viewing goal progress

**Format:** `goals`

Displays your daily and weekly goals alongside your current intake for today and the current week (Monday to Sunday).

#### Setting goals

**Format:** `goals set [dc/DAILY_CALORIES] [dp/DAILY_PROTEIN] [wc/WEEKLY_CALORIES] [wp/WEEKLY_PROTEIN]`

At least one prefix must be provided. Values must be non-negative numbers.

| Prefix | Goal                        |
|--------|-----------------------------|
| `dc/`  | Daily calorie goal (kcal)   |
| `dp/`  | Daily protein goal (g)      |
| `wc/`  | Weekly calorie goal (kcal)  |
| `wp/`  | Weekly protein goal (g)     |

**Examples:**
```
goals set dc/2500 dp/60
goals set dc/2000 dp/50 wc/14000 wp/350
```

**Default goals (before any changes):**

| Goal             | Default value |
|------------------|---------------|
| Daily calories   | 2000 kcal     |
| Daily protein    | 50 g          |
| Weekly calories  | 14000 kcal    |
| Weekly protein   | 350 g         |

> **Note:** When you save a profile, your daily and weekly calorie goals are automatically updated to match your calculated basal metabolic rate. You can override this at any time with `goals set`.

---

### Managing your profile : `profile`

Your profile stores your physical attributes and is used to compute your BMI and Basal Metabolic Rate (BMR).

#### Viewing your profile

**Format:** `profile`

Displays your name, gender, age, weight, height, BMI (with category), and BMR.

**BMI categories:**

| BMI range    | Category    |
|--------------|-------------|
| Below 18.5   | Underweight |
| 18.5 – 24.9  | Normal      |
| 25.0 – 29.9  | Overweight  |
| 30.0 and above | Obese     |

**BMR formula (Mifflin-St Jeor):**
- Male: `(10 × weight_kg) + (6.25 × height_cm) − (5 × age) + 5`
- Female: `(10 × weight_kg) + (6.25 × height_cm) − (5 × age) − 161`

#### Setting or updating your profile

**Format:** `profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT`

| Parameter | Description                                      |
|-----------|--------------------------------------------------|
| `NAME`    | Your name.                                       |
| `GENDER`  | `male` or `female` (case-insensitive).           |
| `AGE`     | Your age in whole years.                         |
| `WEIGHT`  | Your weight in kilograms (decimals allowed).     |
| `HEIGHT`  | Your height in centimetres (decimals allowed).   |

You may provide any subset of fields to update only those values; existing fields are preserved. Age, weight, and height must be non-negative.

After saving, your calorie goals are automatically updated to match your BMR.

**Example:**
```
profile set n/Alice g/female a/25 w/55 h/165
```

#### Listing all saved profiles

**Format:** `profile list`

Displays all profiles saved on this system, with each user's name, BMI, and BMR.

#### Switching to another profile

**Format:** `profile switch NAME`

Switches the current session to the profile saved under `NAME`. The name must exactly match an existing profile.

**Example:**
```
profile switch Bob
```

#### Clearing your profile

**Format:** `profile clear`

Deletes the profile file for the current user. This cannot be undone.

---

### Switching users : `login`

Switches the active session to a different user by prompting you for their name. This is the recommended way to change between users without restarting the application.

**Format:** `login`

When executed, Bitbites will:
1. Prompt you to enter a username.
2. Set the active session to that user.
3. Load that user's saved goals for the session.
4. If a profile exists for that user, display it as a welcome-back summary.
5. If no profile exists, guide you to create one with `profile set`.

**Example interaction:**
```
login
> Enter your name to login:
Alice
> Welcome back, Alice!
> --- Profile Summary ---
> ...
```

> **Note:** Switching users with `login` updates goals and profile operations immediately. However, the food log and presets are currently shared across all users on the same system.

---


### Viewing nutrition tips : `tips`

Displays a reference guide of common foods with estimated calorie and protein values, including popular Singaporean meals and quick rules of thumb for portion estimation.

**Format:** `tips`

---

### Getting motivated : `motivate`

Displays a random motivational message to encourage your health journey.

**Format:** `motivate`

---

### Exiting the application : `exit`

Saves all data and exits Bitbites.

**Format:** `exit`

---

## Data Storage

Bitbites saves your data automatically to the `data/` folder in the same directory as the JAR file after every command. You do not need to save manually.

| Data              | File location                          |
|-------------------|----------------------------------------|
| Food log          | `data/foods.txt`                       |
| Presets           | `data/presets.txt`                     |
| Profile (per user)| `data/<username>_profile.txt`          |
| Goals (per user)  | `data/<username>_goals.txt`            |

> **Editing data files directly:** The data files are plain text and can be edited manually. However, corrupted or incorrectly formatted entries will be rejected when the application next loads, and affected data may be lost. As such, manual editing is not reccomended.

---

## FAQ

**Q: Can I use Bitbites with multiple user accounts on the same computer?**

Yes. Each user's profile and goals are saved in separate files named after the user (e.g., `alice_profile.txt`, `bob_profile.txt`). Use `login` to change the active user. Note that the food log and presets are shared in the current version.

**Q: What happens if I forget to include the date when adding food?**

The `d/DATE` field is optional. Omitting it will add the food to today's date.

**Q: Can I log food for a past or future date?**

Yes. Simply provide the date you want in `DD-MM-YYYY` format. Bitbites does not restrict which dates you can log.

**Q: What does `history /best N` show?**

It shows the `N` days whose total calorie intake was closest to your daily calorie goal, which are not necessarily the lowest calorie days. This helps you identify which days you managed your intake most accurately.

**Q: Will my data be lost if the application crashes?**

Data is saved after every successfully executed command. Only changes made in a command that did not complete successfully may be lost.

---

## Known Issues

- The food log and presets are shared across all users on the same system. Switching profiles via `login` or `profile switch` does not load a different food log for that user.
- `find` performs exact name matching only. Searching for a partial name (e.g., `find Chicken`) will not match `Chicken Rice`.
- The weekly goal progress in `goals` is calculated based on the current calendar week (Monday to Sunday). Entries from the previous week are not included even if they are recent.

---

## Command Summary

| Command | Format |
|---|---|
| Help | `help` |
| Add food | `add n/NAME c/CALORIES p/PROTEIN d/DATE` |
| List all food | `list` |
| List by date | `list d/DATE` |
| Delete food | `delete INDEX` |
| Edit food | `edit INDEX [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE]` |
| Find food | `find KEYWORD` |
| Summary by date | `summary d/DATE` |
| Summary compare | `summary compare d/DATE1 d/DATE2` |
| Summary range | `summary from/DATE1 to/DATE2` |
| History | `history` |
| History top N | `history /top N` |
| History best N | `history /best N` |
| History streak | `history streak` |
| Add preset | `preset add n/NAME c/CALORIES p/PROTEIN` |
| List presets | `preset list` |
| Delete preset | `preset delete INDEX` |
| Use preset | `preset use INDEX [d/DATE]` |
| View goals | `goals` |
| Set goals | `goals set [dc/CAL] [dp/PROT] [wc/CAL] [wp/PROT]` |
| View profile | `profile` |
| Set profile | `profile set [n/NAME] [g/GENDER] [a/AGE] [w/WEIGHT] [h/HEIGHT]` |
| List profiles | `profile list` |
| Clear profile | `profile clear` |
| Login | `login` |
| Tips | `tips` |
| Motivate | `motivate` |
| Exit | `exit` |
