# User Guide

## Introduction

{Give a product intro}

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

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
