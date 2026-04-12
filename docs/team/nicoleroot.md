# Mao Xiaohan - Project Portfolio Page

## Overview

BitBites is a command-line food tracking chatbot that helps users log daily meals, monitor nutritional intake, and track progress against personal health goals.
It is written in Java and targets users who prefer fast, keyboard-driven interfaces over graphical applications.

My contributions focused on the summary and history features, including nutritional breakdowns, trend visualisation, streak tracking, and the `delete`, `edit`, `help`, and `tips` commands.

---

## Summary of Contributions

### Code Contributed

[Link to tP Code Dashboard](#) *https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=nicoleroot&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=nicoleroot&tabRepo=AY2526S2-CS2113-F14-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false*

---

### Enhancements Implemented

#### 1. Summary Commands (`summary d/DATE`, `summary from/to`, `summary compare`)

Implemented a full suite of nutritional summary commands:
- `summary d/DATE` — per-day breakdown with a segmented ASCII progress bar showing each meal's proportion of the day's total calories, plus goal status showing remaining or exceeded calories/protein against the user's daily goals
- `summary from/DATE1 to/DATE2` — trend view across a date range, with each day's bar scaled relative to the highest-calorie day; days within 20% of the daily calorie goal are marked with `GOAL reached!`
- `summary compare d/DATE1 d/DATE2` — side-by-side comparison of two days showing calorie and protein differences

The date comparison logic in `SummaryRangeCommand` uses `java.time.LocalDate` to correctly handle chronological ordering of `DD-MM-YYYY` dates, avoiding the lexicographic comparison bug that would incorrectly flag valid date ranges.

#### 2. History Commands (`history`, `history /top N`, `history /best N`, `history streak`)

Implemented a full history feature:
- `history` — log of all recorded days with segmented breakdown bars and a reminder if today has not been logged
- `history /top N` — top N highest calorie days
- `history /best N` — top N days closest to the user's daily calorie goal, using `|totalCalories - dailyCalorieGoal|` as the ranking metric rather than simply lowest calories
- `history streak` — current and longest consecutive recording streak, using `LocalDate.now()` to correctly detect broken streaks when the user has not logged today or yesterday

#### 3. `NutritionSummary` and `ProgressBar` classes

Designed and implemented:
- `NutritionSummary` — encapsulates aggregated nutritional data per day, keeping `FoodList` responsible for computation and `UserInterface` responsible for display
- `ProgressBar` — generates segmented ASCII bars where each `=` segment represents one meal's calorie share, with total bar width scaled relative to a maximum value for trend comparisons

#### 4. `delete` Command

Implemented `DeleteCommand` with index validation, bounds checking delegated to `FoodList`, and automatic display of daily goal progress after deletion via `GoalsCommand.showDailyProgress()`.

#### 5. `edit` Command

Implemented `EditCommand` supporting partial field updates — users can edit any combination of `n/`, `c/`, `p/`, `d/` without re-entering unchanged fields.

#### 6. `help` Command

Implemented `HelpCommand` which displays all available commands.

#### 7. `tips` Command

Implemented `TipsCommand` which displays tips for estimating calories and protein.

---

### Contributions to the User Guide

- Section: `delete` command
- Section: `edit` command
- Section: `help` command
- Section: `summary` commands 
- Section: `history` commands 
- Section: `tips` command

---

### Contributions to the Developer Guide

- Acknowledgements
- Section 4: Delete command — implementation details, sequence diagram
- Section 5: Edit command — implementation details, sequence diagram
- Section 7: Help command — implementation details, sequence diagram
- Section 10: Summary commands — implementation details and sequence diagrams
- Section 11: History commands — implementation details and sequence
- Appendix

**UML diagrams added:**
- `delete.png` — sequence diagram
- `edit.png` — sequence diagram
- `summary_date.png` — sequence diagram
- `summary_range.png` — sequence diagram
- `summary_compare.png` — sequence diagram
- `history_streak.png` — sequence diagram
- `history.png` — sequence diagram
- `history_best.png` — sequence diagram
- `history_top` — sequence diagram

---

### Contributions to Team-Based Tasks

- Restructured the entire codebase into a multi-package architecture, organising classes into dedicated packages (`command`, `model`, `parser`, `storage`, `ui`) based on their responsibilities.
- Fixed the data persistence workflow by integrating `foodStorage.save()` and`presetStorage.save()` into the main application loop in `Bitbites.run()`, ensuring all changes are written to disk after every command execution.
- Fixed `showError()` to display the actual exception message instead of always showing the generic unknown command message
- Integrated `GoalsCommand.loadGoalsIfNeeded()` into `Bitbites.run()` so that goal values are loaded at startup rather than requiring the user to run `goals` first

---

### Review/Mentoring Contributions

PRs reviewed by me:
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/65
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/57
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/54
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/47
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/37
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/33
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/21
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/16