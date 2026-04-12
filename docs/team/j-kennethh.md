# John Kenneth - Project Portfolio Page

## Overview

BitBites is a command-line food tracking chatbot that helps users log daily meals, monitor nutritional intake, and track progress against personal health goals.
It is written in Java and targets users who prefer fast, keyboard-driven interfaces over graphical applications.

My contributions focused on the listing, storage, and presets feature, including the `list`, `preset add`, `preset list`, `preset delete`, `preset use` commands.

---

## Summary of Contributions

### Code Contributed

[Link to tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=j-kennethh&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=j-kennethh&tabRepo=AY2526S2-CS2113-F14-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements Implemented

#### 1. List Commands (`list`, `list d/DATE`)

Implemented the `list` command to retrieve logged food items flexibly, handling both complete overview and day-specific filtering.
- **`list`:** Displays the entire history of logged food items, maintaining sequence and 1-based indexing for easy reference by `delete` and `edit` commands.
- **`list d/DATE`:** Provides filtered views by traversing `FoodList` and matching records safely using date string validations. Embedded internal `assert` checks for execution security.

---

#### 2. Preset Commands (`preset add`, `preset list`, `preset delete`, `preset use`)

Designed and implemented the food preset management system end-to-end to streamline the input process for recurrent meals.
- **`PresetList` & `Preset`:** Constructed the core data model encapsulating template food items independent of their consumed dates.
- **`PresetCommand` variants:** Implemented `preset add`, `preset list`, `preset delete`, and `preset use`. The `preset use` command elegantly shifting a saved preset into the `FoodList` combined with the target date (or defaulting to today's date), drastically reducing keystrokes and simplifying user flow.

---

#### 3. Storage Features

Architected and implemented the essential data saving layers to assure data persistence between app sessions.
- **`FoodStorage` & `PresetStorage`:** Engineered the logic responsible for compiling memory contents (`FoodList` and `PresetList`) into encoded string files (`foods.txt`, `presets.txt`) within the `data/` directory.
- Guaranteed fault-tolerance so missing or corrupted files do successfully instantiate a new list instead of terminating unexpectedly to ensure uninterrupted user operations.

---

#### 4. Core Application Startup & Safety

- **`Bitbites` Main Initialisation:** Designed the primary entry point mechanisms connecting UI wrappers, data components (`FoodList`, `PresetList`), and `Storage` engines, proactively maintaining robust launches that instantly create required datasets automatically in absence of predefined files.

---

#### 5. Format Standardisation (`Food` model)

- **Presentation Engine:** Developed the primary `toString` parser defining how stored elements uniformly translate into human-readable console formats globally (e.g., `(500kcal, 30.0g protein) on 01-04-2026`).

---

### Contributions to the User Guide

- Section: Listing food items `list`
- Section: Managing food presets `preset`
- Section: Data Storage

---

### Contributions to the Developer Guide

- Section 2: Listing Food Items `list` - implementation details and sequence diagram.
- Section 12: Storage Components - outlined foundational file handling for `foods.txt` and `presets.txt`.

**UML diagrams added:**
- `listFromDate.png` - Sequence diagram
- `presetUse.png` - Sequence diagram 
- `storageSave.png` - Sequence diagram

---

### Contributions to Team-Based Tasks

- Paved the initial groundwork on core `Storage` utilities, permitting subsequent features and testing workflows to interact safely with file handlers early within project development.
- Set up foundational abstractions and data structures for commands to parse fields accurately.
- Spearheaded rigorous unit test coverage within `BitbitesTest.java`, verifying exhaustive boundary limits for `storage`, `list d/DATE`, and `preset` operations. Testing strategies included simulating severe I/O errors and manipulating corrupted initialisation files to solidify the application's overall resilience against unverified behaviours.

---

### Review/Mentoring Contributions

PRs reviewed by me:

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/84

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/73

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/71

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/69

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/64

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/62

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/60

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/59

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/56

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/51

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/49

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/32

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/30

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/28

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/18

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/17

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/15

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/14

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/12

https://github.com/AY2526S2-CS2113-F14-2/tp/pull/1
