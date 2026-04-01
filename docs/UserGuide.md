# BitBites User Guide

## Introduction

{Give a product intro}

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 installed.
2. Download the latest version of `BitBites` from [here](https://github.com/AY2526S2-CS2113-F14-2/tp/releases).

## Features 

{Give detailed description of each feature}

### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.  

Example of usage: 

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`

### Listing all food items: `list`
Displays a complete list of all the food items you have ever logged.

Format: `list`

* The list will display each food item's name, calories, protein, and the date it was logged.
* Items are numbered sequentially.

Example of usage:

`list`

### Listing food items by date: `list d/DATE`
Filters your food list and displays only the items consumed on a specific date.

Format: `list d/DATE`

* The `DATE` must strictly follow the `DD-MM-YYYY` format.
* If no food items were logged on that date, the list will simply be empty.

Example of usage:

`list d/27-03-2026`

### Adding a food preset: `preset add`
Saves a frequently consumed food item as a template so you can quickly log it later without needing to look up or type out its nutritional details again.

Format: `preset add n/NAME c/CALORIES p/PROTEIN`

* The `NAME` cannot be left empty.
* `CALORIES` must be a positive integer (e.g., `250`).
* `PROTEIN` must be a positive number and can include decimals (e.g., `15` or `15.5`).

Example of usage:

`preset add n/Oatmeal c/150 p/5.0`

`preset add n/Chicken Breast c/165 p/31`

### Listing all food presets: `preset list`
Displays a numbered list of all your saved food presets.

Format: `preset list`

* You will need to use this command to find the `INDEX` number of a preset before you can use or delete it.
* If you haven't saved any presets yet, the application will notify you that your database is empty.

Example of usage:

`preset list`

### Deleting a food preset: `preset delete`
Removes a specific food preset from your saved templates.

Format: `preset delete INDEX`

* The `INDEX` refers to the number shown next to the preset when you use the `preset list` command.
* The `INDEX` must be a positive integer and must exist in the list.

Example of usage:

`preset delete 1`

### Using a food preset: `preset use`
Quickly logs a saved preset into your daily food list.

Format: `preset use INDEX [d/DATE]`

* The `INDEX` refers to the number shown next to the preset when you use the `preset list` command.
* The `d/DATE` parameter is **optional**.
* If you do not provide a date, the application will automatically log the food item under **today's date**.
* If you do provide a date, it must strictly follow the `DD-MM-YYYY` format.

Examples of usage:

`preset use 1` (Logs the first preset for today)

`preset use 2 d/28-03-2026` (Logs the second preset for 28 March 2026)

### Automatically saving data
BitBites handles your data safely and automatically. You never have to worry about manually saving your progress.

* BitBites automatically saves your food items and presets to your computer's hard drive immediately after every successful command you type.
* Your data is stored in the `data` folder located in the same directory as the application.
* Your food items are saved in `foods.txt` and your presets are saved in `presets.txt`.
* If you accidentally delete these files, BitBites will safely create new, empty ones the next time you start the app.

### Exiting the application: `exit`

Exits the application and saves all your food tracking data.

When you execute the `exit` command, the application will:
1. Save all logged food items to your profile's data file
2. Save all food presets to your profile's data file
3. Display a farewell message
4. Close the application

Format: `exit`

**Important:** Your data is automatically saved when you exit. You will not lose any tracked food items or presets.

Example of usage:

`exit`

The system will display: "Bye. Hope to see you again soon!"

---

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
