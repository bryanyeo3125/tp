package command;

import model.Profile;
import seedu.bitbites.AppContext;
import storage.ProfileStorage;
import ui.UserInterface;

// @@author bryanyeo3125
public class ProfileCommand extends Command {
    private final String fullCommand;

    public ProfileCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public boolean execute(AppContext context) {
        UserInterface ui = context.getUi();

        if (fullCommand.equals("profile")) {
            showProfile(ui.getCurrentUser());
        } else if (fullCommand.startsWith("profile set")) {
            if (fullCommand.trim().equals("profile set")) {
                System.out.println("Please specify profile fields.");
                System.out.println("Format: profile set n/NAME a/AGE w/WEIGHT h/HEIGHT");
            } else if (!hasValidProfilePrefix(fullCommand)) {
                System.out.println("Unknown profile prefix.");
                System.out.println("Valid prefixes: n/ (name), a/ (age), w/ (weight kg), h/ (height cm)");
            } else {
                handleSetProfile(fullCommand, ui.getCurrentUser());
            }
        } else if (fullCommand.equals("profile clear")) {
            ProfileStorage.deleteProfile(ui.getCurrentUser());
            System.out.println("Profile cleared.");
        } else {
            System.out.println("Unknown profile command.");
            System.out.println("Available commands:");
            System.out.println("  profile                                    - View your profile");
            System.out.println("  profile set n/NAME a/AGE w/WEIGHT h/HEIGHT - Set profile");
            System.out.println("  profile clear                              - Clear your profile");
        }
        return false;
    }

    private void showProfile(String name) {
        Profile profile = ProfileStorage.loadProfile(name);
        if (profile == null) {
            System.out.println("No profile found. Use: profile set n/NAME a/AGE w/WEIGHT h/HEIGHT");
            return;
        }
        System.out.println("========== YOUR PROFILE ==========");
        System.out.println("Name   : " + profile.getName());
        System.out.println("Age    : " + profile.getAge());
        System.out.printf("Weight : %.1f kg%n", profile.getWeight());
        System.out.printf("Height : %.1f cm%n", profile.getHeight());
        System.out.printf("BMI    : %.1f (%s)%n", profile.getBmi(), profile.getBmiCategory());
        System.out.println("BMR    : " + profile.getBmr() + " kcal/day");
        System.out.println("==================================");
    }

    private void handleSetProfile(String command, String name) {
        try {
            Profile existing = ProfileStorage.loadProfile(name);
            String profileName = existing != null ? existing.getName() : name;
            int age = existing != null ? existing.getAge() : 0;
            double weight = existing != null ? existing.getWeight() : 0.0;
            double height = existing != null ? existing.getHeight() : 0.0;

            if (command.contains("n/")) {
                profileName = extractValue(command, "n/", nextPrefix(command,
                        "n/", new String[]{"n/", "a/", "w/", "h/"}));
            }
            if (command.contains("a/")) {
                age = Integer.parseInt(extractValue(command, "a/", nextPrefix(command,
                        "a/", new String[]{"n/", "a/", "w/", "h/"})));
            }
            if (command.contains("w/")) {
                weight = Double.parseDouble(extractValue(command, "w/", nextPrefix(command,
                        "w/", new String[]{"n/", "a/", "w/", "h/"})));
            }
            if (command.contains("h/")) {
                height = Double.parseDouble(extractValue(command, "h/", nextPrefix(command,
                        "h/", new String[]{"n/", "a/", "w/", "h/"})));
            }

            if (age < 0 || weight < 0 || height < 0) {
                System.out.println("Age, weight, and height must be non-negative.");
                return;
            }

            Profile profile = new Profile(profileName, age, weight, height);
            ProfileStorage.saveProfile(profile);
            System.out.println("Profile updated successfully!");
            showProfile(name);

        } catch (NumberFormatException e) {
            System.out.println("Invalid value. Age must be an integer, weight and height must be numbers.");
        }
    }

    private boolean hasValidProfilePrefix(String command) {
        return command.contains("n/") || command.contains("a/") ||
                command.contains("w/") || command.contains("h/");
    }
}
// @@author
