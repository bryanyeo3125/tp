package command;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Profile;
import storage.ProfileStorage;
import ui.UserInterface;
import seedu.bitbites.AppContext;

/**
 * LoginCommand.java
 *
 * Handles user login functionality. Allows users to switch profiles
 * or initiate a new user profile setup.
 *
 * The login command prompts the user for their name and loads their
 * associated profile if it exists. If no profile is found, the user
 * is guided to create a new one.
 *
 * Usage:
 *   login: Prompts for a username and switches to that user's profile
 */
// @@author RayminQAQ
public class LoginCommand extends Command {
    private static final Logger logger = Logger.getLogger(LoginCommand.class.getName());

    /**
     * Executes the login command, allowing users to switch profiles.
     * Prompts for username input and loads the associated profile or
     * guides the user through profile creation if no existing profile is found.
     *
     * @param context The application context containing FoodList, PresetList, and UserInterface.
     * @return false always, as this command does not trigger application exit.
     */
    @Override
    public boolean execute(AppContext context) {
        UserInterface ui = context.getUi();

        logger.log(Level.INFO, "User initiated login process");
        
        System.out.println("========== LOGIN ==========");
        System.out.println("Enter your name to login:");
        String username = ui.readCommand().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            logger.log(Level.WARNING, "Login attempted with empty username");
            return false;
        }

        logger.log(Level.INFO, "Login attempt for user: " + username);

        // Check if profile exists
        Profile profile = ProfileStorage.loadProfile(username);
        if (profile != null) {
            logger.log(Level.INFO, "Existing profile found for user: " + username);
            System.out.println("Welcome back, " + username + "!");
            displayProfile(profile);
        } else {
            logger.log(Level.INFO, "No existing profile found for user: " + username);
            System.out.println("Hello, " + username + "!");
            System.out.println("No profile found. Let's set up your profile:");
            System.out.println("  profile set n/" + username + " g/GENDER a/AGE w/WEIGHT h/HEIGHT");
            System.out.println();
            System.out.println("Example:");
            System.out.println("  profile set n/" + username + " g/male a/25 w/70 h/180");
        }

        System.out.println("Type 'help' for available commands.");
        System.out.println("==========================");

        logger.log(Level.FINE, "Login process completed for user: " + username);
        return false;
    }

    /**
     * Displays a summary of the user's profile information.
     * Shows name, gender, age, weight, height, BMI, and BMR.
     *
     * @param profile The user's profile object to display.
     */
    private void displayProfile(Profile profile) {
        logger.log(Level.FINE, "Displaying profile information for user: " + profile.getName());
        System.out.println();
        System.out.println("--- Profile Summary ---");
        System.out.println("Name   : " + profile.getName());
        System.out.println("Gender : " + profile.getGender());
        System.out.println("Age    : " + profile.getAge());
        System.out.printf("Weight : %.1f kg%n", profile.getWeight());
        System.out.printf("Height : %.1f cm%n", profile.getHeight());
        System.out.printf("BMI    : %.1f%n", profile.getBmi());
        System.out.println("BMR    : " + profile.getBmr() + " kcal/day");
        System.out.println("-----------------------");
    }
}
// @@author
