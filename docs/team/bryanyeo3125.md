# Bryan Yeo - Project Portfolio Page

## Overview

BitBites is a command-line food tracking chatbot that helps users log daily meals, monitor nutritional intake, and track progress against personal health goals.
It is written in Java and targets users who prefer fast, keyboard-driven interfaces over graphical applications.

My contributions focused on the user profile system, nutritional goal management, the food adding workflow, and the shared command infrastructure that underpins all other commands.

---

## Summary of Contributions

### Code Contributed

[https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=bryanyeo3125&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=bryanyeo3125&tabRepo=AY2526S2-CS2113-F14-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false](#)

---

### Enhancements Implemented

#### 1. User Profile System (`Profile`, `ProfileCommand`, `ProfileStorage`)

Designed and implemented the full user profile feature end-to-end.

- **`Profile` (model):** Stores a user's name, gender, age, weight, and height. Computes BMI using the standard formula and BMR using the Mifflin-St Jeor formula with gender adjustment. Also provides a BMI category classifier (Underweight / Normal / Overweight / Obese).
- **`ProfileStorage`:** Handles per-user profile file I/O in the `data/` directory. Supports loading by name, loading from a `File` object (used for listing all profiles), existence checks, and deletion. Multiple user profiles coexist on the same system without conflicts using a `<username>_profile.txt` naming convention.
- **`ProfileCommand`:** Implements all profile sub-commands — `profile` (view), `profile set` (create or partially update), `profile list`, `profile switch`, and `profile clear`. Uses the shared `extractValue` / `nextPrefix` utilities from `Command` for robust prefix parsing. Saving a profile automatically triggers `GoalsCommand.autoSetGoalsFromBmr()`, keeping calorie goals in sync with the user's physical data.

---

#### 2. Nutritional Goals System (`GoalsCommand`, `GoalsStorage`)

Designed and implemented the goals feature, including persistence and cross-command integration.

- **`GoalsCommand`:** Handles viewing current daily and weekly goal progress (`goals`) and updating individual goal fields (`goals set dc/`, `dp/`, `wc/`, `wp/`). Weekly progress is calculated over the current Monday-to-Sunday window using `LocalDate`. Exposes static methods (`getDailyCalorieGoal`, `getDailyProteinGoal`, `showDailyProgress`, `autoSetGoalsFromBmr`, `loadGoalsIfNeeded`) so that `AddCommand`, `DeleteCommand`, `SummaryByDateCommand`, and `HistoryBestCommand` can display goal-relative feedback without duplicating logic.
- **`GoalsStorage`:** Persists each user's four goal values (daily calories, daily protein, weekly calories, weekly protein) in a per-user `<username>_goals.txt` file. Goals are loaded at session start and re-saved after every update.

The static design of `GoalsCommand` was a deliberate architectural decision to allow goal data to be read by any command without adding goal state to `AppContext`, keeping the context object lean.

---

#### 3. Add Food Command (`AddCommand`)

Implemented the core food logging command with thorough input validation.

- Parses `n/`, `c/`, `p/`, and `d/` fields from the raw command string.
- Date is optional and if omitted, will add to the current day's meals.
- Validates that calories are a non-negative integer, protein is a non-negative decimal, and the date strictly matches `DD-MM-YYYY` format.
- Displays daily goal progress immediately after a successful add via `GoalsCommand.showDailyProgress()`, giving the user instant feedback.

---

#### 4. Shared Command Infrastructure (`Command`, `AppContext`)

Designed the abstract `Command` base class and `AppContext` context object used by all commands in the application.

- **`Command`:** Defines the `execute(AppContext context)` contract. Provides two shared utility methods — `extractValue` and `nextPrefix` — for parsing prefix-based input strings (e.g., `n/`, `dc/`). These are reused across `AddCommand`, `GoalsCommand`, `ProfileCommand`, `EditCommand`, and `PresetCommand`, avoiding duplicated parsing logic.
- **`AppContext`:** Encapsulates `FoodList`, `PresetList`, and `UserInterface` into a single context object passed to all commands. Follows the Context Object design pattern, avoiding the need to update every command's method signature when new components are added

---

#### 5. Application Context Object (`AppContext`)

Designed and implemented the `AppContext` class, which encapsulates the core application state — `FoodList`, `PresetList`, and `UserInterface` — into a single object passed to every command during execution.

- Follows the **Context Object design pattern**, keeping command method signatures stable as the application grows. Without `AppContext`, adding a new component (e.g., a settings manager) would require updating the `execute` signature of every command class.
- Adopted by all teammates as the standard way to access application state, making it one of the most-touched interfaces in the codebase despite being a small class.

---

### Contributions to the User Guide

- Section: Introduction
- Section: Table of Contents
- Section: Quick Start
- Section: Notes on Command Format
- Section: `add` command
- Section: `goals` commands
- Section: `profile` commands
- Section: FAQ
- Section: Known Issues
- Section: Command Summary

---

### Contributions to the Developer Guide

- Section 1. Design: Command Pattern and AppContext
- Section 3. Adding a Food Item
- Section 8. Managing User Profiles
- Section 9. Managing Nutritional Goals

**UML Diagrams Added:**
- `add_sequence.png`: Sequence Diagram
- `command_class.png`: Class Diagram
- `goals_set.png`: Sequence Diagram
- `profile_set.png`: Sequence Diagram
---

### Contributions to Team-Based Tasks

- Set up the team organisation and repository.
- Designed `AppContext`, refactored the code midway through to make it the standard way to pass application state into commands, eliminating the need to refactor command signatures as the project grew.
- Established the `extractValue` / `nextPrefix` prefix-parsing pattern in `Command` that teammates reused in `EditCommand` and `PresetCommand`.
- Set up the per-user file naming convention (`<username>_profile.txt`, `<username>_goals.txt`) used consistently across `ProfileStorage` and `GoalsStorage`.
- Added relevant routing entries in `Parser` and response strings in `BitbitesResponses` for `add`, `goals`, and `profile` commands.

---

### Review/Mentoring Contributions

PRs reviewed by me:      \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/20 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/22 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/29 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/36 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/48 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/53 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/58 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/70 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/74 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/76 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/80 \
https://github.com/AY2526S2-CS2113-F14-2/tp/pull/82 
