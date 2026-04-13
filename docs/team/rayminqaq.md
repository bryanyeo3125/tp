# Jui Ming Yao - Project Portfolio Page

## Overview

BitBites is a command-line food tracking chatbot that helps users log daily meals, monitor nutritional intake, and track progress against personal health goals.
It is written in Java and targets users who prefer fast, keyboard-driven interfaces over graphical applications.

My contributions focused on drafting the overall BitBites system and its high-level OOP structure, as evidenced by the following commits:
[144c211](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/144c211366fa2b5abbf4f45145d7312b40f9352d),
[3cffd51](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/3cffd51dd8d6fe0fe938754b703a339b7ba39ea8),
[ae1240c](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/ae1240c214834ba08f4542324c8fdda03cda2d7a),
[7abd0d7](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/7abd0d7505dedf76f83a6f6588f8fd681c7f3217),
[d20d78b](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/d20d78bfe3a8bfb97509fb3c59044c21afa24465),
[3c80c98](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/3c80c98e778ae87ae6d2ac03362653c0db24dc13),
[4a051f3](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/4a051f39c9d1d283d9e1a7890bcaa3d2b41b0622),
[d257e13](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/d257e13305a57b3df234397b3f4be6acaf1717f6),
[400381f](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/400381fe413db589577fc8eb6052ace5ede9deca), and
[dd1e831](https://github.com/AY2526S2-CS2113-F14-2/tp/commit/dd1e8311261369d61e6f4790e9804e613fe2ab4b).  
I also implemented several commands such as `exit`, `login`, `motivate`, and `find`.

---

## Summary of Contributions

### Enhancements Implemented

#### 1. `find` Command

Implemented a search feature enabling users to locate food items by name with case-insensitive matching. The command parses the search keyword from the user input, iterates through the `FoodList`, and performs full-name matching using `toLowerCase()` to ensure case-insensitive comparison. Results are collected and displayed in a formatted table. Non-matching entries are not returned (exact name match only, no partial matches).

#### 2. `exit` Command

Implemented graceful application termination following the Command Pattern. When the user inputs `exit`, `Parser.parse()` creates an `ExitCommand` instance and logs the request at `Level.INFO` and `Level.FINE`. The command returns `true` to signal the main application loop to break, triggering final save operations in `Bitbites.run()` to persist all unsaved data before shutdown.

#### 3. `motivate` Command

Implemented a motivational message feature to encourage users. The command retrieves a random motivational message from a predefined list and displays it to the user. Execution is logged at `Level.INFO` for auditing. The feature is extensible — support for motivation types based on progress and goals is commented out for future implementation.

#### 4. `login` Command

Implemented user switching functionality that allows session changes without application restart. When invoked, the command prompts for a username, updates the active session context, and loads the target user's saved profile and goals via `ProfileStorage` and `GoalsStorage`. If no profile exists, the user is guided to create one. This enables multi-user support on a single system while maintaining independent goal and profile states per user.

#### 5. Error Handling (`BitbitesException`)

Implemented a custom exception class to encapsulate application-specific errors. `BitbitesException` is thrown for validation failures such as missing or invalid prefixes, malformed dates, out-of-range indices, and non-numeric values. The exception provides clear, user-facing error messages that guide users to correct their input, improving usability and error recovery.

#### 6. Initial Project Structure Design (`Bitbites`, `Parser`, `UserInterface`,  `BitbitesTest`, `BitbitesException`)

Architected the foundational system design and multi-package structure that enables the entire codebase:
- **Command Pattern framework** — designed the abstract `Command` class with the `execute(context) → boolean` contract, enabling extensible command dispatch from `Parser` without modifying core logic.
- **AppContext wrapper** — encapsulates `FoodList`, `PresetList`, and `UserInterface` into a single context object passed through every command, decoupling method signatures from future components.
- **Multi-package organization** — established separate packages for `command`, `model`, `parser`, `storage`, and `ui`, each with clear single responsibility.
- **Main application loop** — designed `Bitbites.run()` to handle session initialization, command parsing, execution, and automatic persistence after each command.
- **Logger integration** — added structured logging throughout the codebase for auditing and debugging.

These architectural decisions provided a stable foundation that the team extended throughout the project lifecycle without requiring significant refactoring.

### Code Contributed

[Link to tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=RayminQAQ&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=RayminQAQ&tabRepo=AY2526S2-CS2113-F14-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Contributions to the User Guide
- Section: [Getting motivated](../UserGuide.md#getting-motivated--motivate) — `motivate` command for displaying motivational messages
- Section: [Exiting the application](../UserGuide.md#exiting-the-application--exit) — `exit` command for safe termination
- Section: [Finding food items](../UserGuide.md#finding-food-items--find) — `find` command for search functionality
- Section: [Switching users](../UserGuide.md#switching-users--login) — `login` command for multi-user session management 

---

### Contributions to the Developer Guide
- Section: [6. Exiting the Application `exit`](../DeveloperGuide.md#6-exiting-the-application-exit) — Design pattern and main loop integration
- Section: [8. Motivational Messages `motivate`](../DeveloperGuide.md#8-motivational-messages-motivate) — Feature implementation and motivation types
- Section: [9. Finding Food Items `find`](../DeveloperGuide.md#9-finding-food-items-find) — Search logic, behavior, and user feedback 

### Contributions to Team-Based Tasks
- Established the initial project structure and architecture that the rest of the team built on top of.
- [Pull Requests authored](https://github.com/AY2526S2-CS2113-F14-2/tp/issues?q=is%3Apr%20author%3ARayminQAQ)
- [Pull Requests reviewer](https://github.com/AY2526S2-CS2113-F14-2/tp/issues?q=is%3Apr%20reviewed-by%3ARayminQAQ)
