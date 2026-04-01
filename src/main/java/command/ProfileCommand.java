package command;

import model.Profile;
import seedu.bitbites.AppContext;
import storage.ProfileStorage;
import ui.UserInterface;

/**
 * ProfileCommand.java
 *
 * Handles viewing, setting, and clearing the user's personal profile.
 * The profile stores name, gender, age, weight, and height, and derives
 * BMI and BMR (Basal Metabolic Rate) from these values.
 * Profiles are persisted to disk and loaded by name on each session.
 *
 * Supported commands:
 *   profile: View your profile
 *   profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT: Set or update profile fields
 *   profile clear: Delete your profile
 */
// @@author bryanyeo3125
public class ProfileCommand extends Command {
    private final String fullCommand;

    /**
     * Constructs a ProfileCommand with the full user input string.
     *
     * @param fullCommand The raw command string entered by the user.
     */
    public ProfileCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Executes the profile command, either displaying, updating, or clearing the profile.
     *
     * @param context The application context containing UserInterface.
     * @return false always, as this command does not trigger application exit.
     */
    @Override
    public boolean execute(AppContext context) {
        UserInterface ui = context.getUi();

        if (fullCommand.equals("profile")) {
            showProfile(ui.getCurrentUser());
        } else if (fullCommand.startsWith("profile set")) {
            if (fullCommand.trim().equals("profile set")) {
                System.out.println("Please specify profile fields.");
                System.out.println("Format: profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT");
            } else if (!hasValidProfilePrefix(fullCommand)) {
                System.out.println("Unknown profile prefix.");
                System.out.println("Valid prefixes: n/ (name), g/ (gender), a/ (age), "
                        + "w/ (weight kg), h/ (height cm)");
            } else {
                handleSetProfile(fullCommand, ui.getCurrentUser());
            }
        } else if (fullCommand.equals("profile clear")) {
            ProfileStorage.deleteProfile(ui.getCurrentUser());
            System.out.println("Profile cleared.");
        } else {
            System.out.println("Unknown profile command.");
            System.out.println("Available commands:");
            System.out.println("  profile                                              - View your profile");
            System.out.println("  profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT - Set profile");
            System.out.println("  profile clear                                        - Clear your profile");
        }
        return false;
    }

    /**
     * Loads and displays the profile for the given user name.
     * Shows name, gender, age, weight, height, BMI and BMR.
     * Prompts the user to set up a profile if none is found.
     *
     * @param name The name of the current user session.
     */
    private void showProfile(String name) {
        Profile profile = ProfileStorage.loadProfile(name);
        if (profile == null) {
            System.out.println("No profile found. Use: profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT");
            return;
        }
        System.out.println("========== YOUR PROFILE ==========");
        System.out.println("Name   : " + profile.getName());
        System.out.println("Gender : " + profile.getGender());
        System.out.println("Age    : " + profile.getAge());
        System.out.printf("Weight : %.1f kg%n", profile.getWeight());
        System.out.printf("Height : %.1f cm%n", profile.getHeight());
        System.out.printf("BMI    : %.1f (%s)%n", profile.getBmi(), profile.getBmiCategory());
        System.out.println("BMR    : " + profile.getBmr() + " kcal/day");
        System.out.println("==================================");
    }

    /**
     * Parses and updates one or more profile fields from the command string.
     * Loads any existing profile first to preserve unchanged fields.
     * Saves the updated profile to disk on success.
     * Auto-sets daily and weekly calorie goals based on the updated BMR.
     *
     * @param command The full command string containing profile prefixes and values.
     * @param name    The name of the current user session.
     */
    private void handleSetProfile(String command, String name) {
        try {
            String[] prefixes = {"n/", "g/", "a/", "w/", "h/"};
            Profile existing = ProfileStorage.loadProfile(name);
            String profileName = existing != null ? existing.getName() : name;
            String gender = existing != null ? existing.getGender() : "male";
            int age = existing != null ? existing.getAge() : 0;
            double weight = existing != null ? existing.getWeight() : 0.0;
            double height = existing != null ? existing.getHeight() : 0.0;

            if (command.contains("n/")) {
                profileName = extractValue(command, "n/", nextPrefix(command, "n/", prefixes));
            }
            if (command.contains("g/")) {
                gender = extractValue(command, "g/", nextPrefix(command, "g/", prefixes));
                if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
                    System.out.println("Gender must be 'male' or 'female'.");
                    return;
                }
            }
            if (command.contains("a/")) {
                age = Integer.parseInt(extractValue(command, "a/", nextPrefix(command, "a/", prefixes)));
            }
            if (command.contains("w/")) {
                weight = Double.parseDouble(extractValue(command, "w/", nextPrefix(command, "w/", prefixes)));
            }
            if (command.contains("h/")) {
                height = Double.parseDouble(extractValue(command, "h/", nextPrefix(command, "h/", prefixes)));
            }

            if (age < 0 || weight < 0 || height < 0) {
                System.out.println("Age, weight, and height must be non-negative.");
                return;
            }

            Profile profile = new Profile(profileName, gender, age, weight, height);
            ProfileStorage.saveProfile(profile);
            System.out.println("Profile updated successfully!");
            showProfile(name);
            GoalsCommand.autoSetGoalsFromBmr(name, profile.getBmr());

        } catch (NumberFormatException e) {
            System.out.println("Invalid value. Age must be an integer, weight and height must be numbers.");
        }
    }

    /**
     * Checks whether the command contains at least one valid profile prefix.
     *
     * @param command The command string to check.
     * @return true if a valid prefix is found, false otherwise.
     */
    private boolean hasValidProfilePrefix(String command) {
        return command.contains("n/") || command.contains("g/") || command.contains("a/") ||
                command.contains("w/") || command.contains("h/");
    }
}
// @@author
