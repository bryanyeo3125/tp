package storage;

import model.Profile;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// @@author bryanyeo3125
public class ProfileStorage {
    private static final String FILE_PATH = "data/profile.txt";

    public static void saveProfile(Profile profile) {
        try {
            String safeName = profile.getName().toLowerCase().replaceAll("\\s+", "_");
            File file = new File("data/" + safeName + "_profile.txt");
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write("name=" + profile.getName() + "\n");
            writer.write("age=" + profile.getAge() + "\n");
            writer.write("weight=" + profile.getWeight() + "\n");
            writer.write("height=" + profile.getHeight() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    public static Profile loadProfile(String name) {
        try {
            String safeName = name.toLowerCase().replaceAll("\\s+", "_");
            File file = new File("data/" + safeName + "_profile.txt");
            if (!file.exists()) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String profileName = reader.readLine().split("=")[1];
            int age = Integer.parseInt(reader.readLine().split("=")[1]);
            double weight = Double.parseDouble(reader.readLine().split("=")[1]);
            double height = Double.parseDouble(reader.readLine().split("=")[1]);
            reader.close();
            return new Profile(profileName, age, weight, height);
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }

    public static boolean profileExists(String name) {
        String safeName = name.toLowerCase().replaceAll("\\s+", "_");
        return new File("data/" + safeName + "_profile.txt").exists();
    }

    public static void deleteProfile(String name) {
        String safeName = name.toLowerCase().replaceAll("\\s+", "_");
        new File("data/" + safeName + "_profile.txt").delete();
    }
}
// @@author
