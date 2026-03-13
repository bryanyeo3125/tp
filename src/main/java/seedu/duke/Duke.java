package seedu.duke;

import java.util.Scanner;

public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("What is your name?");

        Scanner in = new Scanner(System.in);
        System.out.println("Hello " + in.nextLine());


        // Exit Command - rayminQAQ
    }

    // TODO: List all food items

    // TODO: List food items for a specific date
}

/*
Command - Add (Bryan)
Adds a food item to the list with its calorie and protein information

Format:
add n/NAME c/CALORIES_IN_KCAL p/PROTEIN_IN_G d/DATE
 */
